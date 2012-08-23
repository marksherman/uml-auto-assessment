/*==========================================================================*\
 |  $Id: HttpProtocol.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
	public class HttpProtocol : IProtocol
	{
		public void Submit(SubmissionManifest parameters)
		{
			WebRequest req = WebRequest.Create(parameters.ResolvedTransport);

			if (req is HttpWebRequest)
			{
				HttpWebRequest request = (HttpWebRequest)req;
				MultipartBuilder multipart = new MultipartBuilder(request);

				ITarget assignment = parameters.Assignment;

				foreach (string key in assignment.TransportParameters.Keys)
				{
					string value = assignment.TransportParameters[key];
					string resolvedValue = parameters.ResolveParameter(value);

					if (key.StartsWith("$file."))
					{
						string trueKey = key.Substring("$file.".Length);

						Stream stream = multipart.BeginWriteFile(trueKey,
							resolvedValue, "application/octet-stream");
						parameters.PackageContentsIntoStream(stream);
						multipart.EndWriteFile();
					}
					else
					{
						multipart.WriteParameter(key, resolvedValue);
					}
				}

				multipart.Close();

				WebResponse response = request.GetResponse();
				Stream respStream = response.GetResponseStream();

				StringBuilder responseHolder = new StringBuilder();
				StreamReader reader = new StreamReader(respStream, Encoding.UTF8);
				char[] buffer = new char[BufferSize];

				int len;
				while ((len = reader.Read(buffer, 0, BufferSize)) > 0)
				{
					responseHolder.Append(buffer, 0, len);
				}

				response.Close();

				this.response = responseHolder.ToString();
			}
		}

		public bool HasResponse
		{
			get
			{
				return true;
			}
		}

		public string SubmissionResponse
		{
			get
			{
				return response;
			}
		}

		private const int BufferSize = 32768;

		private string response;
	}
}
