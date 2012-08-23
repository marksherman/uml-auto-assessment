/*==========================================================================*\
 |  $Id: ReportMetadata.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
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

package org.webcat.oda.commons;

import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.IntegerPropertyType;
import org.eclipse.birt.report.model.metadata.StringPropertyType;

//-------------------------------------------------------------------------
/**
 * Methods in this class mirror each of the properties that Web-CAT uses to
 * maintain a report template. These methods provide uniform access to these
 * properties regardless of whether they are implemented as user properties or
 * by shadowing an existing BIRT property.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ReportMetadata.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
 */
public class ReportMetadata
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * A private constructor to prevent instantiation.
     */
    private ReportMetadata()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the title of the specified report template.
     *
     * @param module
     *            the report template handle
     * @return the title of the report
     */
    public static String getTitle(ModuleHandle module)
    {
        return module.getStringProperty(ModuleHandle.TITLE_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the title of the specified report template.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the new title of the report
     */
    public static void setTitle(ModuleHandle module, String value)
    {
        try
        {
            module.setStringProperty(ModuleHandle.TITLE_PROP, value);
        }
        catch (SemanticException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the description of the specified report template.
     *
     * @param module
     *            the report template handle
     * @return the description of the report
     */
    public static String getDescription(ModuleHandle module)
    {
        return module.getDescription();
    }


    // ----------------------------------------------------------
    /**
     * Sets the description of the specified report template. The description
     * should be written so that the first sentence is a brief abstract of the
     * purpose of the report; further content should describe the report in more
     * detail but may be elided in the Web-CAT interface (similar to Javadoc).
     *
     * @param module
     *            the report template handle
     * @param value
     *            the new description of the report
     */
    public static void setDescription(ModuleHandle module, String value)
    {
        try
        {
            module.setDescription(value);
        }
        catch (SemanticException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the number of authors in the specified report template.
     *
     * @param module
     *            the report template handle
     * @return the number of authors
     */
    public static int getAuthorsCount(ModuleHandle module)
    {
        Integer result = safeGetIntUserProperty(module, AUTHORS_COUNT);

        return (result == null ? 0 : result);
    }


    // ----------------------------------------------------------
    /**
     * Sets the number of authors in the specified report template.
     *
     * A side-effect of this method is that all of the existing author
     * information in the report template is dropped, so only call this method
     * if you intend to replace all of this information afterward.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the number of authors
     */
    public static void setAuthorsCount(ModuleHandle module, int value)
    {
        Integer _oldCount = safeGetIntUserProperty(module, AUTHORS_COUNT);

        safeSetIntUserProperty(module, AUTHORS_COUNT, value);

        if (_oldCount != null)
        {
            int oldCount = _oldCount;

            for (int i = 0; i < oldCount; i++)
            {
                try
                {
                    module.dropUserPropertyDefn(getAuthorProperty(
                            AUTHOR_FIRST_NAME_PROP, i));
                    module.dropUserPropertyDefn(getAuthorProperty(
                            AUTHOR_LAST_NAME_PROP, i));
                    module.dropUserPropertyDefn(getAuthorProperty(
                            AUTHOR_EMAIL_PROP, i));
                    module.dropUserPropertyDefn(getAuthorProperty(
                            AUTHOR_URL_PROP, i));
                    module.dropUserPropertyDefn(getAuthorProperty(
                            AUTHOR_AFFILIATION_PROP, i));
                    module.dropUserPropertyDefn(getAuthorProperty(
                            AUTHOR_PHONE_PROP, i));
                }
                catch (UserPropertyException e)
                {
                    e.printStackTrace();
                }
            }
        }

        updateBIRTAuthorProperty(module);
    }


    // ----------------------------------------------------------
    /**
     * Gets the first name of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @return the first name of the author
     */
    public static String getAuthorFirstName(ModuleHandle module, int index)
    {
        String property = getAuthorProperty(AUTHOR_FIRST_NAME_PROP, index);
        return safeGetStringUserProperty(module, property);
    }


    // ----------------------------------------------------------
    /**
     * Sets the first name of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @param value
     *            the first name of the author
     */
    public static void setAuthorFirstName(ModuleHandle module, int index,
            String value)
    {
        String property = getAuthorProperty(AUTHOR_FIRST_NAME_PROP, index);
        safeSetStringUserProperty(module, property, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the last name of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @return the last name of the author
     */
    public static String getAuthorLastName(ModuleHandle module, int index)
    {
        String property = getAuthorProperty(AUTHOR_LAST_NAME_PROP, index);
        return safeGetStringUserProperty(module, property);
    }


    // ----------------------------------------------------------
    /**
     * Sets the last name of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @param value
     *            the last name of the author
     */
    public static void setAuthorLastName(ModuleHandle module, int index,
            String value)
    {
        String property = getAuthorProperty(AUTHOR_LAST_NAME_PROP, index);
        safeSetStringUserProperty(module, property, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the full name of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @return the full name of the author
     */
    public static String getAuthorName(ModuleHandle module, int index)
    {
        String firstName = getAuthorFirstName(module, index);
        String lastName = getAuthorLastName(module, index);

        String name = ""; //$NON-NLS-1$

        if (firstName != null)
            name += firstName;

        if (firstName != null && lastName != null)
            name += " "; //$NON-NLS-1$

        if (lastName != null)
            name += lastName;

        if (name.trim().length() == 0)
            return null;
        else
            return name;
    }


    // ----------------------------------------------------------
    /**
     * Sets the full name of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @param value
     *            the full name of the author
     */
    public static void setAuthorName(ModuleHandle module, int index,
            String value)
    {
        String firstName = null, lastName = null;

        if (value != null && value.trim().length() > 0)
        {
            int lastSpace = value.lastIndexOf(' ');

            if (lastSpace == -1)
            {
                lastName = value;
            }
            else
            {
                firstName = value.substring(0, lastSpace);
                lastName = value.substring(lastSpace + 1);
            }
        }

        setAuthorFirstName(module, index, firstName);
        setAuthorLastName(module, index, lastName);

        updateBIRTAuthorProperty(module);
    }


    // ----------------------------------------------------------
    /**
     * Gets the e-mail address of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @return the e-mail address of the author
     */
    public static String getAuthorEmail(ModuleHandle module, int index)
    {
        String property = getAuthorProperty(AUTHOR_EMAIL_PROP, index);
        return safeGetStringUserProperty(module, property);
    }


    // ----------------------------------------------------------
    /**
     * Sets the e-mail address of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @param value
     *            the e-mail address of the author
     */
    public static void setAuthorEmail(ModuleHandle module, int index,
            String value)
    {
        String property = getAuthorProperty(AUTHOR_EMAIL_PROP, index);
        safeSetStringUserProperty(module, property, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the website URL of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @return the website URL of the author
     */
    public static String getAuthorURL(ModuleHandle module, int index)
    {
        String property = getAuthorProperty(AUTHOR_URL_PROP, index);
        return safeGetStringUserProperty(module, property);
    }


    // ----------------------------------------------------------
    /**
     * Sets the website URL of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @param value
     *            the website URL of the author
     */
    public static void setAuthorURL(ModuleHandle module, int index, String value)
    {
        String property = getAuthorProperty(AUTHOR_URL_PROP, index);
        safeSetStringUserProperty(module, property, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the departmental or institutional affiliation of an author in the
     * specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @return the affiliation of the author
     */
    public static String getAuthorAffiliation(ModuleHandle module, int index)
    {
        String property = getAuthorProperty(AUTHOR_AFFILIATION_PROP, index);
        return safeGetStringUserProperty(module, property);
    }


    // ----------------------------------------------------------
    /**
     * Sets the departmental or institutional affiliation of an author in the
     * specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @param value
     *            the affiliation of the author
     */
    public static void setAuthorAffiliation(ModuleHandle module, int index,
            String value)
    {
        String property = getAuthorProperty(AUTHOR_AFFILIATION_PROP, index);
        safeSetStringUserProperty(module, property, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the phone number of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @return the phone number of the author
     */
    public static String getAuthorPhone(ModuleHandle module, int index)
    {
        String property = getAuthorProperty(AUTHOR_PHONE_PROP, index);
        return safeGetStringUserProperty(module, property);
    }


    // ----------------------------------------------------------
    /**
     * Sets the phone number of an author in the specified report template.
     *
     * @param module
     *            the report template handle
     * @param index
     *            the index of the author
     * @param value
     *            the phone number of the author
     */
    public static void setAuthorPhone(ModuleHandle module, int index,
            String value)
    {
        String property = getAuthorProperty(AUTHOR_PHONE_PROP, index);
        safeSetStringUserProperty(module, property, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the user-specified keywords associated with a report template.
     *
     * @param module
     *            the report template handle
     * @return the keywords associated with the template
     */
    public static String getKeywords(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, KEYWORDS_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the user-specified keywords associated with a report template.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the keywords associated with the template
     */
    public static void setKeywords(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, KEYWORDS_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the natural language in which a report template was written. Values
     * for this property should conform to RFC 4646 language identifier tags.
     *
     * @param module
     *            the report template handle
     * @return the language in which the template was written
     */
    public static String getLanguage(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, LANGUAGE_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the natural language in which a report template was written. Values
     * for this property should conform to RFC 4646 language identifier tags.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the language in which the template was written
     */
    public static void setLanguage(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, LANGUAGE_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the copyright notice for a report template.
     *
     * @param module
     *            the report template handle
     * @return the copyright notice for the template
     */
    public static String getCopyright(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, COPYRIGHT_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the copyright notice for a report template.
     *
     * If this is not set, Web-CAT will assume a default of "(c) <current year>
     * <affiliation of first author>" when the template is uploaded.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the copyright notice for the template
     */
    public static void setCopyright(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, COPYRIGHT_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the name of the license under which a report template is
     * distributed.
     *
     * @param module
     *            the report template handle
     * @return the license under which the template is distributed
     */
    public static String getLicense(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, LICENSE_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the name of the license under which a report template is
     * distributed.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the license under which the template is distributed
     */
    public static void setLicense(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, LICENSE_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the website URL containing the text of the license under which a
     * report template is distributed.
     *
     * @param module
     *            the report template handle
     * @return the website URL containing the text of the license
     */
    public static String getLicenseURL(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, LICENSE_URL_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the website URL containing the text of the license under which a
     * report template is distributed.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the website URL containing the text of the license
     */
    public static void setLicenseURL(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, LICENSE_URL_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the identifier of the rendering method that this report is best
     * rendered in.
     *
     * @param module
     *            the report template handle
     * @return the preferred rendering method for this template
     */
    public static String getPreferredRenderer(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, PREFERRED_RENDERER_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the identifier of the rendering method that this report is best
     * rendered in.
     *
     * Examples of valid renderers are "html", "csv", and "pdf".
     *
     * @param module
     *            the report template handle
     * @param value
     *            the preferred rendering method for this template
     */
    public static void setPreferredRenderer(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, PREFERRED_RENDERER_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the unique identifier that was assigned to a report template when it
     * was uploaded to Web-CAT.
     *
     * @param module
     *            the report template handle
     * @return the unique identifier of the report, or null if it has not yet
     *         been uploaded to a Web-CAT server
     */
    public static String getRepositoryId(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, REPOSITORY_ID_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the unique identifier that was assigned to a report template when it
     * was uploaded to Web-CAT.
     *
     * This method should only be called by Web-CAT, never from the report
     * designer.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the unique identifier of the report, or null if it has not yet
     *            been uploaded to a Web-CAT server
     */
    public static void setRepositoryId(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, REPOSITORY_ID_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the string that encodes the version and branch history of a report
     * template, as assigned when it was uploaded to Web-CAT.
     *
     * @param module
     *            the report template handle
     * @return the version identifier of the template, or null if it has not yet
     *         been uploaded to a Web-CAT server
     */
    public static String getRepositoryVersion(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, REPOSITORY_VERSION_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the string that encodes the version and branch history of a report
     * template, as assigned when it was uploaded to Web-CAT.
     *
     * This method should only be called by Web-CAT, never from the report
     * designer.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the version identifier of the template, or null if it has not
     *            yet been uploaded to a Web-CAT server
     */
    public static void setRepositoryVersion(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, REPOSITORY_VERSION_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets a string that specifies the date and time at which a report template
     * was uploaded to Web-CAT.
     *
     * @param module
     *            the report template handle
     * @return the date and time when the template was uploaded, or null if it
     *         has not yet been uploaded to a Web-CAT server
     */
    public static String getRepositoryUploadDate(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, REPOSITORY_UPLOAD_DATE_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets a string that specifies the date and time at which a report template
     * was uploaded to Web-CAT.
     *
     * This method should only be called by Web-CAT, never from the report
     * designer.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the date and time when the template was uploaded, or null if
     *            it has not yet been uploaded to a Web-CAT server
     */
    public static void setRepositoryUploadDate(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, REPOSITORY_UPLOAD_DATE_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the unique identifier of the root of the report template tree to
     * which a template belongs.
     *
     * @param module
     *            the report template handle
     * @return the unique identifier of the root of the template tree, or null
     *         if it has not yet been uploaded to a Web-CAT server
     */
    public static String getRepositoryRootId(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, REPOSITORY_ROOT_ID_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets the unique identifier of the root of the report template tree to
     * which a template belongs.
     *
     * This method should only be called by Web-CAT, never from the report
     * designer.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the unique identifier of the root of the template tree, or
     *            null if it has not yet been uploaded to a Web-CAT server
     */
    public static void setRepositoryRootId(ModuleHandle module, String value)
    {
        safeSetStringUserProperty(module, REPOSITORY_ROOT_ID_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets a string describing the changes that were made between the previous
     * version and the current version of a report template as entered when it
     * was uploaded to Web-CAT.
     *
     * @param module
     *            the report template handle
     * @return the change history for the current version of the template, or
     *         null if it has not yet been uploaded to a Web-CAT server
     */
    public static String getRepositoryChangeHistory(ModuleHandle module)
    {
        return safeGetStringUserProperty(module, REPOSITORY_CHANGE_HISTORY_PROP);
    }


    // ----------------------------------------------------------
    /**
     * Sets a string describing the changes that were made between the previous
     * version and the current version of a report template as entered when it
     * was uploaded to Web-CAT.
     *
     * This method should only be called by Web-CAT, never from the report
     * designer.
     *
     * @param module
     *            the report template handle
     * @param value
     *            the change history for the current version of the template, or
     *            null if it has not yet been uploaded to a Web-CAT server
     */
    public static void setRepositoryChangeHistory(ModuleHandle module,
            String value)
    {
        safeSetStringUserProperty(module, REPOSITORY_CHANGE_HISTORY_PROP, value);
    }


    // ----------------------------------------------------------
    /**
     * Gets the name of an author property with the specified index.
     *
     * @param property
     *            the property to retrieve
     * @param index
     *            the index of the author to retrieve
     * @return the full name of the property
     */
    private static String getAuthorProperty(String property, int index)
    {
        return String.format(property, index + 1);
    }


    // ----------------------------------------------------------
    /**
     * Updates the built-in BIRT author property by setting it to a string
     * containing the concatenation of all of the authors specified in the
     * Web-CAT metadata properties.
     *
     * @param module
     *            the report template handle
     */
    private static void updateBIRTAuthorProperty(ModuleHandle module)
    {
        int count = getAuthorsCount(module);
        StringBuffer buffer = new StringBuffer(128);

        if (count > 0)
        {
            String name = getAuthorName(module, 0);

            if (name != null)
                buffer.append(name);

            boolean comma;

            for (int i = 1; i < count; i++)
            {
                comma = false;

                if (count > 2 && buffer.length() > 0)
                {
                    comma = true;
                    buffer.append(", ");
                }

                if (i == count - 1 && buffer.length() > 0)
                {
                    if (!comma)
                        buffer.append(' ');
                    buffer.append("and ");
                }

                name = getAuthorName(module, i);

                if (name != null)
                    buffer.append(name);
            }
        }

        module.setAuthor(buffer.toString());
    }


    // ----------------------------------------------------------
    /**
     * Gets the value of a string-type user property in a report template,
     * safely falling back if the property does not exist.
     *
     * @param module
     *            the report template handle
     * @param property
     *            the name of the property to retrieve
     * @return the value of the property, or null if it does not exist
     */
    private static String safeGetStringUserProperty(ModuleHandle module,
            String property)
    {
        if (module.getUserPropertyDefnHandle(property) == null)
        {
            return null;
        }
        else
        {
            return module.getStringProperty(property);
        }
    }


    // ----------------------------------------------------------
    /**
     * Sets the value of a string-type user property in a report template,
     * creating it if the property does not exist or clearing it if the new
     * value is null or the empty string.
     *
     * @param module
     *            the report template handle
     * @param property
     *            the name of the property to retrieve
     * @param value
     *            the new value of the property
     */
    private static void safeSetStringUserProperty(ModuleHandle module,
            String property, String value)
    {
        try
        {
            if (module.getUserPropertyDefnHandle(property) == null)
            {
                UserPropertyDefn userProperty = new UserPropertyDefn();
                userProperty.setName(property);
                userProperty.setType(new StringPropertyType());

                module.addUserPropertyDefn(userProperty);
            }

            if (value == null || value.length() == 0)
            {
                module.clearProperty(property);
            }
            else
            {
                module.setStringProperty(property, value);
            }
        }
        catch (UserPropertyException e)
        {
            e.printStackTrace();
        }
        catch (SemanticException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the value of an integer-type user property in a report template,
     * safely falling back if the property does not exist.
     *
     * @param module
     *            the report template handle
     * @param property
     *            the name of the property to retrieve
     * @return the value of the property, or null if it does not exist
     */
    private static Integer safeGetIntUserProperty(ModuleHandle module,
            String property)
    {
        if (module.getUserPropertyDefnHandle(property) == null)
        {
            return null;
        }
        else
        {
            Object value = module.getProperty(property);

            if (value == null)
            {
                return null;
            }
            else
            {
                return (Integer) value;
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Sets the value of an integer-type user property in a report template,
     * creating it if the property does not exist or clearing it if the new
     * value is null.
     *
     * @param module
     *            the report template handle
     * @param property
     *            the name of the property to retrieve
     * @param value
     *            the new value of the property
     */
    private static void safeSetIntUserProperty(ModuleHandle module,
            String property, Integer value)
    {
        try
        {
            if (module.getUserPropertyDefnHandle(property) == null)
            {
                UserPropertyDefn userProperty = new UserPropertyDefn();
                userProperty.setName(property);
                userProperty.setType(new IntegerPropertyType());

                module.addUserPropertyDefn(userProperty);
            }

            if (value == null)
            {
                module.clearProperty(property);
            }
            else
            {
                module.setIntProperty(property, value);
            }
        }
        catch (UserPropertyException e)
        {
            e.printStackTrace();
        }
        catch (SemanticException e)
        {
            e.printStackTrace();
        }
    }


    //~ Static/instance variables .............................................

    private static final String PROP_PREFIX = "webcat.";

    private static final String KEYWORDS_PROP = PROP_PREFIX + "keywords";
    private static final String COPYRIGHT_PROP = PROP_PREFIX + "copyright";
    private static final String AUTHORS_COUNT = PROP_PREFIX + "authors.count";
    private static final String AUTHOR_FIRST_NAME_PROP = PROP_PREFIX
            + "author%d.name.first";
    private static final String AUTHOR_LAST_NAME_PROP = PROP_PREFIX
            + "author%d.name.last";
    private static final String AUTHOR_EMAIL_PROP = PROP_PREFIX
            + "author%d.email";
    private static final String AUTHOR_URL_PROP = PROP_PREFIX + "author%d.url";
    private static final String AUTHOR_AFFILIATION_PROP = PROP_PREFIX
            + "author%d.affiliation";
    private static final String AUTHOR_PHONE_PROP = PROP_PREFIX
            + "author%d.phone";
    private static final String LICENSE_PROP = PROP_PREFIX + "license";
    private static final String LICENSE_URL_PROP = PROP_PREFIX + "license.url";
    private static final String LANGUAGE_PROP = PROP_PREFIX + "language";
    private static final String PREFERRED_RENDERER_PROP = PROP_PREFIX
            + "preferred.renderer";
    private static final String REPOSITORY_ID_PROP = PROP_PREFIX
            + "repository.id";
    private static final String REPOSITORY_VERSION_PROP = PROP_PREFIX
            + "repository.version";
    private static final String REPOSITORY_UPLOAD_DATE_PROP = PROP_PREFIX
            + "repository.upload.date";
    private static final String REPOSITORY_ROOT_ID_PROP = PROP_PREFIX
            + "repository.root.id";
    private static final String REPOSITORY_CHANGE_HISTORY_PROP = PROP_PREFIX
            + "repository.history";
}
