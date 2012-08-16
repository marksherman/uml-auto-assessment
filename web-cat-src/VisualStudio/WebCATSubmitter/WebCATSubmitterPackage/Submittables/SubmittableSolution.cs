/*==========================================================================*\
 |  $Id: SubmittableSolution.cs,v 1.2 2008/12/12 01:44:09 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT Electronic Submission Package for Visual
 |	Studio.NET.
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
using WebCAT.Submitter;
using System.IO;
using WebCAT.Submitter.VisualStudio.Utility;
using Microsoft.VisualStudio.Shell.Interop;

namespace WebCAT.Submitter.VisualStudio.Submittables
{
	/// <summary>
	/// A submittable item that represents a Visual Studio solution.
	/// </summary>
	class SubmittableSolution : ISubmittableItem
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new submittable solution item.
		/// </summary>
		/// <param name="solution">
		/// A solution to be represented by this item.
		/// </param>
		public SubmittableSolution(IVsSolution solution)
		{
			this.solution = solution;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the solution that this item represents.
		/// </summary>
		internal IVsSolution Solution
		{
			get
			{
				return solution;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the name of this solution.
		/// </summary>
		internal string SolutionName
		{
			get
			{
				string solutionDir;
				string solutionFile;
				string solutionUser;
				solution.GetSolutionInfo(out solutionDir, out solutionFile,
					out solutionUser);

				return Path.GetFileNameWithoutExtension(solutionFile);
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the absolute path to the directory that contains this
		/// solution.
		/// </summary>
		internal string SolutionPath
		{
			get
			{
				string solutionDir;
				string solutionFile;
				string solutionUser;
				solution.GetSolutionInfo(out solutionDir, out solutionFile,
					out solutionUser);

				return solutionDir;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the portion of the given path that is relative to the
		/// directory that contains this solution.
		/// </summary>
		/// <param name="fullPath">
		/// The absolute path to the file whose relative path should be
		/// computed.
		/// </param>
		/// <returns>
		/// The relative path of the file.
		/// </returns>
		internal string GetRelativePath(string fullPath)
		{
			return PathUtils.GetRelativePath(SolutionPath, fullPath);
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Returns an empty string to indicate that the solution is the top
		/// level of the destination package.
		/// </summary>
		public string Filename
		{
			get
			{
				return "";
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Gets the kind of this item (a folder, containing the files in the
		/// solution).
		/// </summary>
		public SubmittableItemKind Kind
		{
			get
			{
				return SubmittableItemKind.Folder;
			}
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Returns null since a solution item represents a folder.
		/// </summary>
		/// <returns>
		/// Returns null.
		/// </returns>
		public Stream GetStream()
		{
			return null;
		}

		//  -------------------------------------------------------------------
		/// <summary>
		/// Enumerates the projects in this solution.
		/// </summary>
		public IEnumerable<ISubmittableItem> Children
		{
			get
			{
				// Yield the solution .sln file.
				string solutionDir;
				string solutionFile;
				string solutionUser;
				solution.GetSolutionInfo(out solutionDir, out solutionFile,
					out solutionUser);

				yield return new SubmittableDirectFile(
					solutionDir, solutionFile);

				// Yield the projects themselves.
				foreach (HierarchyItem item in
					VsShellUtils.GetLoadedProjects(solution))
				{
					if (item.Hierarchy is IVsProject)
					{
						yield return new SubmittableProject(solutionDir, item);
					}
				}
			}
		}


		// ==== Fields ========================================================

		// The solution that this item represents.
		private IVsSolution solution;
	}
}
