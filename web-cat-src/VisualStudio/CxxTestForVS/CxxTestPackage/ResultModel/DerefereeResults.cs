/*==========================================================================*\
 |  $Id: DerefereeResults.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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

namespace WebCAT.CxxTest.VisualStudio.ResultModel
{
	class DerefereeResults
	{
		public DerefereeResults()
		{
			leaks = new List<MemoryLeak>();
		}

		public void AddLeak(MemoryLeak leak)
		{
			leaks.Add(leak);
		}

		public MemoryLeak[] Leaks
		{
			get
			{
				return leaks.ToArray();
			}
		}

		public int ActualLeakCount
		{
			get
			{
				return actualLeakCount;
			}
			set
			{
				actualLeakCount = value;
			}
		}

		public int TotalBytesAllocated
		{
			get
			{
				return totalBytesAllocated;
			}
			set
			{
				totalBytesAllocated = value;
			}
		}

		public int MaximumBytesInUse
		{
			get
			{
				return maximumBytesInUse;
			}
			set
			{
				maximumBytesInUse = value;
			}
		}

		public int CallsToNew
		{
			get
			{
				return callsToNew;
			}
			set
			{
				callsToNew = value;
			}
		}

		public int CallsToArrayNew
		{
			get
			{
				return callsToArrayNew;
			}
			set
			{
				callsToArrayNew = value;
			}
		}

		public int CallsToDelete
		{
			get
			{
				return callsToDelete;
			}
			set
			{
				callsToDelete = value;
			}
		}

		public int CallsToArrayDelete
		{
			get
			{
				return callsToArrayDelete;
			}
			set
			{
				callsToArrayDelete = value;
			}
		}

		public int CallsToDeleteNull
		{
			get
			{
				return callsToDeleteNull;
			}
			set
			{
				callsToDeleteNull = value;
			}
		}

		private List<MemoryLeak> leaks;

		private int actualLeakCount;
		private int totalBytesAllocated;
		private int maximumBytesInUse;
		private int callsToNew;
		private int callsToArrayNew;
		private int callsToDelete;
		private int callsToArrayDelete;
		private int callsToDeleteNull;
	}
}
