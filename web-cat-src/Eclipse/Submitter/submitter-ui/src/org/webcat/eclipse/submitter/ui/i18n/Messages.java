/*==========================================================================*\
 |  $Id: Messages.java,v 1.3 2010/12/06 21:08:41 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Eclipse Plugins.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.eclipse.submitter.ui.i18n;

import org.eclipse.osgi.util.NLS;

//--------------------------------------------------------------------------
/**
 * The message bundle used by the submitter.ui plug-in.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2010/12/06 21:08:41 $
 */
@SuppressWarnings("all")
public class Messages extends NLS
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Prevent instantiation.
	 */
	private Messages()
	{
		// Prevent instantiation.
	}


	//~ Constants .............................................................

	public static String AMBIGUOUSSELECTION_CANCEL;

	public static String AMBIGUOUSSELECTION_DIALOG_MESSAGE;

	public static String AMBIGUOUSSELECTION_DIALOG_TITLE;

	public static String AMBIGUOUSSELECTION_OK;

	public static String AMBIGUOUSSELECTION_OPTION_1;

	public static String AMBIGUOUSSELECTION_OPTION_2;

	public static String BROWSEREDITOR_INVALID_INPUT;

	public static String BROWSEREDITOR_TITLE;

	public static String PARSERERROR_DIALOG_TITLE;

	public static String PARSERERROR_ERROR_MESSAGE_MULTIPLE;

	public static String PARSERERROR_ERROR_MESSAGE_SINGLE;

	public static String PLUGINUI_NO_DEF_URL_DESCRIPTION;

	public static String PLUGINUI_NO_DEF_URL_TITLE;

	public static String STARTPAGE_CHOOSE_PROJECT;

	public static String STARTPAGE_CHOOSE_PROJECT_TITLE;

	public static String STARTPAGE_CLICK_FINISH_TO_EXIT;

	public static String STARTPAGE_ERROR_BAD_URL;

	public static String STARTPAGE_ERROR_COULD_NOT_CONNECT;

	public static String STARTPAGE_ERROR_GENERIC;

	public static String STARTPAGE_ERROR_INVALID_TARGET;

	public static String STARTPAGE_ERROR_NO_PROJECT;

	public static String STARTPAGE_ERROR_NO_TARGET;

	public static String STARTPAGE_ERROR_NO_USERNAME;

	public static String STARTPAGE_ERROR_REQUIRED_FILES_MISSING;

	public static String STARTPAGE_PAGE_DESCRIPTION;

	public static String STARTPAGE_PAGE_NAME;

	public static String STARTPAGE_PAGE_TITLE;

	public static String STARTPAGE_PASSWORD;

	public static String STARTPAGE_PARTNERS_DESCRIPTION;

	public static String STARTPAGE_PARTNERS;

	public static String STARTPAGE_PROJECT;

	public static String STARTPAGE_SUBMISSION_CANCELED;

	public static String STARTPAGE_SUBMIT_AS;

	public static String STARTPAGE_USERNAME;

	public static String SUMMARYPAGE_PAGE_DESCRIPTION;

	public static String SUMMARYPAGE_PAGE_NAME;

	public static String SUMMARYPAGE_PAGE_TITLE;

	public static String SUMMARYPAGE_STATUS_CANCELED;

	public static String SUMMARYPAGE_STATUS_FAILED;

	public static String SUMMARYPAGE_STATUS_INCOMPLETE;

	public static String SUMMARYPAGE_STATUS_SUCCESS;

	public static String WIZARD_TITLE;


	//~ Static/instance variables .............................................

	private static final String BUNDLE_NAME =
		"org.webcat.eclipse.submitter.ui.i18n.messages"; //$NON-NLS-1$

	static
	{
		// Initialize the strings.

		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
