/*==========================================================================*\
 |  $Id: ITarget.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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

namespace WebCAT.Submitter
{
	/// <summary>
	/// The interface for all submission target in the system. The root and all
	/// assignment groups and assignments implement this common interface. It
	/// provides methods for traversing the submission target tree as well as
	/// for accessing inherited properties, such as included and excluded
	/// files.
	/// </summary>
	public interface ITarget
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the parent node to this node in the tree.
		/// </summary>
		ITarget Parent
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the root of the tree that this object is contained in.
		/// </summary>
		ITargetRoot Root
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether file filter ambiguities are
		/// resolved by including the file or excluding it.
		/// </summary>
		AmbiguityResolution AmbiguityResolution
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the included file patterns for this node (and only this node).
		/// </summary>
		string[] IncludedFiles
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the excluded file patterns for this node (and only this node).
		/// </summary>
		string[] ExcludedFiles
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the required file patterns for this node (and only this node).
		/// </summary>
		string[] RequiredFiles
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Recursively walks up the tree from the current node and collects a
		/// list of all the required file patterns for a submission at this
		/// level.
		/// </summary>
		string[] AllRequiredFiles
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Returns the transport URI for submission targets at this level in
		/// the tree. This function walks up the tree to find an inherited
		/// transport, if necessary.
		/// </summary>
		string Transport
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the transport URI for submission targets at this level in the
		/// tree. This function does not walk up the tree to find an inherited
		/// transport -- it returns the transport specified for this node only.
		/// </summary>
		string LocalTransport
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a dictionary containing the transport parameter name/value
		/// pairs for this node. This function walks up the tree to find an
		/// inherited transport, if necessary.
		/// </summary>
		Dictionary<string, string> TransportParameters
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a dictionary containing the transport parameter name/value
		/// pairs for this node. This function does not walk up the tree to
		/// find an inherited transport -- it returns the transport specified
		/// for this node only.
		/// </summary>
		Dictionary<string, string> LocalTransportParameters
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the identifier of the packager used to submit the project.
		/// This function walks up the tree to find an inherited packager, if
		/// necessary.
		/// </summary>
		string Packager
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the identifier of the packager used to submit the project.
		/// This function does not walk up the tree to find an inherited
		/// packager -- it returns the packager specified for this node only.
		/// </summary>
		string LocalPackager
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a dictionary containing the packager parameter name/value
		/// pairs for this node.
		/// </summary>
		Dictionary<string, string> PackagerParameters
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a dictionary containing the packager parameter name/value
		/// pairs for this node. This function does not walk up the tree to
		/// find an inherited packager -- it returns the transport specified
		/// for this node only.
		/// </summary> 
		Dictionary<string, string> LocalPackagerParameters
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the children of this node.
		/// </summary>
		ITarget[] Children
		{
			get;
			set;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Recursively walks up the submission target tree to determine if the
		/// specified file should be excluded from the submission.
		/// </summary>
		bool IsFileExcluded(string projectRelativePath);

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a value specifying whether the node may contain children.
		/// </summary>
		bool IsContainer
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a value specifying whether the node should be displayed at the
		/// same level as its parent, or if it should be nested underneath it.
		/// </summary>
		bool IsNested
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a value specifying whether an action can be taken on this
		/// node. In a wizard, for example, this would enable the Next/Finish
		/// button so the user can continue with the submission.
		/// </summary>
		bool IsActionable
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a value specifying whether the node has been loaded into local
		/// memory. This is always true for most nodes except imported groups,
		/// which return true only if the external XML file has already been
		/// processed.
		/// </summary>
		bool IsLoaded
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Parses the specified XML node and builds a subtree from the data.
		/// </summary>
		void Parse(XmlNode node);
	}


	// ==== Related Types =====================================================

	/// <summary>
	/// Values in this enumeration determine how to resolve an ambiguity when
	/// a file is both included and excluded at the same level.
	/// </summary>
	public enum AmbiguityResolution
	{
		/// <summary>
		/// Specifies that a file should be included in the event that it
		/// satisfies an include and exclude directive at the same level of the
		/// tree.
		/// </summary>
		Include,

		/// <summary>
		/// Specifies that a file should be excluded in the event that it
		/// satisfies an include and exclude directive at the same level of the
		/// tree.
		/// </summary>
		Exclude
	}
}
