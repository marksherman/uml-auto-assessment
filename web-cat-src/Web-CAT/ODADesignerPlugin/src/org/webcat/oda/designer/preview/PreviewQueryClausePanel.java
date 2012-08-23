/*==========================================================================*\
 |  $Id: PreviewQueryClausePanel.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.contentassist.ContentAssistManager;
import org.webcat.oda.designer.contentassist.ContentAssistObjectDescription;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.util.ImageUtils;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewQueryClausePanel.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class PreviewQueryClausePanel extends Composite
{
    public PreviewQueryClausePanel(Composite parent,
            IPreviewQueryClauseEventHandler handler, String entityType)
    {
        super(parent, SWT.NONE);

        this.entityType = entityType;
        this.eventHandler = handler;

        contentAssistManager = DesignerActivator.getDefault()
                .getContentAssistManager();

        addImage = ImageUtils.getImage("icons/querybuilder/plus.gif"); //$NON-NLS-1$
        removeImage = ImageUtils.getImage("icons/querybuilder/minus.gif"); //$NON-NLS-1$

        createContents();
    }


    @Override
    public void dispose()
    {
        addImage.dispose();
        removeImage.dispose();

        super.dispose();
    }


    private void createContents()
    {
        GridLayout layout = createGridLayout(6, false);
        layout.marginHeight = 4;
        layout.marginWidth = 4;
        this.setLayout(layout);

        GridData gd;

        // Key path field
        keyPathField = new Text(this, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd.widthHint = 140;
        keyPathField.setLayoutData(gd);
        keyPathField.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                keyPathModified();
            }
        });

        // Operator drop-down
        comparisonCombo = new Combo(this, SWT.BORDER | SWT.DROP_DOWN
                | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd.widthHint = 64;
        comparisonCombo.setLayoutData(gd);
        comparisonCombo.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                comparisonChanged();
            }
        });

        // Comparand type panel
        comparandTypeCombo = new Combo(this, SWT.BORDER | SWT.DROP_DOWN
                | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd.widthHint = 64;
        comparandTypeCombo.setLayoutData(gd);
        comparandTypeCombo.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                comparandTypeChanged();
            }
        });

        // Detail stack panel
        detailContainer = new Composite(this, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        detailContainer.setLayoutData(gd);

        detailStack = new StackLayout();
        detailContainer.setLayout(detailStack);

        keyPathDetailPanel = createKeyPathDetailPanel(detailContainer);
        booleanDetailPanel = createBooleanDetailPanel(detailContainer);
        numStringDetailPanel = createNumStringDetailPanel(detailContainer);
        numRangeDetailPanel = createNumRangeDetailPanel(detailContainer);
        timestampDetailPanel = createTimestampDetailPanel(detailContainer);
        timestampRangeDetailPanel = createTimestampRangeDetailPanel(detailContainer);
        objectDetailPanel = createObjectDetailPanel(detailContainer);

        // Remove button
        removeButton = new Button(this, SWT.PUSH);
        removeButton.setImage(removeImage);
        removeButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                removeButtonClicked();
            }
        });

        // Add button
        addButton = new Button(this, SWT.PUSH);
        addButton.setImage(addImage);
        addButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                addButtonClicked();
            }
        });

        keyPathModified();
    }


    private Composite createBooleanDetailPanel(Composite parent)
    {
        GridData gd;

        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(gd);

        GridLayout layout = createGridLayout(1, true);
        composite.setLayout(layout);

        booleanOperandCombo = new Combo(composite, SWT.BORDER | SWT.DROP_DOWN
                | SWT.READ_ONLY);
        gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gd.widthHint = 80;
        booleanOperandCombo.setLayoutData(gd);
        booleanOperandCombo.add("false"); //$NON-NLS-1$
        booleanOperandCombo.add("true"); //$NON-NLS-1$

        booleanOperandCombo.select(0);

        return composite;
    }


    private Composite createNumStringDetailPanel(Composite parent)
    {
        GridData gd;

        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(gd);

        GridLayout layout = createGridLayout(1, true);
        composite.setLayout(layout);

        numStringOperandField = new Text(composite, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        numStringOperandField.setLayoutData(gd);

        return composite;
    }


    private Composite createNumRangeDetailPanel(Composite parent)
    {
        GridData gd;

        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(gd);

        GridLayout layout = createGridLayout(3, false);
        composite.setLayout(layout);

        numRangeMinOperandField = new Text(composite, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd.widthHint = 96;
        numRangeMinOperandField.setLayoutData(gd);

        Label label = new Label(composite, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        label.setText(Messages.QUERY_CLAUSE_AND);

        numRangeMaxOperandField = new Text(composite, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gd.widthHint = 96;
        numRangeMaxOperandField.setLayoutData(gd);

        return composite;
    }


    private Composite createTimestampDetailPanel(Composite parent)
    {
        GridData gd;

        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(gd);

        GridLayout layout = createGridLayout(2, false);
        composite.setLayout(layout);

        dateOperandField = new DateTime(composite, SWT.DATE | SWT.MEDIUM);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        dateOperandField.setLayoutData(gd);

        timeOperandField = new DateTime(composite, SWT.TIME | SWT.SHORT);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        timeOperandField.setLayoutData(gd);

        return composite;
    }


    private Composite createTimestampRangeDetailPanel(Composite parent)
    {
        GridData gd;

        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(gd);

        GridLayout layout = createGridLayout(5, false);
        composite.setLayout(layout);

        dateRangeMinOperandField = new DateTime(composite, SWT.DATE
                | SWT.MEDIUM);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        dateRangeMinOperandField.setLayoutData(gd);

        timeRangeMinOperandField = new DateTime(composite, SWT.TIME | SWT.SHORT);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        timeRangeMinOperandField.setLayoutData(gd);

        Label label = new Label(composite, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        label.setText(Messages.QUERY_CLAUSE_AND);

        dateRangeMaxOperandField = new DateTime(composite, SWT.DATE
                | SWT.MEDIUM);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        dateRangeMaxOperandField.setLayoutData(gd);

        timeRangeMaxOperandField = new DateTime(composite, SWT.TIME | SWT.SHORT);
        gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        timeRangeMaxOperandField.setLayoutData(gd);

        return composite;
    }


    private Composite createObjectDetailPanel(Composite parent)
    {
        GridData gd;

        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(gd);

        GridLayout layout = createGridLayout(1, true);
        composite.setLayout(layout);

        objectOperandCombo = new Combo(composite, SWT.BORDER | SWT.DROP_DOWN
                | SWT.READ_ONLY);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        objectOperandCombo.setLayoutData(gd);

        return composite;
    }


    private Composite createKeyPathDetailPanel(Composite parent)
    {
        GridData gd;

        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        composite.setLayoutData(gd);

        GridLayout layout = createGridLayout(1, true);
        composite.setLayout(layout);

        destinationKeyPathField = new Text(composite, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        destinationKeyPathField.setLayoutData(gd);

        return composite;
    }


    private GridLayout createGridLayout(int columns, boolean equalSize)
    {
        GridLayout layout = new GridLayout(columns, equalSize);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 2;
        layout.verticalSpacing = 0;

        return layout;
    }


    private String typeOfCurrentKeyPath()
    {
        String keyPath = keyPathField.getText();
        String type = contentAssistManager.getKeyPathType(entityType, keyPath);

        return type;
    }


    private void keyPathModified()
    {
        String type = typeOfCurrentKeyPath();

        if (type != null)
        {
            comparisonCombo.setVisible(true);
            comparandTypeCombo.setVisible(true);
            detailContainer.setVisible(true);

            PreviewQueryComparison[] comparisons = PreviewQueryComparison
                    .comparisonsForType(type);

            comparisonCombo.removeAll();

            for (PreviewQueryComparison comparison : comparisons)
            {
                comparisonCombo.add(comparison.representation());
            }

            comparisonCombo.select(0);
            comparisonChanged();
        }
        else
        {
            comparisonCombo.setVisible(false);
            comparandTypeCombo.setVisible(false);
            detailContainer.setVisible(false);
        }
    }


    private void comparisonChanged()
    {
        String type = typeOfCurrentKeyPath();

        PreviewQueryComparison[] comparisons = PreviewQueryComparison
                .comparisonsForType(type);

        PreviewQueryComparison comparison = comparisons[comparisonCombo
                .getSelectionIndex()];

        comparandTypeCombo.removeAll();

        if (comparison.supportsKeyPaths())
        {
            comparandTypeCombo.add(Messages.QUERY_CLAUSE_THE_VALUE);
            comparandTypeCombo.add(Messages.QUERY_CLAUSE_THE_KEY_PATH);
        }
        else
        {
            comparandTypeCombo.add(Messages.QUERY_CLAUSE_THE_VALUE);
        }

        comparandTypeCombo.select(0);
        comparandTypeChanged();
    }


    private void comparandTypeChanged()
    {
        String type = typeOfCurrentKeyPath();

        PreviewQueryComparison[] comparisons = PreviewQueryComparison
                .comparisonsForType(type);

        PreviewQueryComparison comparison = comparisons[comparisonCombo
                .getSelectionIndex()];

        Control controlToReveal = null;

        if (comparandTypeCombo.getSelectionIndex() == 1)
        {
            controlToReveal = keyPathDetailPanel;
        }
        else if (comparisons == PreviewQueryComparison.BOOLEAN_COMPARISONS)
        {
            controlToReveal = booleanDetailPanel;
        }
        else if (comparisons == PreviewQueryComparison.NUMERIC_COMPARISONS
                || comparisons == PreviewQueryComparison.STRING_COMPARISONS)
        {
            if (comparison == PreviewQueryComparison.IS_BETWEEN
                    || comparison == PreviewQueryComparison.IS_NOT_BETWEEN)
            {
                controlToReveal = numRangeDetailPanel;
            }
            else
            {
                controlToReveal = numStringDetailPanel;
            }
        }
        else if (comparisons == PreviewQueryComparison.TIMESTAMP_COMPARISONS)
        {
            if (comparison == PreviewQueryComparison.IS_BETWEEN
                    || comparison == PreviewQueryComparison.IS_NOT_BETWEEN)
            {
                controlToReveal = timestampRangeDetailPanel;
            }
            else
            {
                controlToReveal = timestampDetailPanel;
            }
        }
        else if (comparisons == PreviewQueryComparison.OBJECT_COMPARISONS)
        {
            controlToReveal = objectDetailPanel;
            populateObjectCombo(type);
        }

        detailStack.topControl = controlToReveal;
        detailContainer.layout();
    }


    private void populateObjectCombo(String type)
    {
        ContentAssistObjectDescription[] objects = contentAssistManager
                .getObjectDescriptions(type);

        int index = objectOperandCombo.getSelectionIndex();
        objectOperandCombo.removeAll();

        for (ContentAssistObjectDescription object : objects)
        {
            objectOperandCombo.add(object.description());
        }

        objectOperandCombo.select(index);
    }


    private void addButtonClicked()
    {
        eventHandler.addClauseBelow(this);
    }


    private void removeButtonClicked()
    {
        eventHandler.removeClause(this);
    }


    public PreviewQueryClause getClause()
    {
        String type = typeOfCurrentKeyPath();

        PreviewQueryComparison[] comparisons = PreviewQueryComparison
                .comparisonsForType(type);

        PreviewQueryComparison comparison = comparisons[comparisonCombo
                .getSelectionIndex()];

        PreviewQueryClause clause = new PreviewQueryClause();

        clause.setKeyPath(keyPathField.getText());
        clause.setComparison(comparison);
        clause.setComparandType(comparandTypeCombo.getSelectionIndex());
        String valueRep = getValueRepresentation(type, comparisons, comparison);

        if (valueRep == null)
        {
            return null;
        }
        else
        {
            clause.setValueRepresentation(valueRep);
        }

        return clause;
    }


    public void setClause(PreviewQueryClause clause)
    {
        keyPathField.setText(clause.keyPath());
        keyPathModified();

        String type = typeOfCurrentKeyPath();

        if (type == null)
            return;

        PreviewQueryComparison[] comparisons = PreviewQueryComparison
                .comparisonsForType(type);

        if (comparisons == null)
            return;

        for (int i = 0; i < comparisons.length; i++)
        {
            if (comparisons[i] == clause.comparison())
            {
                comparisonCombo.select(i);
                break;
            }
        }

        comparisonChanged();

        comparandTypeCombo.select(clause.comparandType());

        comparandTypeChanged();

        // Populate detail controls with the value.
        String valueRep = clause.valueRepresentation();

        try
        {
            if (comparandTypeCombo.getSelectionIndex() == 1)
            {
                destinationKeyPathField.setText(valueRep);
            }
            else if (comparisons == PreviewQueryComparison.BOOLEAN_COMPARISONS)
            {
                boolean boolVal = Boolean.parseBoolean(valueRep);
                booleanOperandCombo.select(boolVal ? 1 : 0);
            }
            else if (comparisons == PreviewQueryComparison.NUMERIC_COMPARISONS
                    || comparisons == PreviewQueryComparison.STRING_COMPARISONS)
            {
                if (clause.comparison() == PreviewQueryComparison.IS_BETWEEN
                        || clause.comparison() == PreviewQueryComparison.IS_NOT_BETWEEN)
                {
                    String[] parts = valueRep.split(","); //$NON-NLS-1$
                    numRangeMinOperandField.setText(parts[0]);
                    numRangeMaxOperandField.setText(parts[1]);
                }
                else
                {
                    numStringOperandField.setText(valueRep);
                }
            }
            else if (comparisons == PreviewQueryComparison.TIMESTAMP_COMPARISONS)
            {
                if (clause.comparison() == PreviewQueryComparison.IS_BETWEEN
                        || clause.comparison() == PreviewQueryComparison.IS_NOT_BETWEEN)
                {
                    String[] parts = valueRep.split(","); //$NON-NLS-1$
                    representationToDateTime(parts[0],
                            dateRangeMinOperandField, timeRangeMinOperandField);
                    representationToDateTime(parts[1],
                            dateRangeMaxOperandField, timeRangeMaxOperandField);
                }
                else
                {
                    representationToDateTime(valueRep, dateOperandField,
                            timeOperandField);
                }
            }
            else if (comparisons == PreviewQueryComparison.OBJECT_COMPARISONS)
            {
                ContentAssistObjectDescription[] objects = contentAssistManager
                        .getObjectDescriptions(type);

                String idString = valueRep.substring(valueRep.indexOf(':') + 1);
                int id = Integer.parseInt(idString);

                for (int i = 0; i < objects.length; i++)
                {
                    if (objects[i].id() == id)
                    {
                        objectOperandCombo.select(i);
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            // Ignore exception.
        }
    }


    private String getValueRepresentation(String type,
            PreviewQueryComparison[] comparisons,
            PreviewQueryComparison comparison)
    {
        try
        {
            if (comparandTypeCombo.getSelectionIndex() == 1)
            {
                return destinationKeyPathField.getText();
            }
            else if (comparisons == PreviewQueryComparison.BOOLEAN_COMPARISONS)
            {
                if (booleanOperandCombo.getSelectionIndex() == 0)
                    return Boolean.FALSE.toString();
                else
                    return Boolean.TRUE.toString();
            }
            else if (comparisons == PreviewQueryComparison.NUMERIC_COMPARISONS
                    || comparisons == PreviewQueryComparison.STRING_COMPARISONS)
            {
                if (comparison == PreviewQueryComparison.IS_BETWEEN
                        || comparison == PreviewQueryComparison.IS_NOT_BETWEEN)
                {
                    return String.format("%s,%s", numRangeMinOperandField //$NON-NLS-1$
                            .getText(), numRangeMaxOperandField.getText());
                }
                else
                {
                    return numStringOperandField.getText();
                }
            }
            else if (comparisons == PreviewQueryComparison.TIMESTAMP_COMPARISONS)
            {
                if (comparison == PreviewQueryComparison.IS_BETWEEN
                        || comparison == PreviewQueryComparison.IS_NOT_BETWEEN)
                {
                    return String.format("%s,%s", //$NON-NLS-1$
                            representationFromDateTime(
                                    dateRangeMinOperandField,
                                    timeRangeMinOperandField),
                            representationFromDateTime(
                                    dateRangeMaxOperandField,
                                    timeRangeMaxOperandField));
                }
                else
                {
                    return representationFromDateTime(dateOperandField,
                            timeOperandField);
                }
            }
            else if (comparisons == PreviewQueryComparison.OBJECT_COMPARISONS)
            {
                ContentAssistObjectDescription[] objects = contentAssistManager
                        .getObjectDescriptions(type);

                ContentAssistObjectDescription object = objects[objectOperandCombo
                        .getSelectionIndex()];

                return object.valueRepresentation();
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            return null;
        }
    }


    private String representationFromDateTime(DateTime date, DateTime time)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(date.getYear());
        builder.append(' ');
        builder.append(date.getMonth());
        builder.append(' ');
        builder.append(date.getDay());
        builder.append(' ');
        builder.append(time.getHours());
        builder.append(' ');
        builder.append(time.getMinutes());

        return builder.toString();
    }


    private void representationToDateTime(String rep, DateTime date,
            DateTime time)
    {
        String[] parts = rep.split(" "); //$NON-NLS-1$

        date.setYear(Integer.parseInt(parts[0]));
        date.setMonth(Integer.parseInt(parts[1]));
        date.setDay(Integer.parseInt(parts[2]));
        time.setHours(Integer.parseInt(parts[3]));
        time.setMinutes(Integer.parseInt(parts[4]));
    }


    private IPreviewQueryClauseEventHandler eventHandler;

    private String entityType;

    private ContentAssistManager contentAssistManager;

    private Text keyPathField;

    private Combo comparisonCombo;

    private Combo comparandTypeCombo;

    private StackLayout detailStack;

    private Composite detailContainer;

    private Composite booleanDetailPanel;
    private Combo booleanOperandCombo;

    private Composite numStringDetailPanel;
    private Text numStringOperandField;

    private Composite numRangeDetailPanel;
    private Text numRangeMinOperandField;
    private Text numRangeMaxOperandField;

    private Composite timestampDetailPanel;
    private DateTime dateOperandField;
    private DateTime timeOperandField;

    private Composite timestampRangeDetailPanel;
    private DateTime dateRangeMinOperandField;
    private DateTime timeRangeMinOperandField;
    private DateTime dateRangeMaxOperandField;
    private DateTime timeRangeMaxOperandField;

    private Composite objectDetailPanel;
    private Combo objectOperandCombo;

    private Composite keyPathDetailPanel;
    private Text destinationKeyPathField;

    private Button addButton;

    private Button removeButton;

    private Image addImage;

    private Image removeImage;
}
