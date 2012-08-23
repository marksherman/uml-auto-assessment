/*==========================================================================*\
 |  $Id: SubmissionManifest.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
using System.Text.RegularExpressions;
using System.Text;
using System.IO;
using WebCAT.Submitter.Utility;

namespace WebCAT.Submitter
{
	/// <summary>
	/// Bundles any necessary information required to make a submission, such
	/// as the remote username and password, the array of submittable items to
	/// be delivered, and the submission target to deliver them to.
	/// </summary>
	public class SubmissionManifest
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the target assignment to send the submission to.
		/// </summary>
		public ITargetAssignment Assignment
		{
			get
			{
				return assignment;
			}

			set
			{
				assignment = value;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the array of submittable items that represent the tree
		/// of folders and files to be delivered to the submission target.
		/// </summary>
		public ISubmittableItem[] SubmittableItems
		{
			get
			{
				return submittableItems;
			}

			set
			{
				submittableItems = value;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the username to be used to log into the remote system
		/// that the submission is sent to.
		/// </summary>
		public string Username
		{
			get
			{
				return username;
			}

			set
			{
				username = value;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets or sets the password to be used to log into the remote system
		/// that the submission is sent to.
		/// </summary>
		public string Password
		{
			get
			{
				return password;
			}

			set
			{
				password = value;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// A URI that has had any parameter placeholers replaced by their
		/// actual values. Protocols should call this to quickly obtain the
		/// destination URI, avoiding the burden of resolving the parameters
		/// directly.
		/// </summary>
		public Uri ResolvedTransport
		{
			get
			{
				string uriString = ResolveParameter(Assignment.Transport);
				return new Uri(uriString);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Resolves the specified parameter string by replacing any variable
		/// placeholders with their actual values. Currently the following
		/// placeholders are supported:
		/// 
		/// * ${user} - the username
		/// * ${pw} - the password
		/// * ${assignment.name} - the name of the assignment
		///
		/// </summary>
		/// <param name="value">
		/// The string containing placeholders to be resolved.
		/// </param>
		/// <returns>
		/// A copy of the original string with the placeholders replaced by
		/// their actual values.
		/// </returns>
		public string ResolveParameter(string value)
		{
			Regex regex;

			regex = new Regex("\\$\\{user\\}");
			value = regex.Replace(value, username);

			regex = new Regex("\\$\\{pw\\}");
			value = regex.Replace(value, password);

			if (assignment is INameableTarget)
			{
				INameableTarget nameable = (INameableTarget)assignment;

				if (nameable != null)
				{
					regex = new Regex("\\$\\{assignment\\.name\\}");
					string asmtName = nameable.Name.Replace(" ", "%20");
					value = regex.Replace(value, asmtName);
				}
			}

			return value;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Takes the tree of submittable items and sends those that should be
		/// included (based on the target assignment) to the specified stream,
		/// using the packager defined for the target assignment.
		/// </summary>
		/// <remarks>
		/// Users implementing a user interface for this submitter core need
		/// not call this method; rather, it is exposed as public so that
		/// custom protocol handlers can call it from within their Submit
		/// method in order to transfer the final submitted package to its
		/// destination.
		/// </remarks>
		/// <param name="stream">
		/// The System.IO.Stream to write the package contents to.
		/// </param>
		public void PackageContentsIntoStream(Stream stream)
		{
			IPackager packager =
				PackagerRegistry.CreatePackagerInstance(assignment.Packager);

			Dictionary<string, string> parameters =
				assignment.PackagerParameters;

			packager.StartPackage(stream, parameters);

			foreach (ISubmittableItem item in
				new DepthFirstTraversal<ISubmittableItem>(submittableItems,
				delegate(ISubmittableItem i) { return i.Children; }))
			{
				if (!assignment.IsFileExcluded(item.Filename))
				{
					packager.AddSubmittableItem(item);
				}
			}

			packager.EndPackage();
		}


		// === Fields =========================================================

		// The assignment to which the user is submitting.
		private ITargetAssignment assignment;

		// The array of submittalbe items that should be packaged and
		// submitted.
		private ISubmittableItem[] submittableItems;

		// The username used to log into the remote system.
		private string username;

		// The password used to log into the remote system, if required.
		private string password;
	}
}
