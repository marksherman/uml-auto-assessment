/*==========================================================================*\
 |  $Id: PDFReportSaver.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.webcat.core.DeliverFile;
import org.webcat.core.NSMutableDataOutputStream;

//-------------------------------------------------------------------------
/**
 * Renders and saves a generated report in PDF format.
 *
 * @author  Tony Allevato
 * @version $Id: PDFReportSaver.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class PDFReportSaver extends AbstractReportSaver
{
    //~ Constructors ..........................................................
    
    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the PDF report saver.
     * 
     * @param report the generated report to be saved
     */
    public PDFReportSaver(GeneratedReport report)
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
    
            PDFRenderOption option = new PDFRenderOption();
            option.setOutputFormat("PDF");
            option.setOutputStream(outputStream);
    
            IRenderTask task = reportEngine().createRenderTask(
                    reportDocument());
            task.setRenderOption(option);
    
            org.mozilla.javascript.Context.enter();
            task.render();
            org.mozilla.javascript.Context.exit();

            outputStream.close();
            close();

            file.setFileData(outputStream.data());
            file.setContentType("application/pdf");
            file.setStartDownload(true);
            file.setDeliveredName(generatedReport().description() + ".pdf");
        }
        catch (Exception e)
        {
            return e;
        }
        
        return null;
    }
}
