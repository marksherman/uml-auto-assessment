/*==========================================================================*\
 |  $Id: BrowserUtils.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

// ------------------------------------------------------------------------
/**
 * A small class to ease opening an external browser.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: BrowserUtils.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class BrowserUtils
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Prevent instantiation.
     */
    private BrowserUtils()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Opens an external browser with the specified URL.
     *
     * @param url
     *            the URL to open in the browser
     */
    public static void openURL(String url)
    {
        try
        {
            PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser()
                    .openURL(new URL(url));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (PartInitException e)
        {
            e.printStackTrace();
        }
    }
}
