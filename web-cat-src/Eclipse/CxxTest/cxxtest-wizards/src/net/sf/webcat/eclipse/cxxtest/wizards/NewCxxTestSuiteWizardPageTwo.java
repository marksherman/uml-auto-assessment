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

import java.util.Vector;

import net.sf.webcat.eclipse.cxxtest.wizards.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.wizards.ui.SWTUtil;

import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.IFunctionDeclaration;
import org.eclipse.cdt.core.model.IMethodDeclaration;
import org.eclipse.cdt.core.model.INamespace;
import org.eclipse.cdt.core.model.IParent;
import org.eclipse.cdt.core.model.IStructure;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.core.parser.ast.ASTAccessVisibility;
import org.eclipse.cdt.ui.CElementLabelProvider;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

public class NewCxxTestSuiteWizardPageTwo extends WizardPage
{
	private static final String PAGE_NAME = "NewCxxTestSuiteWizardPageTwo"; //$NON-NLS-1$

	private static final String PAGE_TITLE = Messages.NewCxxTestSuiteWizardPageTwo_PageTitle;
	private static final String PAGE_DESCRIPTION =
		Messages.NewCxxTestSuiteWizardPageTwo_PageDescription;

	private ContainerCheckedTreeViewer functionsTree;
	private Button selectAllButton;
	private Button deselectAllButton;
	private Label selectedFunctionsLabel;

	private IPath headerUnderTestPath;
	private Object[] checkedObjects;

	public NewCxxTestSuiteWizardPageTwo()
	{
		super(PAGE_NAME);
		
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
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
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);

		createFunctionsTreeControls(container);

