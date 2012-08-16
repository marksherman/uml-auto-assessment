/*==========================================================================*\
 |  $Id: OgnlEditorInput.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OgnlEditorInput.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OgnlEditorInput implements IEditorInput
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * @param _name
     *            the name of the editor input
     */
    public OgnlEditorInput(String _name)
    {
        super();
        this.name = _name;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public boolean exists()
    {
        return false;
    }


    // ----------------------------------------------------------
    public ImageDescriptor getImageDescriptor()
    {
        return null;
    }


    // ----------------------------------------------------------
    public String getName()
    {
        return name;
    }


    // ----------------------------------------------------------
    public IPersistableElement getPersistable()
    {
        return null;
    }


    // ----------------------------------------------------------
    public String getToolTipText()
    {
        return null;
    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter)
    {
        return null;
    }


    // ----------------------------------------------------------
    /**
     * @see org.eclipse.ui.IStorageEditorInput#getStorage()
     * @return IStorage
     * @throw CoreException
     */
    public IStorage getStorage()
    {
        return new OgnlStorage();
    }


    //~ Nested classes ........................................................

    // ----------------------------------------------------------
    private class OgnlStorage implements IStorage
    {
        //~ Constructor .......................................................

        // ----------------------------------------------------------
        public OgnlStorage()
        {
            super();
        }


        //~ Methods ...........................................................

        // ----------------------------------------------------------
        public InputStream getContents() throws CoreException
        {
            if (name == null)
            {
                name = ""; //$NON-NLS-1$
            }

            return new ByteArrayInputStream(name.getBytes());
        }


        // ----------------------------------------------------------
        public IPath getFullPath()
        {
            return null;
        }


        // ----------------------------------------------------------
        public String getName()
        {
            return name;
        }


        // ----------------------------------------------------------
        public boolean isReadOnly()
        {
            return false;
        }


        // ----------------------------------------------------------
        @SuppressWarnings("unchecked")
        public Object getAdapter(Class adapter)
        {
            return null;
        }
    }


    //~ Static/instance variables .............................................

    private String name = null;
}
