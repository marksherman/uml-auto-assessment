using System;
using System.Collections.Generic;
using System.Text;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;
using WebCAT.CxxTest.VisualStudio.Utility;

namespace WebCAT.CxxTest.VisualStudio.Events
{
	class SolutionEventsHandler : IVsSolutionEvents3, IVsSolutionEvents4
	{
		public SolutionEventsHandler(IVsSolution solution)
		{
			this.solution = solution;

			hierarchyCookies = new Dictionary<string, uint>();
		}

		public int OnAfterCloseSolution(object pUnkReserved)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesView();
			return VSConstants.S_OK;
		}

		public int OnAfterClosingChildren(IVsHierarchy pHierarchy)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnAfterLoadProject(IVsHierarchy pStubHierarchy, IVsHierarchy pRealHierarchy)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnAfterMergeSolution(object pUnkReserved)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnAfterOpenProject(IVsHierarchy pHierarchy, int fAdded)
		{
			HierarchyEventsHandler events = new HierarchyEventsHandler(
				solution, pHierarchy);

			uint cookie;
			pHierarchy.AdviseHierarchyEvents(events, out cookie);

			string name = new HierarchyItem(pHierarchy).CanonicalName;
			hierarchyCookies[name] = cookie;

			if(fAdded != 0)
				CxxTestPackage.Instance.TryToRefreshTestSuitesView();

			return VSConstants.S_OK;
		}

		public int OnAfterOpenSolution(object pUnkReserved, int fNewSolution)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesView();
			return VSConstants.S_OK;
		}

		public int OnAfterOpeningChildren(IVsHierarchy pHierarchy)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnBeforeCloseProject(IVsHierarchy pHierarchy, int fRemoved)
		{
			string name = new HierarchyItem(pHierarchy).CanonicalName;
			uint cookie = hierarchyCookies[name];

			pHierarchy.UnadviseHierarchyEvents(cookie);

			return VSConstants.S_OK;
		}

		public int OnBeforeCloseSolution(object pUnkReserved)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnBeforeClosingChildren(IVsHierarchy pHierarchy)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnBeforeOpeningChildren(IVsHierarchy pHierarchy)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnBeforeUnloadProject(IVsHierarchy pRealHierarchy, IVsHierarchy pStubHierarchy)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnQueryCloseProject(IVsHierarchy pHierarchy, int fRemoving, ref int pfCancel)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnQueryCloseSolution(object pUnkReserved, ref int pfCancel)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnQueryUnloadProject(IVsHierarchy pRealHierarchy, ref int pfCancel)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnAfterAsynchOpenProject(IVsHierarchy pHierarchy, int fAdded)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnAfterChangeProjectParent(IVsHierarchy pHierarchy)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnAfterRenameProject(IVsHierarchy pHierarchy)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnQueryChangeProjectParent(IVsHierarchy pHierarchy, IVsHierarchy pNewParentHier, ref int pfCancel)
		{
			return VSConstants.E_NOTIMPL;
		}

		private IVsSolution solution;

		private Dictionary<string, uint> hierarchyCookies;
	}
}
