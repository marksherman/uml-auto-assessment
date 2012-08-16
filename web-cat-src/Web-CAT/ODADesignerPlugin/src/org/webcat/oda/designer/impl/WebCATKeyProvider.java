/*==========================================================================*\
 |  $Id: WebCATKeyProvider.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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

package org.webcat.oda.designer.impl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.contentassist.ContentAssistAttributeInfo;
import org.webcat.oda.designer.contentassist.ContentAssistManager;
import org.webcat.oda.designer.util.ImageUtils;
import org.webcat.oda.designer.widgets.IKeyLabelProvider;
import org.webcat.oda.designer.widgets.IKeyProvider;

//------------------------------------------------------------------------
/**
 * A content and label provider for the KeyBrowser widget that provides keys and
 * images based on data retrieved from the Web-CAT server through the content
 * assist manager.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: WebCATKeyProvider.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class WebCATKeyProvider implements IKeyProvider, IKeyLabelProvider
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public WebCATKeyProvider()
    {
        contentAssist = DesignerActivator.getDefault()
                .getContentAssistManager();

        propertyImage = ImageUtils.getImage("icons/keypath/property.gif"); //$NON-NLS-1$
        methodImage = ImageUtils.getImage("icons/keypath/method.gif"); //$NON-NLS-1$
        deprecatedPropertyImage = ImageUtils.getImage("icons/keypath/property-deprecated.gif"); //$NON-NLS-1$
        deprecatedMethodImage = ImageUtils.getImage("icons/keypath/method-deprecated.gif"); //$NON-NLS-1$
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void dispose()
    {
        propertyImage.dispose();
        methodImage.dispose();
        deprecatedPropertyImage.dispose();
        deprecatedMethodImage.dispose();
    }


    // ----------------------------------------------------------
    public String getKeyType(String className, String key)
    {
        return contentAssist.getAttributeType(className, key);
    }


    // ----------------------------------------------------------
    public boolean hasKeys(String className)
    {
        return contentAssist.isEntity(className);
    }


    // ----------------------------------------------------------
    public String[] getKeys(String className)
    {
        return contentAssist.getAttributeNames(className);
    }


    // ----------------------------------------------------------
    public Image getImage(String className, String key)
    {
        ContentAssistAttributeInfo attrInfo =
            contentAssist.getAttributeInfo(className, key);

        Object depObj = attrInfo.valueForProperty("deprecated");
        boolean deprecated =
            (depObj instanceof Boolean) ? (Boolean) depObj : false;

        boolean resultIsEntity = contentAssist.isEntity(
                getKeyType(className, key));
        
        if (resultIsEntity)
        {
            if (deprecated)
            {
                return deprecatedMethodImage;
            }
            else
            {
                return methodImage;
            }
        }
        else
        {
            if (deprecated)
            {
                return deprecatedPropertyImage;
            }
            else
            {
                return propertyImage;
            }
        }
    }


    // ----------------------------------------------------------
    public String getLabel(String className, String key)
    {
        String destType = getKeyType(className, key);
        return key + " (" + destType + ")"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    
    // ----------------------------------------------------------
    public Color getForegroundColor(String className, String key)
    {
        ContentAssistAttributeInfo attrInfo =
            contentAssist.getAttributeInfo(className, key);

        Object depObj = attrInfo.valueForProperty("deprecated");
        boolean deprecated =
            (depObj instanceof Boolean) ? (Boolean) depObj : false;

        if (deprecated)
        {
            return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
        }
        else
        {
            return null;
        }
    }

    
    //~ Static/instance variables .............................................

    private ContentAssistManager contentAssist;
    private Image propertyImage;
    private Image methodImage;
    private Image deprecatedPropertyImage;
    private Image deprecatedMethodImage;
}
