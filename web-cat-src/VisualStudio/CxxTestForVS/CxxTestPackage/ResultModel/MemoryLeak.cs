/*==========================================================================*\
 |  $Id: MemoryLeak.cs,v 1.1 2008/06/02 23:27:39 aallowat Exp $
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
using System.Xml;

namespace WebCAT.CxxTest.VisualStudio.ResultModel
{
	class MemoryLeak
	{
		public MemoryLeak(XmlAttributeCollection attributes)
		{
			backtrace = new List<BacktraceFrame>();

			address = attributes["address"].Value;

			if (attributes["type"] != null)
				type = attributes["type"].Value;

			if (attributes["is-array"] != null)
				bool.TryParse(attributes["is-array"].Value, out isArray);

			int.TryParse(attributes["size"].Value, out size);
		}

		public BacktraceFrame[] Backtrace
		{
			get
			{
				return backtrace.ToArray();
			}
		}

		public string Address
		{
			get
			{
				return address;
			}
		}

		public bool IsArray
		{
			get
			{
				return isArray;
			}
		}

		public string Type
		{
			get
			{
				return type;
			}
		}

		public int Size
		{
			get
			{
				return size;
			}
		}

		public void AddBacktraceFrame(BacktraceFrame frame)
		{
			backtrace.Add(frame);
		}

		private List<BacktraceFrame> backtrace;
		private bool isArray;
		private string address;
		private string type;
		private int size;
	}
}
