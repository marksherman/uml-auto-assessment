/*==========================================================================*\
 |  $Id: CxxTestResultsToolWindowPanel.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using WebCAT.CxxTest.VisualStudio.Model;
using WebCAT.CxxTest.VisualStudio.ResultModel;
using System.Reflection;
using WebCAT.CxxTest.VisualStudio.Controls;
using WebCAT.CxxTest.VisualStudio.Utility;

namespace WebCAT.CxxTest.VisualStudio.ToolWindows
{
    public partial class CxxTestResultsToolWindowPanel : UserControl
    {
		public CxxTestResultsToolWindowPanel()
        {
            InitializeComponent();

			hierarchyButton.Image = new Bitmap(
				Assembly.GetExecutingAssembly().GetManifestResourceStream(
				"WebCAT.CxxTest.VisualStudio.Resources.ToolbarImages.Hierarchy.gif"));

			memoryButton.Image = new Bitmap(
				Assembly.GetExecutingAssembly().GetManifestResourceStream(
				"WebCAT.CxxTest.VisualStudio.Resources.ToolbarImages.Memory.gif"));
		}


		public void RefreshFromLastRun()
		{
			containingProject = CxxTestPackage.Instance.LastRunProject;
			suiteResults = CxxTestPackage.Instance.SuiteResultsOfLastRun;
			derefereeResults = CxxTestPackage.Instance.DerefereeResultsOfLastRun;

			if(hierarchyButton.Checked)
				SwitchToHierarchyView();
			else
				hierarchyButton.Checked = true;

			int total, failures, errors;
			CountTestCases(out total, out failures, out errors);

			testResultsProgress.Minimum = 0;
			testResultsProgress.Maximum = total;
			testResultsProgress.Value = total - failures - errors;
			testResultsProgress.Mode = PassFailBarMode.Bar;

			runsLabel.Text = string.Format("Runs: {0}", total);
			errorsLabel.Text = string.Format("Errors: {0}", errors);
			failuresLabel.Text = string.Format("Failures: {0}", failures);
		}

		private void CountTestCases(out int total, out int failures, out int errors)
		{
			total = 0;
			failures = 0;
			errors = 0;

			foreach (TestSuiteResult suite in suiteResults)
			{
				foreach (ITestSuiteChildResult child in suite.Children(false))
				{
					total++;

					if (child.Status == TestResultStatus.Error)
						errors++;
					else if (child.Status == TestResultStatus.Failed)
						failures++;
				}
			}
		}

		public void SetToTextMode(string text)
		{
			testResultsProgress.Text = text;
			testResultsProgress.Mode = PassFailBarMode.Text;

			runsLabel.Text = "Runs:";
			errorsLabel.Text = "Errors:";
			failuresLabel.Text = "Failures:";

			masterView.Nodes.Clear();
			detailsView.Nodes.Clear();
		}

		private void hierarchyButton_CheckedChanged(object sender, EventArgs e)
		{
			if (hierarchyButton.Checked)
			{
				memoryButton.Checked = false;
				SwitchToHierarchyView();
			}
		}

		private void memoryButton_CheckedChanged(object sender, EventArgs e)
		{
			if (memoryButton.Checked)
			{
				hierarchyButton.Checked = false;
				SwitchToMemoryView();
			}
		}

		private string GetImageForStatus(string prefix, TestResultStatus status)
		{
			string key;

			switch (status)
			{
				case TestResultStatus.OK:
					key = "OK";
					break;

				case TestResultStatus.Warning:
					key = "Warning";
					break;

				case TestResultStatus.Error:
					key = "Error";
					break;

				case TestResultStatus.Failed:
					key = "Failure";
					break;

				default:
					key = "";
					break;
			}

			return prefix + key;
		}

		private void SwitchToHierarchyView()
		{
			masterView.Nodes.Clear();
			detailsView.Nodes.Clear();

			if (suiteResults == null)
			{
				// Handle this error.
				return;
			}

			foreach (TestSuiteResult suite in suiteResults)
			{
				TreeNode suiteNode = new TreeNode(suite.Name);
				suiteNode.ImageKey = suiteNode.SelectedImageKey =
					GetImageForStatus("Suite", suite.Status);
				suiteNode.Tag = suite;
				masterView.Nodes.Add(suiteNode);

				foreach (ITestSuiteChildResult suiteChild in suite.Children(false))
				{
					TreeNode childNode = new TreeNode(suiteChild.ToString());
					childNode.ImageKey = childNode.SelectedImageKey =
						GetImageForStatus("Test", suiteChild.Status);
					childNode.Tag = suiteChild;
					suiteNode.Nodes.Add(childNode);
				}

				if (suite.Status != TestResultStatus.OK)
					suiteNode.Expand();
			}
		}

		private void SwitchToMemoryView()
		{
			masterView.Nodes.Clear();
			detailsView.Nodes.Clear();

			if (derefereeResults != null)
			{
				TreeNode rootNode = new TreeNode();
				rootNode.ImageKey = rootNode.SelectedImageKey = "Leaks";

				int actualLeaks = derefereeResults.ActualLeakCount;
				MemoryLeak[] leaks = derefereeResults.Leaks;

				if (actualLeaks != leaks.Length)
				{
					rootNode.Text = string.Format(
						"{0} leaks detected (showing the first {1})",
						actualLeaks, leaks.Length);
				}
				else
				{
					rootNode.Text = string.Format(
						"{0} leaks detected", actualLeaks);
				}

				masterView.Nodes.Add(rootNode);

				foreach (MemoryLeak leak in leaks)
				{
					TreeNode leakNode = new TreeNode();
					leakNode.Tag = leak;

					string array = leak.IsArray ? "array" : "object";

					if (leak.Type != null)
					{
						leakNode.Text = string.Format("{0} ({1}-byte {2} at address {3})",
							leak.Type, leak.Size, array, leak.Address);
					}
					else
					{
						leakNode.Text = string.Format("{0}-byte {1} at address {2}",
							leak.Size, array, leak.Address);
					}

					if (leak.IsArray)
					{
						leakNode.ImageKey = leakNode.SelectedImageKey =
							"LeakArray";
					}
					else
					{
						leakNode.ImageKey = leakNode.SelectedImageKey =
							"LeakNonArray";
					}

					rootNode.Nodes.Add(leakNode);
				}
			}

			masterView.ExpandAll();
		}

		private void masterView_AfterSelect(object sender, TreeViewEventArgs e)
		{
			object item = e.Node.Tag;

			detailsView.Nodes.Clear();

			if (item is TestCaseResult)
			{
				TestCaseResult testCase = (TestCaseResult)item;
				AddAssertionsToDetail(testCase.Assertions, detailsView.Nodes);
			}
			else if (item is TestSuiteErrorResult)
			{
				TestSuiteErrorResult suiteError = (TestSuiteErrorResult)item;
				
				TreeNode node = new TreeNode();
				node.Text = suiteError.Message;
				node.ImageKey = node.SelectedImageKey =
					GetImageForStatus("Suite", suiteError.Status);
				detailsView.Nodes.Add(node);

				AddBacktraceToDetail(suiteError.Backtrace, node.Nodes);
			}
			else if (item is MemoryLeak)
			{
				MemoryLeak leak = (MemoryLeak)item;

				TreeNode node = new TreeNode();
				node.Text = "Allocated in:";
				node.ImageKey = node.SelectedImageKey = e.Node.ImageKey;
				detailsView.Nodes.Add(node);

				AddBacktraceToDetail(leak.Backtrace, node.Nodes);
			}

			detailsView.ExpandAll();
		}

		private void AddAssertionsToDetail(ITestAssertion[] assertions,
			TreeNodeCollection nodes)
		{
			foreach (ITestAssertion assertion in assertions)
			{
				TreeNode node = new TreeNode();
				node.Tag = assertion;
				node.Text = assertion.GetMessage(true).Replace("\n", "");
				node.ImageKey = node.SelectedImageKey =
					GetImageForStatus("Assert", assertion.Status);
				nodes.Add(node);

				AddBacktraceToDetail(assertion.Backtrace, node.Nodes);
				node.Expand();
			}
		}

		private void AddBacktraceToDetail(BacktraceFrame[] backtrace,
			TreeNodeCollection nodes)
		{
			if (backtrace != null)
			{
				foreach (BacktraceFrame frame in backtrace)
				{
					TreeNode node = new TreeNode();

					// If the filename is an absolute path to a file contained
					// in the project directory, just make it relative to the
					// project. Otherwise, use the whole absolute path.

					string text = frame.Function;

					string filename = frame.Filename;

					if(filename != null)
					{
						if (Path.IsPathRooted(filename))
						{
							string relPath = PathUtils.RelativePathTo(
								containingProject.ProjectDirectory, filename);

							if (!relPath.StartsWith(".."))
								filename = relPath;
						}

						text += " in " + filename;

						if(frame.LineNumber != 0)
							text += ":" + frame.LineNumber.ToString();
					}

					node.Tag = frame;
					node.Text = text;
					node.ImageKey = node.SelectedImageKey = "BacktraceFrame";
					nodes.Add(node);
				}
			}
		}

		private void masterView_NodeMouseDoubleClick(object sender, TreeNodeMouseClickEventArgs e)
		{
			HandleNodeDoubleClick(e.Node);
		}

		private void detailsView_NodeMouseDoubleClick(object sender, TreeNodeMouseClickEventArgs e)
		{
			HandleNodeDoubleClick(e.Node);
		}

		private void HandleNodeDoubleClick(TreeNode node)
		{
			object obj = node.Tag;

			string filename = null;
			int lineNumber = 1;

			if (obj is TestSuiteResult)
			{
				TestSuiteResult suite = (TestSuiteResult)obj;

				filename = suite.Filename;
				lineNumber = suite.LineNumber;
			}
			else if (obj is ITestSuiteChildResult)
			{
				ITestSuiteChildResult child = (ITestSuiteChildResult)obj;

				filename = ((TestSuiteResult)child.Parent).Filename;
				lineNumber = child.LineNumber;
			}
			else if (obj is ITestAssertion)
			{
				ITestAssertion assertion = (ITestAssertion)obj;

				TestCaseResult testCase = (TestCaseResult)assertion.Parent;
				TestSuiteResult suite = (TestSuiteResult)testCase.Parent;

				filename = suite.Filename;
				lineNumber = assertion.LineNumber;
			}
			else if (obj is BacktraceFrame)
			{
				BacktraceFrame frame = (BacktraceFrame)obj;

				filename = frame.Filename;
				lineNumber = frame.LineNumber;
			}

			if (filename != null)
			{
				CxxTestPackage.Instance.OpenFileInEditor(filename, lineNumber);
			}
		}

		private HierarchyItem containingProject;
		private TestSuiteResult[] suiteResults;
		private DerefereeResults derefereeResults;
	}
}
