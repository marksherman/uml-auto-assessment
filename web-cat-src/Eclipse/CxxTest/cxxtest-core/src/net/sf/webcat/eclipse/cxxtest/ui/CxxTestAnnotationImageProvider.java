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

import net.sf.webcat.eclipse.cxxtest.CxxTestPlugin;
import net.sf.webcat.eclipse.cxxtest.ICxxTestConstants;
import net.sf.webcat.eclipse.cxxtest.model.ICxxTestBase;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.IAnnotationImageProvider;
import org.eclipse.ui.texteditor.MarkerAnnotation;

/**
 * A class that provides the left margin images for CxxTest markers/annotations
 * based on the status code of the assertion they represent.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestAnnotationImageProvider implements IAnnotationImageProvider
{
	private static ImageRegistry registry;
	
	public Image getManagedImage(Annotation annotation)
	{
		if(annotation instanceof MarkerAnnotation)
		{
			MarkerAnnotation ma = (MarkerAnnotation)annotation;
			IMarker marker = ma.getMarker();
			
			try
			{
				if(ICxxTestConstants.MARKER_FAILED_TEST.equals(marker.getType()))
				{
					int assertType = marker.getAttribute(
							ICxxTestConstants.ATTR_ASSERTIONTYPE,
							ICxxTestBase.STATUS_OK);
					
					return getImage(assertType);
				}
			}
			catch (CoreException e) { }
		}

		return null;
	}

	private Image getImage(int type)
	{
		if(registry == null)
			initializeRegistry();
		
		switch(type)
		{
			case ICxxTestBase.STATUS_OK:
				return registry.get("trace"); //$NON-NLS-1$

			case ICxxTestBase.STATUS_WARNING:
				return registry.get("warn"); //$NON-NLS-1$

			case ICxxTestBase.STATUS_FAILED:
				return registry.get("fail"); //$NON-NLS-1$

			case ICxxTestBase.STATUS_ERROR:
				return registry.get("error"); //$NON-NLS-1$
				
			default:
				return null;
		}
	}

	private static void initializeRegistry()
	{
		registry = new ImageRegistry(Display.getCurrent());

		registry.put("trace", CxxTestPlugin.getImageDescriptor( //$NON-NLS-1$
			"/icons/full/obj16/asserttrace.gif")); //$NON-NLS-1$
		registry.put("warn", CxxTestPlugin.getImageDescriptor( //$NON-NLS-1$
			"/icons/full/obj16/assertwarn.gif")); //$NON-NLS-1$
		registry.put("fail", CxxTestPlugin.getImageDescriptor( //$NON-NLS-1$
			"/icons/full/obj16/assertfail.gif")); //$NON-NLS-1$
		registry.put("error", CxxTestPlugin.getImageDescriptor( //$NON-NLS-1$
			"/icons/full/obj16/asserterror.gif")); //$NON-NLS-1$
	}

	public String getImageDescriptorId(Annotation annotation)
	{
		// not supported (managed images only)
		return null;
	}

	public ImageDescriptor getImageDescriptor(String imageDescritporId)
	{
		// not supported (managed images only)
		return null;
	}
}
