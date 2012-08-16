/*==========================================================================*\
 |  $Id: ReflectionsPage.java,v 1.1 2010/05/11 14:51:50 aallowat Exp $
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

import com.webobjects.appserver.*;
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
public class ReflectionsPage
    extends BasePage
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public ReflectionsPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public FacultyReflection reflection;
    public User anInstructor;
    public int  index;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public String modificationsText()
    {
        String value = reflection.modifications();
        return value == null
            ? DEFAULT
            : value;
    }


    // ----------------------------------------------------------
    public void setModificationsText(String value)
    {
        if (DEFAULT.equals(value))
        {
            value = null;
        }
        reflection.setModifications(value);
    }


    // ----------------------------------------------------------
    public String reflectionText()
    {
        String value = reflection.reflection();
        return value == null
            ? DEFAULT
            : value;
    }


    // ----------------------------------------------------------
    public void setReflectionText(String value)
    {
        if (DEFAULT.equals(value))
        {
            value = null;
        }
        reflection.setReflection(value);
    }


    // ----------------------------------------------------------
    public String studentFeedbackText()
    {
        String value = reflection.studentFeedback();
        return value == null
            ? DEFAULT
            : value;
    }


    // ----------------------------------------------------------
    public void setStudentFeedbackText(String value)
    {
        if (DEFAULT.equals(value))
        {
            value = null;
        }
        reflection.setStudentFeedback(value);
    }


    // ----------------------------------------------------------
    public String actionText()
    {
        String value = reflection.proposedActions();
        return value == null
            ? DEFAULT
            : value;
    }


    // ----------------------------------------------------------
    public void setActionText(String value)
    {
        if (DEFAULT.equals(value)
            || (value != null && DEFAULT.trim().equals(value.trim())))
        {
            value = null;
        }
        reflection.setProposedActions(value);
    }


    // ----------------------------------------------------------
    public boolean applyLocalChanges()
    {
        if (reflection.changedProperties().count() > 0)
        {
            reflection.setLastChange(new NSTimestamp());
        }
        return super.applyLocalChanges();
    }


    //~ Instance/static variables .............................................

    private static final String DEFAULT = "[ Replace this text with your "
        + "response. Write \"see other course offering\" if you have "
        + "already entered comments for this course under another section "
        + "this semester. ]\n\n";
}
