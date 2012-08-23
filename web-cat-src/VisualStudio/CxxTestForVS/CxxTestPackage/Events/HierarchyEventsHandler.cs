using System;
using System.Collections.Generic;
using System.Text;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio;

namespace WebCAT.CxxTest.VisualStudio.Events
{
	class HierarchyEventsHandler : IVsHierarchyEvents
	{
		public HierarchyEventsHandler(IVsSolution solution, IVsHierarchy hierarchy)
		{
			this.solution = solution;
			this.hierarchy = hierarchy;
		}

		public int OnInvalidateIcon(IntPtr hicon)
		{
			return VSConstants.E_NOTIMPL;
		}

		public int OnInvalidateItems(uint itemidParent)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesView();
			return VSConstants.S_OK;
		}

		public int OnItemAdded(uint itemidParent, uint itemidSiblingPrev, uint itemidAdded)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesView();
			return VSConstants.S_OK;
		}

		public int OnItemDeleted(uint itemid)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesView();
			return VSConstants.S_OK;
		}

		public int OnItemsAppended(uint itemidParent)
		{
			CxxTestPackage.Instance.TryToRefreshTestSuitesView();
			return VSConstants.S_OK;
		}

		public int OnPropertyChanged(uint itemid, int propid, uint flags)
		{
			return VSConstants.E_NOTIMPL;
		}

		private IVsSolution solution;
		private IVsHierarchy hierarchy;
	}
}
