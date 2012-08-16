/*==========================================================================*\
 |  $Id: CreateTransformedDataSetDialog.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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

package org.webcat.oda.designer.transform;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.URLResourceLoader;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.webcat.oda.designer.DesignerActivator;

// ------------------------------------------------------------------------
/**
 * A dialog that lets the user derive a scripted data set that transforms data
 * from another data set.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: CreateTransformedDataSetDialog.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class CreateTransformedDataSetDialog extends TitleAreaDialog
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new Create Transformed Data Set dialog that operates on the
     * specified report model.
     *
     * @param parentShell
     *            the Shell that will own the dialog
     * @param model
     *            the report model to operate on
     */
    public CreateTransformedDataSetDialog(Shell parentShell, ModuleHandle model)
    {
        super(parentShell);

        int style = getShellStyle() | SWT.RESIZE;
        setShellStyle(style);

        this.model = model;
        
        initializeVelocity();
    }


    private void initializeVelocity()
    {
        velocity = new VelocityEngine();
        
        try
        {
            URL url = FileLocator.find(
                    DesignerActivator.getDefault().getBundle(),
                    new Path("velocity"), null);
            
            url = FileLocator.resolve(url);

            Properties props = new Properties();
            props.setProperty(RuntimeConstants.RESOURCE_LOADER, "url");
            props.setProperty("url.resource.loader.description",
                    "Velocity URL Resource Loader");
            props.setProperty("url.resource.loader.class",
                    URLResourceLoader.class.getName());
            props.setProperty("url.resource.loader.root",
                    url.toString());
            
            velocity.init(props);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    // ----------------------------------------------------------
    @Override
    protected Control createContents(Composite parent)
    {
        Control control = super.createContents(parent);

        getShell().setText("Create Transformed Data Set");
        setTitle("Create Transformed Data Set");
        setMessage("Use the options below to create a new scripted data set that derives its data from an existing data set.");

        return control;
    }


    // ----------------------------------------------------------
    @Override
    protected Control createDialogArea(Composite parent)
    {
        Composite composite = (Composite) super.createDialogArea(parent);

        Composite panel = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        panel.setLayout(layout);
        panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Label label;
        GridData gd;

        GridData gdSep = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gdSep.horizontalSpan = 2;

        GridData gdWideLabel = new GridData(SWT.FILL, SWT.TOP, true, false);
        gdWideLabel.horizontalSpan = 2;

        label = new Label(panel, SWT.NONE);
        label.setText("New data set name:");

        dataSetNameField = new Text(panel, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        dataSetNameField.setLayoutData(gd);

        label = new Label(panel, SWT.NONE);
        label.setText("Data set to transform:");

        sourceDataSetField = new Combo(panel, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        sourceDataSetField.setLayoutData(gd);
        sourceDataSetField.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                sourceDataSetFieldChanged();
            }
        });

        new Label(panel, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gdSep);

        Composite subPanel = new Composite(panel, SWT.NONE);
        layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        subPanel.setLayout(layout);
        subPanel.setLayoutData(gdWideLabel);

        existingDataSourceButton = new Button(subPanel, SWT.RADIO);
        existingDataSourceButton
                .setText("Use an existing scripted data source:");

        existingDataSourceField = new Combo(subPanel, SWT.DROP_DOWN
                | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        existingDataSourceField.setLayoutData(gd);

        newDataSourceButton = new Button(subPanel, SWT.RADIO);
        newDataSourceButton.setText("Create a new scripted data source named:");

        newDataSourceField = new Text(subPanel, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        newDataSourceField.setLayoutData(gd);

        new Label(panel, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(gdSep);

        label = new Label(panel, SWT.NONE);
        label.setText("Take one row for each");

        groupingKeyField = new Combo(panel, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        groupingKeyField.setLayoutData(gd);

        label = new Label(panel, SWT.NONE);
        label.setText("that has the");

        aggregationField = new Combo(panel, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        aggregationField.setLayoutData(gd);

        label = new Label(panel, SWT.NONE);
        label.setText("value for the column");

        aggregationKeyField = new Combo(panel, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        aggregationKeyField.setLayoutData(gd);

        initializeFields();

        return composite;
    }


    // ----------------------------------------------------------
    @Override
    protected void okPressed()
    {
        try
        {
            populateModel();
        }
        catch (Exception e)
        {
            // TODO handle properly
            e.printStackTrace();
        }

        super.okPressed();
    }


    //~ Private Methods .......................................................

    private void sourceDataSetFieldChanged()
    {
        DataSetHandle sourceDataSet = (DataSetHandle) model.getDataSets().get(
                sourceDataSetField.getSelectionIndex());

        PropertyHandle sourceColumns = sourceDataSet
                .getPropertyHandle(DataSetHandle.RESULT_SET_PROP);

        int numColumns = sourceColumns.getIntValue();

        groupingKeyField.removeAll();
        aggregationKeyField.removeAll();

        for (int i = 0; i < numColumns; i++)
        {
            OdaResultSetColumnHandle sourceColumn = (OdaResultSetColumnHandle) sourceColumns
                    .get(i);

            groupingKeyField.add(sourceColumn.getColumnName());
            aggregationKeyField.add(sourceColumn.getColumnName());
        }
    }


    // ----------------------------------------------------------
    /**
     * Initialize controls with values from the report model.
     */
    private void initializeFields()
    {
        for (int i = 0; i < model.getDataSets().getCount(); i++)
        {
            DataSetHandle dataSet = (DataSetHandle) model.getDataSets().get(i);
            String name = dataSet.getName() + " ("
                    + dataSet.getDataSourceName() + ")";

            sourceDataSetField.add(name);
        }

        int numScriptedDataSources = 0;
        scriptedDataSources = new ArrayList<ScriptDataSourceHandle>();

        for (int i = 0; i < model.getDataSources().getCount(); i++)
        {
            DataSourceHandle dataSource = (DataSourceHandle) model
                    .getDataSources().get(i);

            if (dataSource instanceof ScriptDataSourceHandle)
            {
                scriptedDataSources.add((ScriptDataSourceHandle) dataSource);
                existingDataSourceField.add(dataSource.getName());
                numScriptedDataSources++;
            }
        }

        if (numScriptedDataSources > 0)
        {
            existingDataSourceButton.setSelection(true);
            existingDataSourceField.select(0);
        }
        else
        {
            existingDataSourceButton.setEnabled(false);
            existingDataSourceField.setEnabled(false);
            newDataSourceButton.setSelection(true);
        }

        aggregationField.add("maximum");
        aggregationField.add("minimum");

    }


    // ----------------------------------------------------------
    private void populateModel() throws NameException, ContentException,
            SemanticException
    {
        ElementFactory elementFactory = model.getElementFactory();

        DataSetHandle sourceDataSet = (DataSetHandle) model.getDataSets().get(
                sourceDataSetField.getSelectionIndex());

        // Create the new scripted data source if necessary, or get the
        // existing one.

        ScriptDataSourceHandle scriptDataSource;

        if (newDataSourceButton.getSelection())
        {
            String dataSourceName = newDataSourceField.getText();
            scriptDataSource = elementFactory
                    .newScriptDataSource(dataSourceName);
            model.getDataSources().add(scriptDataSource);
        }
        else
        {
            int index = existingDataSourceField.getSelectionIndex();
            scriptDataSource = scriptedDataSources.get(index);
        }

        String varName = sanitizeDataSetName(sourceDataSet) + "_results";

        Properties params = new Properties();
        params.setProperty(PROP_RESULTS_VARIABLE_NAME_KEY, varName);
        params.setProperty(PROP_GROUPING_KEY, groupingKeyField.getText());
        params.setProperty(PROP_AGGREGATION, aggregationField.getText());
        params.setProperty(PROP_AGGREGATION_KEY, aggregationKeyField.getText());

        // Add script code to the source data set to store the data in
        // intermediate JavaScript objects.

        addSourceDataSetScripts(sourceDataSet, params);

        // Create the new scripted data set.

        ScriptDataSetHandle newDataSet = createScriptedDataSet(elementFactory,
                scriptDataSource, params);

        copyDataSetColumns(elementFactory, sourceDataSet, newDataSet);

        // Add a hidden table to the top of the report that references the
        // source data set.
        //
        // TODO Consider adding a new report item to the designer that acts as
        // a non-rendered data set runner

        createHiddenTable(elementFactory, sourceDataSet);
    }


    // ----------------------------------------------------------
    /**
     * Creates a hidden table at the top of the report body that forces data
     * from the specified data set to be generated (and the data set's scripts
     * to be executed).
     *
     * @param elementFactory
     *            the element factory to use
     * @param sourceDataSet
     *            the source data set for the table
     *
     * @throws SemanticException
     * @throws ContentException
     * @throws NameException
     */
    private void createHiddenTable(ElementFactory elementFactory,
            DataSetHandle sourceDataSet) throws SemanticException,
            ContentException, NameException
    {
        String tableName = "Hidden Table to Generate Data For "
                + sourceDataSet.getName();

        // Get the list of columns from the source data set.

        PropertyHandle columns = sourceDataSet
                .getPropertyHandle(DataSetHandle.RESULT_SET_PROP);
        int numColumns = columns.getIntValue();

        // Create the table item.

        TableHandle hiddenTable = elementFactory.newTableItem(tableName,
                numColumns, 0, 1, 0);
        hiddenTable.setDataSet(sourceDataSet);

        // Create the data bindings for each column in the table.

        PropertyHandle tableBindings = hiddenTable.getColumnBindings();
        for (int i = 0; i < numColumns; i++)
        {
            OdaResultSetColumnHandle column = (OdaResultSetColumnHandle) columns
                    .get(i);

            ComputedColumn binding = StructureFactory.createComputedColumn();
            binding.setDataType(column.getDataType());
            binding.setName(column.getColumnName());
            binding.setExpression("dataSetRow[\"" + column.getColumnName() + "\"]");

            tableBindings.addItem(binding);
        }

        // Create the cells and data items for the table.

        RowHandle detailRow = (RowHandle) hiddenTable.getDetail().get(0);

        for (int i = 0; i < numColumns; i++)
        {
            OdaResultSetColumnHandle column = (OdaResultSetColumnHandle) columns
                    .get(i);

            CellHandle cell = (CellHandle) detailRow.getCells().get(i);
            DataItemHandle dataItem = elementFactory.newDataItem(null);
            dataItem.setResultSetColumn(column.getColumnName());

            cell.getContent().add(dataItem);
        }

        // Set the table to be hidden in all rendering formats.

        HideRule hideRule = StructureFactory.createHideRule();
        hideRule.setFormat("all");
        hideRule.setExpression("true");
        hiddenTable.getPropertyHandle(TableHandle.VISIBILITY_PROP).addItem(
                hideRule);

        // Add the table to the top of the report body.

        ((ReportDesignHandle) model).getBody().add(hiddenTable, 0);
    }


    // ----------------------------------------------------------
    /**
     * Creates a valid JavaScript identifier from the name of a data set.
     *
     * @param dataSet
     *            the data set whose name should be sanitized
     *
     * @return the sanitized name of the data set
     */
    private String sanitizeDataSetName(DataSetHandle dataSet)
    {
        String name = dataSet.getName();

        StringBuilder buffer = new StringBuilder(name.length());

        if (!Character.isJavaIdentifierStart(name.charAt(0)))
        {
            buffer.append('_');
        }

        for (int i = 0; i < name.length(); i++)
        {
            char ch = name.charAt(i);

            if (Character.isJavaIdentifierPart(ch))
            {
                buffer.append(ch);
            }
            else
            {
                buffer.append('_');
            }
        }

        return buffer.toString();
    }


    // ----------------------------------------------------------
    private void addSourceDataSetScripts(DataSetHandle sourceDataSet,
            Properties params) throws SemanticException
    {
//        String groupingKey = params.getProperty(PROP_GROUPING_KEY);
//        String aggregation = params.getProperty(PROP_AGGREGATION);
//        String aggKey = params.getProperty(PROP_AGGREGATION_KEY);

/*        StringBuilder buffer = new StringBuilder(256);
        buffer.append(resultsName + " = new java.util.Hashtable();\n");*/

        StringWriter writer = new StringWriter();
        mergeTemplate("dsxform_source_afterOpen.jstemplate", params, writer);
        sourceDataSet.setAfterOpen(writer.toString());

/*        buffer = new StringBuilder(512);
        buffer.append("var key = row[\"" + groupingKey + "\"];\n");
        buffer.append("var put = false;\n\n");
        buffer.append("if (" + resultsName + ".containsKey(key))\n");
        buffer.append("{\n");
        buffer.append("    var current = " + resultsName + ".get(key);\n");

        buffer.append("    // Modify this expression to alter the decision\n");
        buffer.append("    // about whether the row is included.\n");
        if ("maximum".equals(aggregation))
        {
            buffer.append("    put = (row[\"" + aggKey + "\"] > current[\""
                    + aggKey + "\"]);\n");
        }
        else if ("minimum".equals(aggregation))
        {
            buffer.append("    put = (row[\"" + aggKey + "\"] < current[\""
                    + aggKey + "\"]);\n");
        }

        buffer.append("}\n");
        buffer.append("else\n");
        buffer.append("{\n");
        buffer.append("    put = true;\n");
        buffer.append("}\n\n");
        buffer
                .append("// Add the row to the table that will be carried into the\n");
        buffer.append("// scripted data set.\n");
        buffer.append("if (put)\n");
        buffer.append("{\n");
        buffer.append("    var bundle = new Object();\n");
        buffer.append("    for (var i = 0; i < row.columnDefns.length; i++)\n");
        buffer.append("    {\n");
        buffer.append("        var name = row.columnDefns[i].name;\n");
        buffer.append("        bundle[name] = row[name];\n");
        buffer.append("    }\n\n");
        buffer.append("    " + resultsName + ".put(key, bundle);\n");
        buffer.append("}\n");*/
        
        writer = new StringWriter();
        mergeTemplate("dsxform_source_onFetch.jstemplate", params, writer);
        sourceDataSet.setOnFetch(writer.toString());
    }


    // ----------------------------------------------------------
    /**
     * Creates a scripted data set and the associated scripts.
     *
     * @param elementFactory
     *            the factory to use to create design elements
     * @param scriptDataSource
     *            the data source under which the data set will be created
     *
     * @return the new scripted data set
     *
     * @throws SemanticException
     * @throws ContentException
     * @throws NameException
     */
    private ScriptDataSetHandle createScriptedDataSet(
            ElementFactory elementFactory,
            ScriptDataSourceHandle scriptDataSource, Properties params)
            throws SemanticException, ContentException, NameException
    {
        String dataSetName = dataSetNameField.getText();
        ScriptDataSetHandle newDataSet = elementFactory
                .newScriptDataSet(dataSetName);
        newDataSet.setDataSource(scriptDataSource.getName());
        model.getDataSets().add(newDataSet);

        // Build the JavaScript code for the data set.

        StringWriter writer = new StringWriter();
        mergeTemplate("dsxform_derived_open.jstemplate", params, writer);
        newDataSet.setOpen(writer.toString());

        writer = new StringWriter();
        mergeTemplate("dsxform_derived_fetch.jstemplate", params, writer);
        newDataSet.setFetch(writer.toString());
        
/*        StringBuilder buffer = new StringBuilder();
        buffer
                .append("iterator = " + resultsName
                        + ".entrySet().iterator();\n");
        newDataSet.setOpen(buffer.toString());

        buffer = new StringBuilder();
        buffer.append("if (iterator.hasNext())\n");
        buffer.append("{\n");
        buffer.append("    var entry = iterator.next();\n");
        buffer.append("    var columnBundle = entry.getValue();\n");
        buffer.append("    for (var column in columnBundle)\n");
        buffer.append("        row[column] = columnBundle[column];\n\n");
        buffer.append("    return true;\n");
        buffer.append("}\n");
        buffer.append("else\n");
        buffer.append("{\n");
        buffer.append("    return false;\n");
        buffer.append("}\n");
        newDataSet.setFetch(buffer.toString());
*/
        return newDataSet;
    }


    // ----------------------------------------------------------
    /**
     * Copies the data set columns from the source data set to the new scripted
     * data set.
     *
     * @param elementFactory
     *            the element factory to use
     * @param sourceDataSet
     *            the data set from which columns should be copied
     * @param scriptDataSet
     *            the data set to which columns should be copied
     *
     * @throws SemanticException
     */
    private void copyDataSetColumns(ElementFactory elementFactory,
            DataSetHandle sourceDataSet, ScriptDataSetHandle scriptDataSet)
            throws SemanticException
    {
        PropertyHandle sourceColumns = sourceDataSet
                .getPropertyHandle(DataSetHandle.RESULT_SET_PROP);
        PropertyHandle sourceColumnHints = sourceDataSet
                .getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);

        PropertyHandle scriptColumns = scriptDataSet
                .getPropertyHandle(DataSetHandle.RESULT_SET_PROP);
        PropertyHandle scriptColumnHints = scriptDataSet
                .getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);

        int numColumns = sourceColumns.getIntValue();

        for (int i = 0; i < numColumns; i++)
        {
            OdaResultSetColumnHandle sourceColumn = (OdaResultSetColumnHandle) sourceColumns
                    .get(i);
            ColumnHintHandle sourceHint = (ColumnHintHandle) sourceColumnHints
                    .get(i);

            ResultSetColumn resultColumn = StructureFactory
                    .createResultSetColumn();
            resultColumn.setPosition(i + 1);
            resultColumn.setColumnName(sourceColumn.getColumnName());
            resultColumn.setDataType(sourceColumn.getDataType());
            scriptColumns.addItem(resultColumn);

            ColumnHint hint = StructureFactory.createColumnHint();
            hint.setProperty(ColumnHint.COLUMN_NAME_MEMBER, sourceColumn
                    .getColumnName());
            hint.setProperty(ColumnHint.ALIAS_MEMBER, sourceHint.getAlias());
            hint.setProperty(ColumnHint.DISPLAY_NAME_MEMBER, sourceHint
                    .getDisplayName());
            scriptColumnHints.addItem(hint);
        }
    }


    private void mergeTemplate(String name, Properties params, Writer writer)
    {
        try
        {
            VelocityContext context = new VelocityContext(params);

            Template template = velocity.getTemplate(name);
            template.merge(context, writer);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    //~ Static/instance variables .............................................

    private static final String PROP_RESULTS_VARIABLE_NAME_KEY =
        "resultsVariableName";
    private static final String PROP_GROUPING_KEY = "groupingKey";
    private static final String PROP_AGGREGATION = "aggregation";
    private static final String PROP_AGGREGATION_KEY = "aggregationKey";

    private ModuleHandle model;
    private List<ScriptDataSourceHandle> scriptedDataSources;
    private VelocityEngine velocity;

    private Text dataSetNameField;
    private Combo sourceDataSetField;

    private Button existingDataSourceButton;
    private Combo existingDataSourceField;
    private Button newDataSourceButton;
    private Text newDataSourceField;

    private Combo groupingKeyField;
    private Combo aggregationField;
    private Combo aggregationKeyField;
}
