/*==========================================================================*\
 |  $Id: TargetRoot.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT Electronic Submission engine for the
 |	.NET framework.
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

namespace WebCAT.Submitter.Internal
{
	internal class TargetRoot : AbstractTarget, ITargetRoot
	{
		public TargetRoot() : base(null)
		{
		}

		public override bool IsContainer
		{
			get
			{
				return true;
			}
		}

		public override bool IsActionable
		{
			get
			{
				return false;
			}
		}

		public override bool IsNested
		{
			get
			{
				return false;
			}
		}

		public override bool IsLoaded
		{
			get
			{
				return true;
			}
		}

		public override void Parse(XmlNode node)
		{
			string nodeName = node.LocalName;

			switch (nodeName)
			{
				case Xml.Elements.SubmissionTargets:
					ParseSubmissionTargets(node);
					break;
			}
		}

		private void ParseSubmissionTargets(XmlNode parentNode)
		{
			List<string> includes = new List<string>();
			List<string> excludes = new List<string>();
			List<string> required = new List<string>();
			List<ITarget> children = new List<ITarget>();

			foreach(XmlNode node in parentNode.ChildNodes)
			{
				string nodeName = node.LocalName;

				switch(nodeName)
				{
					case Xml.Elements.FilterAmbiguity:
						ParseFilterAmbiguity(node);
						break;

					case Xml.Elements.Include:
						includes.Add(ParseFilePattern(node));
						break;

					case Xml.Elements.Exclude:
						excludes.Add(ParseFilePattern(node));
						break;

					case Xml.Elements.Required:
						required.Add(ParseFilePattern(node));
						break;

					case Xml.Elements.Transport:
						ParseTransport(node);
						break;

					case Xml.Elements.Packager:
						ParsePackager(node);
						break;

					case Xml.Elements.AssignmentGroup:
						children.Add(ParseAssignmentGroup(node));
						break;
				
					case Xml.Elements.ImportGroup:
						children.Add(ParseImportGroup(node));
						break;

					case Xml.Elements.Assignment:
						children.Add(ParseAssignment(node));
						break;
				}
			}

			IncludedFiles = includes.ToArray();
			ExcludedFiles = excludes.ToArray();
			RequiredFiles = required.ToArray();
			Children = children.ToArray();
		}

		private ITarget ParseAssignmentGroup(XmlNode node)
		{
			TargetAssignmentGroup group = new TargetAssignmentGroup(this);
			group.Parse(node);
			return group;
		}

		private ITarget ParseImportGroup(XmlNode node)
		{
			TargetImportGroup group = new TargetImportGroup(this);
			group.Parse(node);
			return group;
		}

		private ITarget ParseAssignment(XmlNode node)
		{
			TargetAssignment assignment = new TargetAssignment(this);
			assignment.Parse(node);
			return assignment;
		}
	}
}
