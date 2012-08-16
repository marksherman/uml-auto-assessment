/*==========================================================================*\
 |  $Id: DataSetDescription.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

// ------------------------------------------------------------------------
/**
 * This class represents the description of a data set as it is stored in a BIRT
 * report template, which includes information such as the entity type, a unique
 * identifier, column expressions, and constraints.
 *
 * This class also allows for easy transformation back and forth between a
 * convenient in-memory representation of the data set that can be easily
 * manipulated, and a text representation that can be stored in the report.
 *
 * The format of the description string is:
 *
 * <pre>
 * ENTITY
 * (entityType)
 * ID
 * (uniqueId)
 * COLUMN
 * (column name)
 * (column type)
 * (keypath or OGNL expression)
 * ====
 * COLUMN
 * ...
 * ====
 * </pre>
 *
 * In an upcoming version, data set constraints will also be a part of this
 * descriptor.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DataSetDescription.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
 */
public class DataSetDescription
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates an uninitialized data set description.
     */
    public DataSetDescription()
    {
        columns = new ArrayList<ColumnInfo>();
    }


    // ----------------------------------------------------------
    /**
     * Creates a data set description based on the given text string from the
     * report template XML source.
     *
     * @param descriptionText
     *            the text representation of the data set
     */
    public DataSetDescription(String descriptionText)
    {
        this();

        parse(descriptionText);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Parses the given description text and stores the results in this
     * description object.
     *
     * @param descriptionText
     *            the text to parse
     */
    private void parse(String descriptionText)
    {
        if (descriptionText == null || descriptionText.trim().length() == 0)
            return;

        try
        {
            BufferedReader reader = new BufferedReader(new StringReader(
                    descriptionText));
            String line;

            while ((line = reader.readLine()) != null)
            {
                if (ENTITY_SECTION.equals(line))
                {
                    entityType = reader.readLine();
                }
                else if (ID_SECTION.equals(line))
                {
                    uniqueId = reader.readLine();
                }
                else if (COLUMN_SECTION.equals(line))
                {
                    ColumnInfo column = new ColumnInfo();
                    column.name = reader.readLine();
                    column.type = reader.readLine();

                    StringBuilder expression = new StringBuilder();

                    line = reader.readLine();
                    while (line != null && !"====".equals(line))
                    {
                        expression.append(line);
                        expression.append('\n');

                        line = reader.readLine();
                    }

                    column.expression = expression.toString().trim();

                    columns.add(column);
                }
                else if (CONSTRAINT_SECTION.equals(line))
                {
                    // TODO: Implement constraints.
                }
            }
        }
        catch (IOException e)
        {
            // Ignore for now.
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the entity type name that this data set will operate on.
     *
     * @return the name of the entity type
     */
    public String getEntityType()
    {
        return entityType;
    }


    // ----------------------------------------------------------
    /**
     * Sets the entity type name that this data set will operate on.
     *
     * @param value
     *            the name of the entity type
     */
    public void setEntityType(String value)
    {
        entityType = value;
    }


    // ----------------------------------------------------------
    /**
     * <p>
     * Gets the unique identifier assigned to this data set.
     * </p><p>
     * Unique identifiers are reassigned when a report template is uploaded to
     * Web-CAT, to ensure true uniqueness. This identifier takes the form of a
     * URL constructed based on the server hostname and a report template
     * identification number.
     * </p><p>
     * Before a template is uploaded to Web-CAT, it is assigned a temporary
     * random unique identifier for the purposes of saving preview queries for
     * the user.
     * </p>
     *
     * @return the unique identifier of the data set
     */
    public String getUniqueId()
    {
        return uniqueId;
    }


    // ----------------------------------------------------------
    /**
     * Sets the unique identifier assigned to this data set.
     *
     * @param uniqueId
     *            the unique identifier of the data set
     */
    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }


    // ----------------------------------------------------------
    /**
     * Removes all columns from the data set.
     */
    public void clearColumns()
    {
        columns.clear();
    }


    // ----------------------------------------------------------
    /**
     * Adds a new column to the data set.
     *
     * @param name
     *            the name of the column
     * @param expression
     *            the key path or OGNL expression that calculates the column
     *            value
     * @param type
     *            the type of the column
     */
    public void addColumn(String name, String expression, String type)
    {
        ColumnInfo info = new ColumnInfo();
        info.name = name;
        info.expression = expression;
        info.type = type;
        columns.add(info);
    }


    // ----------------------------------------------------------
    /**
     * Gets the number of columns in the data set.
     *
     * @return the number of columns in the data set
     */
    public int getColumnCount()
    {
        return columns.size();
    }


    // ----------------------------------------------------------
    /**
     * Gets the name of the column at the specified index.
     *
     * @param index
     *            the index of the column
     * @return the name of the column
     */
    public String getColumnName(int index)
    {
        return columns.get(index).name;
    }


    // ----------------------------------------------------------
    /**
     * Gets the key path or OGNL expression associated with the column at the
     * specified index.
     *
     * @param index
     *            the index of the column
     * @return the key path or OGNL expression associated with the column
     */
    public String getColumnExpression(int index)
    {
        return columns.get(index).expression;
    }


    // ----------------------------------------------------------
    /**
     * Gets the type of the column at the specified index.
     *
     * @param index
     *            the index of the column
     * @return the type of the column
     */
    public String getColumnType(int index)
    {
        return columns.get(index).type;
    }


    // -----------------------------------------------------------
    /**
     * Gets the text representation of the data set.
     *
     * @return the text representation of the data set
     */
    public String getQueryText()
    {
        StringBuilder query = new StringBuilder();

        query.append(ENTITY_SECTION);
        query.append('\n');
        query.append(entityType);
        query.append('\n');
        query.append(ID_SECTION);
        query.append('\n');
        query.append(uniqueId);
        query.append('\n');

        for (int i = 0; i < columns.size(); i++)
        {
            ColumnInfo info = columns.get(i);

            query.append(COLUMN_SECTION);
            query.append('\n');
            query.append(info.name);
            query.append('\n');
            query.append(info.type);
            query.append('\n');
            query.append(info.expression);
            query.append("\n====\n");
        }

        return query.toString();
    }


    //~ Nested Classes ........................................................

    // ----------------------------------------------------------
    /**
     * Contains information about a column in the data set.
     */
    private class ColumnInfo
    {
        /** The name of the column. */
        public String name;

        /** The key path or OGNL expression for the column. */
        public String expression;

        /** The data type of the column. */
        public String type;
    }


    //~ Static/Instance variables .............................................

    private static final String ENTITY_SECTION = "ENTITY";
    private static final String ID_SECTION = "ID";
    private static final String COLUMN_SECTION = "COLUMN";
    private static final String CONSTRAINT_SECTION = "CONSTRAINT";

    private String uniqueId;
    private String entityType;
    private List<ColumnInfo> columns;
}
