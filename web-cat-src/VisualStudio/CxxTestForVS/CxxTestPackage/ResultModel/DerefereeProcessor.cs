/*==========================================================================*\
 |  $Id: DerefereeProcessor.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.IO;

namespace WebCAT.CxxTest.VisualStudio.ResultModel
{
	class DerefereeProcessor
	{
		public DerefereeProcessor(string resultsPath)
		{
			results = new DerefereeResults();

			try
			{
				using (TextReader textReader = File.OpenText(resultsPath))
				using (XmlReader reader = XmlReader.Create(textReader))
				{
					XmlDocument document = new XmlDocument();
					document.Load(reader);

					ProcessDocument(document);
				}
			}
			catch (Exception e)
			{
				// Handle this.
			}
		}

		public DerefereeResults DerefereeResults
		{
			get
			{
				return results;
			}
		}

		private void ProcessDocument(XmlNode documentNode)
		{
			foreach (XmlNode child in documentNode.ChildNodes)
			{
				if (child.Name == "dereferee")
					ProcessDereferee(child);
			}
		}

		private void ProcessDereferee(XmlNode derefereeNode)
		{
			int actualLeaks = 0;
			int.TryParse(derefereeNode.Attributes["actual-leak-count"].Value,
				out actualLeaks);

			results.ActualLeakCount = actualLeaks;

			foreach (XmlNode child in derefereeNode.ChildNodes)
			{
				if (child.Name == "leak")
					ProcessLeak(child);
				else if (child.Name == "summary")
					ProcessSummary(child);
			}
		}

		private void ProcessLeak(XmlNode leakNode)
		{
			MemoryLeak leak = new MemoryLeak(leakNode.Attributes);
			results.AddLeak(leak);

			foreach (XmlNode child in leakNode.ChildNodes)
			{
				if (child.Name == "stack-frame")
					leak.AddBacktraceFrame(ProcessStackFrame(child));
			}
		}

		private void ProcessSummary(XmlNode summaryNode)
		{
			int val;
			
			int.TryParse(summaryNode.Attributes["total-bytes-allocated"].Value,
				out val);
			results.TotalBytesAllocated = val;

			int.TryParse(summaryNode.Attributes["max-bytes-in-use"].Value,
				out val);
			results.MaximumBytesInUse = val;

			int.TryParse(summaryNode.Attributes["calls-to-new"].Value, out val);
			results.CallsToNew = val;

			int.TryParse(summaryNode.Attributes["calls-to-array-new"].Value,
				out val);
			results.CallsToArrayNew = val;

			int.TryParse(summaryNode.Attributes["calls-to-delete"].Value,
				out val);
			results.CallsToDelete = val;

			int.TryParse(summaryNode.Attributes["calls-to-array-delete"].Value,
				out val);
			results.CallsToArrayDelete = val;

			int.TryParse(summaryNode.Attributes["calls-to-delete-null"].Value,
				out val);
			results.CallsToDeleteNull = val;
		}

		private BacktraceFrame ProcessStackFrame(XmlNode frameNode)
		{
			string function = frameNode.Attributes["function"].Value;
			string file = null;
			int lineNumber = 0;

			if (frameNode.Attributes["location"] != null)
			{
				string fileLine = frameNode.Attributes["location"].Value;
				if (fileLine != null)
				{
					int colonPos = fileLine.LastIndexOf(':');

					if (colonPos != -1)
					{
						file = fileLine.Substring(0, colonPos);

						if (!int.TryParse(fileLine.Substring(colonPos + 1),
							out lineNumber))
						{
							file = fileLine;
							lineNumber = 0;
						}
					}
					else
					{
						file = fileLine;
					}
				}
			}

			return new BacktraceFrame(function, file, lineNumber);
		}

		private DerefereeResults results;
	}
}
