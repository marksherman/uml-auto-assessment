/*==========================================================================*\
 |  $Id: CxxTestSuitesToolWindowPanel.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT CxxTest integration package for Visual
 |	Studio.NET.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

using System;
using System.IO;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio.VCProject;
using Microsoft.VisualStudio.VCCodeModel;
using Microsoft.VisualStudio.VCProjectEngine;
using EnvDTE;
using WebCAT.CxxTest.VisualStudio.Model;
using WebCAT.CxxTest.VisualStudio.Utility;
using EnvDTE80;

namespace WebCAT.CxxTest.VisualStudio.ToolWindows
{
    /// <summary>
    /// The CxxTestView control provides a way in which to visualize currently 
    /// available test cases in the current VC project.  They are listed and can 
    /// be selected by checking them off in the checkbox list.  
    /// </summary>
    internal partial class CxxTestSuitesToolWindowPanel : UserControl
    {
        public TestSuiteCollection Suites
        {
            get
            {
                return suites;
            }
        }

        private TestSuiteCollection suites;
        
		public Dictionary<string, bool> SelectedTestCases
		{
			get
			{
				Dictionary<string, bool> selectedTestCases =
					new Dictionary<string, bool>();

				if (testsTreeView.Nodes.Count == 0)
					return selectedTestCases;

				foreach (TreeNode suiteNode in testsTreeView.Nodes[0].Nodes)
				{
					string suiteName = suiteNode.Text;

					foreach (TreeNode caseNode in suiteNode.Nodes)
					{
						string caseName = caseNode.Text;
						string qualifiedName = suiteName + "::" + caseName;
						selectedTestCases[qualifiedName] = caseNode.Checked;
					}
				}

				return selectedTestCases;
			}
		}
        
        public CxxTestSuitesToolWindowPanel()
        {
            InitializeComponent();

			testsTreeView.TreeViewNodeSorter = new NodeComparer();
		}

		private TreeNode AddTestSuiteToNode(TestSuite suite, TreeNode parentNode,
			Dictionary<string, bool> selectedTests)
		{
			TreeNode suiteNode = new TreeNode(suite.Name);
			suiteNode.Tag = suite;
			suiteNode.ImageKey = suiteNode.SelectedImageKey = "TestSuite";
			parentNode.Nodes.Add(suiteNode);

			foreach (TestCase testCase in suite.TestCases)
			{
				AddTestCaseToNode(suite, testCase, suiteNode, selectedTests);
			}

			FixChecksUpward(suiteNode);

			return suiteNode;
		}

		private void AddTestCaseToNode(TestSuite suite, TestCase testCase,
			TreeNode parentNode,
			Dictionary<string, bool> selectedTests)
		{
			string qName = suite.Name + "::" + testCase.Name;

			TreeNode caseNode = new TreeNode(testCase.Name);
			caseNode.Tag = testCase;
			caseNode.ImageKey = caseNode.SelectedImageKey = "TestCase";

			if (selectedTests == null ||
				!selectedTests.ContainsKey(qName) ||
				selectedTests[qName])
			{
				caseNode.Checked = true;
			}

			parentNode.Nodes.Add(caseNode);
		}

		private TreeNode FindNodeForTestSuite(object classOrStruct)
		{
			if (testsTreeView.Nodes.Count == 0)
				return null;

			TreeNode solutionNode = testsTreeView.Nodes[0];
			string suiteName;

			if (classOrStruct is VCCodeClass || classOrStruct is VCCodeStruct)
				suiteName = ((CodeElement2)classOrStruct).Name;
			else
				return null;

			foreach (TreeNode suiteNode in solutionNode.Nodes)
			{
				TestSuite suite = (TestSuite)suiteNode.Tag;
				if (suite.Name == suiteName)
					return suiteNode;
			}

			return null;
		}

		private TreeNode FindNodeForTestCase(object parent, VCCodeFunction function)
		{
			TreeNode suiteNode = FindNodeForTestSuite(parent);

			if (suiteNode == null)
				return null;

			string testName = ((CodeElement2)function).Name;

			foreach (TreeNode testNode in suiteNode.Nodes)
			{
				TestCase testCase = (TestCase)testNode.Tag;
				if (testCase.Name == testName)
					return testNode;
			}

			return null;
		}

		public void AddElement(CodeElement element)
		{
			if (testsTreeView.Nodes.Count == 0)
				return;

			if (element is VCCodeClass || element is VCCodeStruct)
			{
				TestSuiteCollector collector = new TestSuiteCollector();
				collector.Process(element);

				foreach (TestSuite suite in collector.Suites)
					AddTestSuiteToNode(suite, testsTreeView.Nodes[0], null);

				testsTreeView.Sort();
			}
			else if (element is VCCodeFunction)
			{
				VCCodeFunction function = (VCCodeFunction)element;

				TreeNode suiteNode = FindNodeForTestSuite(function.Parent);

				TestCaseCollector collector = new TestCaseCollector();
				collector.Process((CodeElement)function);

				foreach (TestCase testCase in collector.TestCases)
					AddTestCaseToNode((TestSuite)suiteNode.Tag, testCase, suiteNode, null);

				testsTreeView.Sort();
			}
		}

		public void ChangeElement(CodeElement element, vsCMChangeKind changeKind)
		{
			if (testsTreeView.Nodes.Count == 0)
				return;

			if (element is VCCodeClass || element is VCCodeStruct)
			{
				testsTreeView.BeginUpdate();

				VCCodeModel model = ((VCCodeElement)element).CodeModel;
				
				TestSuiteCollector collector = new TestSuiteCollector();
				collector.Process(model);
				List<TestSuite> suitesInModel = new List<TestSuite>(collector.Suites);

				// Iterate through the current suites in the view and pull out any
				// that are no longer in the model.
				
				TreeNode solutionNode = testsTreeView.Nodes[0];

				for (int i = 0; i < solutionNode.Nodes.Count; )
				{
					TreeNode viewNode = solutionNode.Nodes[i];
					int index = suitesInModel.FindIndex(
						new Predicate<TestSuite>(delegate(TestSuite suite)
						{
							return suite.Name == viewNode.Text;
						}));

					if (index == -1)
						viewNode.Remove();
					else
					{
						// The suites that are left over will be those in the
						// model that aren't yet in the tree, so they need to
						// be added.

						suitesInModel.RemoveAt(index);
						i++;
					}
				}

				// Make sure a suite with the same name isn't already in the
				// view (this could happen if the name changes and then quickly
				// changes back before the event fires. If it's not there, add
				// it.

				foreach(TestSuite suite in suitesInModel)
					AddTestSuiteToNode(suite, solutionNode, null).Expand();

				testsTreeView.Sort();
				testsTreeView.EndUpdate();
			}
			else if (element is VCCodeFunction)
			{
				testsTreeView.BeginUpdate();

				VCCodeElement parent = (VCCodeElement)((VCCodeElement)element).Parent;

				TestCaseCollector collector = new TestCaseCollector();
				collector.Process(parent.Children);
				List<TestCase> casesInModel = new List<TestCase>(collector.TestCases);

				// Iterate through the current test cases for this suite in the
				// view and pull out any that are no longer in the model.

				TreeNode suiteNode = FindNodeForTestSuite(parent);

				if (suiteNode == null)
					return;

				for (int i = 0; i < suiteNode.Nodes.Count; )
				{
					TreeNode viewNode = suiteNode.Nodes[i];
					int index = casesInModel.FindIndex(
						new Predicate<TestCase>(delegate(TestCase testCase)
						{
							return testCase.Name == viewNode.Text;
						}));

					if (index == -1)
						viewNode.Remove();
					else
					{
						// The test cases that are left over will be those in
						// the model that aren't yet in the tree, so they need
						// to be added.

						casesInModel.RemoveAt(index);
						i++;
					}
				}

				foreach (TestCase testCase in casesInModel)
					AddTestCaseToNode((TestSuite)suiteNode.Tag, testCase, suiteNode, null);

				testsTreeView.Sort();
				testsTreeView.EndUpdate();
			}
		}

		public void DeleteElement(object parent, CodeElement element)
		{
			if (element is VCCodeClass || element is VCCodeStruct)
			{
				TreeNode node = FindNodeForTestSuite(element);
				
				if(node != null)
					node.Remove();
			}
			else if (element is VCCodeFunction)
			{
				VCCodeFunction function = (VCCodeFunction)element;

				TreeNode node = FindNodeForTestCase(parent, function);

				if(node != null)
					node.Remove();
			}
		}

        /// <summary> 
        /// Let this control process the mnemonics.
        /// </summary>
        protected override bool ProcessDialogChar(char charCode)
        {
              // If we're the top-level form or control, we need to do the mnemonic handling
              if (charCode != ' ' && ProcessMnemonic(charCode))
              {
                    return true;
              }

              return base.ProcessDialogChar(charCode);
        }


		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when a node is checked or unchecked to update the check
		/// states of the node's descendants.
		/// </summary>
		/// <param name="node">
		/// The node whose descendants need to be updated.
		/// </param>
		private void FixChecksDownward(TreeNode node)
		{
			foreach (TreeNode child in node.Nodes)
			{
				child.Checked = node.Checked;
				FixChecksDownward(child);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called when a node is checked or unchecked to update the check
		/// states of the node's ancestors.
		/// </summary>
		/// <param name="node">
		/// The node whose ancestors need to be updated.
		/// </param>
		private void FixChecksUpward(TreeNode node)
		{
			if (node == null)
				return;

			bool allChecked = true;

			foreach (TreeNode child in node.Nodes)
			{
				if (child.Checked == false)
				{
					allChecked = false;
					break;
				}
			}

			node.Checked = allChecked;

			FixChecksUpward(node.Parent);
		}

		private void testsTreeView_AfterCheck(object sender, TreeViewEventArgs e)
		{
			if (!fixingChecks)
			{
				fixingChecks = true;
				FixChecksDownward(e.Node);
				FixChecksUpward(e.Node.Parent);
				fixingChecks = false;
			}
		}

		public void ToggleAllTestsSelection()
		{
			bool allSelected = false;

			foreach (TreeNode node in testsTreeView.Nodes)
				allSelected = allSelected && node.Checked;

			allSelected = !allSelected;

			foreach (TreeNode node in testsTreeView.Nodes)
				node.Checked = allSelected;
		}

		public void RefreshFromSolution()
		{
			suites = CxxTestPackage.Instance.AllTestSuites;

			if (testsTreeView.Created)
			{
				testsTreeView.Invoke(new MethodInvoker(delegate()
				{
					PopulateTree(suites);
				}));
			}
        }

        private void PopulateTree(TestSuiteCollection suites)
        {
			fixingChecks = true;

			// Preserve the original selection so that we can reselect them.
			Dictionary<string, bool> selectedTests = SelectedTestCases;

            // clear the current contents
            testsTreeView.Nodes.Clear();

			TreeNode solutionNode = new TreeNode();
			HierarchyItem solHier = new HierarchyItem(suites.Solution as IVsHierarchy);

			// Don't do anything if the solution is empty.
			if (solHier.Name == null)
				return;

			solutionNode.Text = "Solution '" + solHier.Name + "'";
			solutionNode.ImageKey = solutionNode.SelectedImageKey = "Solution";
			testsTreeView.Nodes.Add(solutionNode);

            foreach(TestSuite suite in suites.Suites)
            {
				AddTestSuiteToNode(suite, solutionNode, selectedTests);
			}

			fixingChecks = false;

			testsTreeView.Sort();

			solutionNode.ExpandAll();
        }


		private class NodeComparer : IComparer
		{
			public int Compare(object x, object y)
			{
				TreeNode lhs = x as TreeNode;
				TreeNode rhs = y as TreeNode;

				if (lhs.Tag is TestSuite && rhs.Tag is TestSuite)
				{
					TestSuite lhsSuite = (TestSuite)lhs.Tag;
					TestSuite rhsSuite = (TestSuite)rhs.Tag;
					return string.Compare(lhsSuite.Name, rhsSuite.Name);
				}
				else if (lhs.Tag is TestCase && rhs.Tag is TestCase)
				{
					TestCase lhsCase = (TestCase)lhs.Tag;
					TestCase rhsCase = (TestCase)rhs.Tag;
					return lhsCase.LineNumber - rhsCase.LineNumber;
				}
				else
				{
					return 0;
				}
			}
		}

		private void testsTreeView_NodeMouseDoubleClick(object sender, TreeNodeMouseClickEventArgs e)
		{
			object item = e.Node.Tag;

			if (item is TestSuite)
			{
				TestSuite suite = (TestSuite)item;
				CxxTestPackage.Instance.OpenFileInEditor(suite.FullPath, suite.LineNumber);
			}
			else if (item is TestCase)
			{
				TestCase testCase = (TestCase)item;
				TestSuite suite = (TestSuite)e.Node.Parent.Tag;

				CxxTestPackage.Instance.OpenFileInEditor(suite.FullPath, testCase.LineNumber);
			}
		}

		private bool fixingChecks;
    }
}
