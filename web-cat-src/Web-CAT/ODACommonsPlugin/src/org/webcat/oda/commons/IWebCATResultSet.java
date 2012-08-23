/*==========================================================================*\
 |  $Id: IWebCATResultSet.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
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

import java.math.BigDecimal;
import java.sql.Timestamp;

//-------------------------------------------------------------------------
/**
 * This interface should be implemented by systems that want to provide data to
 * the reporting engine.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: IWebCATResultSet.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
 */
public interface IWebCATResultSet
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Prepares the Web-CAT result set object with the entity type name and
     * column expressions specified.
     *
     * @param entityType
     *            the entity type used by this result set
     * @param expressions
     *            an array of expressions that represent the columns in the BIRT
     *            result set
     * @throws WebCATDataException
     */
    void prepare(String entityType, String[] expressions)
            throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Executes the query to generate this result set.
     *
     * @throws WebCATDataException
     */
    void execute() throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Called when the result set is no longer needed by the reporting engine.
     *
     * @throws WebCATDataException
     */
    void close() throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Returns the index of the current row in the result set.
     *
     * @return the 0-based index of the current row in the result set.
     * @throws WebCATDataException
     */
    int currentRow() throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Moves the cursor to the next row in the result set, if possible.
     *
     * @return true if the cursor was able to be moved; false if it was already
     *         at the end of the result set.
     * @throws WebCATDataException
     */
    boolean moveToNextRow() throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Gets the string value of the specified column at the current row in the
     * result set.
     *
     * @param columnIndex
     *            the 0-based index of the column whose value should be returned
     *
     * @return a String containing the value of the column
     * @throws WebCATDataException
     */
    String stringValueAtIndex(int columnIndex) throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Gets the integer value of the specified column at the current row in the
     * result set.
     *
     * @param columnIndex
     *            the 0-based index of the column whose value should be returned
     *
     * @return an int containing the value of the column
     * @throws WebCATDataException
     */
    int intValueAtIndex(int columnIndex) throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Gets the boolean value of the specified column at the current row in the
     * result set.
     *
     * @param columnIndex
     *            the 0-based index of the column whose value should be returned
     *
     * @return the boolean value of the column
     * @throws WebCATDataException
     */
    boolean booleanValueAtIndex(int columnIndex) throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Gets the double value of the specified column at the current row in the
     * result set.
     *
     * @param columnIndex
     *            the 0-based index of the column whose value should be returned
     *
     * @return the double value of the column
     * @throws WebCATDataException
     */
    double doubleValueAtIndex(int columnIndex) throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Gets the decimal value of the specified column at the current row in the
     * result set.
     *
     * @param columnIndex
     *            the 0-based index of the column whose value should be returned
     *
     * @return a BigDecimal containing the value of the column
     * @throws WebCATDataException
     */
    BigDecimal decimalValueAtIndex(int columnIndex) throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Gets the date/time value of the specified column at the current row in
     * the result set.
     *
     * @param columnIndex
     *            the 0-based index of the column whose value should be returned
     *
     * @return a Timestamp containing the value of the column
     * @throws WebCATDataException
     */
    Timestamp timestampValueAtIndex(int columnIndex) throws WebCATDataException;


    // ----------------------------------------------------------
    /**
     * Returns a value indicating whether the last call to one of the
     * *ValueAtIndex methods resulted in a null value.
     *
     * @return true if the last value queried was null; otherwise, false.
     * @throws WebCATDataException
     */
    boolean wasValueNull() throws WebCATDataException;
}
