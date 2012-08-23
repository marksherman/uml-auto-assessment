/*==========================================================================*\
 |  $Id: AuthorsSection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.oda.designer.metadata;

import java.util.ArrayList;
import java.util.List;


import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.webcat.oda.commons.ReportMetadata;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * A section in the Overview page that edits properties related to the authors
 * of a report template.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: AuthorsSection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class AuthorsSection extends AbstractSection
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public AuthorsSection(OverviewFormPage formPage, Composite parent,
            FormToolkit toolkit, ModuleHandle model)
    {
        super(formPage, parent, toolkit, model, Messages.AUTHORS_SECTION_TITLE,
                Messages.AUTHORS_SECTION_DESCRIPTION);

        authors = new ArrayList<AuthorInfo>();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    protected void createContent(Composite parent)
    {
        GridLayout layout = new GridLayout(4, false);
        parent.setLayout(layout);

        Composite tableContainer = createGridComposite(parent, 2, false);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd.horizontalSpan = 4;
        tableContainer.setLayoutData(gd);

        authorTable = new TableViewer(tableContainer, SWT.BORDER
                | SWT.FULL_SELECTION);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 36;
        authorTable.getControl().setLayoutData(gd);

        authorTable
                .addPostSelectionChangedListener(new ISelectionChangedListener()
                {
                    public void selectionChanged(SelectionChangedEvent event)
                    {
                        authorSelectionChanged();
                    }
                });

        AuthorProvider provider = new AuthorProvider();
        authorTable.setContentProvider(provider);
        authorTable.setLabelProvider(provider);
        authorTable.setInput(authors);

        Composite buttonContainer = createGridComposite(tableContainer, 1, true);
        gd = new GridData(SWT.FILL, SWT.FILL, false, true);
        buttonContainer.setLayoutData(gd);

        createButton(buttonContainer, Messages.AUTHORS_ADD,
                new SelectionAdapter()
                {
                    public void widgetSelected(SelectionEvent e)
                    {
                        addAuthor();
                    }
                });

        removeButton = createButton(buttonContainer, Messages.AUTHORS_REMOVE,
                new SelectionAdapter()
                {
                    public void widgetSelected(SelectionEvent e)
                    {
                        removeAuthor();
                    }
                });

        createLabel(parent, Messages.AUTHORS_NAME, SWT.CENTER);
        nameField = createText(parent, false, SWT.NONE, SWT.DEFAULT, 3,
                new TrackingFocusListener(getFormPage())
        {
            @Override
            protected void textDidChange()
            {
                nameFieldModified();
            }
        });

        createLabel(parent, Messages.AUTHORS_EMAIL, SWT.CENTER);
        emailField = createText(parent, false, SWT.NONE,
                new TrackingFocusListener(getFormPage())
        {
            @Override
            protected void textDidChange()
            {
                emailFieldModified();
            }
        });

        createLabel(parent, Messages.AUTHORS_URL, SWT.CENTER);
        urlField = createText(parent, false, SWT.NONE,
                new TrackingFocusListener(getFormPage())
        {
            @Override
            protected void textDidChange()
            {
                urlFieldModified();
            }
        });

        createLabel(parent, Messages.AUTHORS_AFFILIATION, SWT.CENTER);
        affiliationField = createText(parent, false, SWT.NONE,
                new TrackingFocusListener(getFormPage())
        {
            @Override
            protected void textDidChange()
            {
                affiliationFieldModified();
            }
        });

        createLabel(parent, Messages.AUTHORS_PHONE, SWT.CENTER);
        phoneField = createText(parent, false, SWT.NONE,
                new TrackingFocusListener(getFormPage())
        {
            @Override
            protected void textDidChange()
            {
                phoneFieldModified();
            }
        });
    }


    // ----------------------------------------------------------
    public void updateControls()
    {
        ModuleHandle model = getModel();

        authors.clear();

        int count = ReportMetadata.getAuthorsCount(model);

        for (int i = 0; i < count; i++)
        {
            AuthorInfo author = new AuthorInfo();

            author.name = ReportMetadata.getAuthorName(model, i);
            author.email = ReportMetadata.getAuthorEmail(model, i);
            author.url = ReportMetadata.getAuthorURL(model, i);
            author.affiliation = ReportMetadata.getAuthorAffiliation(model, i);
            author.phone = ReportMetadata.getAuthorPhone(model, i);

            authors.add(author);
        }

        authorTable.setInput(authors);
        authorTable.refresh();
        updateErrors();

        authorSelectionChanged();
    }


    // ----------------------------------------------------------
    public void saveModel()
    {
        ModuleHandle model = getModel();

        ReportMetadata.setAuthorsCount(model, authors.size());

        for (int i = 0; i < authors.size(); i++)
        {
            AuthorInfo author = authors.get(i);

            ReportMetadata.setAuthorName(model, i, author.name);
            ReportMetadata.setAuthorEmail(model, i, author.email);
            ReportMetadata.setAuthorURL(model, i, author.url);
            ReportMetadata.setAuthorAffiliation(model, i,
                    author.affiliation);
            ReportMetadata.setAuthorPhone(model, i, author.phone);
        }
    }


    // ----------------------------------------------------------
    private void updateErrors()
    {
        int authorCount = authors.size();

        if(authorCount == 0)
        {
            addMessage(NO_AUTHORS_KEY,
                    NO_AUTHORS_MESSAGE, null,
                    IMessageProvider.ERROR, authorTable.getControl());
        }
        else
        {
            removeMessage(NO_AUTHORS_KEY, authorTable.getControl());
            removeMessage(NO_NAME_AUTHOR_KEY, authorTable.getControl());

            for(int i = 0; i < authorCount; i++)
            {
                String name = authors.get(i).name;

                if(name == null || name.trim().length() == 0)
                {
                    addMessage(NO_NAME_AUTHOR_KEY, NO_NAME_AUTHOR_MESSAGE, null,
                            IMessageProvider.ERROR, authorTable.getControl());

                    break;
                }
            }
        }
    }


    // ----------------------------------------------------------
    private AuthorInfo getSelectedAuthor()
    {
        IStructuredSelection selection =
            (IStructuredSelection) authorTable.getSelection();

        if(selection.isEmpty())
        {
            return null;
        }
        else
        {
            return (AuthorInfo) selection.getFirstElement();
        }
    }


    // ----------------------------------------------------------
    private void authorSelectionChanged()
    {
        AuthorInfo author = getSelectedAuthor();

        if(author == null)
        {
            nameField.setText(Messages.AUTHORS_SECTION_0);
            emailField.setText(Messages.AUTHORS_SECTION_1);
            urlField.setText(Messages.AUTHORS_SECTION_2);
            affiliationField.setText(Messages.AUTHORS_SECTION_3);
            phoneField.setText(Messages.AUTHORS_SECTION_4);

            nameField.setEnabled(false);
            emailField.setEnabled(false);
            urlField.setEnabled(false);
            affiliationField.setEnabled(false);
            phoneField.setEnabled(false);

            removeButton.setEnabled(false);
        }
        else
        {
            safeSetText(nameField, author.name);
            safeSetText(emailField, author.email);
            safeSetText(urlField, author.url);
            safeSetText(affiliationField, author.affiliation);
            safeSetText(phoneField, author.phone);

            nameField.setEnabled(true);
            emailField.setEnabled(true);
            urlField.setEnabled(true);
            affiliationField.setEnabled(true);
            phoneField.setEnabled(true);

            removeButton.setEnabled(true);
        }
    }


    // ----------------------------------------------------------
    private void addAuthor()
    {
        authors.add(new AuthorInfo());
        authorTable.refresh();
        authorTable.getTable().setSelection(authors.size() - 1);

        updateErrors();

        authorSelectionChanged();

        nameField.setFocus();

        getFormPage().markAsDirty();
    }


    // ----------------------------------------------------------
    private void removeAuthor()
    {
        AuthorInfo author = getSelectedAuthor();

        if (author != null)
        {
            authors.remove(author);
            authorTable.refresh();

            updateErrors();

            authorSelectionChanged();

            getFormPage().markAsDirty();
        }
    }


    // ----------------------------------------------------------
    private void nameFieldModified()
    {
        AuthorInfo author = getSelectedAuthor();

        if (author != null)
        {
            author.name = nameField.getText();
            authorTable.update(author, null);
            updateErrors();
        }
    }


    // ----------------------------------------------------------
    private void emailFieldModified()
    {
        AuthorInfo author = getSelectedAuthor();

        if (author != null)
        {
            author.email = emailField.getText();
            authorTable.update(author, null);
        }
    }


    // ----------------------------------------------------------
    private void urlFieldModified()
    {
        AuthorInfo author = getSelectedAuthor();

        if (author != null)
        {
            author.url = urlField.getText();
        }
    }


    // ----------------------------------------------------------
    private void affiliationFieldModified()
    {
        AuthorInfo author = getSelectedAuthor();

        if (author != null)
        {
            author.affiliation = affiliationField.getText();
        }
    }


    // ----------------------------------------------------------
    private void phoneFieldModified()
    {
        AuthorInfo author = getSelectedAuthor();

        if (author != null)
        {
            author.phone = phoneField.getText();
        }
    }


    // ----------------------------------------------------------
    private class AuthorInfo
    {
        public String name;
        public String email;
        public String url;
        public String affiliation;
        public String phone;
    }


    // ----------------------------------------------------------
    private class AuthorProvider
    implements IStructuredContentProvider, ITableLabelProvider
    {
        // ----------------------------------------------------------
        public Object[] getElements(Object inputElement)
        {
            return authors.toArray();
        }


        // ----------------------------------------------------------
        public void dispose()
        {
            // Do nothing.
        }


        // ----------------------------------------------------------
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            // Do nothing.
        }


        // ----------------------------------------------------------
        public Image getColumnImage(Object element, int columnIndex)
        {
            return null;
        }


        // ----------------------------------------------------------
        public String getColumnText(Object element, int columnIndex)
        {
            AuthorInfo author = (AuthorInfo) element;

            String name = Messages.AUTHORS_SECTION_5;

            if(author.name == null)
            {
                name = Messages.AUTHORS_SECTION_NO_NAME_PROVIDED;
            }
            else
            {
                name = author.name;
            }

            if(author.email != null)
            {
                name += " (" + author.email + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            }

            return name;
        }


        // ----------------------------------------------------------
        public void addListener(ILabelProviderListener listener)
        {
            // Do nothing.
        }


        // ----------------------------------------------------------
        public boolean isLabelProperty(Object element, String property)
        {
            return false;
        }


        // ----------------------------------------------------------
        public void removeListener(ILabelProviderListener listener)
        {
            // Do nothing.
        }
    }


    //~ Static/instance variables .............................................

    private static final String NO_AUTHORS_KEY = "noAuthors"; //$NON-NLS-1$
    private static final String NO_NAME_AUTHOR_KEY = "author.noName"; //$NON-NLS-1$

    private static final String NO_AUTHORS_MESSAGE = Messages.AUTHORS_SECTION_ERROR_NO_AUTHORS;
    private static final String NO_NAME_AUTHOR_MESSAGE = Messages.AUTHORS_SECTION_ERROR_AUTHOR_NO_NAME;

    private TableViewer authorTable;
    private Button removeButton;
    private Text nameField;
    private Text emailField;
    private Text urlField;
    private Text affiliationField;
    private Text phoneField;
    private List<AuthorInfo> authors;
}
