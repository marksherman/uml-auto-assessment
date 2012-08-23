/*
 *	This file is part of Web-CAT Eclipse Plugins.
 *
 *	Web-CAT is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Web-CAT is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Web-CAT; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.webcat.eclipse.cxxtest.wizards;

import net.sf.webcat.eclipse.cxxtest.wizards.dialogs.TranslationUnitSelectionDialog;
import net.sf.webcat.eclipse.cxxtest.wizards.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.wizards.ui.SWTUtil;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ISourceRoot;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.ui.wizards.SourceFolderSelectionDialog;
import org.eclipse.cdt.internal.ui.wizards.classwizard.NewClassWizardUtil;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class NewCxxTestSuiteWizardPageOne extends WizardPage
{
	private static final String PAGE_NAME = "NewCxxTestSuiteWizardPageOne"; //$NON-NLS-1$

	private static final String PAGE_TITLE = Messages.NewCxxTestSuiteWizardPageOne_PageTitle;
	private static final String PAGE_DESCRIPTION =
		Messages.NewCxxTestSuiteWizardPageOne_PageDescription;

	private IPath sourceFolder;
	private IPath headerUnderTest;
	private String suiteName;
	private String superClass;
	private boolean createSetUp;
	private boolean createTearDown;

	private Text sourceFolderField;
	private Text suiteNameField;
	private Text superClassField;
	private Text headerUnderTestField;
	private Button setUpButton;
	private Button tearDownButton;

	private NewCxxTestSuiteWizardPageTwo pageTwo;

	public NewCxxTestSuiteWizardPageOne(NewCxxTestSuiteWizardPageTwo pageTwo)
	{
		super(PAGE_NAME);
		this.pageTwo = pageTwo;

		sourceFolder = Path.EMPTY;
		headerUnderTest = Path.EMPTY;

		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
	}

	public void init(IStructuredSelection selection)
	{
		ICElement element = getSelectedElement(selection);

		if(element != null)
		{
			ICProject cproject = element.getCProject();
			IPath projectPath = cproject.getProject().getFullPath();
			sourceFolder = projectPath.makeRelative();
			
			ICElement tuElement = element.getAncestor(ICElement.C_UNIT);
			if(tuElement != null)
			{
				ITranslationUnit unit = (ITranslationUnit)tuElement;
				headerUnderTest = unit.getResource().getFullPath().makeRelative();
			}
		}
	}
	
	public ICElement getSelectedElement(IStructuredSelection selection)
	{
		ICElement element = null;
		if(selection != null && !selection.isEmpty())
		{
			Object selElem = selection.getFirstElement();
			
			if(selElem instanceof IAdaptable)
			{
				IAdaptable adaptable = (IAdaptable)selElem;
				element = (ICElement)adaptable.getAdapter(ICElement.class);
				
				if(element == null)
				{
					IResource resource = (IResource)adaptable.getAdapter(IResource.class);
					if(resource != null && resource.getType() != IResource.ROOT)
					{
						while(element == null && resource.getType() != IResource.PROJECT)
						{
							resource = resource.getParent();
							element = (ICElement)resource.getAdapter(ICElement.class);
						}
						
						if(element == null)
						{
							element = CoreModel.getDefault().create(resource);
						}
					}
				}
			}
		}
		
		return element;
	}

	public void createControl(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		
		int numColumns = 4;
		
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		composite.setLayout(layout);
		
		createContainerControls(composite, numColumns);
		createSeparator(composite, numColumns);
		createTypeNameControls(composite, numColumns);
		createSuperClassControls(composite, numColumns);
		createMethodStubSelectionControls(composite, numColumns);
		setSuperClass("CxxTest::TestSuite"); //$NON-NLS-1$
		createSeparator(composite, numColumns);
		createHeaderUnderTestControls(composite, numColumns);
		
		setControl(composite);
	}
	
	private void createSeparator(Composite composite, int numColumns)
	{
		Label sep = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		gd.horizontalSpan = numColumns;
		sep.setLayoutData(gd);
	}
	
	private void createContainerControls(Composite composite, int numColumns)
	{
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.NewCxxTestSuiteWizardPageOne_SourceFolder);
		label.setLayoutData(gridDataForLabel(1));

		sourceFolderField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		sourceFolderField.setLayoutData(gridDataForText(2));
		sourceFolderField.setText(sourceFolder.toString());
		sourceFolderField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e)
			{
				checkForErrors();
			}			
		});

		Button browseButton = new Button(composite, SWT.NONE);
		browseButton.setText(Messages.NewCxxTestSuiteWizardPageOne_Browse);
		browseButton.setLayoutData(gridDataForButton(browseButton, 1));
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e)
			{
				selectSourceFolder();
			}
		});
	}
	
	private void selectSourceFolder()
	{
	    IPath oldFolderPath = sourceFolder;
		IPath newFolderPath = chooseSourceFolder(oldFolderPath);
		if (newFolderPath != null)
		{
			sourceFolder = newFolderPath.makeRelative();
			sourceFolderField.setText(sourceFolder.toString());
		}
	}

    private IPath chooseSourceFolder(IPath initialPath) {
        ICElement initElement = NewClassWizardUtil.getSourceFolder(initialPath);
        if (initElement instanceof ISourceRoot) {
            ICProject cProject = initElement.getCProject();
            ISourceRoot projRoot = cProject.findSourceRoot(cProject.getProject());
            if (projRoot != null && projRoot.equals(initElement))
                initElement = cProject;
        }
        
        SourceFolderSelectionDialog dialog = new SourceFolderSelectionDialog(getShell());
        dialog.setInput(CoreModel.create(NewClassWizardUtil.getWorkspaceRoot()));
        dialog.setInitialSelection(initElement);
        
        if (dialog.open() == Window.OK) {
            Object result = dialog.getFirstResult();
            if (result instanceof ICElement) {
                ICElement element = (ICElement)result;
                if (element instanceof ICProject) {
                    ICProject cproject = (ICProject)element;
                    ISourceRoot folder = cproject.findSourceRoot(cproject.getProject());
                    if (folder != null)
                        return folder.getResource().getFullPath();
                }
                return element.getResource().getFullPath();
            }
        }
        return null;
    }   

	private void selectHeaderUnderTest()
	{
	    IPath oldFolderPath = headerUnderTest;
		IPath newFolderPath = chooseHeaderUnderTest(oldFolderPath);
		if (newFolderPath != null)
		{
			headerUnderTest = newFolderPath.makeRelative();
			headerUnderTestField.setText(headerUnderTest.toString());
		}
	}

    private IPath chooseHeaderUnderTest(IPath initialPath)
    {
        ICElement initElement = NewClassWizardUtil.getSourceFolder(initialPath);
        if (initElement instanceof ISourceRoot) {
            ICProject cProject = initElement.getCProject();
            ISourceRoot projRoot = cProject.findSourceRoot(cProject.getProject());
            if (projRoot != null && projRoot.equals(initElement))
                initElement = cProject;
        }
        
        TranslationUnitSelectionDialog dialog = new TranslationUnitSelectionDialog(getShell());
        dialog.setInput(CoreModel.create(NewClassWizardUtil.getWorkspaceRoot()));
        dialog.setInitialSelection(initElement);
        
        if (dialog.open() == Window.OK) {
            Object result = dialog.getFirstResult();
            if (result instanceof ICElement) {
                ICElement element = (ICElement)result;
                if (element instanceof ICProject) {
                    ICProject cproject = (ICProject)element;
                    ISourceRoot folder = cproject.findSourceRoot(cproject.getProject());
                    if (folder != null)
                        return folder.getResource().getFullPath();
                }
                return element.getResource().getFullPath();
            }
        }

        return null;
    }  

    private void createTypeNameControls(Composite composite, int numColumns)
	{
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.NewCxxTestSuiteWizardPageOne_Name);
		label.setLayoutData(gridDataForLabel(1));

		suiteNameField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		suiteNameField.setLayoutData(gridDataForText(2));
		suiteNameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e)
			{
				checkForErrors();
			}			
		});

		suiteNameField.setFocus();

		label = new Label(composite, SWT.NONE);
		label.setLayoutData(gridDataForLabel(1));
	}

	private void createSuperClassControls(Composite composite, int numColumns)
	{
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.NewCxxTestSuiteWizardPageOne_Superclass);
		label.setLayoutData(gridDataForLabel(1));

		superClassField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		superClassField.setLayoutData(gridDataForText(2));
		superClassField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e)
			{
				checkForErrors();
			}			
		});

		label = new Label(composite, SWT.NONE);
		label.setLayoutData(gridDataForLabel(1));
	}

	private void createMethodStubSelectionControls(Composite composite, int numColumns)
	{
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.NewCxxTestSuiteWizardPageOne_MethodStubsPrompt);
		label.setLayoutData(gridDataForLabel(4));

		label = new Label(composite, SWT.NONE);
		label.setLayoutData(gridDataForLabel(1));

		setUpButton = new Button(composite, SWT.CHECK);
		setUpButton.setText("setUp()"); //$NON-NLS-1$
		setUpButton.setLayoutData(gridDataForButton(setUpButton, 3));

		label = new Label(composite, SWT.NONE);
		label.setLayoutData(gridDataForLabel(1));

		tearDownButton = new Button(composite, SWT.CHECK);
		tearDownButton.setText("tearDown()"); //$NON-NLS-1$
		tearDownButton.setLayoutData(gridDataForButton(tearDownButton, 3));
	}

	private void createHeaderUnderTestControls(Composite composite, int numColumns)
	{
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.NewCxxTestSuiteWizardPageOne_HeaderUnderTest);
		label.setLayoutData(gridDataForLabel(1));

		headerUnderTestField = new Text(composite, SWT.SINGLE | SWT.BORDER);
		headerUnderTestField.setLayoutData(gridDataForText(2));
		headerUnderTestField.setText(headerUnderTest.toString());
		headerUnderTestField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e)
			{
				checkForErrors();
			}			
		});

		Button browseButton = new Button(composite, SWT.NONE);
		browseButton.setText(Messages.NewCxxTestSuiteWizardPageOne_Browse);
		browseButton.setLayoutData(gridDataForButton(browseButton, 1));
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e)
			{
				selectHeaderUnderTest();
			}
		});
	}

	private boolean isValidSuiteName()
	{
		String suiteName = suiteNameField.getText();
		
		char ch = suiteName.charAt(0);
		if(!Character.isLetter(ch) && ch != '_')
			return false;

		for(int i = 1; i < suiteName.length(); i++)
		{
			ch = suiteName.charAt(i);
			
			if(!Character.isLetterOrDigit(ch) && ch != '_')
				return false;
		}
		
		return true;
	}

	private void checkForErrors()
	{
		String msg = null;

		if(sourceFolderField.getText().length() == 0)
			msg = Messages.NewCxxTestSuiteWizardPageOne_EnterSourceFolderMsg;
		else if(suiteNameField.getText().length() == 0)
			msg = Messages.NewCxxTestSuiteWizardPageOne_EnterSuiteNameMsg;
		else if(!isValidSuiteName())
			msg = Messages.NewCxxTestSuiteWizardPageOne_SuiteNameInvalidMsg;
		else if(superClassField.getText().length() == 0)
			msg = Messages.NewCxxTestSuiteWizardPageOne_EnterSuperclassNameMsg;
		
		setErrorMessage(msg);
		setPageComplete(msg == null);
	}

	private static GridData gridDataForLabel(int span)
	{
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.horizontalSpan = span;
		return gd;
	}

	private static GridData gridDataForText(int span)
	{
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = span;
		return gd;
	}

	private static GridData gridDataForButton(Button button, int span)
	{
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = false;
		gd.horizontalSpan = span;
		gd.widthHint = SWTUtil.getButtonWidthHint(button);
		return gd;
	}
	
	private void setSuperClass(String name)
	{
		superClass = name;
		superClassField.setText(name);
	}

	public void collectFields()
	{
		if(suiteNameField.getText().length() == 0)
			suiteName = null;
		else
			suiteName = suiteNameField.getText();

		if(superClassField.getText().length() == 0)
			superClass = null;
		else
			superClass = superClassField.getText();

		if(sourceFolderField.getText().length() == 0)
			sourceFolder = null;
		else
			sourceFolder = new Path(sourceFolderField.getText());
		
		if(headerUnderTestField.getText().length() == 0)
			headerUnderTest = null;
		else
			headerUnderTest = new Path(headerUnderTestField.getText());

		createSetUp = setUpButton.getSelection();
		createTearDown = tearDownButton.getSelection();
	}
	
	public IPath getSourceFolder()
	{
		return sourceFolder;
	}
	
	public IPath getHeaderUnderTest()
	{
		return headerUnderTest;
	}
	
	public String getSuiteName()
	{
		return suiteName;
	}
	
	public String getSuperClass()
	{
		return superClass;
	}

	public boolean getCreateSetUp()
	{
		return createSetUp;
	}
	
	public boolean getCreateTearDown()
	{
		return createTearDown;
	}

	public IWizardPage getNextPage()
	{
		collectFields();
		pageTwo.setHeaderUnderTestPath(headerUnderTest);

		return super.getNextPage();
	}
}
