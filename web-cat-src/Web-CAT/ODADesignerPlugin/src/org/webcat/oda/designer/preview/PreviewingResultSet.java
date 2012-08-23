/*==========================================================================*\
 |  $Id: PreviewingResultSet.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.webcat.oda.commons.IWebCATResultSet;
import org.webcat.oda.commons.WebCATDataException;
import org.webcat.oda.designer.DesignerActivator;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewingResultSet.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class PreviewingResultSet implements IWebCATResultSet
{
    // === Methods ============================================================

    // ------------------------------------------------------------------------
    /**
     * Creates a new instance of the PreviewingResultSet class.
     *
     * @param uuid
     * @param maxRecords
     */
    public PreviewingResultSet(String uuid, int maxRecords)
    {
        this.uuid = uuid;
        this.maxRecords = maxRecords;

        currentRow = 0;
    }


    private PreviewingResultCache previewCache()
    {
        if (previewCache == null)
        {
            // Try again to get the previewing cache. This might fail and
            // return null if the user hasn't set the server URL in the
            // preferences yet.

            previewCache = DesignerActivator.getDefault().getPreviewCache();
        }

        return previewCache;
    }


    public void prepare(String entityType, String[] expressions)
            throws WebCATDataException
    {
        this.entityType = entityType;
        this.expressions = expressions;
    }


    public void execute() throws WebCATDataException
    {
        PreviewingResultCache cache = previewCache();

        if (cache != null)
        {
            cache.ensureResultsAreCached(uuid, entityType, expressions, true);

            recordCount = cache.getRowCount(uuid);
        }
        else
        {
            recordCount = 0;
        }

        currentRow = -1;
    }


    public void close() throws WebCATDataException
    {
        // Do nothing.
    }


    public boolean moveToNextRow() throws WebCATDataException
    {
        if (currentRow < rowCount() - 1)
        {
            currentRow++;
            return true;
        }
        else
        {
            return false;
        }
    }


    public int currentRow() throws WebCATDataException
    {
        return currentRow;
    }


    public Object currentRowValueAtIndex(int columnIndex)
    {
        PreviewingResultCache cache = previewCache();

        if (cache != null)
        {
            cache.ensureResultsAreCached(uuid, entityType, expressions, true);

            return cache.getValue(uuid, currentRow, columnIndex);
        }
        else
        {
            return null;
        }
    }


    public boolean booleanValueAtIndex(int columnIndex)
            throws WebCATDataException
    {
        Object value = currentRowValueAtIndex(columnIndex);

        if (value == null)
        {
            wasValueNull = true;
            return false;
        }
        else
        {
            wasValueNull = false;

            if (value instanceof Boolean)
                return ((Boolean) value).booleanValue();
            else
                return Boolean.parseBoolean(value.toString());
        }
    }


    public Timestamp timestampValueAtIndex(int columnIndex)
            throws WebCATDataException
    {
        Object value = currentRowValueAtIndex(columnIndex);

        if (value == null)
        {
            wasValueNull = true;
            return new Timestamp(0);
        }
        else
        {
            wasValueNull = false;

            if (value instanceof Timestamp)
            {
                return (Timestamp) value;
            }
            else
            {
                try
                {
                    return new Timestamp(Long.parseLong(value.toString()));
                }
                catch (NumberFormatException e)
                {
                    throw formatException(columnIndex, value, "Timestamp");
                }
            }
        }
    }


    public BigDecimal decimalValueAtIndex(int columnIndex)
            throws WebCATDataException
    {
        Object value = currentRowValueAtIndex(columnIndex);

        if (value == null)
        {
            wasValueNull = true;
            return BigDecimal.ZERO;
        }
        else
        {
            wasValueNull = false;

            if (value instanceof BigDecimal)
            {
                return (BigDecimal) value;
            }
            else
            {
                try
                {
                    return new BigDecimal(value.toString());
                }
                catch (NumberFormatException e)
                {
                    throw formatException(columnIndex, value, "Decimal");
                }
            }
        }
    }


    public double doubleValueAtIndex(int columnIndex)
            throws WebCATDataException
    {
        Object value = currentRowValueAtIndex(columnIndex);

        if (value == null)
        {
            wasValueNull = true;
            return 0;
        }
        else
        {
            wasValueNull = false;

            if (value instanceof Number)
            {
                return ((Number) value).doubleValue();
            }
            else
            {
                try
                {
                    return Double.parseDouble(value.toString());
                }
                catch (NumberFormatException e)
                {
                    throw formatException(columnIndex, value, "Float");
                }
            }
        }
    }


    public int intValueAtIndex(int columnIndex) throws WebCATDataException
    {
        Object value = currentRowValueAtIndex(columnIndex);

        if (value == null)
        {
            wasValueNull = true;
            return 0;
        }
        else
        {
            wasValueNull = false;

            if (value instanceof Number)
                return ((Number) value).intValue();
            else
            {
                // Try to parse the string as an integer. If this fails (for
                // example, if it's a floating point value), then try it as a
                // double and take the floor.

                try
                {
                    return Integer.parseInt(value.toString());
                }
                catch (NumberFormatException e)
                {
                    try
                    {
                        return (int) Double.parseDouble(value.toString());
                    }
                    catch (IllegalArgumentException e2)
                    {
                        throw formatException(columnIndex, value, "Integer");
                    }
                }
            }
        }
    }


    public String stringValueAtIndex(int columnIndex)
            throws WebCATDataException
    {
        Object value = currentRowValueAtIndex(columnIndex);

        if (value == null)
        {
            wasValueNull = true;
            return "";
        }
        else
        {
            wasValueNull = false;

            if (value instanceof String)
                return (String) value;
            else
                return value.toString();
        }
    }


    public boolean wasValueNull() throws WebCATDataException
    {
        return wasValueNull;
    }


    private int rowCount()
    {
        return Math.min(recordCount, maxRecords);
    }


    private IllegalArgumentException formatException(int columnIndex,
            Object value, String destinationType)
    {
        return new IllegalArgumentException(
                "The result (\"" + value.toString() +
                "\") of the expression [ " + expressions[columnIndex] +
                " ] could not be converted " +
                "to type " + destinationType + ".");
    }


    // === Instance Variables =================================================

    private String uuid;

    private int maxRecords;

    private int recordCount;

    private int currentRow;

    private String entityType;

    private String[] expressions;

    private boolean wasValueNull;

    private PreviewingResultCache previewCache;
}
