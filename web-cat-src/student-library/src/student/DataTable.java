/*==========================================================================*\
 |  $Id: DataTable.java,v 1.3 2012/02/29 03:58:16 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

//-------------------------------------------------------------------------
/**
 * A database table or spreadsheet-like abstraction that consists of
 * rows, each of which has an unordered collection of columns.
 * Each row has a row number which, by default, is
 * the primary key of the table.
 * Each row's columns contain string values. Each row can
 * be named as desired by the user.  The default column names
 * are "0", "1", "2", ....
 *
 * <p>A primary key for a table is a column where each value
 * uniquely determines the row.  For example, if you had a table
 * of grades, the student's name would be the primary key column.
 * The primary key can be set to whichever row is desired, but
 * the column must uniquely identify each row in the table.</p>
 *
 * <p>The purpose of this class is to actually pull data from the
 * internet for use in the class, however, it also can
 * be used as a table, where one adds, removes, and updates
 * data on one's own.  The table updates can be saved when
 * desired.</p>
 *
 * <p>The following invariants should be maintained for any
 * implementation:</p>
 * <ul>
 * <li><p>At the end of each method, the getRow* methods can reference
 * a row by its primary key on the next instruction.</p></li>
 * <li><p>The table will have data from one data source at a time.  In
 * other words, setting the dataset to another source will wipe out
 * the previously held data in the DataTable.</p></li>
 * </ul>
 *
 * @author Matthew Thornton
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2012/02/29 03:58:16 $
 */
