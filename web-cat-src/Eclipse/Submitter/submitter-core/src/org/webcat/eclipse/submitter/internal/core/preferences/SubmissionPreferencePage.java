/*==========================================================================*\
 |  $Id: SubmissionPreferencePage.java,v 1.1 2010/08/31 17:29:07 aallowat Exp $
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

package org.webcat.eclipse.submitter.internal.core.preferences;


import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.webcat.eclipse.submitter.core.SubmitterCore;

//--------------------------------------------------------------------------
/**
 * The preference page used to edit settings for the electronic submission
 * plug-in.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/08/31 17:29:07 $
 */
public class SubmissionPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Creates a new instance of the preference page.
	 */
	public SubmissionPreferencePage()
	{
		super(FieldEditorPreferencePage.GRID);

		setPreferenceStore(SubmitterCore.getDefault().getPreferenceStore());
		setDescription("Please enter the URL provided by your instructor "
		        + "that contains the assignment definitions to be used by the "
		        + "electronic submission plug-in in the field below.\n");
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors()
	{
		final int FIELD_WIDTH = 40;

		addField(new StringFieldEditor(SubmitterCore.DEFINITIONS_URL,
		        "&Assignment definition URL:", FIELD_WIDTH,
		        getFieldEditorParent()));

		addField(new StringFieldEditor(
		        SubmitterCore.IDENTIFICATION_DEFAULTUSERNAME,
		        "Default &username:", FIELD_WIDTH, getFieldEditorParent()));

		addField(new StringFieldEditor(SubmitterCore.IDENTIFICATION_SMTPSERVER,
		        "&Outgoing (SMTP) mail server:", FIELD_WIDTH,
		        getFieldEditorParent()));

		addField(new StringFieldEditor(
		        SubmitterCore.IDENTIFICATION_EMAILADDRESS, "&E-mail address:",
		        FIELD_WIDTH, getFieldEditorParent()));
	}


	// ----------------------------------------------------------
	public void init(IWorkbench workbench)
	{
		// Does nothing; required by the IWorkbenchPreferencePage interface.
	}
}
