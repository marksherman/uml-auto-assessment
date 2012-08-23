/*==========================================================================*\
 |  $Id: TargetImportGroup.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
	internal class TargetImportGroup : AbstractTarget, ITargetImportGroup
	{
		public TargetImportGroup(AbstractTarget parent)
			: base(parent)
		{
			loaded = false;
		}

		// ------------------------------------------------------------------------
		public override bool IsContainer
		{
			get
			{
				return true;
			}
		}

		// ------------------------------------------------------------------------
		public override bool IsActionable
		{
			get
			{
				return false;
			}
		}

		// ------------------------------------------------------------------------
		public override bool IsNested
		{
			get
			{
				return true;
			}
		}

		// ------------------------------------------------------------------------
		public override bool IsLoaded
		{
			get
			{
				return loaded;
			}
		}

		// ------------------------------------------------------------------------
		public string Name
		{
			get
			{
				return name;
			}
			set
			{
				name = value;
			}
		}

		// ------------------------------------------------------------------------
		public string Href
		{
			get
			{
				return href;
			}
			set
			{
				href = value;
			}
		}

		// ------------------------------------------------------------------------
		public bool Hidden
		{
			get
			{
				return hidden;
			}
			set
			{
				hidden = value;
			}
		}

		// ------------------------------------------------------------------------
		public override AmbiguityResolution AmbiguityResolution
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.AmbiguityResolution;
			}
		}

		// ------------------------------------------------------------------------
		public override string[] IncludedFiles
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.IncludedFiles;
			}
		}

		// ------------------------------------------------------------------------
		public override string[] ExcludedFiles
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.ExcludedFiles;
			}
		}

		// ------------------------------------------------------------------------
		public override string[] RequiredFiles
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.RequiredFiles;
			}
		}

		// ------------------------------------------------------------------------
		public override string Transport
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.Transport;
			}
		}

		// ------------------------------------------------------------------------
		public override Dictionary<string, string> TransportParameters
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.TransportParameters;
			}
		}

		// ------------------------------------------------------------------------
		public override string Packager
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.Packager;
			}
		}

		// ------------------------------------------------------------------------
		public override Dictionary<string, string> PackagerParameters
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.PackagerParameters;
			}
		}

		// ------------------------------------------------------------------------
		public override ITarget[] Children
		{
			get
			{
				if (!loaded)
					LoadImportedDefinitions();

				return base.Children;
			}
		}

		// ------------------------------------------------------------------------
		public override void Parse(XmlNode parentNode)
		{
			XmlAttribute nameNode = parentNode.Attributes[Xml.Attributes.Name];
			XmlAttribute hrefNode = parentNode.Attributes[Xml.Attributes.Href];
			XmlAttribute hiddenNode =
				parentNode.Attributes[Xml.Attributes.Hidden];

			string hiddenString = null;

			if(nameNode != null)
				name = nameNode.Value;

			if(hrefNode != null)
				href = hrefNode.Value;

			if(hiddenNode != null)
				hiddenString = hiddenNode.Value;

			try
			{
				hidden = (hiddenString != null) ?
					Boolean.Parse(hiddenString) : false;
			}
			catch (Exception)
			{
			}
		}

		// ------------------------------------------------------------------------
		private void LoadImportedDefinitions()
		{
			SubmissionEngine engine = new SubmissionEngine();

			try
			{
				engine.OpenDefinitions(new Uri(href));
				ITarget root = engine.Root;

				CopyFrom(root);

				loaded = true;
			}
			catch(Exception)
			{
			}
		}

		private string name;

		private bool hidden;

		private bool loaded;

		private string href;
	}
}
