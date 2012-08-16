/*==========================================================================*\
 |  $Id: PackagerRegistry.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
using WebCAT.Submitter.Internal.Packagers;

namespace WebCAT.Submitter
{
	/// <summary>
	/// Manages the packagers that are available for use by the submission
	/// engine.
	/// </summary>
	public class PackagerRegistry
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Initializes the packager registry and registers the built-in
		/// packagers.
		/// </summary>
		static PackagerRegistry()
		{
			packagerTypes = new Dictionary<string, Type>();

			// Old ID for compatibility with the original Java/Eclipse version
			// of the submitter.

			Add("net.sf.webcat.eclipse.submitter.packagers.zip",
				typeof(ZipPackager));

			// Preferred new ID, removing language- and platform-specific
			// name parts.

			Add("net.sf.webcat.submitter.packagers.zip",
				typeof(ZipPackager));

			// It doesn't make sense for the .NET version of the submitter
			// engine to have a dedicated JAR packager, but since the format
			// is identical to ZIP (with the exception of manifest file
			// properties), we add support here for users who may be
			// defaulting to JARs in their submission definitions.

			Add("net.sf.webcat.eclipse.submitter.packagers.jar",
				typeof(ZipPackager));

			Add("net.sf.webcat.submitter.packagers.jar",
				typeof(ZipPackager));
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Adds a packager to the registry so that it can be referenced in the
		/// submission definitions.
		/// </summary>
		/// <param name="id">
		/// The identifier of the packager. To guarantee uniqueness, we
		/// recommend using Java-style package naming conventions (that is,
		/// reverse domain name).
		/// </param>
		/// <param name="type">
		/// The type of the packager to be associated with this identifier.
		/// This type must implement the IPackager interface.
		/// </param>
		public static void Add(string id, Type type)
		{
			packagerTypes.Add(id, type);
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called by the submission engine internally to create a new instance
		/// of the packager with the specified identifier.
		/// </summary>
		/// <param name="id">
		/// The identifier of the packager to create.
		/// </param>
		/// <returns>
		/// An instance of the requested packager, or null if no packager with
		/// this identifier was registered.
		/// </returns>
		internal static IPackager CreatePackagerInstance(string id)
		{
			if (packagerTypes.ContainsKey(id))
			{
				Type type = packagerTypes[id];
				return (IPackager)Activator.CreateInstance(type);
			}
			else
			{
				return null;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets an array containing the identifiers of all the packagers that
		/// are currently registered.
		/// </summary>
		public static string[] RegisteredPackagerIds
		{
			get
			{
				string[] keys = new string[packagerTypes.Count];
				packagerTypes.Keys.CopyTo(keys, 0);
				return keys;
			}
		}


		// ==== Fields ========================================================

		// The dictionary that maps identifiers to packager types.
		private static Dictionary<string, Type> packagerTypes;
	}
}
