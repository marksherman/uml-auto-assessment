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

package net.sf.webcat.eclipse.cxxtest.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;

import net.sf.webcat.eclipse.cxxtest.ICxxTestConstants;
import net.sf.webcat.eclipse.cxxtest.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestBase;
import net.sf.webcat.eclipse.cxxtest.model.IDerefereeSummary;
import net.sf.webcat.eclipse.cxxtest.model.IDerefereeLeak;
import net.sf.webcat.eclipse.cxxtest.xml.ContextualSAXHandler;
import net.sf.webcat.eclipse.cxxtest.xml.memstats.DocumentContext;

import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.internal.ui.util.EditorUtility;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledFormText;
import org.eclipse.ui.texteditor.ITextEditor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A tab that displays a summary of memory allocation and leaks that occurred
 * during the program execution. 
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
@SuppressWarnings("restriction")
public class TestMemoryTab extends TestRunTab implements ISelectionProvider
{
	private TreeViewer viewer;

	private IDerefereeSummary memWatchInfo;

	private MemWatchContentProvider viewerContent;

	private ICProject project;
	
	private ListenerList selectionListeners= new ListenerList();
	
	private TestRunnerViewPart testRunnerView;
	
	private final Image leakNoArrayIcon =
		TestRunnerViewPart.createImage("obj16/leak_noarray.gif"); //$NON-NLS-1$
	private final Image leakArrayIcon =
		TestRunnerViewPart.createImage("obj16/leak_array.gif"); //$NON-NLS-1$
	private final Image memoryTabIcon =
		TestRunnerViewPart.createImage("obj16/memory.gif"); //$NON-NLS-1$

	private StackLayout stackLayout;

	private FormToolkit toolkit;

	private ScrolledFormText errorMsgField;

	private class MemWatchInfoInput
	{
		public IDerefereeSummary info;
		
		public MemWatchInfoInput(IDerefereeSummary info)
		{
			this.info = info;
		}
	}

	private class MemWatchContentProvider implements ITreeContentProvider
	{
		public Object[] getChildren(Object parent)
		{
			if(parent instanceof IDerefereeSummary)
				return ((IDerefereeSummary)parent).getLeaks();
			else
				return null;
		}

		public Object getParent(Object element)
		{
			return null;
		}

		public boolean hasChildren(Object element)
		{
			if(element instanceof IDerefereeSummary)
				return true;
			else
				return false;
		}

		public Object[] getElements(Object inputElement)
		{
			MemWatchInfoInput input = (MemWatchInfoInput)inputElement;
			
			if(input.info == null)
				return new Object[0];
			else
				return new Object[] { input.info };
		}

		public void dispose() { }

		public void inputChanged(
				Viewer viewer, Object oldInput, Object newInput) { }
	}

	private class MemWatchLabelProvider extends LabelProvider
	{
		public String getText(Object element)
		{
			if(element instanceof IDerefereeSummary)
			{
				int leaksShown = memWatchInfo.getLeaks().length;
				int actualLeaks = memWatchInfo.getActualLeakCount();
				
				String msg;

				if(leaksShown != actualLeaks)
				{
					msg = MessageFormat.format(
							Messages.TestMemoryTab_NumMemoryLeaksElided,
							actualLeaks, leaksShown);
				}
				else
				{
					msg = MessageFormat.format(
							Messages.TestMemoryTab_NumMemoryLeaks,
							actualLeaks);
				}

				return msg;
			}
			else if(element instanceof IDerefereeLeak)
			{
				IDerefereeLeak leak = (IDerefereeLeak)element;
				return leak.toString();
			}
			else
				return element.toString();
		}
		
		public Image getImage(Object element)
		{
			if(element instanceof IDerefereeSummary)
			{
				return memoryTabIcon;
			}
			else if(element instanceof IDerefereeLeak)
			{
				IDerefereeLeak leak = (IDerefereeLeak)element;
				
				if(leak.isArray())
					return leakArrayIcon;
				else
					return leakNoArrayIcon;
			}

			return null;
		}
	}

