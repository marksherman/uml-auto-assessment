/*==========================================================================*\
 |  $Id: SubmissionParserErrorDialog.java,v 1.2 2010/12/06 21:08:41 aallowat Exp $
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

package org.webcat.eclipse.submitter.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.webcat.eclipse.submitter.ui.i18n.Messages;
import org.webcat.submitter.SubmissionTargetException;
import org.webcat.submitter.TargetParseError;
import org.webcat.submitter.TargetParseException;

//--------------------------------------------------------------------------
/**
 * Displays to the user any errors that occurred during the parsing of the
 * submission definitions file.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:08:41 $
 */
public class SubmissionParserErrorDialog extends Dialog
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Create a new instance of the error dialog with the specified parent
	 * shell and getting its information from the given exception.
	 * 
	 * @param shell the shell that will parent this dialog
	 * @param exception the exception described by the dialog.
	 */
	public SubmissionParserErrorDialog(Shell shell, Throwable exception)
	{
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);

		if(exception instanceof TargetParseException)
		{
			setFromParseErrors(((TargetParseException)exception).getErrors());
		}
		else
		{
			setFromException(exception);
		}
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Creates the dialog controls.
	 */
	protected Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite)super.createDialogArea(parent);

		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		composite.setLayout(gl);

		Composite headerComp = new Composite(composite, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 2;
		headerComp.setLayout(gl);

		Label imageLabel = new Label(headerComp, SWT.NONE);
		imageLabel.setSize(32, 32);
		imageLabel.setImage(Display.getCurrent().getSystemImage(
		        SWT.ICON_WARNING));
		GridData gd = new GridData();
		gd.widthHint = 32;
		gd.heightHint = 32;
		gd.verticalAlignment = GridData.BEGINNING;
		imageLabel.setLayoutData(gd);

		summaryLabel = new Label(headerComp, SWT.WRAP);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 368;
		summaryLabel.setLayoutData(gd);

		errorField = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI
		        | SWT.H_SCROLL | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 400;
		gd.heightHint = 150;
		errorField.setLayoutData(gd);

		if(errorString != null)
		{
			errorField.setText(errorString);
		}

		if(summaryString != null)
		{
			summaryLabel.setText(summaryString);
		}

		getShell().setText(Messages.PARSERERROR_DIALOG_TITLE);

		return composite;
	}


	// ----------------------------------------------------------
	/**
	 * Creates the main buttons for the dialog.
	 */
	protected void createButtonsForButtonBar(Composite parent)
	{
		// Create only an OK button.
		
		createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.OK_LABEL, true);
	}


	// ----------------------------------------------------------
	/**
	 * Initializes the dialog's text area with the specified array of parser
	 * error objects.
	 */
	private void setFromParseErrors(TargetParseError[] errors)
	{
		StringBuffer buffer = new StringBuffer();

		for(int i = 0; i < errors.length; i++)
		{
			buffer.append(errors[i].toString());
			buffer.append('\n');
		}

		errorString = buffer.toString();

		summaryString = Messages.PARSERERROR_ERROR_MESSAGE_MULTIPLE;
	}


	// ----------------------------------------------------------
	/**
	 * Initializes the dialog's text area with the specified exception.
	 */
	private void setFromException(Throwable e)
	{
		Throwable exception = e;

		if (e instanceof SubmissionTargetException)
		{
			exception = ((SubmissionTargetException) e).getCause();
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append(exception.toString());
		buffer.append("\n\n"); //$NON-NLS-1$

		StackTraceElement[] trace = exception.getStackTrace();
		for(int i = 0; i < trace.length; i++)
		{
			buffer.append(trace[i].toString());
			buffer.append('\n');
		}

		errorString = buffer.toString();

		summaryString = Messages.PARSERERROR_ERROR_MESSAGE_SINGLE;
	}


	//~ Static/instance variables .............................................

	/* The label that displays the description of the error. */
	private Label summaryLabel;

	/* The text field that displays the error list or stack trace. */
	private Text errorField;

	/* The string containing the description of the error. */
	private String summaryString;

	/* The string containing the error list or stack trace. */
	private String errorString;
}
