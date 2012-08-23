/*==========================================================================*\
 |  $Id: SubmitterSummaryPage.java,v 1.3 2010/12/06 21:08:41 aallowat Exp $
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

package org.webcat.eclipse.submitter.ui.wizards;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.webcat.eclipse.submitter.ui.i18n.Messages;
import org.webcat.submitter.Submitter;

//--------------------------------------------------------------------------
/**
 * The summary page shows the status of the submission, as well as any errors
 * that may have occurred.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2010/12/06 21:08:41 $
 */
public class SubmitterSummaryPage extends WizardPage
{
	//~ Constructors ..........................................................
	
	// ----------------------------------------------------------
	/**
	 * Creates a new instance of the wizard summary page.
	 * 
	 * @param engine the {@link Submitter} to use to submit
	 * @param project the project being submitted
	 */
	protected SubmitterSummaryPage(Submitter engine, IProject project)
	{
		super(Messages.SUMMARYPAGE_PAGE_NAME);

		setTitle(Messages.SUMMARYPAGE_PAGE_TITLE);
		setDescription(Messages.SUMMARYPAGE_PAGE_DESCRIPTION);
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	public void createControl(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		composite.setLayout(gl);

		infoImage = Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION);
		warningImage = Display.getCurrent().getSystemImage(SWT.ICON_WARNING);
		errorImage = Display.getCurrent().getSystemImage(SWT.ICON_ERROR);

		FontData fd = parent.getFont().getFontData()[0];
		fd.setStyle(fd.getStyle() | SWT.BOLD);
		boldFont = new Font(parent.getDisplay(), fd);

		imageLabel = new Label(composite, SWT.NONE);
		imageLabel.setSize(32, 32);
		GridData gd = new GridData();
		gd.widthHint = 32;
		gd.heightHint = 32;
		gd.verticalAlignment = GridData.BEGINNING;
		imageLabel.setLayoutData(gd);

		Composite subComposite = new Composite(composite, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 1;
		subComposite.setLayout(gl);
		gd = new GridData(GridData.FILL_BOTH);
		subComposite.setLayoutData(gd);

		summaryLabel = new Label(subComposite, SWT.NONE);
		summaryLabel.setFont(boldFont);
		summaryLabel.setText(""); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		summaryLabel.setLayoutData(gd);

		descriptionField = new Text(subComposite, SWT.READ_ONLY | SWT.MULTI
		        | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		descriptionField.setLayoutData(gd);

		setControl(composite);
	}


	// ----------------------------------------------------------
	public void dispose()
	{
		if(boldFont != null)
			boldFont.dispose();
	}


	// ----------------------------------------------------------
	public boolean canFlipToNextPage()
	{
		return false;
	}


	// ----------------------------------------------------------
	/**
	 * Sets the result code/error status that will be displayed in the summary.
	 * 
	 * @param result
	 *            One of the RESULT_* values indicating the status of the
	 *            submission.
	 * @param description
	 *            A String containing a description of the error.
	 */
	public void setResultCode(int result, String description)
	{
		setResultCode(result, description, null);
	}


	// ----------------------------------------------------------
	/**
	 * Sets the result code/error status that will be displayed in the summary.
	 * 
	 * @param result
	 *            One of the RESULT_* values indicating the status of the
	 *            submission.
	 * @param description
	 *            A String containing a description of the error.
	 * @param throwable
	 *            A throwable or exception describing an error in more detail.
	 */
	public void setResultCode(int result, String description,
			Throwable throwable)
	{
		String desc = description;

		if (throwable != null)
		{
			StringWriter writer = new StringWriter();
			throwable.printStackTrace(new PrintWriter(writer));
			desc = description + writer.toString();
		}

		switch(result)
		{
			case RESULT_OK:
				imageLabel.setImage(infoImage);
				summaryLabel.setText(Messages.SUMMARYPAGE_STATUS_SUCCESS);
				descriptionField.setText(desc);
				break;
	
			case RESULT_INCOMPLETE:
				imageLabel.setImage(warningImage);
				summaryLabel.setText(Messages.SUMMARYPAGE_STATUS_INCOMPLETE);
				descriptionField.setText(desc);
				break;
	
			case RESULT_CANCELED:
				imageLabel.setImage(warningImage);
				summaryLabel.setText(Messages.SUMMARYPAGE_STATUS_CANCELED);
				descriptionField.setText(desc);
				break;
	
			case RESULT_ERROR:
				imageLabel.setImage(errorImage);
				summaryLabel.setText(Messages.SUMMARYPAGE_STATUS_FAILED);
				descriptionField.setText(desc);
				break;
		}
	}


	// === Static Variables ===================================================

	/**
	 * The submission succeeded.
	 */
	public static final int RESULT_OK = 0;

	/**
	 * The submission was canceled by the user.
	 */
	public static final int RESULT_CANCELED = 1;

	/**
	 * The submission was incomplete (i.e., not all required files were found).
	 */
	public static final int RESULT_INCOMPLETE = 2;

	/**
	 * There was some other error during submission.
	 */
	public static final int RESULT_ERROR = 3;

	
	//~ Static/instance variables .............................................
	/**
	 * The label control used to display the error/info status of the
	 * submission.
	 */
	private Label imageLabel;

	/**
	 * The label that displays a summary of the status of the submission.
	 */
	private Label summaryLabel;

	/**
	 * A text field that displays a detailed description of the result of the
	 * submission.
	 */
	private Text descriptionField;

	/**
	 * The image that represents an informational bubble.
	 */
	private Image infoImage;

	/**
	 * The image that represents a warning sign.
	 */
	private Image warningImage;

	/**
	 * The image that represents an error.
	 */
	private Image errorImage;

	/**
	 * A bold variant of the standard user interface font that is used to
	 * display the submission summary message.
	 */
	private Font boldFont;
}
