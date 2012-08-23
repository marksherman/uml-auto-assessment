/*==========================================================================*\
 |  $Id: ProtocolRegistry.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
using System.Reflection;
using WebCAT.Submitter.Internal.Packagers;
using WebCAT.Submitter.Internal.Protocols;

namespace WebCAT.Submitter
{
	/// <summary>
	/// Manages the protocol handlers that are available for use by the
	/// submission engine.
	/// </summary>
	public class ProtocolRegistry
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Initializes the protocol registry and registers the built-in
		/// protocol handlers.
		/// </summary>
		static ProtocolRegistry()
		{
			protocolTypes = new Dictionary<string, Type>();

			// Register the built-in handlers.

			Add("http", typeof(HttpProtocol));
			Add("https", typeof(HttpProtocol));
			Add("file", typeof(FileProtocol));
			Add("ftp", typeof(FtpProtocol));
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Adds a protocol handler to the registry so that it can be
		/// referenced in the submission definitions.
		/// </summary>
		/// <param name="scheme">
		/// The URI scheme of the protocol to register.
		/// </param>
		/// <param name="type">
		/// The type of the protocol handler to be associated with this scheme.
		/// This type must implement the IProtocol interface.
		/// </param>
		public static void Add(string scheme, Type type)
		{
			protocolTypes.Add(scheme, type);
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Called by the submission engine internally to create a new instance
		/// of the protocol handler with the specified URI scheme.
		/// </summary>
		/// <param name="scheme">
		/// The URI scheme of the protocol handler to create.
		/// </param>
		/// <returns>
		/// An instance of the requested protocol handler, or null if no
		/// protocol handler with this URI scheme was registered.
		/// </returns>
		internal static IProtocol CreateProtocolInstance(string scheme)
		{
			if (protocolTypes.ContainsKey(scheme))
			{
				Type type = protocolTypes[scheme];
				return (IProtocol)Activator.CreateInstance(type);
			}
			else
			{
				return null;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets an array containing the URI schemes of all the protocol
		/// handlers that are currently registered.
		/// </summary>
		public static string[] RegisteredSchemes
		{
			get
			{
				string[] keys = new string[protocolTypes.Count];
				protocolTypes.Keys.CopyTo(keys, 0);
				return keys;
			}
		}


		// ==== Fields ========================================================

		// The dictionary that maps URI schemes to protocol handler types.
		private static Dictionary<string, Type> protocolTypes;
	}
}
