/*==========================================================================*\
 |  $Id: ManagedHostDescriptor.java,v 1.2 2010/09/27 00:30:22 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2008-2009 Virginia Tech
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

package org.webcat.jobqueue;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.util.Enumeration;
import org.apache.log4j.Logger;
import org.webcat.core.IndependentEOManager;

// -------------------------------------------------------------------------
/**
 * A subclass of IndependentEOManager that holds one {@link HostDescriptor}.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:30:22 $
 */
public class ManagedHostDescriptor
    extends IndependentEOManager
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     * @param descriptor The host descriptor to wrap
     */
    public ManagedHostDescriptor(HostDescriptor descriptor)
    {
        super(descriptor);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>hostName</code> value.
     * @return the value of the attribute
     */
    public String hostName()
    {
        return (String)valueForKey(HostDescriptor.HOST_NAME_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Change the value of this object's <code>hostName</code>
     * property.
     *
     * @param value The new value for this property
     */
    public void setHostName(String value)
    {
        takeValueForKey(value, HostDescriptor.HOST_NAME_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entities pointed to by the <code>workers</code>
     * relationship.
     * @return an NSArray of the entities in the relationship
     */
    public NSArray workers()
    {
        return (NSArray)valueForKey(HostDescriptor.WORKERS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Replace the list of entities pointed to by the
     * <code>workers</code> relationship.
     *
     * @param value The new set of entities to relate to
     */
    public void setWorkers(NSMutableArray value)
    {
        takeValueForKey( value, HostDescriptor.WORKERS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>workers</code>
     * relationship (DO NOT USE--instead, use
     * <code>addToWorkersRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The new entity to relate to
     */
    public void addToWorkers(org.webcat.jobqueue.WorkerDescriptor value)
    {
        addObjectToPropertyWithKey(value, HostDescriptor.WORKERS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>workers</code>
     * relationship (DO NOT USE--instead, use
     * <code>removeFromWorkersRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromWorkers(
        org.webcat.jobqueue.WorkerDescriptor value)
    {
        removeObjectFromPropertyWithKey(value, HostDescriptor.WORKERS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>workers</code>
     * relationship.
     *
     * @param value The new entity to relate to
     */
    public void addToWorkersRelationship(
        org.webcat.jobqueue.WorkerDescriptor value)
    {
        addObjectToBothSidesOfRelationshipWithKey(
            value, HostDescriptor.WORKERS_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>workers</code>
     * relationship.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromWorkersRelationship(
        org.webcat.jobqueue.WorkerDescriptor value)
    {
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, HostDescriptor.WORKERS_KEY);
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger( HostDescriptor.class );
}
