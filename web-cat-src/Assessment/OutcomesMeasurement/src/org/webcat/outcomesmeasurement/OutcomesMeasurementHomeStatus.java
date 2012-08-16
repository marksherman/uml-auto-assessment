/*==========================================================================*\
 |  $Id: OutcomesMeasurementHomeStatus.java,v 1.1 2010/05/11 14:51:50 aallowat Exp $
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
import org.webcat.core.Course;
import org.webcat.core.CourseOffering;
import org.webcat.core.User;
import org.webcat.core.WCComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;

//-------------------------------------------------------------------------
/**
* An entry form for all of the assessment measures associated with a
* given course offering.
*
*  @author Stephen Edwards
 *  @author Last changed by $Author: aallowat $
 *  @version $Revision: 1.1 $, $Date: 2010/05/11 14:51:50 $
*/
public class OutcomesMeasurementHomeStatus
    extends BasePage
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public OutcomesMeasurementHomeStatus(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public NSArray<CourseOffering> offerings;
    public CourseOffering offering;
    public User thisUser;

    public NSArray<User> allUsers;
    public User          aUser;
    public int           index;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        if (thisUser == null)
        {
            thisUser = user();
        }
        log.debug("thisUser = " + thisUser);
        if (allUsers == null)
        {
            allUsers = User.objectsMatchingQualifier(
                localContext(),
                null,
                User.lastName.ascInsensitive()
                .then(User.firstName.ascInsensitive()));
        }

        offerings = CourseOffering.objectsMatchingQualifier(
            localContext(),
            CourseOffering.instructors.is(thisUser)
            .and(CourseOffering.semester.is(semester)),
            CourseOffering.course.dot(Course.number).asc().then(
                CourseOffering.crn.asc()));
        log.debug("offerings = " + offerings);
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public WOComponent switchUser()
    {
        return null;
    }


    // ----------------------------------------------------------
    public WOComponent reflections()
    {
        ReflectionsPage page = pageWithName(ReflectionsPage.class);
        page.nextPage = (WCComponent)context().page();
        page.reflection =
            FacultyReflection.uniqueObjectMatchingQualifier(
                localContext(),
                FacultyReflection.courseOffering.is(offering)
                .and(FacultyReflection.user.is(thisUser)));
        if (page.reflection == null)
        {
            page.reflection =
                FacultyReflection.create(localContext(), offering, thisUser);
        }
        return page;
    }


    // ----------------------------------------------------------
    public boolean reflectionsEntered()
    {
        FacultyReflection reflection =
            FacultyReflection.uniqueObjectMatchingQualifier(
                localContext(),
                FacultyReflection.courseOffering.is(offering)
                .and(FacultyReflection.user.is(thisUser)));
        return reflection != null && reflection.reflection() != null
            && reflection.reflection().length() > 0;
    }


    // ----------------------------------------------------------
    public WOComponent measures()
    {
        MeasuresPage page = pageWithName(MeasuresPage.class);
        page.nextPage = (WCComponent)context().page();
        page.offering = offering;
        page.thisUser = thisUser;
        return page;
    }


    // ----------------------------------------------------------
    public boolean measuresEntered()
    {
        NSArray<Measure> targets = Measure.objectsMatchingQualifier(
            localContext(),
            Measure.courses.is(offering.course()));
        NSArray<MeasureOfOffering> taken =
            MeasureOfOffering.objectsMatchingQualifier(localContext(),
                MeasureOfOffering.courseOffering.is(offering)
                .and(MeasureOfOffering.measure.in(targets)));
        if (taken.count() == targets.count())
        {
            NSArray<MeasureChange> changes =
                MeasureChange.objectsMatchingQualifier(localContext(),
                    MeasureChange.measureOfOffering.in(taken));
            return changes.count() >= taken.count();
        }
        else
        {
            return false;
        }
    }


    // ----------------------------------------------------------
    public boolean hasMeasures()
    {
        NSArray<Measure> targets = Measure.objectsMatchingQualifier(
            localContext(),
            Measure.courses.is(offering.course()));
        return targets.count() > 0;
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(OutcomesMeasurementHomeStatus.class);
}
