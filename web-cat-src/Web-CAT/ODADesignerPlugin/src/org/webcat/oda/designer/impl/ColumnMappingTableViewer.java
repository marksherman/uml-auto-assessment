/*==========================================================================*\
 |  $Id: ColumnMappingTableViewer.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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

package org.webcat.oda.designer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.webcat.oda.commons.DataSetDescription;
import org.webcat.oda.core.impl.DataTypes;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * This class handles the creation of the column mapping table as well as the
 * accessory buttons that permit moving and removing columns.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ColumnMappingTableViewer.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class ColumnMappingTableViewer
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates the column mapping table and accessory buttons.
     *
     * @param parent
     *            the Composite that should contain the controls
     */
    public ColumnMappingTableViewer(Composite parent)
    {
        GridData gd;

        // Main container
        container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        container.setLayout(layout);

        // Table viewer
        viewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION |
                SWT.MULTI);
        viewer.getTable().setHeaderVisible(true);
        viewer.getTable().setLinesVisible(true);

        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        viewer.getControl().setLayoutData(gd);

        // Up/down/remove button container
        Composite buttonContainer = new Composite(container, SWT.NONE);
        buttonContainer.setLayout(new FillLayout(SWT.VERTICAL));

        gd = new GridData(SWT.CENTER, SWT.BEGINNING, false, false);
        buttonContainer.setLayoutData(gd);

        // Up button
        upButton = new Button(buttonContainer, SWT.PUSH);
        upButton.setText(Messages.DATASET_COLUMN_MOVE_UP);

        // Down button
        downButton = new Button(buttonContainer, SWT.PUSH);
        downButton.setText(Messages.DATASET_COLUMN_MOVE_DOWN);

        // Remove button
        removeButton = new Button(buttonContainer, SWT.PUSH);
        removeButton.setText(Messages.DATASET_COLUMN_REMOVE);

        // Duplicate button
        duplicateButton = new Button(buttonContainer, SWT.PUSH);
        duplicateButton.setText(Messages.DATASET_COLUMN_DUPLICATE);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the underlying table viewer.
     *
     * @return the underlying table viewer
     */
    public TableViewer getViewer()
    {
        return viewer;
    }


    // ----------------------------------------------------------
    /**
     * Gets the Composite control that contains the table viewer and the
     * accessory buttons.
     *
     * @return the Composite control that contains the table viewer and
     *         accessory buttons
     */
    public Composite getControl()
    {
        return container;
    }


    // ----------------------------------------------------------
    /**
     * Gets the button that, when clicked, moves a column up.
     *
     * @return the button that, when clicked, moves a column up
     */
    public Button getUpButton()
    {
        return upButton;
    }


    // ----------------------------------------------------------
    /**
     * Gets the button that, when clicked, moves a column down.
     *
     * @return the button that, when clicked, moves a column down
     */
    public Button getDownButton()
    {
        return downButton;
    }


    // ----------------------------------------------------------
    /**
     * Gets the button that, when clicked, removes the selected column.
     *
     * @return the button that, when clicked, removes the selected column
     */
    public Button getRemoveButton()
    {
        return removeButton;
    }


    // ----------------------------------------------------------
    /**
     * Gets the button that, when clicked, removes the selected column.
     *
     * @return the button that, when clicked, removes the selected column
     */
    public Button getDuplicateButton()
    {
        return duplicateButton;
    }


    // ----------------------------------------------------------
    /**
     * Refreshes the table viewer from the specified data set description.
     *
     * @param info
     *            the data set description from which to obtain the columns
     * @param columnMapping
     *            used to store a mapping from column names to
     *            ColumnMappingElements
     * @return the list of columns in the table, wrapped as
     *         ColumnMappingElements
     */
    public List<ColumnMappingElement> refresh(DataSetDescription info,
            Map<String, ColumnMappingElement> columnMapping)
    {
        ArrayList<ColumnMappingElement> columnsList =
            new ArrayList<ColumnMappingElement>();

        if (info == null)
            return columnsList;

        for (int i = 0; i < info.getColumnCount(); i++)
        {
            ColumnMappingElement element = new ColumnMappingElement();
            element.setColumnName(info.getColumnName(i));
            element.setExpression(info.getColumnExpression(i));

            try
            {
                element.setType(DataTypeUtil.getDataTypeDisplayName(DataTypes
                        .getType(info.getColumnType(i))));
            }
            catch (OdaException e)
            {
                // Ignore exception.
            }

            columnMapping.put(info.getColumnName(i), element);
            columnsList.add(element);
        }

        return columnsList;
    }


    //~ Static/instance variables .............................................

    private Composite container;
    private TableViewer viewer;
    private Button removeButton;
    private Button upButton;
    private Button downButton;
    private Button duplicateButton;
}
