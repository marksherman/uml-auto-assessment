/*==========================================================================*\
 |  $Id: MeasurementSetupPage.java,v 1.1 2010/05/11 14:51:50 aallowat Exp $
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

import java.io.InputStreamReader;
import org.apache.log4j.Logger;
import org.webcat.core.AuthenticationDomain;
import org.webcat.core.Course;
import org.webcat.core.CourseOffering;
import org.webcat.core.User;
import com.Ostermiller.util.CSVParser;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import er.extensions.foundation.ERXValueUtilities;

//-------------------------------------------------------------------------
/**
 *  Controls for setting up assessment/measurement entities.
 *
 *  @author Stephen Edwards
 *  @author Last changed by $Author: aallowat $
 *  @version $Revision: 1.1 $, $Date: 2010/05/11 14:51:50 $
*/
public class MeasurementSetupPage
    extends BasePage
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public MeasurementSetupPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public String   filePath;
    public NSData   data;
    public int      deptOutcomeCount;
    public int      abetOutcomeCount;
    public int      deptCourseCount;
    public int      offeringCount;
    public int      userCount;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        deptOutcomeCount = ProgramOutcome.objectsMatchingQualifier(
            localContext(),
            ProgramOutcome.program.dot(Program.department).is(dept))
            .count();
        abetOutcomeCount = abet.outcomes().count();
        deptCourseCount = dept.courses().count();
        offeringCount = CourseOffering.offeringsForSemester(
            localContext(), semester).count();
        userCount = User.allObjects(localContext()).count();
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public WOComponent abetUpload()
    {
        clearAllMessages();
//        OutcomeSet set = null;
//        // Search for the outcome set matching the current semester
//        for (OutcomeSet aSet : abet.outcomeSets())
//        {
//            if (aSet.semester() == semester)
//            {
//                set = aSet;
//                break;
//            }
//        }
//
//        // Create a new outcome set, if necessary
//        if (set == null)
//        {
//            set = OutcomeSet.create(localContext(), semester);
//            abet.addToOutcomeSetsRelationship(set);
//            applyLocalChanges();
//        }

        try
        {
            InputStreamReader stream =
                new InputStreamReader(data.stream(), "UTF8");
            CSVParser in = new CSVParser(stream);
            in.changeDelimiter(',');

            String[] line = in.getLine();
            while (line != null)
            {
                String microLabel = trim(line[0]);
                String label = trim(line[1]);
                String description = trim(line[2]);

                NSArray<ExternalOutcome> candidates =
                    ExternalOutcome.objectsMatchingQualifier(localContext(),
                        ExternalOutcome.label.is(label).and(
                            ExternalOutcome.outcomeSets.dot(
                                OutcomeSet.accreditingBodies).is(abet)));

                if (candidates.size() == 0)
                {
                    ExternalOutcome outcome =
                        ExternalOutcome.create(localContext(), abet);
                    if (notEmpty(microLabel))
                    {
                        outcome.setMicroLabel(microLabel);
                    }
                    if (notEmpty(label))
                    {
                        outcome.setLabel(label);
                    }
                    if (notEmpty(description))
                    {
                        outcome.setDescription(description);
                    }
//                    outcome.addToOutcomeSetsRelationship(set);
                }

                applyLocalChanges();
                line = in.getLine();
            }

            applyLocalChanges();
            if (!hasMessages())
            {
                confirmationMessage("outcomes processed successfully.");
            }
        }
        catch (Exception e)
        {
            log.error("Problem!", e);
            String msg = e.getMessage();
            error((msg == null) ? e.getClass().getSimpleName() : msg);
        }
        return null;
    }


    // ----------------------------------------------------------
    public WOComponent deptUpload()
    {
        clearAllMessages();
//        OutcomeSet set = null;
//        // Search for the outcome set matching the current semester
//        for (OutcomeSet aSet : program.outcomeSets())
//        {
//            if (aSet.semester() == semester)
//            {
//                set = aSet;
//                break;
//            }
//        }
//
//        // Create a new outcome set, if necessary
//        if (set == null)
//        {
//            set = OutcomeSet.create(localContext(), semester);
//            program.addToOutcomeSetsRelationship(set);
//            applyLocalChanges();
//        }

        try
        {
            InputStreamReader stream =
                new InputStreamReader(data.stream(), "UTF8");
            CSVParser in = new CSVParser(stream);
            in.changeDelimiter(',');

            String[] line = in.getLine();
            while (line != null)
            {
                String microLabel = trim(line[0]);
                String label = trim(line[1]);
                String description = trim(line[2]);

                NSArray<ProgramOutcome> candidates =
                    ProgramOutcome.objectsMatchingQualifier(localContext(),
                        ProgramOutcome.label.is(label).and(
                            ProgramOutcome.outcomeSets.dot(OutcomeSet.programs)
                            .is(program)));

                if (candidates.size() == 0)
                {
                    ProgramOutcome outcome =
                        ProgramOutcome.create(localContext(), program);
                    if (notEmpty(microLabel))
                    {
                        outcome.setMicroLabel(microLabel);
                    }
                    if (notEmpty(label))
                    {
                        outcome.setLabel(label);
                    }
                    if (notEmpty(description))
                    {
                        outcome.setDescription(description);
                    }
//                    outcome.addToOutcomeSetsRelationship(set);
                }

                applyLocalChanges();
                line = in.getLine();
            }

            applyLocalChanges();
            if (!hasMessages())
            {
                confirmationMessage("outcomes processed successfully.");
            }
        }
        catch (Exception e)
        {
            log.error("Problem!", e);
            String msg = e.getMessage();
            error((msg == null) ? e.getClass().getSimpleName() : msg);
        }
        return null;
    }


    // ----------------------------------------------------------
    public WOComponent measureUpload()
    {
        clearAllMessages();
        try
        {
            InputStreamReader stream =
                new InputStreamReader(data.stream(), "UTF8");
            CSVParser in = new CSVParser(stream);
            in.changeDelimiter(',');

            String[] line = in.getLine();
            while (line != null)
            {
                String course = trim(line[0]);
                String po1 = trim(line[1]);
                String ao1 = trim(line[2]);
                String po2 = trim(line[3]);
                String ao2 = trim(line[4]);
                String keyPhrase = trim(line[5]);
                String description = trim(line[6]);

                int courseNo = ERXValueUtilities.intValue(course);

                Measure target = null;
                NSArray<Measure> candidates =
                    Measure.objectsMatchingQualifier(localContext(),
                        Measure.description.is(description).and(
                            Measure.keyPhrase.is(keyPhrase)));

                if (candidates.size() > 0)
                {
                    target = candidates.get(0);
                }
                else
                {
                    target = Measure.create(localContext());
                    if (notEmpty(description))
                    {
                        target.setDescription(description);
                    }
                    if (notEmpty(keyPhrase))
                    {
                        target.setKeyPhrase(keyPhrase);
                    }
                }

                Course c = Course.uniqueObjectMatchingQualifier(
                    localContext(),
                    Course.number.is(courseNo)
                    .and(Course.department.is(dept)));
                log.debug("course found for " + courseNo + " = " + c);
                if (c == null)
                {
                    c = Course.create(localContext(), "unknown", courseNo);
                    c.setDepartmentRelationship(dept);
                    log.debug("created new course: " + c);
                }
                target.addToCoursesRelationship(c);

                ExternalOutcome eo = ExternalOutcome
                    .uniqueObjectMatchingQualifier(localContext(),
                        ExternalOutcome.microLabel.is(ao1));
                log.debug("External outcome " + ao1 + " = " + eo);
                if (eo == null)
                {
                    error("could not find external outcome for microLabel "
                        + ao1);
                }
                ProgramOutcome po = ProgramOutcome
                .uniqueObjectMatchingQualifier(localContext(),
                    ProgramOutcome.microLabel.is(po1));
                log.debug("Program outcome " + po1 + " = " + po);
                if (po == null)
                {
                    error("could not find program outcome for microLabel "
                        + po1);
                }

                OutcomePair pair =
                    OutcomePair.uniqueObjectMatchingQualifier(
                        localContext(),
                        OutcomePair.externalOutcome.is(eo)
                        .and(OutcomePair.programOutcome.is(po)));
                if (pair == null)
                {
                    log.debug("create new pair for " + eo + ", " + po);
                    pair = OutcomePair.create(localContext(), eo, po);
                    log.debug("created new pair: " + pair);
                }
                else
                {
                    log.debug("found pair: " + pair);
                }
                target.addToOutcomePairsRelationship(pair);

                if (notEmpty(ao2) && notEmpty(po2))
                {
                    eo = ExternalOutcome
                        .uniqueObjectMatchingQualifier(localContext(),
                            ExternalOutcome.microLabel.is(ao2));
                    log.debug("External outcome " + ao2 + " = " + eo);
                    if (eo == null)
                    {
                        error("could not find external outcome for "
                            + "microLabel " + ao2);
                    }
                    po = ProgramOutcome.uniqueObjectMatchingQualifier(
                        localContext(),
                        ProgramOutcome.microLabel.is(po2));
                    log.debug("Program outcome " + po2 + " = " + po);
                    if (po == null)
                    {
                        error("could not find program outcome for "
                            + "microLabel " + po2);
                    }

                    pair =
                        OutcomePair.uniqueObjectMatchingQualifier(
                            localContext(),
                            OutcomePair.externalOutcome.is(eo)
                            .and(OutcomePair.programOutcome.is(po)));
                    if (pair == null)
                    {
                        pair = OutcomePair.create(localContext(), eo, po);
                        log.debug("create new pair: " + pair);
                    }
                    else
                    {
                        log.debug("found pair: " + pair);
                    }
                    target.addToOutcomePairsRelationship(pair);
                }

                applyLocalChanges();
                line = in.getLine();
            }

            applyLocalChanges();
            if (!hasMessages())
            {
                confirmationMessage("measures processed successfully.");
            }
        }
        catch (Exception e)
        {
            log.error("Problem!", e);
            String msg = e.getMessage();
            error((msg == null) ? e.getClass().getSimpleName() : msg);
        }
        return null;
    }


    // ----------------------------------------------------------
    public WOComponent courseUpload()
    {
        clearAllMessages();
        try
        {
            InputStreamReader stream =
                new InputStreamReader(data.stream(), "UTF8");
            CSVParser in = new CSVParser(stream);
            in.changeDelimiter(',');

            String[] line = in.getLine();
            while (line != null)
            {
                String crn = trim(line[0]);
                String course = trim(line[1]);
                String courseName = trim(line[2]);
                String last = trim(line[3]);
                String first = trim(line[4]);
                String pid = trim(line[5]);

                int pos = course.lastIndexOf(' ');
                if (pos > 0)
                {
                    course = course.substring(pos + 1);
                }
                pos = course.lastIndexOf('-');
                if (pos > 0)
                {
                    course = course.substring(pos + 1);
                }

                int courseNo = ERXValueUtilities.intValue(course);

                Course c = Course.uniqueObjectMatchingQualifier(localContext(),
                    Course.number.is(courseNo).and(Course.name.is(courseName)));
                if (c == null)
                {
                    c = Course.uniqueObjectMatchingQualifier(localContext(),
                        Course.number.is(courseNo)
                        .and(Course.name.is("unknown")));
                    if (c == null)
                    {
                        c = Course.create(localContext(), courseName, courseNo);
                        c.setDepartment(dept);
                    }
                    else
                    {
                        c.setName(courseName);
                    }
                }

                CourseOffering offering =
                    CourseOffering.uniqueObjectMatchingQualifier(localContext(),
                        CourseOffering.crn.is(crn)
                        .and(CourseOffering.course.is(c)));
                if (offering == null)
                {
                    offering = CourseOffering.create(localContext());
                    offering.setCourseRelationship(c);
                    offering.setCrn(crn);
                    offering.setSemester(semester);
                }

                User user = User.uniqueObjectMatchingQualifier(localContext(),
                    User.userName.is(pid));
                if (user == null)
                {
                    user = User.create(localContext(), (byte)0, false, pid,
                        AuthenticationDomain.defaultDomain());
                }
                if (user.firstName() == null && notEmpty(first))
                {
                    user.setFirstName(first);
                }
                if (user.lastName() == null && notEmpty(last))
                {
                    user.setLastName(last);
                }

                if (!offering.instructors().contains(user))
                {
                    offering.addToInstructorsRelationship(user);
                }

                applyLocalChanges();
                line = in.getLine();
            }

            applyLocalChanges();
            if (!hasMessages())
            {
                confirmationMessage("courses processed successfully.");
            }
        }
        catch (Exception e)
        {
            log.error("Problem!", e);
            String msg = e.getMessage();
            error((msg == null) ? e.getClass().getSimpleName() : msg);
        }
        return null;
    }


    // ----------------------------------------------------------
    private String trim(String in)
    {
        if (in == null)
        {
            return in;
        }
        else
        {
            return in.trim();
        }
    }


    // ----------------------------------------------------------
    private boolean notEmpty(String in)
    {
        return in != null && !"".equals(in);
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(MeasurementSetupPage.class);
}
