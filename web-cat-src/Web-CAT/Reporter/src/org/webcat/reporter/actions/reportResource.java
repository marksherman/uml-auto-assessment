/*==========================================================================*\
 |  $Id: reportResource.java,v 1.2 2011/12/25 21:18:25 stedwar2 Exp $
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

package org.webcat.reporter.actions;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSData;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.webcat.core.DirectAction;
import org.webcat.reporter.GeneratedReport;
import org.webcat.woextensions.ECAction;
import static org.webcat.woextensions.ECAction.run;

//-------------------------------------------------------------------------
/**
 * Return resources (like images) to which generated HTML reports refer. Image
 * references in a rendered report use this direct action as their source URL
 * since this rendered content is not actually stored in a web-accessible
 * location.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/12/25 21:18:25 $
 */
public class reportResource
    extends DirectAction
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     * @param request The incoming request
     */
    public reportResource(WORequest request)
    {
        super(request);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    public WOActionResults imageAction()
    {
        final WOResponse response = new WOResponse();

        final int reportId = Integer.parseInt(
            request().stringFormValueForKey("reportId"));
        final String image = request().stringFormValueForKey("image");

        run(new ECAction() { public void action() {
            GeneratedReport report = GeneratedReport.forId(ec, reportId);

            File file = new File(report.renderedResourcePath(image));

            try
            {
                NSData data = new NSData(new FileInputStream(file),
                    (int)file.length());

                response.appendContentData(data);
            }
            catch (IOException e)
            {
                log.error(e);
            }
        }});

        return response;
    }


    // ----------------------------------------------------------
    public WOActionResults csvAction()
    {
        final WOResponse response = new WOResponse();

        final int reportId = Integer.parseInt(
            request().stringFormValueForKey("reportId"));
        final String name = request().stringFormValueForKey("name");

        final String filename = name + ".csv";

        response.setHeader("text/csv", "Content-Type");
        response.setHeader("attachment; filename=\"" + filename + "\"",
            "Content-Disposition");

        run(new ECAction() { public void action() {
            GeneratedReport report = GeneratedReport.forId(ec, reportId);

            File file = new File(report.renderedResourcePath(filename));

            try
            {
                NSData data = new NSData(new FileInputStream(file),
                    (int)file.length());

                response.appendContentData(data);
            }
            catch (IOException e)
            {
                log.error(e);
            }
        }});

        return response;
    }


    // ----------------------------------------------------------
    public WOActionResults genericAction()
    {
        final WOResponse response = new WOResponse();

        final int reportId = Integer.parseInt(
            request().stringFormValueForKey("reportId"));
        String name = request().stringFormValueForKey("name");
        String type = request().stringFormValueForKey("contentType");
        String deliveredName =
            request().stringFormValueForKey("deliveredName");
        boolean inline = Boolean.parseBoolean(
            request().stringFormValueForKey("inline"));

        final String filename = name;

        if(deliveredName == null)
        {
            deliveredName = filename;
        }

        response.setHeader(type, "Content-Type");

        if(!inline)
        {
            response.setHeader(
                "attachment; filename=\"" + deliveredName + "\"",
                "Content-Disposition");
        }

        run(new ECAction() { public void action() {
            GeneratedReport report = GeneratedReport.forId(ec, reportId);

            File file = new File(report.renderedResourcePath(filename));

            try
            {
                NSData data = new NSData(new FileInputStream(file),
                    (int)file.length());

                response.appendContentData(data);
            }
            catch (IOException e)
            {
                log.error(e);
            }
        }});

        return response;
    }


    //~ Instance/static variables .............................................

    private static Logger log = Logger.getLogger(reportResource.class);
}
