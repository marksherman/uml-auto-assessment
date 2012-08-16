/*==========================================================================*\
 |  $Id: BatchFeedbackSection.java,v 1.2 2010/09/27 00:15:32 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
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
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSComparator.ComparisonException;

// -------------------------------------------------------------------------
/**
 * TODO: place a real description here.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:15:32 $
 */
public class BatchFeedbackSection
    extends _BatchFeedbackSection
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new BatchFeedbackSection object.
     */
    public BatchFeedbackSection()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static NSArray<BatchFeedbackSection> sortedSectionsUsingOrder(
            NSArray<BatchFeedbackSection> array) throws ComparisonException
    {
        return array.sortedArrayUsingComparator(orderComparator());
    }


    // ----------------------------------------------------------
    public static void sortSectionsUsingOrder(
            NSMutableArray<BatchFeedbackSection> array)
    throws ComparisonException
    {
        array.sortUsingComparator(orderComparator());
    }


    // ----------------------------------------------------------
    public static NSComparator orderComparator()
    {
        if (orderComparator == null)
        {
            orderComparator = new NSComparator() {
                @Override
                public int compare(Object lhs_, Object rhs_)
                throws ComparisonException
                {
                    BatchFeedbackSection lhs = (BatchFeedbackSection) lhs_;
                    BatchFeedbackSection rhs = (BatchFeedbackSection) rhs_;

                    int locationCmp = lhs.location().compareTo(rhs.location());

                    if (locationCmp != 0)
                    {
                        return locationCmp;
                    }
                    else
                    {
                        int orderCmp =
                            new Integer(lhs.order()).compareTo(rhs.order());

                        return orderCmp;
                    }
                }
            };
        }

        return orderComparator;
    }


    // ----------------------------------------------------------
    public File feedbackFile()
    {
        return new File(batchResult().resultDirName(), fileName());
    }


    private static NSComparator orderComparator = null;
}
