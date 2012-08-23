/*==========================================================================*\
 |  $Id: SubmittableEclipseResource.java,v 1.4 2010/12/06 21:07:26 aallowat Exp $
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

package org.webcat.eclipse.submitter.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.webcat.submitter.ISubmittableItem;
import org.webcat.submitter.SubmittableItemKind;

//--------------------------------------------------------------------------
/**
 * A concrete implementation of {@link org.webcat.submitter.ISubmittableItem}
 * that is based on Eclipse {@link org.eclipse.core.resources.IResource}
 * handles, to support packaging and submitting projects directly from an
 * Eclipse workspace.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.4 $ $Date: 2010/12/06 21:07:26 $
 */
public class SubmittableEclipseResource implements ISubmittableItem
{
	//~ Constructors .........................................................

	// ----------------------------------------------------------
    /**
     * Initializes a new {@code SubmittableEclipseResource} as a root with the
     * specified resource.
     *
     * @param resource the Eclipse resource
     */
	public SubmittableEclipseResource(IResource resource)
	{
		this(null, resource);
	}


	// ----------------------------------------------------------
    /**
     * Initializes a new {@code SubmittableEclipseResource} with the specified
     * parent and path.
     *
     * @param parent the {@code SubmittableEclipseResource} that is the parent
     *     of this one
     * @param resource the Eclipse resource
     */
	public SubmittableEclipseResource(SubmittableEclipseResource parent,
			IResource resource)
	{
		this.parent = parent;
		this.resource = resource;
	}


	//~ Methods ..............................................................

	// ----------------------------------------------------------
    /**
     * @see ISubmittableItem#getChildren()
     */
	public ISubmittableItem[] getChildren()
	{
		if (getKind() == SubmittableItemKind.FILE)
		{
			return new ISubmittableItem[0];
		}
		else
		{
			IContainer container = (IContainer) resource;

			IResource[] children;
			try
			{
				children = container.members();

				ISubmittableItem[] items =
					new ISubmittableItem[children.length];

				int i = 0;
				for (IResource child : children)
				{
					items[i] = new SubmittableEclipseResource(this, child);
					i++;
				}

				return items;
			}
			catch (CoreException e)
			{
				return new ISubmittableItem[0];
			}
		}
	}


	// ----------------------------------------------------------
    /**
     * @see ISubmittableItem#getFilename()
     */
	public String getFilename()
	{
		if (parent == null)
		{
			return resource.getName();
		}
        else
        {
            String parentName = parent.getFilename();

            if (parentName.length() > 0)
            {
                parentName += "/";
            }

            return parentName + resource.getName();
        }
	}


	// ----------------------------------------------------------
    /**
     * @see ISubmittableItem#getKind()
     */
	public SubmittableItemKind getKind()
	{
		if (resource instanceof IFile)
		{
			return SubmittableItemKind.FILE;
		}
		else
		{
			return SubmittableItemKind.FOLDER;
		}
	}


	// ----------------------------------------------------------
    /**
     * @see ISubmittableItem#getStream()
     */
	public InputStream getStream() throws IOException
	{
		if (getKind() == SubmittableItemKind.FILE)
		{
			IFile file = (IFile) resource;
			IPath path = file.getLocation();

			return new FileInputStream(path.toFile());
		}
		else
		{
			return null;
		}
	}


    // ----------------------------------------------------------
    /**
     * Gets a human-readable string representation of this item.
     *
     * @return a human-readable string representation of the item
     */
    @Override
    public String toString()
    {
        return "<SubmittableEclipseResource: " + getFilename() + ">";
    }


	//~ Static/instance variables ............................................

    /* The submittable item that is the parent of this one. */
	private SubmittableEclipseResource parent;

	/* The resource represented by this submittable item. */
	private IResource resource;
}
