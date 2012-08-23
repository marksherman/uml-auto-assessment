/*==========================================================================*\
 |  $Id: BatchHandlerManager.java,v 1.3 2010/10/15 00:39:16 stedwar2 Exp $
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
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.apache.log4j.Logger;
import org.webcat.core.Application;
import org.webcat.core.Subsystem;
import org.webcat.core.SubsystemManager;
import org.webcat.core.WCProperties;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;

//-------------------------------------------------------------------------
/**
 * <p>
 * A singleton that manages the batch handlers registered by each subsystem.
 * In order that subsystems do not need to depend on the BatchProcessor
 * subsystem directly, there is not a formal interface that a batch handler
 * must implement. Instead, the class registered as a batch handler for a
 * specific entity must implement a constructor with the following signature:
 * </p>
 * <dl>
 * <dt>SomeBatchHandler(WCProperties properies, File workingDir)</dt>
 * <dd><code>properties</code> represents the batch properties file.
 * <code>workingDir</code> is the working directory of the batch job, where
 * files can be copied/stored if necessary.</dd>
 * <p>
 * A new instance of the handler class is instantiated for each batch job, so
 * it may safely retain state if it wishes. The class may implement the
 * following methods (all are optional). The type <code>T</code> in the
 * parameter lists is the strongly-typed object of the appropriate entity type;
 * for example, if the handler is for Submission objects, then <code>T</code>
 * would be the class <code>Submission</code>.
 * </p>
 * <dl>
 * <dt>boolean shouldProcessItem(T item)</dt>
 * <dd>Gives a batch handler an opportunity to skip an item if it is malformed
 * in some way.</dd>
 * <dt>void setUpItem(T item)</dt>
 * <dd>Performs any set-up actions required to process this item in the batch,
 * such as copying files to the working area or writing properties into the
 * properties file.</dd>
 * <dt>void tearDownItem(T item)</dt>
 * <dd>Performs any necessary cleanup from the setUpItem method.</dd>
 * </dl>
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2010/10/15 00:39:16 $
 */
public class BatchHandlerManager
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    private BatchHandlerManager()
    {
        handlers = new NSMutableDictionary<String, Class<?>>();

        SubsystemManager subsystemManager =
            Application.wcApplication().subsystemManager();
        for (Subsystem subsystem : subsystemManager.subsystems())
        {
            try
            {
                loadHandlersFromSubsystem(subsystem);
            }
            catch (IOException e)
            {
                log.warn("Error loading batch handlers from subsystem "
                        + subsystem.name(), e);
            }
        }
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the single instance of the BatchInjectionManager class.
     *
     * @return the instance
     */
    public static BatchHandlerManager getInstance()
    {
        if (instance == null)
        {
            instance = new BatchHandlerManager();
        }

        return instance;
    }


    // ----------------------------------------------------------
    public BatchHandlerProxy createHandler(String entityName,
                                           EOEditingContext ec,
                                           WCProperties properties,
                                           File workingDir)
    {
        Class<?> handlerClass = handlers.objectForKey(entityName);

        EOEntity entity = EOUtilities.entityNamed(ec, entityName);
        Class<?> entityClass = null;

        try
        {
            entityClass = Class.forName(entity.className());
        }
        catch (ClassNotFoundException e)
        {
            log.error("Could not find class for entity named "
                    + entityName + "; this should not happen.");

            return null;
        }

        return new BatchHandlerProxy(handlerClass, entityClass,
                properties, workingDir);
    }


    // ----------------------------------------------------------
    private void loadHandlersFromSubsystem(Subsystem subsystem)
    throws IOException
    {
        File file = new File(subsystem.myResourcesDir(),
                BATCH_HANDLERS_PLIST_FILENAME);

        if (!file.exists())
        {
            return;
        }

        @SuppressWarnings("unchecked")
        NSDictionary<String, Object> plist = (NSDictionary<String, Object>)
            NSPropertyListSerialization.propertyListFromData(
                    new NSData(new FileInputStream(file), 0), "UTF-8");

        for (String entityName : plist.allKeys())
        {
            String handlerClassName = (String) plist.objectForKey(entityName);
            Class<?> handlerClass = null;

            try
            {
                handlerClass = Class.forName(handlerClassName);
            }
            catch (ClassNotFoundException e)
            {
                log.error("Could not find the class " + handlerClassName);
            }

            if (handlerClass != null)
            {
                try
                {
                    getHandlerConstructor(handlerClass);
                    handlers.setObjectForKey(handlerClass, entityName);
                }
                catch (Exception e)
                {
                    log.error("The class " + handlerClassName
                            + " does not implement a constructor with the "
                            + "signature (WCProperties, WCFile), or it is not "
                            + "accessible.");
                }
            }
        }
    }


    // ----------------------------------------------------------
    /*package*/ static Constructor<?> getHandlerConstructor(
        Class<?> handlerClass
    )
        throws SecurityException, NoSuchMethodException
    {
        return handlerClass.getConstructor(WCProperties.class, File.class);
    }


    //~ Static/instance variables .............................................

    private static BatchHandlerManager instance;

    private NSMutableDictionary<String, Class<?>> handlers;

    private static final String BATCH_HANDLERS_PLIST_FILENAME =
        "BatchHandlers.plist";

    private static final Logger log = Logger.getLogger(
            BatchHandlerManager.class);
}
