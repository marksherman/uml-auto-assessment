/*==========================================================================*\
 |  $Id: MultipartBuilder.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
using System.Net.Cache;
using System.IO;

namespace WebCAT.Submitter.Internal
{
	/// <summary>
	/// A utility class to assist in the creation of a multipart/form-data
	/// HTTP request.
	/// </summary>
	internal class MultipartBuilder
	{
		//  -------------------------------------------------------------------
		static MultipartBuilder()
		{
			BoundaryString = GenerateBoundaryString();
			BoundaryBytes = Encoding.ASCII.GetBytes(BoundaryString);
		}


		//  -------------------------------------------------------------------
		public MultipartBuilder(HttpWebRequest request)
		{
			this.request = request;

			RequestCachePolicy cache = new RequestCachePolicy(
				RequestCacheLevel.NoCacheNoStore);
			request.CachePolicy = cache;

			request.Method = WebRequestMethods.Http.Post;
			request.ContentType = "multipart/form-data; boundary=" +
				BoundaryString;
			request.KeepAlive = true;
			request.Credentials = CredentialCache.DefaultCredentials;

			stream = request.GetRequestStream();
		}


		//  -------------------------------------------------------------------
		private void WriteBoundary()
		{
			Write("--");
			Write(BoundaryBytes);
			WriteLine();
		}


		//  -------------------------------------------------------------------
		private void Write(string s)
		{
			Write(Encoding.UTF8.GetBytes(s));
		}


		//  -------------------------------------------------------------------
		private void Write(byte[] bytes)
		{
			stream.Write(bytes, 0, bytes.Length);
		}


		//  -------------------------------------------------------------------
		private void WriteLine()
		{
			Write("\r\n");
		}


		//  -------------------------------------------------------------------
		private void WriteLine(string s)
		{
			Write(s);
			WriteLine();
		}


		//  -------------------------------------------------------------------
		public void WriteParameter(string name, string value)
		{
			WriteBoundary();
			Write(String.Format(ParameterFormat, name, value));
		}


		//  -------------------------------------------------------------------
		public Stream BeginWriteFile(string name, string filename,
			string contentType)
		{
			WriteBoundary();
			Write(String.Format(FileHeaderFormat,
				name, filename, contentType));

			return stream;
		}


		//  -------------------------------------------------------------------
		public void EndWriteFile()
		{
			WriteLine();
		}


		//  -------------------------------------------------------------------
		public void Close()
		{
			WriteBoundary();
			WriteLine("--");
			
			stream.Close();
		}


		//  -------------------------------------------------------------------
		private static string GenerateBoundaryString()
		{
			Random rand = new Random();
			char[] chars = new char[rand.Next(11) + 30];

			for (int i = 0; i < chars.Length; i++)
			{
				chars[i] = BoundaryCharacters[
					rand.Next(BoundaryCharacters.Length)];
			}

			return new string(chars);
		}


		// ==== Fields ========================================================

		private static readonly char[] BoundaryCharacters =
			"-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".
			ToCharArray();

		private HttpWebRequest request;

		private Stream stream;

		private static readonly string BoundaryString;

		private static readonly byte[] BoundaryBytes;

		private const string ParameterFormat =
			"Content-Disposition: form-data; name=\"{0}\";\r\n\r\n{1}\r\n";

		private const string FileHeaderFormat =
			"Content-Disposition: form-data; name=\"{0}\"; " +
			"filename=\"{1}\"\r\nContent-Type: {2}\r\n\r\n";
	}
}
