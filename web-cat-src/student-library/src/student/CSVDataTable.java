/*==========================================================================*\
 |  $Id: CSVDataTable.java,v 1.4 2012/02/29 03:58:16 stedwar2 Exp $
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

import com.Ostermiller.util.ExcelCSVParser;
import com.Ostermiller.util.ExcelCSVPrinter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//-------------------------------------------------------------------------
/**
 *  CSVDataTable is an implementation of DataTable
 *  that allows the user to interact with CSV Files.
 *  The CSVDataTable class loads a CSV file into
 *  a DataTable format that can be maniupulated using
 *  the methods of the DataTable class.
 *
 *
 *  @author Matthew Thornton
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.4 $, $Date: 2012/02/29 03:58:16 $
 */
public class CSVDataTable
    implements DataTable
{
    //~ Instance/static variables .............................................

    private List<Row> tableRows;
    private Map<String, Row> keyMappings;
    private List<String> columnNames;
    private String primaryKey;
    private char delimiter;
    private String url;

    private static final String ROW_INDEX_MARKER = "";
    private static final String COL_INDEX_PREFIX = "_C";


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * This constructor creates an empty DataTable with no columns.
     * The key is set to the empty string.
     */
    public CSVDataTable()
    {
        tableRows = new ArrayList<Row>();
        columnNames = new ArrayList<String>();
        keyMappings = new HashMap<String, Row>();
        delimiter = ',';
    }


    // ----------------------------------------------------------
    /**
     * Create a new data table using CSV data on a website.  The URL
     * parameter is a link to a data file stored on the web.  All data rows
     * will be loaded (i.e., the first row will not be treated as column
     * names), and lines will be parsed assuming the delimiter is a
     * comma.
     *
     * @param url the URL path to the CSV file.
     */
    public CSVDataTable(String url)
    {
        this(url, false, ',');
    }


    // ----------------------------------------------------------
    /**
     * Create a new data table using CSV data on a website.  The URL
     * parameter is a link to a data file stored on the web.  All data rows
     * will be loaded, and lines will be parsed assuming the delimiter is a
     * comma.
     *
     * @param url the URL path to the CSV file.
     * @param firstRowNames If true, then the first row in the data source
     *        should be treated as containing the names for the columns
     *        in the data set.
     */
    public CSVDataTable(String url, boolean firstRowNames)
    {
        this(url, firstRowNames, ',');
    }


    // ----------------------------------------------------------
    /**
     * Create a new data table using CSV data on a website.  The URL
     * parameter is a link to a data file stored on the web.
     *
     * @param url the URL path to the CSV file.
     * @param firstRowNames If true, then the first row in the data source
     *        should be treated as containing the names for the columns
     *        in the data set.
     * @param delimiter The separator between fields on the same line
     *        (e.g., a comma, tab, colon, etc.).
     */
    public CSVDataTable(String url, boolean firstRowNames, char delimiter)
    {
        this.url = url;
        try
        {
            InputStream in = (new URL(url)).openStream();
            loadFrom(in, firstRowNames, delimiter);
            in.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     * Create a new data table using CSV data from a file.  All data rows
     * will be loaded (i.e., the first row will not be treated as column
     * names), and lines will be parsed assuming the delimiter is a
     * comma.
     *
     * @param file the CSV file to read.
     */
    public CSVDataTable(File file)
    {
        this(file, false, ',');
    }


    // ----------------------------------------------------------
    /**
     * Create a new data table using CSV data from a file.  All data rows
     * will be loaded, and lines will be parsed assuming the delimiter is a
     * comma.
     *
     * @param file the CSV file to read.
     * @param firstRowNames If true, then the first row in the data source
     *        should be treated as containing the names for the columns
     *        in the data set.
     */
    public CSVDataTable(File file, boolean firstRowNames)
    {
        this(file, firstRowNames, ',');
    }


    // ----------------------------------------------------------
    /**
     * Create a new data table using CSV data on a website.  The URL
     * parameter is a link to a data file stored on the web.
     *
     * @param file the CSV file to read.
     * @param firstRowNames If true, then the first row in the data source
     *        should be treated as containing the names for the columns
     *        in the data set.
     * @param delimiter The separator between fields on the same line
     *        (e.g., a comma, tab, colon, etc.).
     */
    public CSVDataTable(File file, boolean firstRowNames, char delimiter)
    {
        try
        {
            InputStream in = new FileInputStream(file);
            loadFrom(in, firstRowNames, delimiter);
            in.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     * Create a new data table using CSV data from an input stream.  The URL
     * parameter is a link to a data file stored on the web.
     *
     * @param in the input stream that contains the CSV data.
     * @param firstRowNames If true, then the first row in the data source
     *        should be treated as containing the names for the columns
     *        in the data set.
     * @param delimiter The separator between fields on the same line
     *        (e.g., a comma, tab, colon, etc.).
     */
    public CSVDataTable(InputStream in, boolean firstRowNames, char delimiter)
    {
        url = null;
        loadFrom(in, firstRowNames, delimiter);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    public int rowCount()
    {
        return tableRows.size();
    }


    // ----------------------------------------------------------
    public int colCount()
    {
        if (tableRows.size() == 0)
        {
            return 0;
        }
        else
        {
            return tableRows.get(0).size();
        }
    }


    // ----------------------------------------------------------
    public String getCell(int row, String column)
    {
        return tableRows.get(row).get(column);
    }


    // ----------------------------------------------------------
    public String getCell(int row, int column)
    {
        return tableRows.get(row).get(COL_INDEX_PREFIX + column);
    }


    // ----------------------------------------------------------
    public String getCell(String row, String column)
    {
        return keyMappings.get(row).get(column);
    }


    // ----------------------------------------------------------
    public String getCell(String row, int column)
    {
        return keyMappings.get(row).get(COL_INDEX_PREFIX + column);
    }


    // ----------------------------------------------------------
    public int getIntCell(int row, String column)
        throws NumberFormatException
    {
        return Integer.parseInt(getCell(row, column));
    }


    // ----------------------------------------------------------
    public int getIntCell(int row, int column)
        throws NumberFormatException
    {
        return Integer.parseInt(getCell(row, column));
    }


    // ----------------------------------------------------------
    public int getIntCell(String row, String column)
        throws NumberFormatException
    {
        return Integer.parseInt(getCell(row, column));
    }


    // ----------------------------------------------------------
    public int getIntCell(String row, int column)
        throws NumberFormatException
    {
        return Integer.parseInt(getCell(row, column));
    }


    // ----------------------------------------------------------
    public double getDoubleCell(int row, String column)
        throws NumberFormatException
    {
        return Double.parseDouble(getCell(row, column));
    }


    // ----------------------------------------------------------
    public double getDoubleCell(int row, int column)
        throws NumberFormatException
    {
        return Double.parseDouble(getCell(row, column));
    }


    // ----------------------------------------------------------
    public double getDoubleCell(String row, String column)
        throws NumberFormatException
    {
        return Double.parseDouble(getCell(row, column));
    }


    // ----------------------------------------------------------
    public double getDoubleCell(String row, int column)
        throws NumberFormatException
    {
        return Double.parseDouble(getCell(row, column));
    }


    // ----------------------------------------------------------
    public Row getRow(int row)
    {
        return tableRows.get(row);
    }


    // ----------------------------------------------------------
    public Row getRow(String row)
    {
        return keyMappings.get(row);
    }


    // ----------------------------------------------------------
    public void setCell(int row, String column, String value)
    {
        setCell(tableRows.get(row), column, value);
    }


    // ----------------------------------------------------------
    public void setCell(int row, String column, int value)
    {
        setCell(tableRows.get(row), column, Integer.toString(value));
    }


    // ----------------------------------------------------------
    public void setCell(int row, String column, double value)
    {
        setCell(tableRows.get(row), column, Double.toString(value));
    }


    // ----------------------------------------------------------
    public void setCell(int row, int column, String value)
    {
        setCell(tableRows.get(row), column, value);
    }


    // ----------------------------------------------------------
    public void setCell(int row, int column, int value)
    {
        setCell(tableRows.get(row), column, Integer.toString(value));
    }


    // ----------------------------------------------------------
    public void setCell(int row, int column, double value)
    {
        setCell(tableRows.get(row), column, Double.toString(value));
    }


    // ----------------------------------------------------------
    public void setCell(String row, String column, String value)
    {
        setCell(keyMappings.get(row), column, value);
    }


    // ----------------------------------------------------------
    public void setCell(String row, String column, int value)
    {
        setCell(keyMappings.get(row), column, Integer.toString(value));
    }


    // ----------------------------------------------------------
    public void setCell(String row, String column, double value)
    {
        setCell(keyMappings.get(row), column, Double.toString(value));
    }


    // ----------------------------------------------------------
    public void setCell(String row, int column, String value)
    {
        setCell(keyMappings.get(row), column, value);
    }


    // ----------------------------------------------------------
    public void setCell(String row, int column, int value)
    {
        setCell(keyMappings.get(row), column, Integer.toString(value));
    }


    // ----------------------------------------------------------
    public void setCell(String row, int column, double value)
    {
        setCell(keyMappings.get(row), column, Double.toString(value));
    }


    // ----------------------------------------------------------
    public Row removeRow(int row)
    {
        Row removedRow = tableRows.remove(row);
        if (primaryKey != null)
        {
            keyMappings.remove(removedRow.get(primaryKey));
        }
        remapTableRows(row);
        return removedRow;
    }


    // ----------------------------------------------------------
    public Row removeRow(String row)
    {
        Row removedRow = keyMappings.remove(row);
        int rowNumber = tableRows.indexOf(removedRow);
        tableRows.remove(rowNumber);
        remapTableRows(rowNumber);
        return removedRow;
    }


    // ----------------------------------------------------------
    public void addColumn(String columnName)
    {
        addColumn(columnName, null);
    }


    // ----------------------------------------------------------
    public void addColumn(String columnName, String defaultValue)
    {
        int colNumber = columnNames.size();
        columnNames.add(columnName);
        for (int i = 0; i < tableRows.size(); i++)
        {
            Row row = tableRows.get(i);
            if (defaultValue != null)
            {
                row.put(columnName, defaultValue);
                row.put(COL_INDEX_PREFIX + colNumber, defaultValue);
            }
        }
    }


    // ----------------------------------------------------------
    public void addRow(Row row)
    {
        row.put(ROW_INDEX_MARKER, Integer.toString(tableRows.size()));
        tableRows.add(row);
        for (int i = 0; i < columnNames.size(); i++)
        {
            String data = row.get(columnNames.get(i));
            row.put(COL_INDEX_PREFIX + i, data);
        }
        keyMappings.put(row.get(primaryKey), row);
    }


    // ----------------------------------------------------------
    public void setPrimaryKey(String key)
    {
        assert columnNames.contains(key)
            : "The primary key must be one of the column names.";
        primaryKey = key;
        constructBindings();
    }


    // ----------------------------------------------------------
    public String getPrimaryKey()
    {
        return primaryKey;
    }


    // ----------------------------------------------------------
    public List<String> getColumnNames()
    {
        return columnNames;
    }


    // ----------------------------------------------------------
    public void setColumnNames(List<String> columns)
    {
        // assert columns.size() == columnNames.size()
        //     : "Array passed should map to all of the existing columns.";

        if (primaryKey != null)
        {
            primaryKey = columns.get(columnNames.indexOf(primaryKey));
        }
        for (int i = 0; i < tableRows.size(); i++)
        {
            Row row = tableRows.get(i);

            // Clear old col names
            for (String col : columnNames)
            {
                row.remove(col);
            }

            // Add new col names
            for (int j = 0; j < columns.size(); j++)
            {
                row.put(columns.get(j), row.get(COL_INDEX_PREFIX + j));
            }
        }
        constructBindings();
        columnNames = new ArrayList<String>(columns);
    }


    // ----------------------------------------------------------
    public void setColumnNames(String... columns)
    {
        setColumnNames(Arrays.asList(columns));
    }


    // ----------------------------------------------------------
    public void remapColumnNames(Map<String, String> columnMap)
    {
        ArrayList<String> newColumnNames = new ArrayList<String>();

        assert columnMap.size() == columnNames.size()
            : "The columnMap parameter must map each of the column "
                + "names to a new value.";
        if (primaryKey != null)
        {
            primaryKey = columnMap.get(primaryKey);
        }
        for (int i = 0; i < columnNames.size(); i++)
        {
            newColumnNames.add(columnMap.get(columnNames.get(i)));
        }
        for (int i = 0; i < tableRows.size(); i++)
        {
            Row row = tableRows.get(i);
            for (String col : columnNames)
            {
                String val = row.remove(col);
                row.put(columnMap.get(col), val);
            }
        }
        constructBindings();
        columnNames = newColumnNames;
    }


    // ----------------------------------------------------------
    public List<Row> getAllRows()
    {
        return tableRows;
    }


    // ----------------------------------------------------------
    public Iterator<Row> iterator()
    {
        return getAllRows().iterator();
    }


    // ----------------------------------------------------------
    /**
     * Get the URL used to load this data table.
     * @return The URL used to load this data table, or null if it was
     * not loaded from a URL.
     */
    public String getUrl()
    {
        return url;
    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void copyRowsFrom(DataTable dataSet)
    {
        tableRows = (List<Row>)((ArrayList<Row>)dataSet.getAllRows()).clone();
        columnNames = (List<String>)((ArrayList<String>)dataSet
            .getColumnNames()).clone();
        constructBindings();
    }


    // ----------------------------------------------------------
    public int count(String column, String value)
    {
        int count = 0;
        for (Row row : tableRows)
        {
            if ( (value == null && row.get(column) == null)
                 || (value != null && value.equals(row.get(column))))
            {
                count++;
            }
        }
        return count;
    }


    // ----------------------------------------------------------
    public double avg(String column)
        throws NumberFormatException
    {
        double sum   = 0.0;
        int    count = 0;
        for (Row row : tableRows)
        {
            String val = row.get(column);
            if (val != null)
            {
                sum += Double.parseDouble(val);
                count++;
            }
        }
        return count == 0
            ? 0.0
            : sum / count;
    }


    // ----------------------------------------------------------
    public double max(String column)
        throws NumberFormatException
    {
        assert tableRows.size() > 0
            : "There are no values in the table";

        boolean found = false;
        double max = Double.NEGATIVE_INFINITY;
        for (Row row : tableRows)
        {
            String val = row.get(column);
            if (val != null)
            {
                double d = Double.parseDouble(val);
                if (d > max)
                {
                    found = true;
                    max = d;
                }
            }
        }
        assert found : "There are no values in this column of the table";
        return max;
    }


    // ----------------------------------------------------------
    public double min(String column)
        throws NumberFormatException
    {
        assert tableRows.size() > 0
        : "There are no values in the table";

        boolean found = false;
        double min = Double.POSITIVE_INFINITY;
        for (Row row : tableRows)
        {
            String val = row.get(column);
            if (val != null)
            {
                double d = Double.parseDouble(val);
                if (d < min)
                {
                    found = true;
                    min = d;
                }
            }
        }
        assert found : "There are no values in this column of the table";
        return min;
    }


    // ----------------------------------------------------------
    /**
    * This method saves the CSVDataTable in a CSV file format
    * with the first row the column names and the remaining rows
    * the data in the table.  The delimiter for the CSV is determined
    * either the value that was used in creating the data table or the
    * value that is set with the setDelimiter() method.
    *
    * @param filename the path to the file to save the CSVDataTable to.
    */
    public void save(String filename)
    {
        try
        {
            FileWriter out = new FileWriter(IOHelper.getFile(filename));
            ExcelCSVPrinter printer = new ExcelCSVPrinter(out);
            printer.changeDelimiter(delimiter);
            for (Row row : tableRows)
            {
                for (String col : columnNames)
                {
                    printer.print(row.get(col));
                }
                int col = columnNames.size();
                String coln = COL_INDEX_PREFIX + col;
                String val = row.get(coln);
                while (val != null)
                {
                    printer.print(val);
                    col++;
                    coln = COL_INDEX_PREFIX + col;
                    val = row.get(coln);
                }
                printer.println();
            }
            printer.close();
            out.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     * Set the delimiter used by this data set, which will affect how
     * this object will be written to a file in the future.
     * @param delimiter The separator between fields on the same line
     *        (e.g., a comma, tab, colon, etc.).
     */
    public void setDelimiter(char delimiter)
    {
        this.delimiter = delimiter;
    }


    // ----------------------------------------------------------
    /**
     * Get the delimiter used by this data set.
     * @return the current delimiter
     */
    public char getDelimiter()
    {
        return delimiter;
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    private void loadFrom(
        InputStream in, boolean firstRowNames, char newDelimiter)
    {
        tableRows = new ArrayList<Row>();
        columnNames = new ArrayList<String>();
        keyMappings = new HashMap<String, Row>();
        this.delimiter = newDelimiter;
        try
        {
            String[][] table =
                ExcelCSVParser.parse(new InputStreamReader(in), newDelimiter);
            constructColumnNames(table, firstRowNames);
            constructTable(table, firstRowNames);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    // ----------------------------------------------------------
    private void constructTable(String[][] parsedTable, boolean firstRowNames)
    {
        tableRows.clear();
        int rowCount = 0;

        for (int i = firstRowNames ? 1 : 0; i < parsedTable.length; i++)
        {
            HashedRow newRow = new HashedRow();
            newRow.put(ROW_INDEX_MARKER, Integer.toString(rowCount));
            tableRows.add(newRow);
            for (int j = 0; j < parsedTable[i].length; j++)
            {
                newRow.put(COL_INDEX_PREFIX + j, parsedTable[i][j]);
                if (columnNames.size() > j)
                {
                    newRow.put(columnNames.get(j), parsedTable[i][j]);
                }
            }
            if (primaryKey != null)
            {
                keyMappings.put(newRow.get(primaryKey), newRow);
            }
            rowCount++;
        }
    }


    // ----------------------------------------------------------
    private void constructColumnNames(String[][] table, boolean firstRowNames)
    {
        columnNames.clear();
        if (firstRowNames && table.length > 0)
        {
            for (String col : table[0])
            {
                columnNames.add(col);
            }
        }
    }


    // ----------------------------------------------------------
    private void constructBindings()
    {
        keyMappings.clear();
        if (primaryKey == null)
        {
            return;
        }
        for (int i = 0;  i < tableRows.size(); i++)
        {
            // assert !keyMappings.containsKey(
            //     tableRows.get(i).get(primaryKey))
            //     : "Primary Key must be unique.";
            Row row = tableRows.get(i);
            keyMappings.put(row.get(primaryKey), row);
        }
    }


    // ----------------------------------------------------------
    private void remapTableRows(int row)
    {
        for (int i = row;i < tableRows.size(); i++)
        {
            tableRows.get(i).put(ROW_INDEX_MARKER, Integer.toString(i));
        }
    }


    // ----------------------------------------------------------
    private void setCell(Row row, String col, String value)
    {
        row.put(col, value);
        int num = columnNames.indexOf(col);
        row.put(COL_INDEX_PREFIX + num, value);
    }


    // ----------------------------------------------------------
    private void setCell(Row row, int col, String value)
    {
        row.put(COL_INDEX_PREFIX + col, value);
        if (columnNames.size() > col)
        {
            row.put(columnNames.get(col), value);
        }
    }
}
