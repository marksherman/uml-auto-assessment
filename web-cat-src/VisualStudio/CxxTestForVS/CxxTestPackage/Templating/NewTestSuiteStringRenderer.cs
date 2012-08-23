/*==========================================================================*\
 |  $Id: NewTestSuiteStringRenderer.cs,v 1.1 2008/06/02 23:27:40 aallowat Exp $
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
using Antlr.StringTemplate;
using WebCAT.CxxTest.VisualStudio.Utility;

namespace WebCAT.CxxTest.VisualStudio.Templating
{
	class NewTestSuiteStringRenderer : IAttributeRenderer
	{
		public NewTestSuiteStringRenderer(string suitePath)
		{
			this.suitePath = suitePath;
		}

		public string ToString(object o)
		{
			return o.ToString();
		}

		public string ToString(object o, string formatName)
		{
			if (formatName == "suiteRelativePath")
				return PathUtils.RelativePathTo(suitePath, o.ToString());
			else if (formatName == "guard")
				return o.ToString().ToUpper() + "_H_";
			else
				return ToString(o);
		}

		private string suitePath;
	}
}
