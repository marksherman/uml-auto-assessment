/*==========================================================================*\
 |  $Id: FilePattern.cs,v 1.2 2008/12/12 01:41:40 aallowat Exp $
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
using System.Text.RegularExpressions;

namespace WebCAT.Submitter.Internal
{
	/// <summary>
	/// A utility class that converts a DOS-style file pattern (using * and ?
	/// as "any string" and "any character", respectively) to a regular
	/// expression to perform a match.
	/// </summary>
	internal class FilePattern
	{
		//  -------------------------------------------------------------------
		/// <summary>
		/// Creates a new file pattern object from the given pattern string.
		/// </summary>
		/// <param name="pattern">
		/// The DOS-style file pattern that should be matched against.
		/// </param>
		public FilePattern(string pattern)
		{
			int length = pattern.Length;

			StringBuilder buffer = new StringBuilder(length);

			for (int i = 0; i < length; i++)
			{
				char c = pattern[i];

				if (!Char.IsLetterOrDigit(c))
				{
					switch (c)
					{
						case '?': // Fall through
						case '*':
							buffer.Append('.');
							break;
						default:
							buffer.Append('\\');
							break;
					}
				}

				buffer.Append(c);
			}

			this.regex = new Regex(buffer.ToString());
		}


		//  -------------------------------------------------------------------
		/// <summary>
		/// Determines if the specified path matches this pattern.
		/// </summary>
		/// <param name="path">
		/// The path to match.
		/// </param>
		/// <returns>
		/// true if the patch matches the pattern; otherwise, false.
		/// </returns>
		public bool Matches(string path)
		{
			return regex.IsMatch(path);
		}


		// ==== Fields ========================================================

		// The regular expression that represents this file pattern.
		private Regex regex;
	}
}