	public void createTabControl(CTabFolder tabFolder, Clipboard clipboard, TestRunnerViewPart runner)
	{
		testRunnerView = runner;
		
		CTabItem memoryTab = new CTabItem(tabFolder, SWT.NONE);
		memoryTab.setText(getName());
		memoryTab.setImage(memoryTabIcon);
		
		Composite testTreePanel= new Composite(tabFolder, SWT.NONE);
		stackLayout = new StackLayout();
		testTreePanel.setLayout(stackLayout);
		
		GridData gridData= new GridData(
				GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		testTreePanel.setLayoutData(gridData);
		
		memoryTab.setControl(testTreePanel);
		memoryTab.setToolTipText(Messages.TestMemoryTab_MemoryUsageTooltip); 
		
		viewer = new TreeViewer(testTreePanel, SWT.V_SCROLL | SWT.SINGLE);
		gridData= new GridData(GridData.FILL_BOTH |
				GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		viewerContent = new MemWatchContentProvider();
		viewer.setContentProvider(viewerContent);
		viewer.setLabelProvider(new MemWatchLabelProvider());

		viewer.getTree().setLayoutData(gridData);
		OpenStrategy handler = new OpenStrategy(viewer.getTree());
		handler.addPostSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e)
			{
				fireSelectionChanged();
			}
		});

		Display display = tabFolder.getDisplay();

		toolkit = new FormToolkit(display);
		errorMsgField = new ScrolledFormText(testTreePanel, true);
		errorMsgField.setBackground(toolkit.getColors().getBackground());
		errorMsgField.getFormText().setColor(ERROR_COLOR_KEY,
				toolkit.getColors().createColor(ERROR_COLOR_KEY, 255, 0, 0));

		errorMsgField.getFormText().addHyperlinkListener(new HyperlinkAdapter()
		{
			public void linkActivated(HyperlinkEvent e)
			{
				String link = e.getHref().toString();
				openLink(link);
			}
		});

		stackLayout.topControl = viewer.getControl();

