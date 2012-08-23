/*==========================================================================*\
 |  $Id: TrackProjectDocumentsEventsHandler.cs,v 1.1 2008/06/02 23:27:38 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008 Virginia Tech
 |
 |  This file is part of the Web-CAT CxxTest integration package for Visual
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
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;

namespace WebCAT.CxxTest.VisualStudio.Events
{
	// --------------------------------------------------------------------
	/// <summary>
	/// A class that listens for and handles events that occur when items in
	/// a project are added or removed.
	/// </summary>
	class TrackProjectDocumentsEventsHandler : IVsTrackProjectDocumentsEvents2
	{
		//~ Methods ..........................................................

		// ------------------------------------------------------
		public int OnAfterAddDirectoriesEx(int cProjects, int cDirectories,
			IVsProject[] rgpProjects, int[] rgFirstIndices,
			string[] rgpszMkDocuments, VSADDDIRECTORYFLAGS[] rgFlags)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesWindow();

			return VSConstants.S_OK;
		}


		// ------------------------------------------------------
		public int OnAfterAddFilesEx(int cProjects, int cFiles,
			IVsProject[] rgpProjects, int[] rgFirstIndices,
			string[] rgpszMkDocuments, VSADDFILEFLAGS[] rgFlags)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesWindow();

			return VSConstants.S_OK;
		}


		// ------------------------------------------------------
		public int OnAfterRemoveDirectories(int cProjects, int cDirectories,
			IVsProject[] rgpProjects, int[] rgFirstIndices,
			string[] rgpszMkDocuments, VSREMOVEDIRECTORYFLAGS[] rgFlags)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesWindow();

			return VSConstants.S_OK;
		}


		// ------------------------------------------------------
		public int OnAfterRemoveFiles(int cProjects, int cFiles,
			IVsProject[] rgpProjects, int[] rgFirstIndices,
			string[] rgpszMkDocuments, VSREMOVEFILEFLAGS[] rgFlags)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesWindow();

			return VSConstants.S_OK;
		}


		// ------------------------------------------------------
		public int OnAfterRenameDirectories(int cProjects, int cDirs,
			IVsProject[] rgpProjects, int[] rgFirstIndices,
			string[] rgszMkOldNames, string[] rgszMkNewNames,
			VSRENAMEDIRECTORYFLAGS[] rgFlags)
		{
			return VSConstants.E_NOTIMPL;
		}


		// ------------------------------------------------------
		public int OnAfterRenameFiles(int cProjects, int cFiles,
			IVsProject[] rgpProjects, int[] rgFirstIndices,
			string[] rgszMkOldNames, string[] rgszMkNewNames,
			VSRENAMEFILEFLAGS[] rgFlags)
		{
			return VSConstants.E_NOTIMPL;
		}


		// ------------------------------------------------------
		public int OnAfterSccStatusChanged(int cProjects, int cFiles,
			IVsProject[] rgpProjects, int[] rgFirstIndices,
			string[] rgpszMkDocuments, uint[] rgdwSccStatus)
		{
			return VSConstants.E_NOTIMPL;
		}


		// ------------------------------------------------------
		public int OnQueryAddDirectories(IVsProject pProject,
			int cDirectories, string[] rgpszMkDocuments,
			VSQUERYADDDIRECTORYFLAGS[] rgFlags,
			VSQUERYADDDIRECTORYRESULTS[] pSummaryResult,
			VSQUERYADDDIRECTORYRESULTS[] rgResults)
		{
			return VSConstants.E_NOTIMPL;
		}


		// ------------------------------------------------------
		public int OnQueryAddFiles(IVsProject pProject, int cFiles,
			string[] rgpszMkDocuments, VSQUERYADDFILEFLAGS[] rgFlags,
			VSQUERYADDFILERESULTS[] pSummaryResult,
			VSQUERYADDFILERESULTS[] rgResults)
		{
			return VSConstants.E_NOTIMPL;
		}


		// ------------------------------------------------------
		public int OnQueryRemoveDirectories(IVsProject pProject,
			int cDirectories, string[] rgpszMkDocuments,
			VSQUERYREMOVEDIRECTORYFLAGS[] rgFlags,
			VSQUERYREMOVEDIRECTORYRESULTS[] pSummaryResult,
			VSQUERYREMOVEDIRECTORYRESULTS[] rgResults)
		{
			return VSConstants.E_NOTIMPL;
		}


		// ------------------------------------------------------
		public int OnQueryRemoveFiles(IVsProject pProject, int cFiles,
			string[] rgpszMkDocuments, VSQUERYREMOVEFILEFLAGS[] rgFlags,
			VSQUERYREMOVEFILERESULTS[] pSummaryResult,
			VSQUERYREMOVEFILERESULTS[] rgResults)
		{
			return VSConstants.E_NOTIMPL;
		}


		// ------------------------------------------------------
		public int OnQueryRenameDirectories(IVsProject pProject, int cDirs,
			string[] rgszMkOldNames, string[] rgszMkNewNames,
			VSQUERYRENAMEDIRECTORYFLAGS[] rgFlags,
			VSQUERYRENAMEDIRECTORYRESULTS[] pSummaryResult,
			VSQUERYRENAMEDIRECTORYRESULTS[] rgResults)
		{
			return VSConstants.E_NOTIMPL;
		}


		// ------------------------------------------------------
		public int OnQueryRenameFiles(IVsProject pProject, int cFiles,
			string[] rgszMkOldNames, string[] rgszMkNewNames,
			VSQUERYRENAMEFILEFLAGS[] rgFlags,
			VSQUERYRENAMEFILERESULTS[] pSummaryResult,
			VSQUERYRENAMEFILERESULTS[] rgResults)
		{
			return VSConstants.E_NOTIMPL;
		}
	}
}
