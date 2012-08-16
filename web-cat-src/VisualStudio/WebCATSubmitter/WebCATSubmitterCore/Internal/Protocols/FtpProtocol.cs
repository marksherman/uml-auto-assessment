/*==========================================================================*\
 |  $Id: FtpProtocol.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
using System.Net;
using System.IO;

namespace WebCAT.Submitter.Internal.Protocols
{
	class FtpProtocol : IProtocol
	{
		public void Submit(SubmissionManifest manifest)
		{
			WebRequest req =
				WebRequest.Create(manifest.ResolvedTransport);

			if (req is FtpWebRequest)
			{
				FtpWebRequest request = (FtpWebRequest)req;

				request.Method = WebRequestMethods.Ftp.UploadFile;
				request.UseBinary = true;

				Stream stream = request.GetRequestStream();
				manifest.PackageContentsIntoStream(stream);
				stream.Close();

				// Send the request by getting the response.

				request.GetResponse();
			}
		}

		public bool HasResponse
		{
			get
			{
				return false;
			}
		}

		public string SubmissionResponse
		{
			get
			{
				return null;
			}
		}
	}
}
