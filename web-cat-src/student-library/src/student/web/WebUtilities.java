/*==========================================================================*\
 |  $Id: WebUtilities.java,v 1.4 2011/02/18 20:38:49 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student.web;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import student.web.internal.ApplicationSupportStrategy;
import student.web.internal.LocalityService;

//-------------------------------------------------------------------------
/**
 *  This class provides static utility methods that streamline some
 *  web-related operations, and also that provide useful services for
 *  CloudSpace-based programs.
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.4 $, $Date: 2011/02/18 20:38:49 $
 */
public class WebUtilities
{
    //~ Instance/static variables .............................................

    private static ApplicationSupportStrategy support =
        LocalityService.getSupportStrategy();


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WebUtilities object.
     */
    private WebUtilities()
    {
        // Nothing to do
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Encodes a string for use in a URL.
     * This operation wraps the behavior of
     * {@link URLEncoder#encode(String,String)}, using a UTF-8 encoding and
     * turning any exceptions into RuntimeExceptions.
     *
     * @param content The string to encode
     * @return The URL-encoded version of the parameter
     */
    public static String urlEncode(String content)
    {
        String result = content;
        try
        {
            result = URLEncoder.encode(content, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Decodes a string extracted from a URL parameter.
     * This operation wraps the behavior of
     * {@link URLDecoder#decode(String,String)}, using a UTF-8 encoding and
     * turning any exceptions into RuntimeExceptions.
     *
     * @param content The string to decode
     * @return The URL-decoded version of the parameter
     */
    public static String urlDecode(String content)
    {
        String result = content;
        try
        {
            result = URLDecoder.decode(content, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Creates a URL object from a string, without throwing declared
     * exceptions.  This wrapper simplifies creation of URL objects in
     * student code by eliminating the need for explicit try/catch blocks
     * if you just want to create a new URL object.
     *
     * @param url The url as a string
     * @return The new URL object for the given address
     */
    public static URL urlFor(String url)
    {
        URL result = null;
        try
        {
            result = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Cause a CloudSpace web application to show a different web page in
     * the user's web browser.
     *
     * @param url  The URL of the new web page to show in the user's browser.
     */
    public static void showWebPage(String url)
    {
        support.showWebPage(url);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the current ZHTML file being shown by a CloudSpace
     * web application, such as "index.zhtml" or "lab02.zhtml".
     *
     * @return The name of the current ZHTML file, without any directory
     *         component, or "" if there is none.
     */
    public static String getCurrentPageName()
    {
        return support.getCurrentPageName();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the relative path name of the current ZHTML file being shown
     * by a CloudSpace web application, such as "/Fall09/mypid/index.zhtml"
     * or "/Fall09/mypid/lab02/lab02.zhtml".
     *
     * @return The name path to the current ZHTML file, or "" if there is none.
     */
    public static String getCurrentPagePath()
    {
        return support.getCurrentPagePath();
    }


    // ----------------------------------------------------------
    /**
     * Get a parameter passed to this web page in the query part of the URL.
     * This method only works in a running CloudSpace web application.
     *
     * @param name The name of the parameter to retrieve.
     * @return The parameter's value on the current page, or null if there is
     *         none.
     */
    public static String getPageParameter(String name)
    {
        return support.getPageParameter(name);
    }
}
