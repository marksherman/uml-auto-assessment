/*==========================================================================*\
 |  $Id: User.java,v 1.16 2012/06/22 16:23:18 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2012 Virginia Tech
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

package org.webcat.core;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.webcat.woextensions.WCEC;
import com.webobjects.appserver.WOComponent;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import er.extensions.eof.ERXKey;
import er.extensions.foundation.ERXArrayUtilities;

// -------------------------------------------------------------------------
/**
 * A user of the system.
 * <p>
 * This class also defines constant values for the access levels supported
 * by Web-CAT.  Each higher access level subsumes all of the rights and
 * privileges of all lower levels.  The levels, in order from lowest to
 * highest, are:
 * </p><ul>
 * <li> STUDENT_PRIVILEGES
 * <li> GRADER_PRIVILEGES
 * <li> GTA_PRIVILEGES
 * <li> INSTRUCTOR_PRIVILEGES
 * <li> WEBCAT_READ_PRIVILEGES
 * <li> WEBCAT_RW_PRIVILEGES
 * </ul>
 *
 * @author  Stephen Edwards
 * @author  Last changed by: $Author: aallowat $
 * @version $Revision: 1.16 $, $Date: 2012/06/22 16:23:18 $
 */
public class User
    extends _User
    implements RepositoryProvider
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new User object.
     */
    public User()
    {
        super();
    }


    //~ Public Constants ......................................................

    public static final byte STUDENT_PRIVILEGES     = 0;
    public static final byte GRADER_PRIVILEGES      = 30;
    public static final byte GTA_PRIVILEGES         = 40;
    public static final byte INSTRUCTOR_PRIVILEGES  = 50;
    public static final byte WEBCAT_READ_PRIVILEGES = 80;
    public static final byte WEBCAT_RW_PRIVILEGES   = 90;

    public static final String TIME_ZONE_NAME_KEY = "timeZoneName";
    public static final String TIME_FORMAT_KEY    = "timeFormat";
    public static final String DATE_FORMAT_KEY    = "dateFormat";

    public static final ERXKey<String> name_LF =
        new ERXKey<String>("name_LF");

    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Creates a new user.
     *
     * @param aUserName         The new username
     * @param aPassword    The user's password
     * @param domain      The domain the user comes from
     * @param anAccessLevel The user's access privilege level
     * @param ec          The editing context in which to create the user
     * @return            The new user object
     */
    public static User createUser(String               aUserName,
                                  String               aPassword,
                                  AuthenticationDomain domain,
                                  byte                 anAccessLevel,
                                  EOEditingContext     ec)
    {
        User u = new User();
        ec.insertObject(u);
        u.setPreferences(new MutableDictionary());
        u.setUserName(aUserName);
        u.setPassword(aPassword);
        u.setAccessLevel(anAccessLevel);
        u.setAuthenticationDomainRelationship(domain);
        ec.saveChanges();
        return u;
    }


    // ----------------------------------------------------------
    /**
     * An exception to indicate that multiple users were found for the
     * given search.
     */
    public static class MultipleUsersFoundException
        extends RuntimeException
    {
        public MultipleUsersFoundException(String msg)
        {
            super(msg);
        }
    }


    // ----------------------------------------------------------
    /**
     * Looks up an existing user by user name and domain.
     *
     * @param ec          The editing context in which to lookup the user
     * @param aUserName    The username to look up
     * @param domain      The domain the user comes from
     * @return            The user object, or null if none is found
     * @throws MultipleUsersFoundException if multiple users matching the
     * search criteria are found.
     */
    public static User lookupUser(EOEditingContext     ec,
                                  String               aUserName,
                                  AuthenticationDomain domain)
    {
        try
        {
            return userWithDomainAndName(ec, domain, aUserName);
        }
        catch (EOUtilities.MoreThanOneException e)
        {
            throw new MultipleUsersFoundException("Multiple users found when "
                + "searching for userName = " + aUserName + " and domain = "
                + domain);
        }
    }


    // ----------------------------------------------------------
    /**
     * Looks up an existing user by email address and domain.
     *
     * @param ec          The editing context in which to lookup the user
     * @param eMail       The email address to look up
     * @param domain      The domain the user comes from
     * @return            The user object, or null if none is found
     * @throws MultipleUsersFoundException if multiple users matching the
     * search criteria are found.
     */
    public static User lookupUserByEmail(EOEditingContext     ec,
                                         String               eMail,
                                         AuthenticationDomain domain)
    {
        // First, try a raw database lookup
        try
        {
            User user = userWithDomainAndEmail(ec, domain, eMail);

            if (user != null)
            {
                return user;
            }
        }
        catch (EOUtilities.MoreThanOneException e)
        {
            throw new MultipleUsersFoundException("Multiple users found when "
                + "searching for email = " + eMail + " and domain = "
                + domain);
        }

        // But if that gives no results, it may be because the user does
        // not have an explicit e-mail address stored in the database,
        // and is instead using <username>@<domain.default> as their
        // e-mail address.  So extract the user name from the e-mail address
        // and check it instead.
        String theUserName = eMail;
        int pos = eMail.indexOf('@');
        if (pos >= 0)
        {
            theUserName = theUserName.substring(0, pos);
        }

        // Look up by user name
        User user = lookupUser(ec, theUserName, domain);
        // Check that the located user has the correct e-mail address
        if (user != null && !eMail.equals(user.email()))
        {
            // What? e-mail addresses didn't match, so ignore that user
            user = null;
        }
        return user;
    }


    // ----------------------------------------------------------
    /**
     * Get a short (no longer than 60 characters) description of this user,
     * which currently returns {@link #name()}.
     * @return the description
     */
    public String userPresentableDescription()
    {
        return name();
    }


    // ----------------------------------------------------------
    /**
     * Return the user's full name as a string, in the format "First Last".
     * @return the name
     */
    public String name()
    {
        String last  = lastName();
        String first = firstName();
        boolean lastIsEmpty = (last == null || last.equals(""));
        boolean firstIsEmpty = (first == null || first.equals(""));

        if (lastIsEmpty && firstIsEmpty)
        {
            return userName();
        }
        else if (lastIsEmpty)
        {
            return first;
        }
        else if (firstIsEmpty)
        {
            return last;
        }
        else
        {
            return first + " " + last;
        }
    }


    // ----------------------------------------------------------
    /**
     * Return the user's full name as a string, in the format "Last, First"
     * (the meaning of the _LF suffix).
     * @return the name
     */
    public String name_LF()
    {
        if (name_LF_cache == null)
        {
            String last  = lastName();
            String first = firstName();
            boolean lastIsEmpty = (last == null || last.equals(""));
            boolean firstIsEmpty = (first == null || first.equals(""));

            if (lastIsEmpty && firstIsEmpty)
            {
                name_LF_cache = userName();
            }
            else if (lastIsEmpty)
            {
                name_LF_cache = first;
            }
            else if (firstIsEmpty)
            {
                name_LF_cache = last;
            }
            else
            {
                name_LF_cache = last + ", " + first;
            }
        }
        return name_LF_cache;
    }


    // ----------------------------------------------------------
    public void setFirstName(String value)
    {
        super.setFirstName(value);
        name_LF_cache = null;
    }


    // ----------------------------------------------------------
    public void setLastName(String value)
    {
        super.setLastName(value);
        name_LF_cache = null;
    }


    // ----------------------------------------------------------
    public void setUserName(String value)
    {
        super.setUserName(value);
        name_LF_cache = null;
    }


    // ----------------------------------------------------------
    /**
     * Return the user's full name as a string, in the format "First Last".
     * @return the name
     */
    public String nameAndUid()
    {
        String name  = name();
        if (name == null || name.equals(""))
        {
            return userName();
        }
        else
        {
            return name + " (" + userName() + ")";
        }
    }


    // ----------------------------------------------------------
    /**
     * Return the user's full name as a string, in the format "Last, First"
     * (the meaning of the _LF suffix).
     * @return the name
     */
    public String shortName()
    {
        String last  = lastName();

        if (last == null || last.equals(""))
        {
            return userName();
        }
        else
        {
            return last;
        }
    }


    // ----------------------------------------------------------
    /**
     * Retrieve this user's e-mail address.  This comes
     * from the <code>email</code> attribute value, if set.  If
     * <code>email</code> is not set, the user's pid is combined
     * with "@" and the authentication domain's default e-mail domain.
     *
     * @return the e-mail address
     */
    public String email()
    {
        String result = super.email();
        if (result == null || result == "")
        {
            result = userName();
            AuthenticationDomain authDomain = authenticationDomain();
            if (authDomain != null)
            {
                String domain = authDomain.defaultEmailDomain();
                if (domain != null && domain != "")
                {
                    result = result + "@" + domain;
                }
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Returns an href for emailing a user.
     * @return a mailto: string containing the e-mail address
     */
    public String emailHref()
    {
        return "mailto:" + email();
    }


    // ----------------------------------------------------------
    /**
     * Get the time format this user prefers.  If the user has not set a
     * preference, the default time format for the user's authentication
     * domain will be used.  The value should be a format string
     * acceptable by {@link NSTimestampFormatter}.
     * @see AuthenticationDomain#timeFormat()
     * @return the time format pattern
     */
    public String timeFormat()
    {
        String result =
            (String)preferences().objectForKey(TIME_FORMAT_KEY);
        if (result == null || result.equals(""))
        {
            result = authenticationDomain().timeFormat();
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Set the time format pattern for this user.
     *
     * @param value The new value for this property
     */
    public void setTimeFormat(String value)
    {
        preferences().setObjectForKey(value, TIME_FORMAT_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Get the time zone name of this user's preferred time zone.  If the
     * user has not set a time zone preference, the default time zone for
     * the user's authentication domain is used instead.
     * @see AuthenticationDomain#timeZoneName()
     * @return the time zone name
     */
    public String timeZoneName()
    {
        String result =
            (String)preferences().objectForKey(TIME_ZONE_NAME_KEY);
        if (result == null || result.equals(""))
        {
            result = authenticationDomain().timeZoneName();
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Set the time zone name for this user's preferred time zone.
     *
     * @param value The new value for this property
     */
    public void setTimeZoneName(String value)
    {
        preferences().setObjectForKey(value, TIME_ZONE_NAME_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Get the date format this user prefers.  If the user has not set a
     * preference, the default date format for the user's authentication
     * domain will be used.  The value should be a format string
     * acceptable by {@link NSTimestampFormatter}.
     * @see AuthenticationDomain#dateFormat()
     * @return the date format pattern
     */
    public String dateFormat()
    {
        String result =
            (String)preferences().objectForKey(DATE_FORMAT_KEY);
        if (result == null || result.equals(""))
        {
            result = authenticationDomain().dateFormat();
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Set the date format pattern for this user.
     *
     * @param value The new value for this property
     */
    public void setDateFormat(String value)
    {
        preferences().setObjectForKey(value, DATE_FORMAT_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Validate the user with the given password.
     * Internally, it uses the <code>CurrentUserAuthenticator</code>
     * class to perform authentication.
     *
     * @param aUserName The user id to validate
     * @param aPassword The password to check
     * @param domain   The domain to which this user belongs
     * @param ec       The editing context in which to create the user object
     * @return True if the username/password combination is valid
     */
    public static User validate(
            String aUserName,
            String aPassword,
            AuthenticationDomain domain,
            com.webobjects.eocontrol.EOEditingContext ec
        )
    {
        UserAuthenticator authenticator = domain.authenticator();
        if (authenticator != null)
        {
            return authenticator.authenticate(
                aUserName, aPassword, domain, ec);
        }
        else
        {
            log.error("no registered authenticator called "
                + domain.propertyName());
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Check whether this user can change his/her password.
     * @return True if users associated with this authenticator can
     *         change their password
     */
    public boolean canChangePassword()
    {
        AuthenticationDomain ad = authenticationDomain();
        if ( ad == null )
            return false;
        else
        {
            UserAuthenticator authenticator = ad.authenticator();
            return authenticator != null
                && authenticator.canChangePassword();
        }
    }


    // ----------------------------------------------------------
    /**
     * Change the user's password, if possible.
     * @param newPassword The password to change to
     * @return True if the password change was successful
     */
    public boolean changePassword(String newPassword)
    {
        AuthenticationDomain ad = authenticationDomain();
        if (ad == null)
        {
            return false;
        }
        else
        {
            UserAuthenticator authenticator = ad.authenticator();
            return authenticator != null
                && authenticator.changePassword(this, newPassword);
        }
    }


    // ----------------------------------------------------------
    /**
     * Reset the user's password to a new random password and e-mail the
     * user a message, if possible.
     * @return True if the password change was successful
     */
    public boolean newRandomPassword()
    {
        AuthenticationDomain ad = authenticationDomain();
        if (ad == null)
        {
            return false;
        }
        else
        {
            UserAuthenticator authenticator = ad.authenticator();
            return authenticator != null
                && authenticator.newRandomPassword(this);
        }
    }


    // ----------------------------------------------------------
    /**
     * Find out if this user can switch between student and staff views.
     * @return true if this user can switch
     */
    public boolean canChangeViews()
    {
        return accessLevel() > STUDENT_PRIVILEGES;
    }


    // ----------------------------------------------------------
    /**
     * Toggle the student view setting for this user.  This only affect's
     * the state within the user object, and does not affect any session
     * navigation or other UI features.  It is intended to be called from
     * {@link Session#toggleStudentView()}, which handles updating the
     * corresponding session navigation data.
     * @return Returns null, to force reloading of the calling page
     * (if desired)
     */
    public WOComponent toggleStudentView()
    {
        if (canChangeViews())
        {
            studentView = !studentView;
            graderFor_cache = null;
            teaching_cache = null;
            graderForButNotStudent_cache = null;
            instructorForButNotGraderOrStudent_cache = null;
            staffFor_cache = null;
            adminForButNotStaff_cache = null;
            adminForButNoOtherRelationships_cache = null;
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Get the string label for the "toggle view" button for this user.
     * @return The button label text, indicating what view the user will
     * toggle to next
     */
    public String toggleViewLabel()
    {
        String result = "Student View";
        if (studentView)
        {
            if (accessLevel() > INSTRUCTOR_PRIVILEGES)
            {
                result = "Admin View";
            }
            else if (accessLevel() > GTA_PRIVILEGES)
            {
                result = "Instructor View";
            }
            else
            {
                result = "Grader View";
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Determine if this user's view should be restricted to student-only
     * features.
     * @return true if this user's view is restricted
     */
    public boolean restrictToStudentView()
    {
        return studentView;
    }


    // ----------------------------------------------------------
    /**
     * @return the array of course offerings that this user is a grader for
     *
     * @deprecated Use the {@link #graderFor()} method instead.
     */
    @Deprecated
    public NSArray<CourseOffering> TAFor()
    {
        return graderFor();
    }


    // ----------------------------------------------------------
    /**
     * @return the array of course offerings that this user is a grader for
     */
    public NSArray<CourseOffering> graderFor()
    {
        return studentView ? NO_COURSES : super.graderFor();
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is a TA for.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     *
     * @deprecated Use the {@link #graderFor(Semester)} method instead.
     */
    @Deprecated
    public NSArray<CourseOffering> TAFor(Semester semester)
    {
        return graderFor(semester);
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is a TA for.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> graderFor(Semester semester)
    {
        NSArray<CourseOffering> result = graderFor();
        if (semester != null)
        {
            result = CourseOffering.semester.is(semester).filtered(result);
        }
        return result;
    }


    // ----------------------------------------------------------
    public NSArray<CourseOffering> teaching()
    {
        return studentView ? NO_COURSES : super.teaching();
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is teaching.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> teaching(Semester semester)
    {
        NSArray<CourseOffering> result = teaching();
        if (semester != null)
        {
            result = CourseOffering.semester.is(semester).filtered(result);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Returns true if the user has grader privileges for Web-CAT.
     * @return true if user has at least grader access level
     */
    public boolean hasGraderPrivileges()
    {
        return !studentView && accessLevel() >= GTA_PRIVILEGES;
    }


    // ----------------------------------------------------------
    /**
     * Returns true if the user has grader privileges for Web-CAT.
     * @return true if user has at least grader access level
     *
     * @deprecated use the {@link #hasGraderPrivileges()} method instead.
     */
    @Deprecated
    public boolean hasTAPrivileges()
    {
        return hasGraderPrivileges();
    }


    // ----------------------------------------------------------
    /**
     * Returns true if the user has faculty privileges for Web-CAT.
     * @return true if user has at least faculty access level
     */
    public boolean hasFacultyPrivileges()
    {
        return !studentView && accessLevel() >= INSTRUCTOR_PRIVILEGES;
    }


    // ----------------------------------------------------------
    /**
     * Returns true if the user has faculty privileges for Web-CAT.
     * @return true if user has at least faculty access level
     */
    public boolean hasAdminPrivileges()
    {
        return !studentView && accessLevel() >= WEBCAT_RW_PRIVILEGES;
    }


    // ----------------------------------------------------------
    /**
     * Adds property definitions to the given properties object containing
     * this user's basic information.  All properties added begin with
     * "user.".
     * @param properties The object to which the definitions will be added
     */
    public void addPropertiesTo(Properties properties)
    {
        String value = email();
        if (value != null)
        {
            properties.setProperty(PREFIX + EMAIL_KEY, value);
        }
        value = firstName();
        if (value != null)
        {
            properties.setProperty(PREFIX + FIRST_NAME_KEY, value);
        }
        value = lastName();
        if (value != null)
        {
            properties.setProperty(PREFIX + LAST_NAME_KEY, value);
        }
        value = password();
        if (value != null)
        {
            properties.setProperty(PREFIX + PASSWORD_KEY, value);
        }
        value = universityIDNo();
        if (value != null)
        {
            properties.setProperty(PREFIX + UNIVERSITY_ID_NO_KEY, value);
        }
        value = url();
        if (value != null)
        {
            properties.setProperty(PREFIX + URL_KEY, value);
        }
        value = userName();
        if (value != null)
        {
            properties.setProperty(PREFIX + USER_NAME_KEY, value);
        }
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is a TA for,
     * without including any courses where this user is also a student.
     * @return a sorted array of the matching course offerings.
     *
     * @deprecated Use the {@link #graderForButNotStudent()} method instead.
     */
    @Deprecated
    public NSArray<CourseOffering> TAForButNotStudent()
    {
        return graderForButNotStudent();
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is a grader for,
     * without including any courses where this user is also a student.
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> graderForButNotStudent()
    {
        if (graderFor_cache != graderFor())
        {
            graderFor_cache = graderFor();
            graderForButNotStudent_cache = null;
        }
        if (graderFor_cache == null || graderFor_cache.count() == 0)
        {
            graderForButNotStudent_cache = NO_COURSES;
        }
        else
        {
            if (enrolledIn_cache != enrolledIn())
            {
                enrolledIn_cache = enrolledIn();
                graderForButNotStudent_cache = null;
            }
            if (graderForButNotStudent_cache == null)
            {
                @SuppressWarnings("unchecked")
                NSArray<CourseOffering> cache =
                    ERXArrayUtilities.filteredArrayWithEntityFetchSpecification(
                        graderFor_cache,
                        CourseOffering.ENTITY_NAME,
                        CourseOffering.OFFERINGS_WITHOUT_STUDENT_FSPEC,
                        userFilteringDictionary()
                        );
                graderForButNotStudent_cache = cache;
            }
        }
        return graderForButNotStudent_cache;
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is a TA for,
     * without including any courses where this user is also a student.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     *
     * @deprecated Use the {@link #graderForButNotStudent(Semester)} method
     *     instead.
     */
    @Deprecated
    public NSArray<CourseOffering> TAForButNotStudent(Semester semester)
    {
        return graderForButNotStudent(semester);
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is a grader for,
     * without including any courses where this user is also a student.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> graderForButNotStudent(Semester semester)
    {
        NSArray<CourseOffering> result = graderForButNotStudent();
        if (semester != null)
        {
            result = CourseOffering.semester.is(semester).filtered(result);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is an
     * instructor for, without including any courses where this user is also
     * a student or a grader.
     * @return a sorted array of the matching course offerings.
     *
     * @deprecated Use the {@link #instructorForButNotGraderOrStudent()} method
     *     instead.
     */
    @Deprecated
    public NSArray<CourseOffering> instructorForButNotTAOrStudent()
    {
        return instructorForButNotGraderOrStudent();
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is an
     * instructor for, without including any courses where this user is also
     * a student or a grader.
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> instructorForButNotGraderOrStudent()
    {
        if (teaching_cache != teaching())
        {
            teaching_cache = teaching();
            instructorForButNotGraderOrStudent_cache = null;
        }
        if (teaching_cache == null || teaching_cache.count() == 0)
        {
            instructorForButNotGraderOrStudent_cache = NO_COURSES;
        }
        else
        {
            if (enrolledIn_cache != enrolledIn())
            {
                enrolledIn_cache = enrolledIn();
                instructorForButNotGraderOrStudent_cache = null;
            }
            if (graderFor_cache != graderFor())
            {
                graderFor_cache = graderFor();
                instructorForButNotGraderOrStudent_cache = null;
            }
            if (instructorForButNotGraderOrStudent_cache == null)
            {
                @SuppressWarnings("unchecked")
                NSArray<CourseOffering> cache =
                    ERXArrayUtilities
                        .filteredArrayWithEntityFetchSpecification(
                            teaching_cache,
                            CourseOffering.ENTITY_NAME,
                            CourseOffering
                                .OFFERINGS_WITHOUT_STUDENT_OR_GRADER_FSPEC,
                            userFilteringDictionary()
                    );
                instructorForButNotGraderOrStudent_cache = cache;
            }
        }
        return instructorForButNotGraderOrStudent_cache;
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is an
     * instructor for, without including any courses where this user is also
     * a student or a grader.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     *
     * @deprecated Use the {@link #instructorForButNotTAOrStudent(Semester)}
     *     method instead.
     */
    @Deprecated
    public NSArray<CourseOffering> instructorForButNotTAOrStudent(
        Semester semester)
    {
        return instructorForButNotGraderOrStudent(semester);
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is an
     * instructor for, without including any courses where this user is also
     * a student or a grader.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> instructorForButNotGraderOrStudent(
        Semester semester)
    {
        NSArray<CourseOffering> result = instructorForButNotGraderOrStudent();
        if (semester != null)
        {
            result = CourseOffering.semester.is(semester).filtered(result);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Returns a list of administrator users.
     * @return an array of administrators.
     */
    public static NSArray<User> administrators(EOEditingContext context)
    {
        return objectsMatchingQualifier(context,
            accessLevel.greaterThanOrEqualTo((int)WEBCAT_RW_PRIVILEGES));
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is either
     * an instructor or grader for.
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> staffFor()
    {
        if (graderFor_cache != graderFor())
        {
            graderFor_cache = graderFor();
            staffFor_cache = null;
        }
        if (teaching_cache != teaching())
        {
            teaching_cache = teaching();
            staffFor_cache = null;
        }
        if (staffFor_cache == null)
        {
            staffFor_cache = EOSortOrdering.sortedArrayUsingKeyOrderArray(
                teaching_cache.arrayByAddingObjectsFromArray(graderFor_cache),
                courseSortOrderings);
        }
        return staffFor_cache;
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user is either
     * an instructor or grader for.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> staffFor(Semester semester)
    {
        NSArray<CourseOffering> result = staffFor();
        if (semester != null)
        {
            result = CourseOffering.semester.is(semester).filtered(result);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user has
     * administrative access to, but is not an instructor or TA
     * for.
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> adminForButNotStaff()
    {
        if (!hasAdminPrivileges())
        {
            return NO_COURSES;
        }
        if (graderFor_cache != graderFor())
        {
            graderFor_cache = graderFor();
            adminForButNotStaff_cache = null;
        }
        if (teaching_cache != teaching())
        {
            teaching_cache = teaching();
            adminForButNotStaff_cache = null;
        }
        if (adminForButNotStaff_cache == null)
        {
            // For some reason, using the fetch directly does not seem
            // to work, at least not when the result is empty.
//            adminForButNoOtherRelationships_cache =
//                CourseOffering.objectsForWithoutAnyRelationshipToUser(
//                    editingContext(), this );
            @SuppressWarnings("unchecked")
            NSArray<CourseOffering> cache =
                ERXArrayUtilities.filteredArrayWithEntityFetchSpecification(
                    EOUtilities.objectsForEntityNamed(editingContext(),
                        CourseOffering.ENTITY_NAME),
                    CourseOffering.ENTITY_NAME,
                    CourseOffering.OFFERINGS_WITHOUT_USER_AS_STAFF_FSPEC,
                    userFilteringDictionary()
                    );
            adminForButNotStaff_cache = cache;
        }
        return adminForButNotStaff_cache;
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user has
     * administrative access to, but is not an instructor or TA
     * for.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> adminForButNotStaff(Semester semester)
    {
        NSArray<CourseOffering> result = adminForButNotStaff();
        if (semester != null)
        {
            result = CourseOffering.semester.is(semester).filtered(result);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user has
     * administrative access to, but is not an instructor, TA, or student
     * for.
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> adminForButNoOtherRelationships()
    {
        if (!hasAdminPrivileges())
        {
            return NO_COURSES;
        }
        if (enrolledIn_cache != enrolledIn())
        {
            enrolledIn_cache = enrolledIn();
            adminForButNoOtherRelationships_cache = null;
        }
        if (graderFor_cache != graderFor())
        {
            graderFor_cache = graderFor();
            adminForButNoOtherRelationships_cache = null;
        }
        if (teaching_cache != teaching())
        {
            teaching_cache = teaching();
            adminForButNoOtherRelationships_cache = null;
        }
        if (adminForButNoOtherRelationships_cache == null)
        {
            // For some reason, using the fetch directly does not seem
            // to work, at least not when the result is empty.
//            adminForButNoOtherRelationships_cache =
//                CourseOffering.objectsForWithoutAnyRelationshipToUser(
//                    editingContext(), this );
            @SuppressWarnings("unchecked")
            NSArray<CourseOffering> temp =
                ERXArrayUtilities.filteredArrayWithEntityFetchSpecification(
                    CourseOffering.allObjects(editingContext()),
                    CourseOffering.ENTITY_NAME,
                    CourseOffering
                        .OFFERINGS_WITHOUT_ANY_RELATIONSHIP_TO_USER_FSPEC,
                    userFilteringDictionary());
            adminForButNoOtherRelationships_cache = temp;
        }
        return adminForButNoOtherRelationships_cache;
    }


    // ----------------------------------------------------------
    /**
     * Returns a sorted list of course offerings that this user has
     * administrative access to, but is not an instructor, TA, or student
     * for.
     * @param semester Only return courses for this semester.  A value of null
     * means all courses (same as staffFor()).
     * @return a sorted array of the matching course offerings.
     */
    public NSArray<CourseOffering> adminForButNoOtherRelationships(
        Semester semester)
    {
        NSArray<CourseOffering> result = adminForButNoOtherRelationships();
        if (semester != null)
        {
            result = CourseOffering.semester.is(semester).filtered(result);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the CoreSelections object associated with this user,
     * creating one if necessary.
     * @return This user's core selections object
     */
    public CoreSelections getMyCoreSelections()
    {
        NSArray<CoreSelections> cs = coreSelections();
        if (cs.count() == 0)
        {
            EOEditingContext ec = WCEC.newEditingContext();
            try
            {
                ec.lock();
                CoreSelections newCoreSelections = new CoreSelections();
                ec.insertObject(newCoreSelections);
                newCoreSelections.setUserRelationship(localInstance(ec));
                ec.saveChanges();
                editingContext().refreshObject(this);
                cs = coreSelections();
            }
            finally
            {
                ec.unlock();
                ec.dispose();
            }
        }
        return cs.objectAtIndex(0);
    }


    // ----------------------------------------------------------
    /**
     * Use a separate editing context to save this user's preferences data,
     * if possible.
     */
    public void savePreferences()
    {
        boolean usingFreshEC = (ecForPrefs == null);
        if (usingFreshEC)
        {
            ecForPrefs = WCEC.newEditingContext();
        }
        EOEditingContext ec = ecForPrefs;
        ec.lock();
        try
        {
            // Use a separate EC to store the changed preferences
            User me = localInstance(ec);
            me.setPreferences(preferences());
            ec.saveChanges();
            // Now refresh the session's user object so that it loads
            // this saved preferences value
            editingContext().refreshObject(this);
        }
        catch (Exception e)
        {
            // If there was an error saving ...
            ecForPrefs = null;
            try
            {
                // Try to unlock first, if possible
                try
                {
                    ec.unlock();
                }
                catch (Exception eee)
                {
                    // nothing
                }
                // Try to clean up the broken editing context, if possible
                ec.dispose();
            }
            catch (Exception ee)
            {
                // if there is an error, ignore it since we're not going to
                // use this ec any more anyway
            }
            ec = null;
            if (!usingFreshEC)
            {
                savePreferences();
            }
        }
        finally
        {
            if (ec != null)
            {
                ec.unlock();
            }
        }
    }


    // ----------------------------------------------------------
    public static String scriptRoot()
    {
        // I don't like having this here, but it's necessary to get the same
        // existing behavior in the function below, and it's only used for
        // migrating data from the old location into the new Git repositories.

        if (scriptRoot == null)
        {
            scriptRoot = org.webcat.core.Application
                .configurationProperties().getProperty("grader.scriptsroot");

            if (scriptRoot == null)
            {
                scriptRoot = org.webcat.core.Application
                    .configurationProperties()
                        .getProperty("grader.submissiondir") + "/UserScripts";
            }
        }

        return scriptRoot;
    }


    // ----------------------------------------------------------
    public static String userDataRoot()
    {
        if (userDataRoot == null)
        {
            userDataRoot = org.webcat.core.Application.configurationProperties()
                .getProperty("grader.scriptsdataroot");

            if (userDataRoot == null)
            {
                userDataRoot = scriptRoot() + "Data";
            }
        }

        return userDataRoot;
    }


    // ----------------------------------------------------------
    public static User findObjectWithApiId(EOEditingContext ec, String apiId)
        throws EOUtilities.MoreThanOneException
    {
        int dotIndex = apiId.indexOf('.');
        EOQualifier qualifier = null;

        if (dotIndex != -1)
        {
            AuthenticationDomain domain = AuthenticationDomain
                .authDomainBySubdirName(apiId.substring(0, dotIndex));
            if (domain != null)
            {
                String name = apiId.substring(dotIndex + 1);

                qualifier = userName.is(name).and(
                    authenticationDomain.is(domain));
            }
        }
        if (qualifier == null)
        {
            qualifier = userName.is(apiId);
        }

        return uniqueObjectMatchingQualifier(ec, qualifier);
    }


    // ----------------------------------------------------------
    @Override
    public String apiId()
    {
        return authenticationDomain().subdirName() + "." + userName();
    }


    // ----------------------------------------------------------
    public void initializeRepositoryContents(File location) throws IOException
    {
        // Migrate the user's existing home directory from the deprecated
        // grader location.

        File oldLocation = new File(userDataRoot(),
                authenticationDomain().subdirName() + "/" + userName());
        if (oldLocation.exists())
        {
            FileUtilities.copyDirectoryContents(oldLocation, location);
        }
        else
        {
            log.warn("Location " + oldLocation.getAbsolutePath() + " does not "
                    + "exist; not copying any files into repository");
        }

        // Create a welcome file that describes the repository.

        String welcomeFilename = "README.txt";

        if (new File(location, welcomeFilename).exists())
        {
            welcomeFilename = "REPOSITORY-README.txt";
        }

        File readme = new File(location, welcomeFilename);
        PrintWriter writer = new PrintWriter(readme);

        writer.print(
          "This Git repository has been created to manage the personal files\n"
        + "for Web-CAT user \"" + userName() + "\" ("
        + authenticationDomain().displayableName() + ").\n\n");
        writer.print(
          "You can store any files you wish here. You can also delete this\n"
        + "readme file if you like; it was provided merely for informational\n"
        + "purposes.");

        writer.close();
    }


    // ----------------------------------------------------------
    public static NSArray<User> repositoriesPresentedToUser(User user,
            EOEditingContext ec)
    {
        if (user.hasTAPrivileges())
        {
            return new NSArray<User>(user.localInstance(ec));
        }
        else
        {
            return NSArray.<User>emptyArray();
        }
    }


    // ----------------------------------------------------------
    public boolean userCanAccessRepository(User user)
    {
        return user.equals(this);
    }


    /*
     * The following two overrides cause changes to preferences to be
     * auto-saved when the modification is made by pushing a value into a KVC
     * binding.
     */

    // ----------------------------------------------------------
    @Override
    public void takeValueForKey(Object value, String key)
    {
        super.takeValueForKey(value, key);

        if (PREFERENCES_KEY.equals(key))
        {
            savePreferences();
        }
    }


    // ----------------------------------------------------------
    @Override
    public void takeValueForKeyPath(Object value, String keyPath)
    {
        super.takeValueForKeyPath(value, keyPath);

        int dotIndex = keyPath.indexOf('.');
        if (dotIndex == -1 && PREFERENCES_KEY.equals(keyPath)
                || PREFERENCES_KEY.equals(keyPath.substring(0, dotIndex)))
        {
            savePreferences();
        }
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    private NSDictionary<String, User> userFilteringDictionary()
    {
        if (userIsMe == null)
        {
            userIsMe = new NSDictionary<String, User>(this, "user");
        }
        return userIsMe;
    }


    //~ Instance/static variables .............................................

    private NSArray<CourseOffering> enrolledIn_cache;
    private NSArray<CourseOffering> graderFor_cache;
    private NSArray<CourseOffering> teaching_cache;
    private NSArray<CourseOffering> graderForButNotStudent_cache;
    private NSArray<CourseOffering> instructorForButNotGraderOrStudent_cache;
    private NSArray<CourseOffering> staffFor_cache;
    private NSArray<CourseOffering> adminForButNotStaff_cache;
    private NSArray<CourseOffering> adminForButNoOtherRelationships_cache;
    private String  name_LF_cache;

    private static String scriptRoot;
    private static String userDataRoot;

    private EOEditingContext ecForPrefs;
    private NSDictionary<String, User> userIsMe;

    private boolean studentView = false;

    private static final String PREFIX = "user.";
    private static final NSArray<CourseOffering> NO_COURSES =
        new NSArray<CourseOffering>();
    private static final NSArray<EOSortOrdering> courseSortOrderings =
        CourseOffering.course.dot(Course.number).asc().then(
            CourseOffering.crn.asc());

    static Logger log = Logger.getLogger(User.class);
}
