/*==========================================================================*\
 |  $Id: IProtocol.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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

namespace WebCAT.Submitter
{
	/// <summary>
	/// Defines the interface used by the submission engine to electronically
	/// submit a project. Submission protocols should implement this interface
	/// and put their custom submission functionality in the Submit method.
	/// </summary>
	public interface IProtocol
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Submits a project by opening an appropriate connection (based on
		/// the transport URI) and sending a package to the destination.
		/// </summary>
		/// <remarks>
		/// Upon obtaining a stream to which the data should be sent,
		/// implementors should call the PackageContentsIntoStream method on
		/// the manifest to ensure that the data is transmitted properly. This
		/// serves to separate the protocol handlers from the packagers such
		/// that neither needs to know how the other is implemented.
		/// </remarks>
		/// <param name="manifest">
		/// A submission manifest that contains the data and parameters to be
		/// submitted.
		/// </param>
		void Submit(SubmissionManifest manifest);

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets a value indicating whether the protocol sends back a response
		/// from the submission.
		/// </summary>
		bool HasResponse
		{
			get;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the response to the submission.
		/// </summary>
		string SubmissionResponse
		{
			get;
		}
	}
}