		addListeners();
	}

	private void openLink(String link)
	{
		if(link.startsWith(ICxxTestConstants.DEREFEREE_RESULTS_FILE))
		{
			String[] parts = link.split(":"); //$NON-NLS-1$
			int lineNumber = 1;

			if(parts.length == 2)
				lineNumber = Integer.parseInt(parts[1]);
			
			ICProject project = testRunnerView.getLaunchedProject();
			IFile file = project.getProject().getFile(
					ICxxTestConstants.DEREFEREE_RESULTS_FILE);
			
			try
			{
				ITextEditor editor =
					(ITextEditor)EditorUtility.openInEditor(file, true);

				try
				{
					IDocument document= editor.getDocumentProvider().
						getDocument(editor.getEditorInput());

					editor.selectAndReveal(
							document.getLineOffset(lineNumber - 1),
							document.getLineLength(lineNumber - 1));
				}
				catch(BadLocationException ex) { }
			}
			catch (PartInitException ex) { }
			catch (CModelException ex) { }
		}
	}

	private void disposeIcons()
	{
		leakNoArrayIcon.dispose();
		leakArrayIcon.dispose();

		memoryTabIcon.dispose();
		
		toolkit.dispose();
	}
	
	public Object getSelectedObject()
	{
		IStructuredSelection sel =
			(IStructuredSelection)viewer.getSelection();

		if(sel.size() == 0)
			return null;
		else
			return sel.getFirstElement();
	}

	public String getName()
	{
		return Messages.TestMemoryTab_TabName; 
	}
	
	public void setSelectedTest(ICxxTestBase testObject)
	{
		viewer.setSelection(new StructuredSelection(testObject));
	}

	public void activate()
	{
		testRunnerView.handleObjectSelected(getSelectedObject());
	}
	
	public void setFocus()
	{
		viewer.getTree().setFocus();
	}

	private void addListeners()
	{
		viewer.getTree().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				activate();
			}
		});
		
		viewer.getTree().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				disposeIcons();
			}
		});
	}
	
	public void setMemWatchInfo(IDerefereeSummary mwInfo)
	{
		this.memWatchInfo = mwInfo;

		viewer.setInput(new MemWatchInfoInput(mwInfo));
		viewer.expandAll();

		errorMsgField.setText(""); //$NON-NLS-1$
		stackLayout.topControl = viewer.getControl();
		viewer.getControl().getParent().layout();
	}
	
	protected void expandAll()
	{
		viewer.expandAll();
	}
	
	public void addSelectionChangedListener(ISelectionChangedListener listener)
	{
		selectionListeners.add(listener);
	}

	public ISelection getSelection()
	{
		return viewer.getSelection();
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener)
	{
		selectionListeners.remove(listener);
	}

	public void setSelection(ISelection selection)
	{
		viewer.setSelection(selection, true);
	}
	
	private void fireSelectionChanged()
	{
		SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());
		Object[] listeners = selectionListeners.getListeners();
		for (int i = 0; i < listeners.length; i++)
		{
			ISelectionChangedListener listener = (ISelectionChangedListener)listeners[i];
			listener.selectionChanged(event);
		}	
	}

	private String escapeXMLString(String str)
	{
		StringBuffer buf = new StringBuffer();
		
		for(int i = 0; i < str.length(); i++)
		{
			char ch = str.charAt(i);
			
			switch(ch)
			{
				case '&':  buf.append("&amp;"); break;  //$NON-NLS-1$
				case '\'': buf.append("&apos;"); break;  //$NON-NLS-1$
				case '"':  buf.append("&quot;"); break;  //$NON-NLS-1$
				case '<':  buf.append("&lt;"); break;  //$NON-NLS-1$
				case '>':  buf.append("&gt;"); break; //$NON-NLS-1$
				default:   buf.append(ch); break;
			}
		}
		
		return buf.toString();
	}

	private void setParseError(Exception e)
	{
		StringBuffer msg = new StringBuffer();
		msg.append("<form>"); //$NON-NLS-1$

		msg.append("<p><b>"); //$NON-NLS-1$
		msg.append(Messages.TestMemoryTab_ErrorTitle);
		msg.append("</b></p>"); //$NON-NLS-1$

		if(e instanceof SAXParseException)
		{
			SAXParseException spe = (SAXParseException) e;
			msg.append(MessageFormat.format(
					Messages.TestMemoryTab_ErrorDescriptionWithLineNumber,
					ICxxTestConstants.DEREFEREE_RESULTS_FILE,
					spe.getLineNumber(), escapeXMLString(e.getMessage())));
		}
		else
		{
			msg.append(MessageFormat.format(
					Messages.TestMemoryTab_ErrorDescription,
					ICxxTestConstants.DEREFEREE_RESULTS_FILE,
					escapeXMLString(e.getMessage())));
		}

		msg.append("</form>"); //$NON-NLS-1$

		errorMsgField.setText(msg.toString());

		stackLayout.topControl = errorMsgField;
		errorMsgField.getParent().layout();
	}

	public void testRunStarted(ICProject project, ILaunch launch)
	{
		this.project = project;
		
		setMemWatchInfo(null);
	}

	@SuppressWarnings("deprecation")
	public void testRunEnded()
	{
		try
		{
			IFile resultsFile = project.getProject().getFile(
					ICxxTestConstants.DEREFEREE_RESULTS_FILE);
			File resultsPath = resultsFile.getLocation().toFile();
	
			// When the CxxTest results file is generated, Eclipse
			// autodetects this change in the project contents and
			// attempts to run the managed make process again
			// (which has no effect because there's nothing new to
			// make, but it does delete warning markers). But it
			// appears setting the derived flag (which does apply
			// here, as it's derived from the runner) will prevent
			// managed make from doing an extraneous run.
			try { resultsFile.setDerived(true); }
			catch (CoreException e1) { }
	
			FileInputStream stream = new FileInputStream(resultsPath);
			InputSource source = new InputSource(stream);

			DocumentContext docContext = new DocumentContext();
			final ContextualSAXHandler handler = new ContextualSAXHandler(docContext);
	
			try
			{
				XMLReader reader = XMLReaderFactory.createXMLReader();
				reader.setContentHandler(handler);
				reader.parse(source);
			}
			catch (Exception e)
			{
				setParseError(e);
				return;
			}
			finally
			{
				stream.close();
			}
	
			IDerefereeSummary mwInfo = docContext.getSummary();
			setMemWatchInfo(mwInfo);
		}
		catch(IOException e) { }
	}
	
	
	private static final String ERROR_COLOR_KEY = "error"; //$NON-NLS-1$
}
