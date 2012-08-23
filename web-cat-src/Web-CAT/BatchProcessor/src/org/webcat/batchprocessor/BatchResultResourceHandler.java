/*==========================================================================*\
 |  $Id: BatchResultResourceHandler.java,v 1.4 2011/03/07 16:11:14 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010 Virginia Tech
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

package org.webcat.batchprocessor;

import java.io.File;
import org.webcat.core.EntityResourceHandler;

//-------------------------------------------------------------------------
/**
 * The Web-CAT entity resource handler for accessing resources associated with
 * BatchResult entities through direct URLs.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: aallowat $
 * @version $Revision: 1.4 $, $Date: 2011/03/07 16:11:14 $
 */
public class BatchResultResourceHandler
    extends EntityResourceHandler<BatchResult>
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public File pathForResource(BatchResult object, String relativePath)
    {
        if (relativePath == null)
        {
            return object.resultDir();
        }
        else
        {
            return new File(object.resultDir(), relativePath);
        }
    }


    // TODO add requirements for log-in and user permissions
}
