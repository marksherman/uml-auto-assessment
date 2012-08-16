/*==========================================================================*\
 |  $Id: MeasuresPage.java,v 1.1 2010/05/11 14:51:50 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
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

import org.apache.log4j.Logger;
import com.webobjects.appserver.*;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;
import org.webcat.core.*;

//-------------------------------------------------------------------------
/**
 *  An entry form for all of the assessment measures associated with a
 *  given course offering.
 *
 *  @author Stephen Edwards
 *  @author Last changed by $Author: aallowat $
 *  @version $Revision: 1.1 $, $Date: 2010/05/11 14:51:50 $
*/
public class MeasuresPage
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public MeasuresPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public CourseOffering offering;
    public User thisUser;

    public NSMutableArray<MeasureOfOffering> measures;
    public MeasureOfOffering aMeasure;
    public int index;
    public OutcomePair pair;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        if (measures == null)
        {
//            log.debug("offering = " + offering);
//            NSArray<Measure> targets = Measure.allObjects(localContext());
//            if (log.isDebugEnabled())
//            {
//                for (Measure m : targets)
//                {
//                    log.debug("measure " + m.id() + ": " + m.description());
//                    String pairs = "    pairs:";
//                    for (OutcomePair pair : m.outcomePairs())
//                    {
//                        pairs += " " + pair.programOutcome().microLabel()
//                            + "-" + pair.externalOutcome().microLabel();
//                    }
//                    log.debug(pairs);
//
//                    String courses = "    courses:";
//                    for (Course c : m.courses())
//                    {
//                        courses += " " + c;
//                    }
//                    log.debug(courses);
//                }
//            }

            NSArray<Measure> targets = Measure.objectsMatchingQualifier(
                localContext(),
                Measure.courses.is(offering.course()),
                Measure.outcomePairs.dot(OutcomePair.programOutcome)
                .dot(ProgramOutcome.microLabel).ascs());
            measures = new NSMutableArray<MeasureOfOffering>(targets.count());
            NSArray<MeasureOfOffering> taken =
                MeasureOfOffering.objectsMatchingQualifier(localContext(),
                    MeasureOfOffering.courseOffering.is(offering)
                    .and(MeasureOfOffering.measure.in(targets)));

            for (Measure m : targets)
            {
                boolean needToCreate = true;
                // First, check to see if there is an existing in taken
                for (MeasureOfOffering mo : taken)
                {
                    if (mo.measure() == m)
                    {
                        log.debug("found existing instance for measure "
                            + m.keyPhrase());
                        measures.add(mo);
                        needToCreate = false;
                        break;
                    }
                }
                if (needToCreate)
                {
                    log.debug("no existing instance found for measure "
                        + m.keyPhrase());
                    MeasureOfOffering mo =
                        MeasureOfOffering.create(localContext());
                    mo.setMeasureRelationship(m);
                    mo.setCourseOfferingRelationship(offering);
                    applyLocalChanges();
                    measures.add(mo);
                    log.debug("created " + mo + " for measure "
                        + m.keyPhrase());
                }
            }

            log.debug("targets for this offering = " + targets);
        }
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public boolean applyLocalChanges()
    {
        for (MeasureOfOffering mo : measures)
        {
            NSDictionary<String, Object> changes = mo.changedProperties();
            if (changes.count() > 0)
            {
                MeasureChange changeLog = MeasureChange.create(
                    localContext(), new NSTimestamp(), false, null, thisUser);
                changeLog.changes().addEntriesFromDictionary(changes);
                changeLog.setMeasureOfOfferingRelationship(mo);
                if (log.isDebugEnabled())
                {
                    log.debug("Recording changes to measure "
                        + mo.measure().keyPhrase()
                        + " by "
                        + thisUser
                        + ": "
                        + changes);
                }
            }
        }
        return super.applyLocalChanges();
    }


    // ----------------------------------------------------------
    public String excellentCriteria()
    {
        String value = aMeasure.excellentCriteria();
        return value == null
            ? EXCELLENT_CRITERIA
            : value;
    }


    // ----------------------------------------------------------
    public void setExcellentCriteria(String value)
    {
        if (value != null) { value = value.trim(); }
        if (EXCELLENT_CRITERIA.equals(value))
        {
            value = null;
        }
        aMeasure.setExcellentCriteria(value);
    }


    // ----------------------------------------------------------
    public String adequateCriteria()
    {
        String value = aMeasure.adequateCriteria();
        return value == null
            ? ADEQUATE_CRITERIA
            : value;
    }


    // ----------------------------------------------------------
    public void setAdequateCriteria(String value)
    {
        if (value != null) { value = value.trim(); }
        if (ADEQUATE_CRITERIA.equals(value))
        {
            value = null;
        }
        aMeasure.setAdequateCriteria(value);
    }


    // ----------------------------------------------------------
    public String unsatisfactoryCriteria()
    {
        String value = aMeasure.unsatisfactoryCriteria();
        return value == null
            ? UNSATISFACTORY_CRITERIA
            : value;
    }


    // ----------------------------------------------------------
    public void setUnsatisfactoryCriteria(String value)
    {
        if (value != null) { value = value.trim(); }
        if (UNSATISFACTORY_CRITERIA.equals(value))
        {
            value = null;
        }
        aMeasure.setUnsatisfactoryCriteria(value);
    }


    // ----------------------------------------------------------
    public String pageTitle()
    {
        return "Outcomes Measures for " + offering.compactName();
    }


    // ----------------------------------------------------------
    public String measureTitle()
    {
        return (index + 1) + ": " + aMeasure.measure().keyPhrase();
    }


    //~ Instance/static variables .............................................

    private static final String EXCELLENT_CRITERIA = ">= 90%";
    private static final String ADEQUATE_CRITERIA = ">= 60%";
    private static final String UNSATISFACTORY_CRITERIA = "< 60%";

    static Logger log = Logger.getLogger(MeasuresPage.class);
}
