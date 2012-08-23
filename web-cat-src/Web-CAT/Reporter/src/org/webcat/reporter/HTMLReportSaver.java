/*==========================================================================*\
 |  $Id: HTMLReportSaver.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
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

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.HTMLImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IImage;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.webcat.core.DeliverFile;
import org.webcat.core.NSMutableDataOutputStream;
import com.webobjects.foundation.NSMutableDictionary;

//-------------------------------------------------------------------------
/**
 * Renders and saves a generated report in a zipped archive that contains the
 * HTML content and its images.
 *
 * @author  Tony Allevato
 * @version $Id: HTMLReportSaver.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class HTMLReportSaver extends AbstractReportSaver
{
    //~ Constructors ..........................................................
    
    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the HTML report saver.
     * 
     * @param report the generated report to be saved
     */
    public HTMLReportSaver(GeneratedReport report)
    {
        super(report);
    }

    
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Saves the rendered report to the specified DeliverFile component, which
     * the caller will return back to the user.
     * 
     * @param file the DeliverFile component to save the file into
     * @return a Throwable if an error occurred (which the caller should
     *     display to the user), or null if the operation was successful
     */
    public Throwable deliverTo(DeliverFile file)
    {
        try
        {
            NSMutableDataOutputStream outputStream =
                new NSMutableDataOutputStream();
            ZipOutputStream zipStream = new ZipOutputStream(outputStream);

            HTMLRenderOption option = new HTMLRenderOption();
            option.setEmbeddable(false);
            option.setImageHandler(new ImageHandler());
    
            IRenderTask task = reportEngine().createRenderTask(
                    reportDocument());
            task.setRenderOption(option);

            // First, the render operation will generate the HTML content, and
            // cache the images via the image handler. Write the HTML file out
            // first.
            
            zipStream.putNextEntry(new ZipEntry(
                    generatedReport().description() + ".html"));
            
            option.setOutputStream(zipStream);

            org.mozilla.javascript.Context.enter();
            task.render();
            org.mozilla.javascript.Context.exit();

            zipStream.closeEntry();

            // Now loop through all of the images that were generated and write
            // those to the zip file.
            
            for (String imageName : imageData.allKeys())
            {
                byte[] data = imageData.objectForKey(imageName);
                
                zipStream.putNextEntry(new ZipEntry(imageName));
                zipStream.write(data);
                zipStream.closeEntry();
            }

            zipStream.finish();
            outputStream.close();
            close();

            file.setFileData(outputStream.data());
            file.setContentType("application/zip");
            file.setStartDownload(true);
            file.setDeliveredName(generatedReport().description() + ".zip");
        }
        catch (Exception e)
        {
            return e;
        }
        
        return null;
    }
    

    //~ Private classes .......................................................

    public class ImageHandler extends HTMLImageHandler
    {
        //~ Constructor .......................................................
    
        // ----------------------------------------------------------
        /**
         * Creates a new object.
         * @param report The report containing the images
         * @param renderedResourceActionUrl The base URL for accessing rendered
         *     images in the final report
         */
        public ImageHandler()
        {
            currentImageIndex = 0;
        }
    
    
        //~ Methods ...........................................................
    
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
    
            String imageName = prefix + "-" +
                Integer.toString(++currentImageIndex);
    
            String extension = image.getExtension();
            if (extension != null && extension.length() > 0)
            {
                imageName += extension;
            }
    
            byte[] bytes = image.getImageData();
            imageData.setObjectForKey(bytes, imageName);
    
            if (needMap)
            {
                imageDictionary.setObjectForKey(imageName, imageKey);
            }
    
            return imageName;
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
        
    
        //~ Static/instance variables .........................................
    
        private int currentImageIndex;
    }
    

    //~ Static/instance variables .............................................

    private NSMutableDictionary<String, byte[]> imageData =
        new NSMutableDictionary<String, byte[]>();

    private NSMutableDictionary<String, String> imageDictionary =
        new NSMutableDictionary<String, String>();

    private static final Logger log = Logger.getLogger(HTMLReportSaver.class);
}
