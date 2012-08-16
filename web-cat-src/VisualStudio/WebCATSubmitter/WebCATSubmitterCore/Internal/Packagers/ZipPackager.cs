/*==========================================================================*\
 |  $Id: ZipPackager.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
using ICSharpCode.SharpZipLib.Zip;
using ICSharpCode.SharpZipLib.Core;

namespace WebCAT.Submitter.Internal.Packagers
{
	/// <summary>
	/// An implementation of IPackager that supports writing a set of
	/// submittable items to a ZIP archive.
	/// </summary>
	internal class ZipPackager : IPackager
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Starts a new package that will be written to the specified stream.
		/// </summary>
		/// <param name="stream">
		/// The stream to which the package will be written.
		/// </param>
		/// <param name="parameters">
		/// A dictionary of parameters that are specified in the target
		/// definition.
		/// </param>
		public void StartPackage(Stream stream,
			Dictionary<string, string> parameters)
		{
			zipStream = new ZipOutputStream(stream);
			zipFactory = new ZipEntryFactory();

			buffer = new byte[BufferSize];
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Adds a submittable item to the package that was started by a call
		/// to StartPackage.
		/// </summary>
		/// <param name="item"></param>
		public void AddSubmittableItem(ISubmittableItem item)
		{
			if (item.Kind == SubmittableItemKind.Folder)
			{
				if (item.Filename != "" && item.Filename != "\\")
				{
					ZipEntry entry =
						zipFactory.MakeDirectoryEntry(item.Filename);

					zipStream.PutNextEntry(entry);
					zipStream.CloseEntry();
				}
			}
			else if (item.Kind == SubmittableItemKind.File)
			{
				Stream itemStream = item.GetStream();

				// If we try to stream the file directly into the zip file
				// without determining its size for the zip entry, the archive
				// will not expand properly under OS X. Until we can fix this,
				// we stream each file entirely into memory to compute its
				// length, then stream the memory buffer out to the zip file.

				MemoryStream memStream = new MemoryStream();
				StreamUtils.Copy(itemStream, memStream, buffer);
				itemStream.Close();
				
				long length = memStream.Length;

				ZipEntry entry = zipFactory.MakeFileEntry(item.Filename);
				entry.Size = length;
				zipStream.PutNextEntry(entry);

				memStream.Seek(0, SeekOrigin.Begin);
				StreamUtils.Copy(memStream, zipStream, buffer);

				zipStream.CloseEntry();
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Finalizes the package that was started by a call to StartPackage.
		/// This method should flush the stream but NOT close it.
		/// </summary>
		public void EndPackage()
		{
			zipStream.Finish();
			zipStream.Flush();
		}

		
		// ==== Fields ========================================================

		// The size of the buffer, in bytes, used to copy data between streams.
		private const int BufferSize = 32768;

		// The buffer used to copy data between streams.
		private byte[] buffer;

		// A wrapper around the package stream that allows us to write out a
		// set of files as a ZIP archive.
		private ZipOutputStream zipStream;

		// An instance of a factory that lets us create ZIP entries for files
		// and directories.
		private ZipEntryFactory zipFactory;
	}
}
