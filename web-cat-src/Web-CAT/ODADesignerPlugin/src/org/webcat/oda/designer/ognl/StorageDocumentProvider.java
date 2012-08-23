/*==========================================================================*\
 |  $Id: StorageDocumentProvider.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.ognl;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: StorageDocumentProvider.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class StorageDocumentProvider extends DocumentProvider
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new document provider.
     *
     */
    public StorageDocumentProvider()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Initializes the given document from the given editor input using the
     * given character encoding.
     *
     * @param document
     *            the document to be initialized
     * @param editorInput
     *            the input from which to derive the content of the document
     * @param encoding
     *            the character encoding used to read the editor input
     * @return <code>true</code> if the document content could be set,
     *         <code>false</code> otherwise
     * @throws CoreException
     *             if the given editor input cannot be accessed
     */
    protected boolean setDocumentContent(IDocument document,
            IEditorInput editorInput) throws CoreException
    {
        IStorage storage = null;
        if (editorInput instanceof OgnlEditorInput)
        {
            storage = ((OgnlEditorInput) editorInput).getStorage();
        }

        if (storage != null)
        {
            InputStream stream = storage.getContents();
            try
            {
                setDocumentContent(document, stream);
            }
            finally
            {
                try
                {
                    stream.close();
                }
                catch (IOException x)
                {
                    // Ignore exception.
                }
            }
            return true;
        }
        return false;
    }


    // ----------------------------------------------------------
    /**
     * Returns the persisted encoding for the given element.
     *
     * @param element
     *            the element for which to get the persisted encoding
     * @return the persisted encoding
     */
    protected String getPersistedEncoding(Object element)
    {
        if (element instanceof OgnlEditorInput)
        {
            IStorage storage;
            try
            {
                storage = ((OgnlEditorInput) element).getStorage();
                if (storage instanceof IEncodedStorage)
                    return ((IEncodedStorage) storage).getCharset();
            }
            catch (CoreException e)
            {
                return null;
            }
        }
        return null;
    }


    // ----------------------------------------------------------
    public boolean isModifiable(Object element)
    {
        return !isReadOnly(element);
    }


    // ----------------------------------------------------------
    public boolean isReadOnly(Object element)
    {
        IStorage storage = null;

        if (element instanceof OgnlEditorInput)
        {
            storage = ((OgnlEditorInput) element).getStorage();
        }

        if (storage != null)
        {
            return storage.isReadOnly();
        }
        return super.isReadOnly(element);
    }
}
