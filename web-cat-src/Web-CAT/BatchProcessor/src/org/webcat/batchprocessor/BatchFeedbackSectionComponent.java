/*==========================================================================*\
 |  $Id: BatchFeedbackSectionComponent.java,v 1.2 2010/09/27 00:15:32 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010 Virginia Tech
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

package org.webcat.batchprocessor;

import java.io.File;
import java.io.FileInputStream;
import org.apache.log4j.Logger;
import org.webcat.core.WCComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSData;

//-------------------------------------------------------------------------
/**
 * TODO real description
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:15:32 $
 */
public class BatchFeedbackSectionComponent extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public BatchFeedbackSectionComponent(WOContext context)
    {
        super(context);
    }


    //~ KVC attributes (must be public) .......................................

    public BatchFeedbackSection feedbackSection;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        String mimeType = feedbackSection.mimeType();
        File file = feedbackSection.feedbackFile();

        boolean isHTML = mimeType != null &&
            (mimeType.equalsIgnoreCase("text/html") ||
             mimeType.equalsIgnoreCase("html"));

        if (!isHTML)
        {
            response.appendContentString("<pre>");
        }

        try
        {
            FileInputStream in = new FileInputStream(file);
            NSData data = new NSData(in, 0);
            response.appendContentData(data);
            in.close();
        }
        catch ( Exception e )
        {
            log.error("Exception copying inline report fragment '"
                       + file.getPath() + "'", e);
        }

        if (!isHTML)
        {
            response.appendContentString("</pre>");
        }
    }


    //~ Static/instance variables .............................................

    private static final Logger log = Logger.getLogger(
            BatchFeedbackSectionComponent.class);
}
