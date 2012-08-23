/*==========================================================================*\
 |  $Id: IRepositoryIdProvider.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

package org.webcat.reporter;

// ------------------------------------------------------------------------
/**
 * <p>
 * Objects that implement this interface are passed into the
 * updateRepositoryDataAndFinalize method of the ReportTemplate class so that
 * it can generate repository IDs for the reports that are uploaded to Web-CAT.
 * </p><p>
 * Since our repository IDs are based on direct action URLs, which require a
 * WOContext in order to be generated, this class isolates the ReportTemplate
 * entity from this implementation detail.
 * </p>
 *
 * @author Tony Allevato
 * @version $Id: IRepositoryIdProvider.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public interface IRepositoryIdProvider
{
    // ----------------------------------------------------------
    /**
     * Gets the unique repository identifier for the specified report template.
     *
     * @param template the report template
     * @return the unique repository identifier of the template
     */
    String idForReportTemplate(ReportTemplate template);
}
