/*==========================================================================*\
 |  $Id: ShellStringUtils.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech 
 |
 |	This file is part of Web-CAT Eclipse Plugins.
 |
 |	Web-CAT is free software; you can redistribute it and/or modify
 |	it under the terms of the GNU General Public License as published by
 |	the Free Software Foundation; either version 2 of the License, or
 |	(at your option) any later version.
 |
 |	Web-CAT is distributed in the hope that it will be useful,
 |	but WITHOUT ANY WARRANTY; without even the implied warranty of
 |	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |	GNU General Public License for more details.
 |
 |	You should have received a copy of the GNU General Public License
 |	along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.cxxtest.internal.options;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Utility functions that split a string into an array of components and join an
 * array into a string using logic similar to a shell. Single and double quotes
 * are handled properly, rather than simply splitting across any delimiting
 * whitespace.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public class ShellStringUtils
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	/**
	 * Splits a string into an array of components. Whitespace is used as the
	 * delimiter, but single and double quotes are handled properly so that a
	 * component in quotes is extracted as a single component, regardless of
	 * whether it contains whitespace or not.
	 * 
	 * @param string
	 *            the String that should be split
	 * 
	 * @return an array of String components extracted from the string
	 */
	public static String[] split(String string)
	{
		ArrayList<String> parts = new ArrayList<String>();
		StringBuffer currentPart = new StringBuffer();

		int state = STATE_BEGIN;

		for(int i = 0; i < string.length(); i++)
		{
			char ch = string.charAt(i);

			switch(state)
			{
			case STATE_BEGIN:
				if(ch == '"')
				{
					currentPart.append(ch);
					state = STATE_DQUOTE;
				}
				else if(ch == '\'')
				{
					currentPart.append(ch);
					state = STATE_SQUOTE;
				}
				else if(ch == '\\')
				{
					state = STATE_ESCAPE_IN_PART;
				}
				else if(!Character.isWhitespace(ch))
				{
					currentPart.append(ch);
					state = STATE_PART;
				}
				break;

			case STATE_PART:
				if(Character.isWhitespace(ch))
				{
					parts.add(currentPart.toString());
					currentPart = new StringBuffer();
					state = STATE_BEGIN;
				}
				else if(ch == '"')
				{
					currentPart.append(ch);
					state = STATE_DQUOTE;
				}
				else if(ch == '\'')
				{
					currentPart.append(ch);
					state = STATE_SQUOTE;
				}
				else if(ch == '\\')
				{
					currentPart.append(ch);
					state = STATE_ESCAPE_IN_PART;
				}
				else
				{
					currentPart.append(ch);
				}
				break;

			case STATE_ESCAPE_IN_PART:
				currentPart.append(ch);
				state = STATE_PART;
				break;

			case STATE_DQUOTE:
				currentPart.append(ch);

				if(ch == '"')
				{
					state = STATE_PART;
				}
				else if(ch == '\\')
				{
					state = STATE_ESCAPE_IN_DQUOTE;
				}
				break;

			case STATE_ESCAPE_IN_DQUOTE:
				currentPart.append(ch);
				state = STATE_DQUOTE;
				break;

			case STATE_SQUOTE:
				currentPart.append(ch);

				if(ch == '\'')
				{
					state = STATE_PART;
				}
				else if(ch == '\\')
				{
					state = STATE_ESCAPE_IN_SQUOTE;
				}
				break;

			case STATE_ESCAPE_IN_SQUOTE:
				currentPart.append(ch);
				state = STATE_SQUOTE;
				break;
			}
		}

		if(currentPart.length() > 0)
			parts.add(currentPart.toString());

		String[] array = new String[parts.size()];
		parts.toArray(array);
		return array;
	}


	// ------------------------------------------------------------------------
	/**
	 * Joins an array of String components into a single whitespace-delimited
	 * string. If any of the elements of array themselves have spaces in them,
	 * they will be properly double-quoted.
	 * 
	 * @param array
	 *            an array of Strings containing the components to be joined
	 * 
	 * @return a String containing the joined components
	 */
	public static String join(String[] array)
	{
		StringBuffer buffer = new StringBuffer();

		if(array.length > 0)
		{
			buffer.append(quoteIfNecessary(array[0]));

			for(int i = 1; i < array.length; i++)
			{
				buffer.append(' ');
				buffer.append(quoteIfNecessary(array[i]));
			}
		}

		return buffer.toString();
	}
	
	
	/**
	 * Performs shell-style quoting on a string if necessary. That is, if the
	 * string contains spaces, it will be surrounded by double quotes;
	 * otherwise, it will not. In either case, double quotes already in the
	 * string will be escaped with a backslash.
	 * 
	 * @param str the string to quote
	 * @return a copy of the string that has been quoted if necessary
	 */
	public static String quoteIfNecessary(String str)
	{
		str = str.replaceAll("\"", Matcher.quoteReplacement("\\\"")); //$NON-NLS-1$ //$NON-NLS-2$

		if (str.indexOf(' ') != -1)
		{
			return "\"" + str + "\""; //$NON-NLS-1$ //$NON-NLS-2$
		}
		else
		{
			return str;
		}
	}
	
	
	/**
	 * Gets a regular expression pattern string based on a plug-in relative
	 * path that will match any version of that plug-in, not just the one
	 * provided.  All other parts of the path must match exactly; the path
	 * from the root to the plug-in directory must be the same, and the
	 * portion of the path after the plug-in directory must also be to the
	 * exact same resource (not a child of it).
	 * 
	 * This function can also be used in development, when the path to the
	 * plug-in is merely the workspace path, because any path that does not
	 * contain a plug-in version identifier will simply be returned unaltered.
	 * 
	 * @param path the plug-in relative path to match
	 * @param isDirectory true if this path is a directory and not a file
	 * 
	 * @return a regular expression that will match this path regardless of
	 *     the version of the plug-in
	 */
	public static String patternForAnyVersionOfPluginRelativePath(
			String path, boolean isDirectory)
	{
		// First escape anything that might be a metacharacter. To play it
		// safe, we'll just escape anything that isn't a character, digit,
		// underscore, hyphen, or forward slash (common path characters).
		
		if (path.endsWith("/")) //$NON-NLS-1$
		{
			path = path.substring(0, path.length() - 1);
		}

		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < path.length(); i++)
		{
			char ch = path.charAt(i);
			
			if (!Character.isLetterOrDigit(ch)
					&& ch != '_' && ch != '/' && ch != '-')
			{
				buffer.append('\\');
			}
			
			buffer.append(ch);
		}
		
		if (isDirectory)
		{
			buffer.append("/?"); //$NON-NLS-1$
		}

		String escapedPath = buffer.toString();

		return escapedPath.replaceFirst(VERSION_REGEX,
				Matcher.quoteReplacement(VERSION_REGEX));
	}


	// === Static Variables ===================================================

	/**
	 * State identifiers used by the DFA in the split method.
	 */
	private static final int STATE_BEGIN = 0;

	private static final int STATE_PART = 1;

	private static final int STATE_ESCAPE_IN_PART = 2;

	private static final int STATE_DQUOTE = 3;

	private static final int STATE_ESCAPE_IN_DQUOTE = 4;

	private static final int STATE_SQUOTE = 5;

	private static final int STATE_ESCAPE_IN_SQUOTE = 6;
	
	private static final String VERSION_REGEX =
		"_[^.]+\\.[^.]+\\.[^./]+(\\.[^/]+)?/"; //$NON-NLS-1$ 
}
