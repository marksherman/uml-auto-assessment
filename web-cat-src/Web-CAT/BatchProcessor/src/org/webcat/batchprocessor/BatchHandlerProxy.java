/*==========================================================================*\
 |  $Id: BatchHandlerProxy.java,v 1.2 2010/09/27 00:15:32 stedwar2 Exp $
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

package org.webcat.batchprocessor;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.webcat.core.WCProperties;
import com.webobjects.eocontrol.EOEnterpriseObject;

//-------------------------------------------------------------------------
/**
 * A proxy class that provides access to the methods in a subsystem's batch
 * handler, providing appropriate default behavior (usually no-ops) if the
 * handler does not implement a method.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:15:32 $
 */
public class BatchHandlerProxy
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public BatchHandlerProxy(Class<?> handlerClass,
                             Class<?> entityClass,
                             WCProperties properties,
                             File workingDir)
    {
        this.handlerClass = handlerClass;
        this.entityClass = entityClass;

        try
        {
            Constructor<?> ctor =
                BatchHandlerManager.getHandlerConstructor(handlerClass);

            handler = ctor.newInstance(properties, workingDir);
        }
        catch (Exception e)
        {
            // We should never get here, because the BatchHandlerManager
            // verifies earlier that the class implements this constructor.
        }
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public boolean shouldProcessItem(EOEnterpriseObject object)
    {
        Boolean result = (Boolean) invokeMethod(
                "shouldProcessItem", entityClass.cast(object));

        return (result != null) ? result.booleanValue() : true;
    }


    // ----------------------------------------------------------
    public void setUpItem(EOEnterpriseObject object)
    {
        invokeMethod("setUpItem", entityClass.cast(object));
    }


    // ----------------------------------------------------------
    public void tearDownItem(EOEnterpriseObject object)
    {
        invokeMethod("tearDownItem", entityClass.cast(object));
    }


    // ----------------------------------------------------------
    private Object invokeMethod(String name, Object... params)
    {
        Method method = null;

        try
        {
            method = handlerClass.getMethod(name, entityClass);
        }
        catch (Exception e)
        {
            return null;
        }

        try
        {
            return method.invoke(handler, params);
        }
        catch (Exception e)
        {
            log.warn("An exception occurred when invoking the " + name
                    + " method on batch handler for " + entityClass.getName(),
                    e);
            return null;
        }
    }


    //~ Static/instance variables .............................................

    private Class<?> handlerClass;
    private Class<?> entityClass;
    private Object handler;

    private static final Logger log = Logger.getLogger(BatchHandlerProxy.class);
}
