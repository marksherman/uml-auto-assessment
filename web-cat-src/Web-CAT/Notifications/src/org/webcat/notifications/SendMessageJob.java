/*==========================================================================*\
 |  $Id: SendMessageJob.java,v 1.3 2011/12/25 21:18:26 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010-2011 Virginia Tech
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

package org.webcat.notifications;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.webcat.core.MutableDictionary;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSTimestamp;

// -------------------------------------------------------------------------
/**
 * TODO: place a real description here.
 *
 * @author  Tony Allevato
 * @author  Last changed by: $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2011/12/25 21:18:26 $
 */
public class SendMessageJob
    extends _SendMessageJob
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new SendMessageJob object.
     */
    public SendMessageJob()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static SendMessageJob create(
        EOEditingContext ec,
        @SuppressWarnings("hiding") boolean isSevere
    )
    {
        return create(ec, new NSTimestamp(), false, false, isSevere, false);
    }


    // ----------------------------------------------------------
    public void setAttachments(List<File> attachments)
    {
        MutableDictionary realAttachments = attachments();
        realAttachments.clear();
        if (attachments != null)
        {
            for (File file : attachments)
            {
                realAttachments.takeValueForKey(
                    file.getPath(), file.getName());
            }
        }
    }


    // ----------------------------------------------------------
    public List<File> attachmentsAsList()
    {
        List<File> result = null;
        MutableDictionary realAttachments = attachments();
        if (realAttachments.size() > 0)
        {
            result = new ArrayList<File>(realAttachments.size());
            for (Object path : realAttachments.allValues())
            {
                result.add(new File(path.toString()));
            }
        }
        return result;
    }
}
