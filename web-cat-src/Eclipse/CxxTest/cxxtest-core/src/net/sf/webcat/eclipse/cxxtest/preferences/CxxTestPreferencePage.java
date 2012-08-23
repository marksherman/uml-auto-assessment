/*==========================================================================*\
 |  $Id$
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

package net.sf.webcat.eclipse.cxxtest.preferences;

import net.sf.webcat.eclipse.cxxtest.CxxTestPlugin;
import net.sf.webcat.eclipse.cxxtest.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.internal.StackTraceDependencyChecker;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The preference page used to edit CxxTest settings.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestPreferencePage extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage
{
	public CxxTestPreferencePage()
	{
		super(FieldEditorPreferencePage.GRID);
	}

	public IPreferenceStore doGetPreferenceStore()
	{
		return CxxTestPlugin.getDefault().getPreferenceStore();
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors()
	{
		addField(new StringFieldEditor(CxxTestPlugin.CXXTEST_PREF_DRIVER_FILENAME,
				Messages.CxxTestPreferencePage_GeneratedDriverFileName, getFieldEditorParent()));

		// TODO: re-enable this later by using conditional code to include
		// Dereferee in the test runner.
		
//		addField(new BooleanFieldEditor(CxxTestPlugin.CXXTEST_PREF_TRACK_HEAP,
//            Messages.CxxTestPreferencePage_EnableBasicHeapChecking, getFieldEditorParent()));

		addField(new BooleanFieldEditor(CxxTestPlugin.CXXTEST_PREF_TRAP_SIGNALS,
            Messages.CxxTestPreferencePage_TrapSignals, getFieldEditorParent()));

        final CancelableBooleanFieldEditor editor;
        editor = new CancelableBooleanFieldEditor(CxxTestPlugin.CXXTEST_PREF_TRACE_STACK,
            Messages.CxxTestPreferencePage_GenerateStackTraces,
            getFieldEditorParent());

        editor.setCancelableListener(new ICancelableBooleanListener() {
			public boolean shouldDenyChange(boolean newValue)
			{
				if (newValue == true)
				{
					boolean found = checkForRequiredLibraries();
					if (!found)
					{
						return true;
					}
				}
				
				return false;
			}
        });

        addField(editor);
	}

	
	private boolean checkForRequiredLibraries()
	{
		return StackTraceDependencyChecker.checkForDependencies(true);
	}


	public void init(IWorkbench workbench)
	{
	}
}
