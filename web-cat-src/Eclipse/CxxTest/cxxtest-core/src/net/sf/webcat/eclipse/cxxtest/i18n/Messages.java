/*==========================================================================*\
 |  $Id: Messages.java,v 1.1 2009/09/13 12:59:29 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.i18n;

import org.eclipse.osgi.util.NLS;

//------------------------------------------------------------------------
/**
 * Message strings used in the plugin.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2009/09/13 12:59:29 $
 */
public class Messages extends NLS
{
	//~ Constructors ..........................................................
	
	// ----------------------------------------------------------
	/**
	 * Prevent instantiation.
	 */
	private Messages()
	{
		// Static class; prevent instantiation.
	}


	//~ Static/instance variables .............................................

	private static final String BUNDLE_NAME = "net.sf.webcat.eclipse.cxxtest.i18n.messages"; //$NON-NLS-1$

	public static String ContextualSAXHandler_StackNotEmptyAtEnd;
	public static String ContextualSAXHandler_StackPrematurelyEmpty;
	public static String ContextualSAXHandler_UnrecognizedTag;
	public static String CounterPanel_ErrorsLabel;
	public static String CounterPanel_FailuresLabel;
	public static String CounterPanel_RunsLabel;
	public static String CxxTestAssertionFactory_AssertThrewDifferentTypeMsg;
	public static String CxxTestAssertionFactory_AssertThrewNothingMsg;
	public static String CxxTestAssertionFactory_AssertThrowsOtherValue;
	public static String CxxTestAssertionFactory_FailedAssertDelta;
	public static String CxxTestAssertionFactory_FailedAssertEq;
	public static String CxxTestAssertionFactory_FailedAssertLe;
	public static String CxxTestAssertionFactory_FailedAssertLt;
	public static String CxxTestAssertionFactory_FailedAssertMsg;
	public static String CxxTestAssertionFactory_FailedAssertNe;
	public static String CxxTestAssertionFactory_FailedAssertNoThrow;
	public static String CxxTestAssertionFactory_FailedAssertPredicate;
	public static String CxxTestAssertionFactory_FailedAssertRelation;
	public static String CxxTestAssertionFactory_FailedAssertSameData;
	public static String CxxTestAssertionFactory_FailedAssertThrows;
	public static String CxxTestAssertionFactory_LineNumber;
	public static String CxxTestAssertionFactory_TraceMsg;
	public static String CxxTestDriverBuilder_ErrorGeneratingDriver;
	public static String CxxTestDriverBuilder_GeneratingDriverTaskDescription;
	public static String CxxTestDriverBuilder_UpgradeProjectSettingsMessage;
	public static String CxxTestDriverBuilder_UpgradeProjectSettingsTitle;
	public static String CxxTestDriverRunner_RunningDriverTaskDescription;
	public static String CxxTestPlugin_ConsoleTitle;
	public static String CxxTestPreferencePage_EnableBasicHeapChecking;
	public static String CxxTestPreferencePage_GeneratedDriverFileName;
	public static String CxxTestPreferencePage_GenerateStackTraces;
	public static String CxxTestPreferencePage_TrapSignals;
	public static String CxxTestPropertyPage_EnableCxxTest;
	public static String CxxTestStackFrame_FileAndLineNumber;
	public static String CxxTestStackFrame_FileOnly;
	public static String DerefereeLeak_ArrayDescription;
	public static String DerefereeLeak_BlockDescription;
	public static String OpenTestAction_OpenInEditorLabel;
	public static String OpenTestAction_OpenInEditorTooltip;
	public static String PathUtils_DestinationPathNull;
	public static String StackTraceAssertion_FailureMsg;
	public static String StackTraceAssertion_LineNumber;
	public static String StackTraceAssertion_WarningMsg;
	public static String StackTraceDependencyChecker_BackgroundJobDescription;
	public static String StackTraceDependencyChecker_BackgroundJobName;
	public static String StackTraceDependencyChecker_MissingDependencyMsgEnd;
	public static String StackTraceDependencyChecker_MissingDependencyMsgStart;
	public static String StackTraceDependencyChecker_MissingDependencyPartDescription;
	public static String StackTraceDependencyChecker_ShellTitle;
	public static String TestHierarchyTab_ErrorDescription;
	public static String TestHierarchyTab_ErrorDescriptionWithLineNumber;
	public static String TestHierarchyTab_ErrorTitle;
	public static String TestHierarchyTab_ExpandAllLabel;
	public static String TestHierarchyTab_ExpandAllTooltip;
	public static String TestHierarchyTab_TabName;
	public static String TestHierarchyTab_TabTooltip;
	public static String TestMemoryTab_ErrorDescription;
	public static String TestMemoryTab_ErrorDescriptionWithLineNumber;
	public static String TestMemoryTab_ErrorTitle;
	public static String TestMemoryTab_MemoryUsageTooltip;
	public static String TestMemoryTab_NumMemoryLeaks;
	public static String TestMemoryTab_NumMemoryLeaksElided;
	public static String TestMemoryTab_TabName;
	public static String TestRunnerViewPart_AllocatedUsingSuffix;
	public static String TestRunnerViewPart_AutomaticViewLabel;
	public static String TestRunnerViewPart_CallsToArrayDeleteNonNull;
	public static String TestRunnerViewPart_CallsToArrayNew;
	public static String TestRunnerViewPart_CallsToDeleteNonNull;
	public static String TestRunnerViewPart_CallsToDeleteNull;
	public static String TestRunnerViewPart_CallsToNew;
	public static String TestRunnerViewPart_DetailsLabel;
	public static String TestRunnerViewPart_HorizontalViewLabel;
	public static String TestRunnerViewPart_MaximumBytesInUse;
	public static String TestRunnerViewPart_RunningMessage;
	public static String TestRunnerViewPart_TerminateLabel;
	public static String TestRunnerViewPart_TerminateTooltip;
	public static String TestRunnerViewPart_TotalBytesAllocated;
	public static String TestRunnerViewPart_VerticalViewLabel;


	// ----------------------------------------------------------
	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