		setControl(container);
	}

	private void createFunctionsTreeControls(Composite container)
	{
		Label label= new Label(container, SWT.LEFT | SWT.WRAP);
		label.setFont(container.getFont());
		label.setText(Messages.NewCxxTestSuiteWizardPageTwo_AvailableFunctions); 
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		functionsTree = new ContainerCheckedTreeViewer(container, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gd.heightHint = 180;
		functionsTree.getTree().setLayoutData(gd);

		functionsTree.setLabelProvider(new CustomCElementLabelProvider());
		functionsTree.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);			
		functionsTree.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				doCheckedStateChanged();
			}	
		});

		Composite buttonContainer = new Composite(container, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonContainer.setLayoutData(gd);
		GridLayout buttonLayout = new GridLayout();
		buttonLayout.marginWidth = 0;
		buttonLayout.marginHeight = 0;
		buttonContainer.setLayout(buttonLayout);

		selectAllButton = new Button(buttonContainer, SWT.PUSH);
		selectAllButton.setText(Messages.NewCxxTestSuiteWizardPageTwo_SelectAll); 
		gd= new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		selectAllButton.setLayoutData(gd);
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				functionsTree.setCheckedElements((Object[]) functionsTree.getInput());
				doCheckedStateChanged();
			}
		});
		SWTUtil.setButtonDimensionHint(selectAllButton);

		deselectAllButton = new Button(buttonContainer, SWT.PUSH);
		deselectAllButton.setText(Messages.NewCxxTestSuiteWizardPageTwo_DeselectAll); 
		gd= new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		deselectAllButton.setLayoutData(gd);
		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				functionsTree.setCheckedElements(new Object[0]);
				doCheckedStateChanged();
			}
		});
		SWTUtil.setButtonDimensionHint(deselectAllButton);

		/* No of selected functions label */
		selectedFunctionsLabel = new Label(container, SWT.LEFT);
		selectedFunctionsLabel.setFont(container.getFont());
		doCheckedStateChanged();
		gd= new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		selectedFunctionsLabel.setLayoutData(gd);
		
		Label emptyLabel = new Label(container, SWT.LEFT);
		gd= new GridData();
		gd.horizontalSpan = 1;
		emptyLabel.setLayoutData(gd);
	}

	public void setVisible(boolean visible)
	{
		super.setVisible(visible);

		if (visible)
		{
			if(headerUnderTestPath == null)
				return;
			
			ITranslationUnit unit = null;
			try
			{
				ICElement element = CoreModel.getDefault().create(headerUnderTestPath);
				if(element instanceof ITranslationUnit)
				{
					unit = (ITranslationUnit)element;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			functionsTree.setContentProvider(new FunctionsTreeContentProvider());
			functionsTree.setInput(new Object[] { unit });

			doCheckedStateChanged();
			
			functionsTree.getControl().setFocus();
		}
		else
		{
			//saveWidgetValues();
		}
	}

	public void setHeaderUnderTestPath(IPath path)
	{
		headerUnderTestPath = path;
	}

	private void doCheckedStateChanged()
	{
		Object[] checked = functionsTree.getCheckedElements();
		checkedObjects = checked;
		
		int checkedFunctionCount= 0;
		for(int i= 0; i < checked.length; i++)
		{
			if(checked[i] instanceof IFunctionDeclaration)
				checkedFunctionCount++;
		}

		String label = Integer.toString(checkedFunctionCount);
		if(checkedFunctionCount == 1)
			label += Messages.NewCxxTestSuiteWizardPageTwo_FunctionSelectedSingular; 
		else
			label += Messages.NewCxxTestSuiteWizardPageTwo_FunctionSelectedPlural; 

		selectedFunctionsLabel.setText(label);
	}

	public IFunctionDeclaration[] getCheckedFunctions()
	{
		int functionCount= 0;
		for(int i = 0; i < checkedObjects.length; i++)
		{
			if(checkedObjects[i] instanceof IFunctionDeclaration)
				functionCount++;
		}
		
		IFunctionDeclaration[] checkedFunctions= new IFunctionDeclaration[functionCount];
		int j = 0;
		for(int i = 0; i < checkedObjects.length; i++)
		{
			if(checkedObjects[i] instanceof IFunctionDeclaration)
			{
				checkedFunctions[j]= (IFunctionDeclaration)checkedObjects[i];
				j++;
			}
		}

		return checkedFunctions;
	}

	private static class CustomCElementLabelProvider extends CElementLabelProvider
	{
		public String getText(Object element)
		{
			if(element instanceof ITranslationUnit)
			{
				return Messages.NewCxxTestSuiteWizardPageTwo_GlobalScope;
			}
			else
			{
				return super.getText(element);
			}
		}
	}

	private static class FunctionsTreeContentProvider implements ITreeContentProvider
	{
		private Object[] elements = new Object[0];
		private final Object[] fEmpty = new Object[0];

		public FunctionsTreeContentProvider()
		{
		}

		private ICElement[] getChildrenOfElement(IParent element)
		{
			Vector<ICElement> vec = new Vector<ICElement>();
			
			try
			{
				ICElement[] children = element.getChildren();
				
				for(int i = 0; i < children.length; i++)
				{
					ICElement child = children[i];
					
					if(child instanceof INamespace || child instanceof IStructure)
					{
						vec.add(child);
					}
					else if(child instanceof IMethodDeclaration)
					{
						IMethodDeclaration method = (IMethodDeclaration)child;
						if (!method.isDestructor() &&
								method.getVisibility() == ASTAccessVisibility.PUBLIC)
						{
							vec.add(method);
						}
					}
					else if(child instanceof IFunctionDeclaration)
					{
						vec.add(child);
					}
				}
			}
			catch(CModelException e)
			{
				e.printStackTrace();
			}

			return vec.toArray(new ICElement[vec.size()]);			
		}

		/*
		 * @see ITreeContentProvider#getChildren(Object)
		 */
		public Object[] getChildren(Object parentElement)
		{
			if(parentElement instanceof IParent)
				return getChildrenOfElement((IParent)parentElement);
			else
				return fEmpty;
		}

		/*
		 * @see ITreeContentProvider#getParent(Object)
		 */
		public Object getParent(Object element)
		{
			if(element instanceof ITranslationUnit)
				return null;
			else if(element instanceof ICElement)
				return ((ICElement)element).getParent();
			else
				return null;
			}

		/*
		 * @see ITreeContentProvider#hasChildren(Object)
		 */
		public boolean hasChildren(Object element)
		{
			return getChildren(element).length > 0;
		}

		/*
		 * @see IStructuredContentProvider#getElements(Object)
		 */
		public Object[] getElements(Object inputElement)
		{
			return elements;
		}

		/*
		 * @see IContentProvider#dispose()
		 */
		public void dispose()
		{
		}

		/*
		 * @see IContentProvider#inputChanged(Viewer, Object, Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{
			this.elements = (Object[])newInput;
		}
	}
}
