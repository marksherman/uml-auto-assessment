/*==========================================================================*\
 |  $Id: DirectActionHTMLImageHandler.java,v 1.2 2011/05/27 15:36:46 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2011 Virginia Tech
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

package org.webcat.reporter;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.HTMLImageHandler;
import org.eclipse.birt.report.engine.api.IImage;

//-------------------------------------------------------------------------
/**
 * A BIRT image handler for online HTML-rendered reports.
 *
 * @author Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/05/27 15:36:46 $
 */
public class DirectActionHTMLImageHandler
    extends HTMLImageHandler
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     * @param report The report containing the images
     * @param renderedResourceActionUrl The base URL for accessing rendered
     *     images in the final report
     */
    public DirectActionHTMLImageHandler(
        GeneratedReport report, String renderedResourceActionUrl)
    {
        this.report = report;
        this.renderedResourceActionUrl = renderedResourceActionUrl;
        currentImageIndex = 0;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    @Override
    public String onCustomImage(IImage image, Object context)
    {
        return handleImage(image, context, "custom", false);
    }


    // ----------------------------------------------------------
    @Override
    public String onDesignImage(IImage image, Object context)
    {
        return handleImage(image, context, "design", true);
    }


    // ----------------------------------------------------------
    @Override
    public String onDocImage(IImage image, Object context)
    {
        return null;
    }


    // ----------------------------------------------------------
    @Override
    public String onURLImage(IImage image, Object context)
    {
        String uri = image.getID();

        if (uri.startsWith("http:") || uri.startsWith("https:"))
        {
            return uri;
        }

        return handleImage(image, context, "uri", true);
    }


    // ----------------------------------------------------------
    @Override
    public String onFileImage(IImage image, Object context)
    {
        return handleImage(image, context, "file", true);
    }


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    /**
     * Handles an image report item and returns an image URL.
     *
     * @param image
     *            represents the image design information
     * @param context
     *            context information
     * @param prefix
     *            image prefix in URL
     * @param needMap
     *            whether image map is needed
     * @return URL for the image
     */
    protected String handleImage(
        IImage image, Object context, String prefix, boolean needMap)
    {
        String imageKey = getImageDictionaryKey(image);

        if (needMap)
        {
            if (imageDictionary.containsKey(imageKey))
            {
                return imageDictionary.objectForKey(imageKey);
            }
        }

        String imageName = prefix + "-" + Integer.toString(++currentImageIndex);

        String extension = image.getExtension();
        if (extension != null && extension.length() > 0)
        {
            imageName += extension;
        }

        String imagePath = report.renderedResourcePath(imageName);

        try
        {
            File file = new File(imagePath);
            image.writeImage(file);
        }
        catch ( IOException e )
        {
            log.error("Could not write image file to " + imagePath, e);
        }

        NSMutableDictionary<Object, Object> parameters =
            new NSMutableDictionary<Object, Object>();

        parameters.setObjectForKey(report.id(), "reportId");
        parameters.setObjectForKey(imageName, "image");

        String imageURL = appendParametersToActionUrl(parameters);

        if (needMap)
        {
            imageDictionary.setObjectForKey(imageURL, imageKey);
        }

        return imageURL;
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    private String appendParametersToActionUrl(
            NSDictionary<Object, Object> parameters)
    {
        if (parameters == null)
        {
            return renderedResourceActionUrl;
        }

        StringBuffer query = new StringBuffer(renderedResourceActionUrl);

        if (!renderedResourceActionUrl.contains("?"))
        {
            query.append('?');
        }
        else
        {
            query.append('&');
        }

        Enumeration<?> e = parameters.keyEnumerator();
        while (e.hasMoreElements())
        {
            String key = e.nextElement().toString();

            try
            {
                query.append( URLEncoder.encode(key, "UTF-8"));
            }
            catch (UnsupportedEncodingException ex)
            {
                // Ignore exception.
            }

            query.append( '=' );

            try
            {
                query.append( URLEncoder.encode(
                        parameters.objectForKey(key).toString(), "UTF-8"));
            }
            catch (UnsupportedEncodingException ex)
            {
                // Ignore exception.
            }

            if (e.hasMoreElements())
            {
                query.append( '&' );
            }
        }

        return query.toString();
    }


    // ----------------------------------------------------------
    /**
     * returns the unique identifier for the image
     *
     * @param image
     *            the image object
     * @return the image id
     */
    private String getImageDictionaryKey(IImage image)
    {
        if (image.getReportRunnable( ) != null)
        {
            return image.getReportRunnable().hashCode() + image.getID();
        }

        return image.getID();
    }


    //~ Instance/static variables .............................................

    private GeneratedReport report;
    private String renderedResourceActionUrl;
    private int currentImageIndex;

    private static NSMutableDictionary<String, String> imageDictionary =
        new NSMutableDictionary<String, String>();

    private static Logger log =
        Logger.getLogger(DirectActionHTMLImageHandler.class);
}
