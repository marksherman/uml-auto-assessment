/*==========================================================================*\
 |  $Id: Xml.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter.internal;

//--------------------------------------------------------------------------
/**
 * Constants denoting the names of elements and attributes used during XML
 * processing.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class Xml
{
    // ----------------------------------------------------------
    public static class Attributes
    {
        public static final String CHOICE = "choice";
        public static final String HIDDEN = "hidden";
        public static final String ID = "id";
        public static final String URI = "uri";
        public static final String HREF = "href";
        public static final String NAME = "name";
        public static final String PATTERN = "pattern";
        public static final String VALUE = "value";
    }


    // ----------------------------------------------------------
    public static class Elements
    {
        public static final String SUBMISSION_TARGETS = "submission-targets";
        public static final String ASSIGNMENT = "assignment";
        public static final String ASSIGNMENT_GROUP = "assignment-group";
        public static final String IMPORT_GROUP = "import-group";
        public static final String TRANSPORT = "transport";
        public static final String PACKAGER = "packager";
        public static final String PARAM = "param";
        public static final String FILE_PARAM = "file-param";
        public static final String INCLUDE = "include";
        public static final String EXCLUDE = "exclude";
        public static final String REQUIRED = "required";
        public static final String FILTER_AMBIGUITY = "filter-ambiguity";
    }
}
