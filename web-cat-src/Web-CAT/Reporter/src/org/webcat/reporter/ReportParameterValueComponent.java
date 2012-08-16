/*==========================================================================*\
 |  $Id: ReportParameterValueComponent.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

package org.webcat.reporter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.webcat.core.WCComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation.NSTimestamp;

//------------------------------------------------------------------------
/**
 * A component that allows for the editing of a report parameter.
 *  
 * @author Tony Allevato
 * @version $Id: ReportParameterValueComponent.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ReportParameterValueComponent extends WCComponent
{
    //~ Constructors ..........................................................
    
    // ----------------------------------------------------------
    public ReportParameterValueComponent(WOContext context)
    {
        super(context);
    }
    

    //~ KVC attributes (must be public) .......................................
    
    public NSDictionary<String, Object> parameter;
    public Object value;

    public NSNumberFormatter integerFormatter = new NSNumberFormatter("0");
    public NSNumberFormatter floatFormatter = new NSNumberFormatter("0.0");

    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public Object value()
    {
        if (value == null)
        {
            return coercedDefaultValue();
        }
        else
        {
            return value;
        }
    }
    
    
    // ----------------------------------------------------------
    public boolean isParameterBoolean()
    {
        return IReportParameterConstants.TYPE_BOOLEAN.equals(parameter.
                objectForKey(IReportParameterConstants.DATA_TYPE_KEY));
    }

    
    // ----------------------------------------------------------
    public boolean isParameterDecimal()
    {
        return IReportParameterConstants.TYPE_DECIMAL.equals(parameter.
                objectForKey(IReportParameterConstants.DATA_TYPE_KEY));
    }

    
    // ----------------------------------------------------------
    public boolean isParameterDate()
    {
        return IReportParameterConstants.TYPE_DATE.equals(parameter.
                objectForKey(IReportParameterConstants.DATA_TYPE_KEY));
    }

    
    // ----------------------------------------------------------
    public boolean isParameterDatetime()
    {
        return IReportParameterConstants.TYPE_DATETIME.equals(parameter.
                objectForKey(IReportParameterConstants.DATA_TYPE_KEY));
    }

    
    // ----------------------------------------------------------
    public boolean isParameterFloat()
    {
        return IReportParameterConstants.TYPE_FLOAT.equals(parameter.
                objectForKey(IReportParameterConstants.DATA_TYPE_KEY));
    }

    
    // ----------------------------------------------------------
    public boolean isParameterInteger()
    {
        return IReportParameterConstants.TYPE_INTEGER.equals(parameter.
                objectForKey(IReportParameterConstants.DATA_TYPE_KEY));
    }

    
    // ----------------------------------------------------------
    public boolean isParameterString()
    {
        return IReportParameterConstants.TYPE_STRING.equals(parameter.
                objectForKey(IReportParameterConstants.DATA_TYPE_KEY));
    }


    // ----------------------------------------------------------
    public boolean isParameterTime()
    {
        return IReportParameterConstants.TYPE_TIME.equals(parameter.
                objectForKey(IReportParameterConstants.DATA_TYPE_KEY));
    }
    
    
    // ----------------------------------------------------------
    private Object coercedDefaultValue()
    {
        String defaultValue = (String) parameter.objectForKey(
                IReportParameterConstants.DEFAULT_VALUE_KEY);

        if (isParameterBoolean())
        {
            return Boolean.valueOf(defaultValue);
        }
        else if (isParameterDate())
        {
            try
            {
                return new NSTimestamp(DATE_FORMAT.parse(defaultValue));
            }
            catch (ParseException e)
            {
                return null;
            }
        }
        else if (isParameterDatetime())
        {
            try
            {
                return new NSTimestamp(DATETIME_FORMAT.parse(defaultValue));
            }
            catch (ParseException e)
            {
                return null;
            }
        }
        else if (isParameterDecimal())
        {
            return new BigDecimal(defaultValue);
        }
        else if (isParameterFloat())
        {
            return Double.valueOf(defaultValue);
        }
        else if (isParameterInteger())
        {
            return Float.valueOf(defaultValue);
        }
        else if (isParameterString())
        {
            return defaultValue;
        }
        else if (isParameterTime())
        {
            try
            {
                return new NSTimestamp(TIME_FORMAT.parse(defaultValue));
            }
            catch (ParseException e)
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    

    //~ Static/instance variables .............................................
    
    private static final DateFormat DATETIME_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final DateFormat DATE_FORMAT =
        new SimpleDateFormat("yyyy-MM-dd");
    
    private static final DateFormat TIME_FORMAT =
        new SimpleDateFormat("HH:mm:ss");
}
