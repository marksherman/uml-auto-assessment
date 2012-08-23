/*==========================================================================*\
 |  $Id: FileProtocol.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
using System.IO;
using System.Net;

namespace WebCAT.Submitter.Internal.Protocols
{
	/// <summary>
	/// An implementation of IProtocol that permits submitting a file to a
	/// location on the file system (either local or a networked drive).
	/// </summary>
	internal class FileProtocol : IProtocol
	{
		//  -------------------------------------------------------------------
		public void Submit(SubmissionManifest manifest)
		{
			WebRequest req = WebRequest.Create(manifest.ResolvedTransport);

			if (req is FileWebRequest)
			{
				FileWebRequest request = (FileWebRequest)req;

				request.Method = WebRequestMethods.File.UploadFile;

				Stream stream = request.GetRequestStream();
				manifest.PackageContentsIntoStream(stream);
				stream.Close();
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Returns false since file: protocol submissions do not generate a
		/// response.
		/// </summary>
		public bool HasResponse
		{
			get
			{
				return false;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Returns null since file: protocol submissions do not generate a
		/// response.
		/// </summary>
		public string SubmissionResponse
		{
			get
			{
				return null;
			}
		}
	}
}
