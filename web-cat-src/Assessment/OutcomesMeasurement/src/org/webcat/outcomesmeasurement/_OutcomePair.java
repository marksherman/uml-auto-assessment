/*==========================================================================*\
 |  _OutcomePair.java
 |*-------------------------------------------------------------------------*|
 |  Created by eogenerator
 |  DO NOT EDIT.  Make changes to OutcomePair.java instead.
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

package org.webcat.outcomesmeasurement;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.eof.ERXEOControlUtilities;
import er.extensions.eof.ERXKey;
import org.apache.log4j.Logger;

// -------------------------------------------------------------------------
/**
 * An automatically generated EOGenericRecord subclass.  DO NOT EDIT.
 * To change, use EOModeler, or make additions in
 * OutcomePair.java.
 *
 * @author Generated by eogenerator
 * @version version suppressed to control auto-generation
 */
public abstract class _OutcomePair
    extends er.extensions.eof.ERXGenericRecord
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new _OutcomePair object.
     */
    public _OutcomePair()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * A static factory method for creating a new
     * OutcomePair object given required
     * attributes and relationships.
     * @param editingContext The context in which the new object will be
     * inserted
     * @param externalOutcomeValue
     * @param programOutcomeValue
     * @return The newly created object
     */
    public static OutcomePair create(
        EOEditingContext editingContext,
        org.webcat.outcomesmeasurement.ExternalOutcome externalOutcomeValue,
        org.webcat.outcomesmeasurement.ProgramOutcome programOutcomeValue
        )
    {
        OutcomePair eoObject = (OutcomePair)
            EOUtilities.createAndInsertInstance(
                editingContext,
                _OutcomePair.ENTITY_NAME);
        eoObject.setExternalOutcomeRelationship(externalOutcomeValue);
        eoObject.setProgramOutcomeRelationship(programOutcomeValue);
        return eoObject;
    }


    // ----------------------------------------------------------
    /**
     * Get a local instance of the given object in another editing context.
     * @param editingContext The target editing context
     * @param eo The object to import
     * @return An instance of the given object in the target editing context
     */
    public static OutcomePair localInstance(
        EOEditingContext editingContext, OutcomePair eo)
    {
        return (eo == null)
            ? null
            : (OutcomePair)EOUtilities.localInstanceOfObject(
                editingContext, eo);
    }


    // ----------------------------------------------------------
    /**
     * Look up an object by id number.  Assumes the editing
     * context is appropriately locked.
     * @param ec The editing context to use
     * @param id The id to look up
     * @return The object, or null if no such id exists
     */
    public static OutcomePair forId(
        EOEditingContext ec, int id )
    {
        OutcomePair obj = null;
        if (id > 0)
        {
            NSArray<OutcomePair> results =
                objectsMatchingValues(ec, "id", new Integer(id));
            if (results != null && results.count() > 0)
            {
                obj = results.objectAtIndex(0);
            }
        }
        return obj;
    }


    // ----------------------------------------------------------
    /**
     * Look up an object by id number.  Assumes the editing
     * context is appropriately locked.
     * @param ec The editing context to use
     * @param id The id to look up
     * @return The object, or null if no such id exists
     */
    public static OutcomePair forId(
        EOEditingContext ec, String id )
    {
        return forId( ec, er.extensions.foundation.ERXValueUtilities.intValue( id ) );
    }


    //~ Constants (for key names) .............................................

    // Attributes ---
    // To-one relationships ---
    public static final String EXTERNAL_OUTCOME_KEY = "externalOutcome";
    public static final ERXKey<org.webcat.outcomesmeasurement.ExternalOutcome> externalOutcome =
        new ERXKey<org.webcat.outcomesmeasurement.ExternalOutcome>(EXTERNAL_OUTCOME_KEY);
    public static final String PROGRAM_OUTCOME_KEY = "programOutcome";
    public static final ERXKey<org.webcat.outcomesmeasurement.ProgramOutcome> programOutcome =
        new ERXKey<org.webcat.outcomesmeasurement.ProgramOutcome>(PROGRAM_OUTCOME_KEY);
    // To-many relationships ---
    public static final String COURSEWORKS_KEY = "courseworks";
    public static final ERXKey<org.webcat.outcomesmeasurement.Coursework> courseworks =
        new ERXKey<org.webcat.outcomesmeasurement.Coursework>(COURSEWORKS_KEY);
    public static final String MEASURES_KEY = "measures";
    public static final ERXKey<org.webcat.outcomesmeasurement.Measure> measures =
        new ERXKey<org.webcat.outcomesmeasurement.Measure>(MEASURES_KEY);
    // Fetch specifications ---
    public static final String ENTITY_NAME = "OutcomePair";


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Get a local instance of this object in another editing context.
     * @param editingContext The target editing context
     * @return An instance of this object in the target editing context
     */
    public OutcomePair localInstance(EOEditingContext editingContext)
    {
        return (OutcomePair)EOUtilities.localInstanceOfObject(
            editingContext, this);
    }


    // ----------------------------------------------------------
    /**
     * Get a list of changes between this object's current state and the
     * last committed version.
     * @return a dictionary of the changes that have not yet been committed
     */
    @SuppressWarnings("unchecked")
    public NSDictionary<String, Object> changedProperties()
    {
        return changesFromSnapshot(
            editingContext().committedSnapshotForObject(this) );
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this object's <code>id</code> value.
     * @return the value of the attribute
     */
    public Number id()
    {
        try
        {
            return (Number)EOUtilities.primaryKeyForObject(
                editingContext() , this ).objectForKey( "id" );
        }
        catch (Exception e)
        {
            return er.extensions.eof.ERXConstant.ZeroInteger;
        }
    }

    // ----------------------------------------------------------
    /**
     * Retrieve the entity pointed to by the <code>externalOutcome</code>
     * relationship.
     * @return the entity in the relationship
     */
    public org.webcat.outcomesmeasurement.ExternalOutcome externalOutcome()
    {
        return (org.webcat.outcomesmeasurement.ExternalOutcome)storedValueForKey( "externalOutcome" );
    }


    // ----------------------------------------------------------
    /**
     * Set the entity pointed to by the <code>externalOutcome</code>
     * relationship (DO NOT USE--instead, use
     * <code>setExternalOutcomeRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The new entity to relate to
     */
    public void setExternalOutcome( org.webcat.outcomesmeasurement.ExternalOutcome value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "setExternalOutcome("
                + value + "): was " + externalOutcome() );
        }
        takeStoredValueForKey( value, "externalOutcome" );
    }


    // ----------------------------------------------------------
    /**
     * Set the entity pointed to by the <code>externalOutcome</code>
     * relationship.  This method is a type-safe version of
     * <code>addObjectToBothSidesOfRelationshipWithKey()</code>.
     *
     * @param value The new entity to relate to
     */
    public void setExternalOutcomeRelationship(
        org.webcat.outcomesmeasurement.ExternalOutcome value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "setExternalOutcomeRelationship("
                + value + "): was " + externalOutcome() );
        }
        if ( value == null )
        {
            org.webcat.outcomesmeasurement.ExternalOutcome object = externalOutcome();
            if ( object != null )
                removeObjectFromBothSidesOfRelationshipWithKey( object, "externalOutcome" );
        }
        else
        {
            addObjectToBothSidesOfRelationshipWithKey( value, "externalOutcome" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entity pointed to by the <code>programOutcome</code>
     * relationship.
     * @return the entity in the relationship
     */
    public org.webcat.outcomesmeasurement.ProgramOutcome programOutcome()
    {
        return (org.webcat.outcomesmeasurement.ProgramOutcome)storedValueForKey( "programOutcome" );
    }


    // ----------------------------------------------------------
    /**
     * Set the entity pointed to by the <code>programOutcome</code>
     * relationship (DO NOT USE--instead, use
     * <code>setProgramOutcomeRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The new entity to relate to
     */
    public void setProgramOutcome( org.webcat.outcomesmeasurement.ProgramOutcome value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "setProgramOutcome("
                + value + "): was " + programOutcome() );
        }
        takeStoredValueForKey( value, "programOutcome" );
    }


    // ----------------------------------------------------------
    /**
     * Set the entity pointed to by the <code>programOutcome</code>
     * relationship.  This method is a type-safe version of
     * <code>addObjectToBothSidesOfRelationshipWithKey()</code>.
     *
     * @param value The new entity to relate to
     */
    public void setProgramOutcomeRelationship(
        org.webcat.outcomesmeasurement.ProgramOutcome value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "setProgramOutcomeRelationship("
                + value + "): was " + programOutcome() );
        }
        if ( value == null )
        {
            org.webcat.outcomesmeasurement.ProgramOutcome object = programOutcome();
            if ( object != null )
                removeObjectFromBothSidesOfRelationshipWithKey( object, "programOutcome" );
        }
        else
        {
            addObjectToBothSidesOfRelationshipWithKey( value, "programOutcome" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entities pointed to by the <code>courseworks</code>
     * relationship.
     * @return an NSArray of the entities in the relationship
     */
    @SuppressWarnings("unchecked")
    public NSArray<org.webcat.outcomesmeasurement.Coursework> courseworks()
    {
        return (NSArray)storedValueForKey( "courseworks" );
    }


    // ----------------------------------------------------------
    /**
     * Replace the list of entities pointed to by the
     * <code>courseworks</code> relationship.
     *
     * @param value The new set of entities to relate to
     */
    public void setCourseworks( NSMutableArray<org.webcat.outcomesmeasurement.Coursework>  value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "setCourseworks("
                + value + "): was " + courseworks() );
        }
        takeStoredValueForKey( value, "courseworks" );
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>courseworks</code>
     * relationship (DO NOT USE--instead, use
     * <code>addToCourseworksRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The new entity to relate to
     */
    public void addToCourseworks( org.webcat.outcomesmeasurement.Coursework value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "addToCourseworks("
                + value + "): was " + courseworks() );
        }
        NSMutableArray<org.webcat.outcomesmeasurement.Coursework> array =
            (NSMutableArray<org.webcat.outcomesmeasurement.Coursework>)courseworks();
        willChange();
        array.addObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>courseworks</code>
     * relationship (DO NOT USE--instead, use
     * <code>removeFromCourseworksRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromCourseworks( org.webcat.outcomesmeasurement.Coursework value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "RemoveFromCourseworks("
                + value + "): was " + courseworks() );
        }
        NSMutableArray<org.webcat.outcomesmeasurement.Coursework> array =
            (NSMutableArray<org.webcat.outcomesmeasurement.Coursework>)courseworks();
        willChange();
        array.removeObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>courseworks</code>
     * relationship.
     *
     * @param value The new entity to relate to
     */
    public void addToCourseworksRelationship( org.webcat.outcomesmeasurement.Coursework value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "addToCourseworksRelationship("
                + value + "): was " + courseworks() );
        }
        addObjectToBothSidesOfRelationshipWithKey(
            value, "courseworks" );
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>courseworks</code>
     * relationship.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromCourseworksRelationship( org.webcat.outcomesmeasurement.Coursework value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "removeFromCourseworksRelationship("
                + value + "): was " + courseworks() );
        }
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, "courseworks" );
    }


    // ----------------------------------------------------------
    /**
     * Create a brand new object that is a member of the
     * <code>courseworks</code> relationship.
     *
     * @return The new entity
     */
    public org.webcat.outcomesmeasurement.Coursework createCourseworksRelationship()
    {
        if (log.isDebugEnabled())
        {
            log.debug( "createCourseworksRelationship()" );
        }
        EOClassDescription eoClassDesc = EOClassDescription
            .classDescriptionForEntityName( "Coursework" );
        EOEnterpriseObject eoObject = eoClassDesc
            .createInstanceWithEditingContext( editingContext(), null );
        editingContext().insertObject( eoObject );
        addObjectToBothSidesOfRelationshipWithKey(
            eoObject, "courseworks" );
        return (org.webcat.outcomesmeasurement.Coursework)eoObject;
    }


    // ----------------------------------------------------------
    /**
     * Remove and then delete a specific entity that is a member of the
     * <code>courseworks</code> relationship.
     *
     * @param value The entity to remove from the relationship and then delete
     */
    public void deleteCourseworksRelationship( org.webcat.outcomesmeasurement.Coursework value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "deleteCourseworksRelationship("
                + value + "): was " + courseworks() );
        }
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, "courseworks" );
        editingContext().deleteObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Remove (and then delete, if owned) all entities that are members of the
     * <code>courseworks</code> relationship.
     */
    public void deleteAllCourseworksRelationships()
    {
        if (log.isDebugEnabled())
        {
            log.debug( "deleteAllCourseworksRelationships(): was "
                + courseworks() );
        }
        for (org.webcat.outcomesmeasurement.Coursework object : courseworks())
        {
            deleteCourseworksRelationship(object);
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entities pointed to by the <code>measures</code>
     * relationship.
     * @return an NSArray of the entities in the relationship
     */
    @SuppressWarnings("unchecked")
    public NSArray<org.webcat.outcomesmeasurement.Measure> measures()
    {
        return (NSArray)storedValueForKey( "measures" );
    }


    // ----------------------------------------------------------
    /**
     * Replace the list of entities pointed to by the
     * <code>measures</code> relationship.
     *
     * @param value The new set of entities to relate to
     */
    public void setMeasures( NSMutableArray<org.webcat.outcomesmeasurement.Measure>  value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "setMeasures("
                + value + "): was " + measures() );
        }
        takeStoredValueForKey( value, "measures" );
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>measures</code>
     * relationship (DO NOT USE--instead, use
     * <code>addToMeasuresRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The new entity to relate to
     */
    public void addToMeasures( org.webcat.outcomesmeasurement.Measure value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "addToMeasures("
                + value + "): was " + measures() );
        }
        NSMutableArray<org.webcat.outcomesmeasurement.Measure> array =
            (NSMutableArray<org.webcat.outcomesmeasurement.Measure>)measures();
        willChange();
        array.addObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>measures</code>
     * relationship (DO NOT USE--instead, use
     * <code>removeFromMeasuresRelationship()</code>.
     * This method is provided for WebObjects use.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromMeasures( org.webcat.outcomesmeasurement.Measure value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "RemoveFromMeasures("
                + value + "): was " + measures() );
        }
        NSMutableArray<org.webcat.outcomesmeasurement.Measure> array =
            (NSMutableArray<org.webcat.outcomesmeasurement.Measure>)measures();
        willChange();
        array.removeObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Add a new entity to the <code>measures</code>
     * relationship.
     *
     * @param value The new entity to relate to
     */
    public void addToMeasuresRelationship( org.webcat.outcomesmeasurement.Measure value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "addToMeasuresRelationship("
                + value + "): was " + measures() );
        }
        addObjectToBothSidesOfRelationshipWithKey(
            value, "measures" );
    }


    // ----------------------------------------------------------
    /**
     * Remove a specific entity from the <code>measures</code>
     * relationship.
     *
     * @param value The entity to remove from the relationship
     */
    public void removeFromMeasuresRelationship( org.webcat.outcomesmeasurement.Measure value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "removeFromMeasuresRelationship("
                + value + "): was " + measures() );
        }
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, "measures" );
    }


    // ----------------------------------------------------------
    /**
     * Create a brand new object that is a member of the
     * <code>measures</code> relationship.
     *
     * @return The new entity
     */
    public org.webcat.outcomesmeasurement.Measure createMeasuresRelationship()
    {
        if (log.isDebugEnabled())
        {
            log.debug( "createMeasuresRelationship()" );
        }
        EOClassDescription eoClassDesc = EOClassDescription
            .classDescriptionForEntityName( "Measure" );
        EOEnterpriseObject eoObject = eoClassDesc
            .createInstanceWithEditingContext( editingContext(), null );
        editingContext().insertObject( eoObject );
        addObjectToBothSidesOfRelationshipWithKey(
            eoObject, "measures" );
        return (org.webcat.outcomesmeasurement.Measure)eoObject;
    }


    // ----------------------------------------------------------
    /**
     * Remove and then delete a specific entity that is a member of the
     * <code>measures</code> relationship.
     *
     * @param value The entity to remove from the relationship and then delete
     */
    public void deleteMeasuresRelationship( org.webcat.outcomesmeasurement.Measure value )
    {
        if (log.isDebugEnabled())
        {
            log.debug( "deleteMeasuresRelationship("
                + value + "): was " + measures() );
        }
        removeObjectFromBothSidesOfRelationshipWithKey(
            value, "measures" );
        editingContext().deleteObject( value );
    }


    // ----------------------------------------------------------
    /**
     * Remove (and then delete, if owned) all entities that are members of the
     * <code>measures</code> relationship.
     */
    public void deleteAllMeasuresRelationships()
    {
        if (log.isDebugEnabled())
        {
            log.debug( "deleteAllMeasuresRelationships(): was "
                + measures() );
        }
        for (org.webcat.outcomesmeasurement.Measure object : measures())
        {
            deleteMeasuresRelationship(object);
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a fetch specification.
     *
     * @param context The editing context to use
     * @param fspec The fetch specification to use
     *
     * @return an NSArray of the entities retrieved
     */
    @SuppressWarnings("unchecked")
    public static NSArray<OutcomePair> objectsWithFetchSpecification(
        EOEditingContext context,
        EOFetchSpecification fspec)
    {
        return context.objectsWithFetchSpecification(fspec);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve all objects of this type.
     *
     * @param context The editing context to use
     *
     * @return an NSArray of the entities retrieved
     */
    public static NSArray<OutcomePair> allObjects(
        EOEditingContext context)
    {
        return objectsMatchingQualifier(context, null, null);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a qualifier.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     *
     * @return an NSArray of the entities retrieved
     */
    public static NSArray<OutcomePair> objectsMatchingQualifier(
        EOEditingContext context,
        EOQualifier qualifier)
    {
        return objectsMatchingQualifier(context, qualifier, null);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a qualifier and sort orderings.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     * @param sortOrderings The sort orderings to use
     *
     * @return an NSArray of the entities retrieved
     */
    public static NSArray<OutcomePair> objectsMatchingQualifier(
        EOEditingContext context,
        EOQualifier qualifier,
        NSArray<EOSortOrdering> sortOrderings)
    {
        EOFetchSpecification fspec = new EOFetchSpecification(
            ENTITY_NAME, qualifier, sortOrderings);
        fspec.setUsesDistinct(true);
        return objectsWithFetchSpecification(context, fspec);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the first object that matches a qualifier, when
     * sorted with the specified sort orderings.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     * @param sortOrderings the sort orderings
     *
     * @return the first entity that was retrieved, or null if there was none
     */
    public static OutcomePair firstObjectMatchingQualifier(
        EOEditingContext context,
        EOQualifier qualifier,
        NSArray<EOSortOrdering> sortOrderings)
    {
        NSArray<OutcomePair> results =
            objectsMatchingQualifier(context, qualifier, sortOrderings);
        return (results.size() > 0)
            ? results.get(0)
            : null;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve a single object using a list of keys and values to match.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     *
     * @return the single entity that was retrieved
     *
     * @throws EOUtilities.MoreThanOneException
     *     if there is more than one matching object
     */
    public static OutcomePair uniqueObjectMatchingQualifier(
        EOEditingContext context,
        EOQualifier qualifier) throws EOUtilities.MoreThanOneException
    {
        NSArray<OutcomePair> results =
            objectsMatchingQualifier(context, qualifier);
        if (results.size() > 1)
        {
            throw new EOUtilities.MoreThanOneException(null);
        }
        return (results.size() > 0)
            ? results.get(0)
            : null;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a list of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a list of keys and values to match, alternating
     *     "key", "value", "key", "value"...
     *
     * @return an NSArray of the entities retrieved
     */
    public static NSArray<OutcomePair> objectsMatchingValues(
        EOEditingContext context,
        Object... keysAndValues)
    {
        if (keysAndValues.length % 2 != 0)
        {
            throw new IllegalArgumentException("There should a value " +
                "corresponding to every key that was passed.");
        }

        NSMutableDictionary<String, Object> valueDictionary =
            new NSMutableDictionary<String, Object>();

        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (!(key instanceof String))
            {
                throw new IllegalArgumentException("Keys should be strings.");
            }

            valueDictionary.setObjectForKey(value, key);
        }

        return objectsMatchingValues(context, valueDictionary);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve objects using a dictionary of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a dictionary of keys and values to match
     *
     * @return an NSArray of the entities retrieved
     */
    @SuppressWarnings("unchecked")
    public static NSArray<OutcomePair> objectsMatchingValues(
        EOEditingContext context,
        NSDictionary<String, Object> keysAndValues)
    {
        return EOUtilities.objectsMatchingValues(context, ENTITY_NAME,
            keysAndValues);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the first object that matches a set of keys and values, when
     * sorted with the specified sort orderings.
     *
     * @param context The editing context to use
     * @param sortOrderings the sort orderings
     * @param keysAndValues a list of keys and values to match, alternating
     *     "key", "value", "key", "value"...
     *
     * @return the first entity that was retrieved, or null if there was none
     */
    public static OutcomePair firstObjectMatchingValues(
        EOEditingContext context,
        NSArray<EOSortOrdering> sortOrderings,
        Object... keysAndValues)
    {
        if (keysAndValues.length % 2 != 0)
        {
            throw new IllegalArgumentException("There should a value " +
                "corresponding to every key that was passed.");
        }

        NSMutableDictionary<String, Object> valueDictionary =
            new NSMutableDictionary<String, Object>();

        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (!(key instanceof String))
            {
                throw new IllegalArgumentException("Keys should be strings.");
            }

            valueDictionary.setObjectForKey(value, key);
        }

        return firstObjectMatchingValues(
            context, sortOrderings, valueDictionary);
    }


    // ----------------------------------------------------------
    /**
     * Retrieves the first object that matches a set of keys and values, when
     * sorted with the specified sort orderings.
     *
     * @param context The editing context to use
     * @param sortOrderings the sort orderings
     * @param keysAndValues a dictionary of keys and values to match
     *
     * @return the first entity that was retrieved, or null if there was none
     */
    public static OutcomePair firstObjectMatchingValues(
        EOEditingContext context,
        NSArray<EOSortOrdering> sortOrderings,
        NSDictionary<String, Object> keysAndValues)
    {
        EOFetchSpecification fspec = new EOFetchSpecification(
            ENTITY_NAME,
            EOQualifier.qualifierToMatchAllValues(keysAndValues),
            sortOrderings);
        fspec.setFetchLimit(1);

        NSArray<OutcomePair> result =
            objectsWithFetchSpecification( context, fspec );

        if ( result.count() == 0 )
        {
            return null;
        }
        else
        {
            return result.objectAtIndex(0);
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve a single object using a list of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a list of keys and values to match, alternating
     *     "key", "value", "key", "value"...
     *
     * @return the single entity that was retrieved, or null if there was none
     *
     * @throws EOUtilities.MoreThanOneException
     *     if there is more than one matching object
     */
    public static OutcomePair uniqueObjectMatchingValues(
        EOEditingContext context,
        Object... keysAndValues) throws EOUtilities.MoreThanOneException
    {
        if (keysAndValues.length % 2 != 0)
        {
            throw new IllegalArgumentException("There should a value " +
                "corresponding to every key that was passed.");
        }

        NSMutableDictionary<String, Object> valueDictionary =
            new NSMutableDictionary<String, Object>();

        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (!(key instanceof String))
            {
                throw new IllegalArgumentException("Keys should be strings.");
            }

            valueDictionary.setObjectForKey(value, key);
        }

        return uniqueObjectMatchingValues(context, valueDictionary);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve an object using a dictionary of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a dictionary of keys and values to match
     *
     * @return the single entity that was retrieved, or null if there was none
     *
     * @throws EOUtilities.MoreThanOneException
     *     if there is more than one matching object
     */
    public static OutcomePair uniqueObjectMatchingValues(
        EOEditingContext context,
        NSDictionary<String, Object> keysAndValues)
        throws EOUtilities.MoreThanOneException
    {
        try
        {
            return (OutcomePair)EOUtilities.objectMatchingValues(
                context, ENTITY_NAME, keysAndValues);
        }
        catch (EOObjectNotAvailableException e)
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the count of all objects of this type.
     *
     * @param context The editing context to use
     *
     * @return the count of all objects
     */
    public static int countOfAllObjects(EOEditingContext context)
    {
        return countOfObjectsMatchingQualifier(context, null);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the count of objects that match a qualifier.
     *
     * @param context The editing context to use
     * @param qualifier The qualifier to use
     *
     * @return the count of objects matching the qualifier
     */
    public static int countOfObjectsMatchingQualifier(
        EOEditingContext context, EOQualifier qualifier)
    {
        return ERXEOControlUtilities.objectCountWithQualifier(
                context, ENTITY_NAME, qualifier);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the count of objects using a list of keys and values to match.
     *
     * @param context The editing context to use
     * @param keysAndValues a list of keys and values to match, alternating
     *     "key", "value", "key", "value"...
     *
     * @return the count of objects that match the specified values
     */
    public static int countOfObjectsMatchingValues(
        EOEditingContext context,
        Object... keysAndValues)
    {
        if (keysAndValues.length % 2 != 0)
        {
            throw new IllegalArgumentException("There should a value " +
                "corresponding to every key that was passed.");
        }

        NSMutableDictionary<String, Object> valueDictionary =
            new NSMutableDictionary<String, Object>();

        for (int i = 0; i < keysAndValues.length; i += 2)
        {
            Object key = keysAndValues[i];
            Object value = keysAndValues[i + 1];

            if (!(key instanceof String))
            {
                throw new IllegalArgumentException("Keys should be strings.");
            }

            valueDictionary.setObjectForKey(value, key);
        }

        return countOfObjectsMatchingValues(context, valueDictionary);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the count of objects using a dictionary of keys and values to
     * match.
     *
     * @param context The editing context to use
     * @param keysAndValues a dictionary of keys and values to match
     *
     * @return the count of objects that matched the specified values
     */
    @SuppressWarnings("unchecked")
    public static int countOfObjectsMatchingValues(
        EOEditingContext context,
        NSDictionary<String, Object> keysAndValues)
    {
        return countOfObjectsMatchingQualifier(context,
                EOQualifier.qualifierToMatchAllValues(keysAndValues));
    }


    // ----------------------------------------------------------
    /**
     * Produce a string representation of this object.  This implementation
     * calls UserPresentableDescription(), which uses WebObjects' internal
     * mechanism to print out the visible fields of this object.  Normally,
     * subclasses would override userPresentableDescription() to change
     * the way the object is printed.
     *
     * @return A string representation of the object's value
     */
    public String toString()
    {
        return userPresentableDescription();
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger( OutcomePair.class );
}
