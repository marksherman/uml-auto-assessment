/*==========================================================================*\
 |  $Id: IWebCATResultSetProvider.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
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

//-------------------------------------------------------------------------
/**
 * This interface should be implemented by systems that want to provide data to
 * the reporting engine. Since a report can contain multiple data sets, each
 * data set is identified by a unique identifier, which is assigned when the
 * report is uploaded to Web-CAT (a placeholder is used until then for
 * previewing purposes). This interface permits access to the data behind those
 * data sets by selecting them with their unique identifier.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: IWebCATResultSetProvider.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
 */
public interface IWebCATResultSetProvider
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Returns the result set for the data set with the specified unique ID.
     *
     * @param id
     *            the unique identifier of the data set whose results should be
     *            returned
     *
     * @return an object that implements the IWebCATResultSet interface, which
     *         provides data to the reporting engine
     */
    public IWebCATResultSet resultSetWithId(String id);
}
