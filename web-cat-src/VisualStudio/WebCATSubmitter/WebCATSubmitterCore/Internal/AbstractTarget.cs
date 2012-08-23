/*==========================================================================*\
 |  $Id: AbstractTarget.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
	/// <summary>
	/// The common base class for all of the targets in the submission target
	/// tree. Much of the shared functionality of the various target types is
	/// implemented here.
	/// </summary>
	internal abstract class AbstractTarget : ITarget
	{
		protected AbstractTarget(AbstractTarget parent)
		{
			packager = DefaultPackager;
			this.parent = parent;
			ambiguityResolution = AmbiguityResolution.Exclude;
			transportParameters = new Dictionary<string, string>();
			packagerParameters = new Dictionary<string, string>();
		}

		public ITargetRoot Root
		{
			get
			{
				ITarget target = this;

				while (target.Parent != null)
				{
					target = target.Parent;
				}

				if (target is ITargetRoot)
				{
					return (ITargetRoot)target;
				}
				else
				{
					return null;
				}
			}
		}

		public ITarget Parent
		{
			get
			{
				return parent;
			}
		}

		public virtual AmbiguityResolution AmbiguityResolution
		{
			get
			{
				return ambiguityResolution;
			}
			set
			{
				ambiguityResolution = value;
			}
		}

		public virtual string[] IncludedFiles
		{
			get
			{
				if (includedFiles == null)
					return new string[0];
				else
					return includedFiles;
			}
			set
			{
				includedFiles = value;
			}
		}

		public virtual string[] ExcludedFiles
		{
			get
			{
				if (excludedFiles == null)
					return new string[0];
				else
					return excludedFiles;
			}
			set
			{
				excludedFiles = value;
			}
		}

		public virtual string[] RequiredFiles
		{
			get
			{
				if (requiredFiles == null)
					return new string[0];
				else
					return requiredFiles;
			}
			set
			{
				requiredFiles = value;
			}
		}

		public virtual string[] AllRequiredFiles
		{
			get
			{
				List<string> list = new List<string>();
				AddAllRequiredFilesToList(list);
				return list.ToArray();
			}
		}

		private void AddAllRequiredFilesToList(List<string> list)
		{
			list.AddRange(requiredFiles);

			if (parent != null)
			{
				parent.AddAllRequiredFilesToList(list);
			}
		}

		public virtual string Transport
		{
			get
			{
				if (transport != null)
				{
					return transport;
				}
				else if (parent != null)
				{
					return parent.Transport;
				}
				else
				{
					return null;
				}
			}
			set
			{
				transport = value;
			}
		}

		public virtual string LocalTransport
		{
			get
			{
				return transport;
			}
		}

		public virtual Dictionary<string, string> TransportParameters
		{
			get
			{
				if (transportParameters != null)
				{
					return transportParameters;
				}
				else if (parent != null)
				{
					return parent.TransportParameters;
				}
				else
				{
					return new Dictionary<string,string>();
				}
			}
			set
			{
				transportParameters = value;
			}
		}

		public virtual Dictionary<string, string> LocalTransportParameters
		{
			get
			{
				return transportParameters;
			}
		}

		public virtual string Packager
		{
			get
			{
				if (packager != null)
				{
					return packager;
				}
				else if (parent != null)
				{
					return parent.Packager;
				}
				else
				{
					return null;
				}
			}
			set
			{
				packager = value;
			}
		}

		public virtual string LocalPackager
		{
			get
			{
				return packager;
			}
		}

		public virtual Dictionary<string, string> PackagerParameters
		{
			get
			{
				if (packagerParameters != null)
				{
					return packagerParameters;
				}
				else if (parent != null)
				{
					return parent.PackagerParameters;
				}
				else
				{
					return new Dictionary<string,string>();
				}
			}
			set
			{
				packagerParameters = value;
			}
		}

		public virtual Dictionary<string, string> LocalPackagerParameters
		{
			get
			{
				return packagerParameters;
			}
		}

		public virtual ITarget[] Children
		{
			get
			{
				if (children == null)
				{
					return new ITarget[0];
				}
				else
				{
					return children;
				}
			}
			set
			{
				children = value;
			}
		}

		public abstract bool IsContainer
		{
			get;
		}

		public abstract bool IsNested
		{
			get;
		}

		public abstract bool IsActionable
		{
			get;
		}

		public abstract bool IsLoaded
		{
			get;
		}

		public virtual bool IsFileExcluded(string projectRelativePath)
		{
			bool localExclude = false;
			bool localInclude = false;

			// Check to see if the file is excluded locally.
			string[] excludes = ExcludedFiles;
			foreach (string exclude in excludes)
			{
				FilePattern pattern = new FilePattern(exclude);
				
				if (pattern.Matches(projectRelativePath))
				{
					localExclude = true;
					break;
				}
			}

			// Check to see if the file is explicitly included
			// locally.
			string[] includes = IncludedFiles;
			foreach (string include in includes)
			{
				FilePattern pattern = new FilePattern(include);

				if (pattern.Matches(projectRelativePath))
				{
					localInclude = true;
					break;
				}
			}

			if (localInclude && localExclude)
			{
				if (ambiguityResolution == AmbiguityResolution.Exclude)
					return true;
				else
					return false;
			}
			else if (localExclude)
			{
				return true;
			}
			else if (localInclude)
			{
				return false;
			}

			// If no explicit mention of the file was found,
			// try going up the assignment tree.
			if (parent != null)
				return parent.IsFileExcluded(projectRelativePath);
			else
			{
				if (includes.Length == 0)
					return false;
				else
					return true;
			}
		}

		protected void CopyFrom(ITarget source)
		{
			// Used for delay-loading. Takes the specified definition object
			// and copies it into the current object.

			AmbiguityResolution = source.AmbiguityResolution;
			Children = source.Children;
			IncludedFiles = source.IncludedFiles;
			ExcludedFiles = source.ExcludedFiles;
			RequiredFiles = source.RequiredFiles;

			Transport = source.Transport;
			TransportParameters = source.TransportParameters;

			Packager = source.Packager;
			PackagerParameters = source.PackagerParameters;
		}

		public abstract void Parse(XmlNode node);

		protected void ParseTransport(XmlNode parentNode)
		{
			XmlAttribute uriNode = parentNode.Attributes[Xml.Attributes.Uri];
			if(uriNode != null)
				transport = uriNode.Value;

			// Parse the parameter tags.
			foreach(XmlNode node in parentNode.ChildNodes)
			{
				string nodeName = node.LocalName;

				switch (nodeName)
				{
					case Xml.Elements.Param:
						ParseTransportParameter(node);
						break;

					case Xml.Elements.FileParam:
						ParseTransportFileParameter(node);
						break;
				}
			}
		}

		protected void ParsePackager(XmlNode parentNode)
		{
			XmlAttribute idNode = parentNode.Attributes[Xml.Attributes.Id];
			if(idNode != null)
				packager = idNode.Value;

			// Parse the parameter tags.
			foreach(XmlNode node in parentNode.ChildNodes)
			{
				string nodeName = node.LocalName;

				switch(nodeName)
				{
					case Xml.Elements.Param:
						ParsePackagerParameter(node);
						break;
				}
			}
		}

		protected void ParsePackagerParameter(XmlNode node)
		{
			XmlAttribute nameNode = node.Attributes[Xml.Attributes.Name];
			XmlAttribute valueNode = node.Attributes[Xml.Attributes.Value];
			
			if(nameNode != null && valueNode != null)
				packagerParameters.Add(nameNode.Value, valueNode.Value);
		}

		protected void ParseTransportParameter(XmlNode node)
		{
			XmlAttribute nameNode = node.Attributes[Xml.Attributes.Name];
			XmlAttribute valueNode = node.Attributes[Xml.Attributes.Value];
			
			if(nameNode != null && valueNode != null)
				transportParameters.Add(nameNode.Value, valueNode.Value);
		}

		protected void ParseTransportFileParameter(XmlNode node)
		{
			XmlAttribute nameNode = node.Attributes[Xml.Attributes.Name];
			XmlAttribute valueNode = node.Attributes[Xml.Attributes.Value];
			
			if(nameNode != null && valueNode != null)
				transportParameters.Add("$file." + nameNode.Value,
					valueNode.Value);
		}

		protected string ParseFilePattern(XmlNode node)
		{
			XmlAttribute patternNode = node.Attributes[Xml.Attributes.Pattern];
			
			if(patternNode != null)
				return patternNode.Value;
			else
				return null;
		}

		protected void ParseFilterAmbiguity(XmlNode node)
		{
			XmlAttribute choiceNode = node.Attributes[Xml.Attributes.Choice];
			
			if(choiceNode != null)
			{
				string value = choiceNode.Value;
				
				if("include" == value)
					AmbiguityResolution = AmbiguityResolution.Include;
				else
					AmbiguityResolution = AmbiguityResolution.Exclude;
			}
		}

		private const string DefaultPackager =
			"net.sf.webcat.eclipse.submitter.packagers.zip";

		private AbstractTarget parent;

		private string[] includedFiles;

		private string[] excludedFiles;

		private string[] requiredFiles;

		private AmbiguityResolution ambiguityResolution;

		private string transport;

		private Dictionary<string, string> transportParameters;

		private string packager;

		private Dictionary<string, string> packagerParameters;

		private ITarget[] children;
	}
}