public interface DataTable
    extends Iterable<DataTable.Row>
{
    // ----------------------------------------------------------
    /**
     * This interface defines the model used for each row in the table.
     */
    public static interface Row
        extends Map<String, String>
    {
        // Simply inherits everything from Map
    };


    // ----------------------------------------------------------
    /**
     * This is a default concrete implementation of the Row interface.
     */
    public static class HashedRow
        extends HashMap<String, String>
        implements Row
    {
        // Simply inherits everything from Map
        private static final long serialVersionUID = -3859136970924319061L;
    };


    // ----------------------------------------------------------
    /**
     * This method returns the number of rows in the data table.
     * @return the number of rows in the data table.
     */
    public int rowCount();


    // ----------------------------------------------------------
    /**
     * This method returns the number of columns in the data table.
     * @return the number of columns in the data table.
     */
    public int colCount();


    // ----------------------------------------------------------
    /**
     * This method returns the String value in the table at a specified
     * row/column location.
     *
     * @param row     index of row to retrieve (starting at zero)
     * @param column  the name of the desired column
     *
     * @return the value in that position as a String object
     */
    public String getCell(int row, String column);


    // ----------------------------------------------------------
    /**
     * This method returns the String value in the table at a specified
     * row/column location.
     *
     * @param row     index of row to retrieve (starting at zero)
     * @param column  index of column to retrieve (starting at zero)
     *
     * @return the value in that position as a String object
     */
    public String getCell(int row, int column);


    // ----------------------------------------------------------
    /**
     * This method returns the String value in the table at a specified
     * row/column location.
     *
     * @param row     primary key of row
     * @param column  the name of the desired column
     *
     * @return the value in that position as a String object
     */
    public String getCell(String row, String column);


    // ----------------------------------------------------------
    /**
     * This method returns the String value in the table at a specified
     * row/column location.
     *
     * @param row     primary key of row
     * @param column  index of column to retrieve (starting at zero)
     *
     * @return the value in that position as a String object
     */
    public String getCell(String row, int column);


    // ----------------------------------------------------------
    /**
     * This method returns the int value in the table at a specified
     * row/column location.
     *
     * @param row     index of row to retrieve (starting at zero)
     * @param column  the name of the desired column
     *
     * @return the value in that position as an int
     * @throws NumberFormatException if the cell does not contain an int
     * value.
     */
    public int getIntCell(int row, String column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method returns the int value in the table at a specified
     * row/column location.
     *
     * @param row     index of row to retrieve (starting at zero)
     * @param column  index of column to retrieve (starting at zero)
     *
     * @return the value in that position as an int
     * @throws NumberFormatException if the cell does not contain an int
     * value.
     */
    public int getIntCell(int row, int column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method returns the int value in the table at a specified
     * row/column location.
     *
     * @param row     primary key of row
     * @param column  the name of the desired column
     *
     * @return the value in that position as an int
     * @throws NumberFormatException if the cell does not contain an int
     * value.
     */
    public int getIntCell(String row, String column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method returns the int value in the table at a specified
     * row/column location.
     *
     * @param row     primary key of row
     * @param column  index of column to retrieve (starting at zero)
     *
     * @return the value in that position as an int
     * @throws NumberFormatException if the cell does not contain an int
     * value.
     */
    public int getIntCell(String row, int column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method returns the double value in the table at a specified
     * row/column location.
     *
     * @param row     index of row to retrieve (starting at zero)
     * @param column  the name of the desired column
     *
     * @return the value in that position as an int
     * @throws NumberFormatException if the cell does not contain a double
     * value.
     */
    public double getDoubleCell(int row, String column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method returns the double value in the table at a specified
     * row/column location.
     *
     * @param row     index of row to retrieve (starting at zero)
     * @param column  index of column to retrieve (starting at zero)
     *
     * @return the value in that position as an int
     * @throws NumberFormatException if the cell does not contain a double
     * value.
     */
    public double getDoubleCell(int row, int column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method returns the double value in the table at a specified
     * row/column location.
     *
     * @param row     primary key of row
     * @param column  the name of the desired column
     *
     * @return the value in that position as an int
     * @throws NumberFormatException if the cell does not contain a double
     * value.
     */
    public double getDoubleCell(String row, String column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method returns the double value in the table at a specified
     * row/column location.
     *
     * @param row     primary key of row
     * @param column  index of column to retrieve (starting at zero)
     *
     * @return the value in that position as an int
     * @throws NumberFormatException if the cell does not contain a double
     * value.
     */
    public double getDoubleCell(String row, int column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method returns a map that represents the desired
     * row (indicated by the primary key).
     *
     * @param row index of row to retrieve (starting from zero)
     * @return the Map representing the desired row.
     */
    public Row getRow(int row);


    // ----------------------------------------------------------
    /**
     * This method returns a Map that represents the desired
     * row.  The map is a map of column names to values.
     *
     * @param row primary key of row to retrieve
     * @return the Map representing the desired row.
     */
    public Row getRow(String row);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as a String.
     *
     * @param row	  index of row to change (starting from zero)
     * @param column  name of the column to change
     * @param value	  the value that is to replace the current value
     */
    public void setCell(int row, String column, String value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as an int.
     *
     * @param row     index of row to change (starting from zero)
     * @param column  name of the column to change
     * @param value   the value that is to replace the current value
     */
    public void setCell(int row, String column, int value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as a double.
     *
     * @param row     index of row to change (starting from zero)
     * @param column  name of the column to change
     * @param value   the value that is to replace the current value
     */
    public void setCell(int row, String column, double value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as a String.
     *
     * @param row     index of row to change (starting from zero)
     * @param column  index of column to change (starting from zero)
     * @param value   the value that is to replace the current value
     */
    public void setCell(int row, int column, String value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as an int.
     *
     * @param row     index of row to change (starting from zero)
     * @param column  index of column to change (starting from zero)
     * @param value   the value that is to replace the current value
     */
    public void setCell(int row, int column, int value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as a double.
     *
     * @param row     index of row to change (starting from zero)
     * @param column  index of column to change (starting from zero)
     * @param value   the value that is to replace the current value
     */
    public void setCell(int row, int column, double value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as a String.
     *
     * @param row     primary key of the row to change
     * @param column  name of the column to change
     * @param value   the value that is to replace the current value
     */
    public void setCell(String row, String column, String value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as an int.
     *
     * @param row     primary key of the row to change
     * @param column  name of the column to change
     * @param value   the value that is to replace the current value
     */
    public void setCell(String row, String column, int value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as a double.
     *
     * @param row     primary key of the row to change
     * @param column  name of the column to change
     * @param value   the value that is to replace the current value
     */
    public void setCell(String row, String column, double value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as a String.
     *
     * @param row     primary key of the row to change
     * @param column  index of column to change (starting from zero)
     * @param value   the value that is to replace the current value
     */
    public void setCell(String row, int column, String value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as an int.
     *
     * @param row     primary key of the row to change
     * @param column  index of column to change (starting from zero)
     * @param value   the value that is to replace the current value
     */
    public void setCell(String row, int column, int value);


    // ----------------------------------------------------------
    /**
     * This method allows you to update a desired row and column with
     * new data as a double.
     *
     * @param row     primary key of the row to change
     * @param column  index of column to change (starting from zero)
     * @param value   the value that is to replace the current value
     */
    public void setCell(String row, int column, double value);


    // ----------------------------------------------------------
    /**
     * This method removes the row identified by row number.  The
     * method permanently deletes the row from the table and when the table
     * is queried the row will not be accessible either.  As with in others,
     * this is independent of the primary key and just the arbitrary
     * ordering of the table.  The method also renumbers the rows so that
     * if the primary key remains the default, then the row numbers will
     * be consistent between using the integer-based row numbers and their
     * String equivalents.
     *
     * @param row	table row to be deleted.
     * @return the Map representing the Row that was removed from the table.
     */
    public Row removeRow(int row);


    // ----------------------------------------------------------
    /**
     * This method removes the row (to be determined by the key).  The
     * method permanently deletes the row from the table and when the table
     * is queried the row will not be accessible either. The method also renumbers
     * the rows so that if the primary
     * key remains the default, then the row numbers will be consistent between
     * using the integer-based row numbers and their String equivalents.
     *
     * @param row	table row to be deleted.
     * @return the Map representing the Row that was removed from the table.
     */
    public Row removeRow(String row);


    // ----------------------------------------------------------
    /**
     * This method adds a column to the table.  For each row in the table,
     * the column's value will be null.
     *
     * @param columnName	the name of the column to be added.
     */
    public void addColumn(String columnName);


    // ----------------------------------------------------------
    /**
     * This method adds a column to the table, setting this column to the
     * specified default value in every row.
     *
     * @param columnName the name of the column to be added.
     * @param defaultValue the default value to use for the column.
     */
    public void addColumn(String columnName, String defaultValue);


    // ----------------------------------------------------------
    /**
     * This method adds a row to the table.  For each column, the Map
     * should have a value for the column name.  If not, that column
     * value is null.  The method also renumbers
     * the rows so that if the primary
     * key remains the default, then the row numbers will be consistent
     * between using the integer-based row numbers and their String
     * equivalents.
     * @param row The row to add
     */
    public void addRow(Row row);


    // ----------------------------------------------------------
    /**
     * This method sets the column name that acts as the primary key for the
     * table.  If the column name is not in the table, then the primary key
     * stays as the current primary key.  The method also renumbers the
     * rows so that if the primary
     * key remains the default, then the row numbers will be consistent
     * between using the integer-based row numbers and their String
     * equivalents.
     *
     * @param key	the column name to be set as the primary key.
     */
    public void setPrimaryKey(String key);


    // ----------------------------------------------------------
    /**
     * This method returns the column name that acts as the primary key for
     * the table.
     *
     * @return the column name that acts as the primary key.  The default is
     * the row number.
     */
    public String getPrimaryKey();


    // ----------------------------------------------------------
    /**
     * This method returns a list of the names of all of the
     * columns of the data table.  The column names are returned
     * as an array list with each position representing one of the
     * columns in the table.
     *
     * @return an array list with a reporting of all of the column names
     * in the table.
     */
    public List<String> getColumnNames();


    // ----------------------------------------------------------
    /**
     * This method sets an arbitrary name for each of the columns
     * in the table.
     *
     * @param columnNames names of columns to be set in the table.
     */
    public void setColumnNames(List<String> columnNames);


    // ----------------------------------------------------------
    /**
     * This method sets an arbitrary name for each of the columns
     * in the table.
     *
     * @param columnNames names of columns to be set in the table.
     */
    public void setColumnNames(String... columnNames);


    // ----------------------------------------------------------
    /**
     * This method replaces each column name in the table with a
     * matching key in columnMap with the value of the mapping.
     *
     * @param columnMap names of columns to be replaced with the
     * associated value to replace it with.
     */
    public void remapColumnNames(Map<String, String> columnMap);


    // ----------------------------------------------------------
    /**
     * This method returns a representation of the Data Table.
     * The representation is an array list where each position represents
     * a row.  Each row is a map with an unordered collection of columns.
     *
     * @return an array list representing the data table.
     */
    public List<Row> getAllRows();


    // ----------------------------------------------------------
    /**
     * This method copies an existing DataTable's rows into the
     * DataTable.  The primary key remains what it was before
     * the copy and the incoming DataTable completely replaces the
     * current DataTable.
     *
     * @param dataSet a DataTable object to copy into the current DataTable.
     */
    public void copyRowsFrom(DataTable dataSet);


    // ----------------------------------------------------------
    /**
     * This method counts the number of rows in the specified column
     * that have the value given by the value parameter.
     *
     * @param column	the desired column to count on.
     * @param value	the desired value to count.
     * @return the count of the number of rows that have the value given
     * by the value parameter.
     */
    public int count(String column, String value);


    // ----------------------------------------------------------
    /**
     * This method computes the average of a column that has numeric
     * values.  Throws an exception if a non-number is found in the
     * column.
     *
     * @param column the column to compute the average of.
     * @return the average of the numerical values in the column.
     * @throws NumberFormatException if the column has a string that cannot
     * be parsed as a number.
     */
    public double avg(String column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method computes the maximum value of a column that has
     * numeric values.  Throws an exception if a non-number is found in the
     * column.
     *
     * @param column the column to compute the max number of.
     * @return the max of the numerical values in the column.
     * @throws NumberFormatException if the column has a string that cannot
     * be parsed as a number.
     */
    public double max(String column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method computes the minimum value of a column that has
     * numeric values.  Throws an exception if a non-number is found in the
     * column.
     *
     * @param column the column to compute the min number of.
     * @return the min of the numerical values in the column.
     * @throws NumberFormatException if the column has a string that cannot
     * be parsed as a number.
     */
    public double min(String column)
        throws NumberFormatException;


    // ----------------------------------------------------------
    /**
     * This method saves the data table that has been generated/manipulated
     * in the format desired for the implementation of the interface.
     *
     * @param filename	the path to the file that is to be written.
     */
    public void save(String filename);
}
