/*==========================================================================*\
 |  $Id: ReportErrorsBlock.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

import org.eclipse.birt.core.exception.BirtException;
import org.webcat.core.MutableArray;
import org.webcat.core.MutableDictionary;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

//------------------------------------------------------------------------
/**
 * A page block that displays errors that may have occurred during the
 * generation of a report. 
 *
 * @author Tony Allevato
 * @version $Id: ReportErrorsBlock.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ReportErrorsBlock extends WOComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public ReportErrorsBlock(WOContext context)
    {
        super(context);
    }

    
    //~ KVC attributes (must be public) .......................................

    public MutableArray errors;
    public MutableDictionary error;
    public MutableDictionary chainEntry;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public String errorSeverity()
    {
        int severity = (Integer)error.objectForKey("severity");

        switch (severity)
        {
            case BirtException.OK:
                return "OK";

            case BirtException.INFO:
                return "INFO";

            case BirtException.WARNING:
                return "WARNING";

            case BirtException.ERROR:
                return "ERROR";

            case BirtException.CANCEL:
                return "CANCEL";
        }

        return "ERROR";
    }


    // ----------------------------------------------------------
    public String errorCssClass()
    {
        int severity = (Integer)error.objectForKey("severity");

        switch (severity)
        {
            case BirtException.OK:
            case BirtException.INFO:
            case BirtException.CANCEL:
                return "infoBox";

            case BirtException.WARNING:
                return "warningBox";

            case BirtException.ERROR:
                return "errorBox";
        }
        return "errorBox";
    }


    // ----------------------------------------------------------
    public boolean isChainEntryFirst()
    {
        return (((MutableArray)error.objectForKey("chain")).objectAtIndex(0)
                == chainEntry);
    }
}
