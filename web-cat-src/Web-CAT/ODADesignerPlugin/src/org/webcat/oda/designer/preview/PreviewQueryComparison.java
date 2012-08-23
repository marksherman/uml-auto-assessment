/*==========================================================================*\
 |  $Id: PreviewQueryComparison.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.preview;

import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.contentassist.ContentAssistManager;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewQueryComparison.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public enum PreviewQueryComparison
{
    IS_EQUAL_TO("==", true), IS_NOT_EQUAL_TO("!=", true), IS_LESS_THAN("<", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            true), IS_LESS_THAN_OR_EQUAL_TO("<=", true), IS_GREATER_THAN(">", //$NON-NLS-1$ //$NON-NLS-2$
            true), IS_GREATER_THAN_OR_EQUAL_TO(">=", true), IS_BETWEEN( //$NON-NLS-1$
            Messages.QUERY_COMPARISON_BETWEEN, false), IS_NOT_BETWEEN(Messages.QUERY_COMPARISON_NOT_BETWEEN, false), IS_LIKE(
            Messages.QUERY_COMPARISON_LIKE, true), IS_NOT_LIKE(Messages.QUERY_COMPARISON_NOT_LIKE, true), IS_ONE_OF(
            Messages.QUERY_COMPARISON_ONE_OF, false), IS_NOT_ONE_OF(Messages.QUERY_COMPARISON_NOT_ONE_OF, false);

    PreviewQueryComparison(String representation, boolean supportsKeyPaths)
    {
        this.representation = representation;
        this.supportsKeyPaths = supportsKeyPaths;
    }


    public String representation()
    {
        return representation;
    }


    public boolean supportsKeyPaths()
    {
        return supportsKeyPaths;
    }


    public static final PreviewQueryComparison[] BOOLEAN_COMPARISONS = {
            IS_EQUAL_TO, IS_NOT_EQUAL_TO };

    public static final PreviewQueryComparison[] NUMERIC_COMPARISONS = {
            IS_EQUAL_TO, IS_NOT_EQUAL_TO, IS_LESS_THAN,
            IS_LESS_THAN_OR_EQUAL_TO, IS_GREATER_THAN,
            IS_GREATER_THAN_OR_EQUAL_TO, IS_BETWEEN, IS_NOT_BETWEEN, IS_ONE_OF,
            IS_NOT_ONE_OF };

    public static final PreviewQueryComparison[] STRING_COMPARISONS = {
            IS_EQUAL_TO, IS_NOT_EQUAL_TO, IS_BETWEEN, IS_NOT_BETWEEN, IS_LIKE,
            IS_NOT_LIKE, IS_ONE_OF, IS_NOT_ONE_OF };

    public static final PreviewQueryComparison[] TIMESTAMP_COMPARISONS = {
            IS_EQUAL_TO, IS_NOT_EQUAL_TO, IS_LESS_THAN,
            IS_LESS_THAN_OR_EQUAL_TO, IS_GREATER_THAN,
            IS_GREATER_THAN_OR_EQUAL_TO, IS_BETWEEN, IS_NOT_BETWEEN };

    public static final PreviewQueryComparison[] OBJECT_COMPARISONS = {
            IS_EQUAL_TO, IS_NOT_EQUAL_TO, IS_ONE_OF, IS_NOT_ONE_OF };


    public static PreviewQueryComparison[] comparisonsForType(String type)
    {
        if (type.equals("boolean") || type.equals("Boolean")) //$NON-NLS-1$ //$NON-NLS-2$
        {
            return BOOLEAN_COMPARISONS;
        }
        else if (type.equals("Number") || type.equals("Integer") //$NON-NLS-1$ //$NON-NLS-2$
                || type.equals("int") || type.equals("Float") //$NON-NLS-1$ //$NON-NLS-2$
                || type.equals("float") || type.equals("Double") //$NON-NLS-1$ //$NON-NLS-2$
                || type.equals("double")) //$NON-NLS-1$
        {
            return NUMERIC_COMPARISONS;
        }
        else if (type.equals("string") || type.equals("String")) //$NON-NLS-1$ //$NON-NLS-2$
        {
            return STRING_COMPARISONS;
        }
        else if (type.equals("Date") || type.equals("NSTimestamp")) //$NON-NLS-1$ //$NON-NLS-2$
        {
            return TIMESTAMP_COMPARISONS;
        }
        else
        {
            ContentAssistManager cam = DesignerActivator.getDefault()
                    .getContentAssistManager();

            if (cam.isEntity(type))
            {
                return OBJECT_COMPARISONS;
            }
            else
            {
                return null;
            }
        }
    }


    public static PreviewQueryComparison comparisonWithRepresentation(String rep)
    {
        if ("==".equals(rep)) //$NON-NLS-1$
            return IS_EQUAL_TO;
        else if ("!=".equals(rep)) //$NON-NLS-1$
            return IS_NOT_EQUAL_TO;
        else if ("<".equals(rep)) //$NON-NLS-1$
            return IS_LESS_THAN;
        else if ("<=".equals(rep)) //$NON-NLS-1$
            return IS_LESS_THAN_OR_EQUAL_TO;
        else if (">".equals(rep)) //$NON-NLS-1$
            return IS_GREATER_THAN;
        else if (">=".equals(rep)) //$NON-NLS-1$
            return IS_GREATER_THAN_OR_EQUAL_TO;
        else if (Messages.QUERY_COMPARISON_BETWEEN.equals(rep))
            return IS_BETWEEN;
        else if (Messages.QUERY_COMPARISON_NOT_BETWEEN.equals(rep))
            return IS_NOT_BETWEEN;
        else if (Messages.QUERY_COMPARISON_LIKE.equals(rep))
            return IS_LIKE;
        else if (Messages.QUERY_COMPARISON_NOT_LIKE.equals(rep))
            return IS_NOT_LIKE;
        else if (Messages.QUERY_COMPARISON_ONE_OF.equals(rep))
            return IS_ONE_OF;
        else if (Messages.QUERY_COMPARISON_NOT_ONE_OF.equals(rep))
            return IS_NOT_ONE_OF;
        else
            return null;
    }


    private String representation;

    private boolean supportsKeyPaths;
}
