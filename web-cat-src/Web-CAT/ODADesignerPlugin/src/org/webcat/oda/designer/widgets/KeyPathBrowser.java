/*==========================================================================*\
 |  $Id: KeyPathBrowser.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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

package org.webcat.oda.designer.widgets;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.webcat.oda.designer.util.ImageUtils;

//------------------------------------------------------------------------
/**
 * A widget modeled after the Mac OS X browser widget that permits drilling down
 * a hierarchy using a horizontal sliding panel.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: KeyPathBrowser.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class KeyPathBrowser extends Composite
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     *
     * @param parent
     * @param style
     */
    public KeyPathBrowser(Composite parent, int style)
    {
        super(parent, style);

        selectionListeners = new ArrayList<SelectionListener>();
        segments = new ArrayList<SegmentInfo>();

        arrowImage = ImageUtils.getImage("icons/keypath/arrow.gif"); //$NON-NLS-1$

        createControls(parent);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    public void dispose()
    {
        arrowImage.dispose();

        super.dispose();
    }


    // ----------------------------------------------------------
    /**
     *
     * @param parent
     */
    private void createControls(Composite parent)
    {
        GridData gd;

        GridLayout thisLayout = new GridLayout(LIST_COUNT, true);
        thisLayout.horizontalSpacing = thisLayout.verticalSpacing = 0;
        thisLayout.marginWidth = thisLayout.marginHeight = 0;
        this.setLayout(thisLayout);

        browserLists = new Table[LIST_COUNT];

        for (int i = 0; i < LIST_COUNT; i++)
        {
            Table list = new Table(this, SWT.V_SCROLL | SWT.FULL_SELECTION);
            new TableColumn(list, SWT.LEFT);

            list.setData(LIST_INDEX_KEY, Integer.valueOf(i));

            list.addControlListener(new ControlAdapter()
            {
                public void controlResized(ControlEvent e)
                {
                    Table table = (Table) e.widget;
                    TableColumn column = table.getColumn(0);
                    column.setWidth(table.getClientArea().width);
                }
            });

            list.addListener(SWT.PaintItem, new Listener()
            {
                public void handleEvent(Event event)
                {
                    Table table = (Table) event.widget;
                    TableItem item = (TableItem) event.item;

                    String className = (String) item.getData("className"); //$NON-NLS-1$
                    String key = (String) item.getData("key"); //$NON-NLS-1$

                    if (className != null && key != null)
                    {
                        String destType = keyProvider
                                .getKeyType(className, key);
                        if (keyProvider.hasKeys(destType))
                        {
                            int width = table.getClientArea().width;
                            int x = width - arrowImage.getBounds().width - 4;
                            int itemHeight = table.getItemHeight();
                            int imageHeight = arrowImage.getBounds().height;
                            int y = event.y + (itemHeight - imageHeight) / 2;
                            event.gc.drawImage(arrowImage, x, y);
                        }
                    }
                }
            });

            list.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    if (!currentlyInSelection)
                    {
                        currentlyInSelection = true;

                        int widgetIndex = ((Integer) e.widget
                                .getData(LIST_INDEX_KEY)).intValue();

                        handleSelectionChanged(widgetIndex);

                        notifySelectionListeners(e, false);

                        currentlyInSelection = false;
                    }
                }


                @Override
                public void widgetDefaultSelected(SelectionEvent e)
                {
                    notifySelectionListeners(e, true);
                }
            });

            gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            list.setLayoutData(gd);

            browserLists[i] = list;
        }

        browserSlider = new Slider(this, SWT.HORIZONTAL);
        browserSlider.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                updateLists(0);
            }
        });

        gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gd.horizontalSpan = LIST_COUNT;
        browserSlider.setLayoutData(gd);
    }


    // ----------------------------------------------------------
    public void addSelectionListener(SelectionListener listener)
    {
        selectionListeners.add(listener);
    }


    // ----------------------------------------------------------
    public void removeSelectionListener(SelectionListener listener)
    {
        selectionListeners.remove(listener);
    }


    // ----------------------------------------------------------
    public void notifySelectionListeners(SelectionEvent e,
            boolean defaultSelection)
    {
        e.widget = this;

        for (SelectionListener listener : selectionListeners)
        {
            if (defaultSelection)
                listener.widgetDefaultSelected(e);
            else
                listener.widgetSelected(e);
        }
    }


    // ----------------------------------------------------------
    /**
     *
     * @param listIndex
     */
    private void handleSelectionChanged(int widgetIndex)
    {
        int selection = browserLists[widgetIndex].getSelectionIndex();

        int offset = browserSlider.getSelection();
        int segmentIndex = widgetIndex + offset;

        SegmentInfo segment = segments.get(segmentIndex);
        String[] theseKeys = keyProvider.getKeys(segment.getClassName());

        segment.setKey(theseKeys[selection]);

        String nextType = keyProvider.getKeyType(segment.getClassName(),
                theseKeys[selection]);

        // Remove any segments after the one that was changed.
        for (int i = segments.size() - 1; i > segmentIndex; i--)
            segments.remove(i);

        // Add a new segment to the end of the list.
        segments.add(new SegmentInfo(nextType, null));

        // Scroll the lists over and put the new keys in the last list.
        int lastList = lastListIndex();

        int oldSelection = browserSlider.getSelection();
        int newSelection = Math.max(0, lastList + 1 - LIST_COUNT);

        updateScroller(newSelection, 0, lastList + 1);

        // If the selection doesn't cause any scrolling to occur, only
        // update the lists to the right of the one where the selection
        // occurred.
        if (oldSelection == newSelection)
            updateLists(widgetIndex + 1);
        else
            updateLists(0);

        // For user-interface consistency, make sure that the focus gets
        // transfered to the list where the selection was just made, if it
        // was scrolled.
        int newWidgetIndex = (widgetIndex + oldSelection) - newSelection;
        if (newWidgetIndex >= 0 && newWidgetIndex < LIST_COUNT)
        {
            browserLists[newWidgetIndex].setFocus();
        }
    }


    // ----------------------------------------------------------
    private void updateScroller(int selection, int min, int max)
    {
        browserSlider.setValues(selection, min, max, LIST_COUNT, 1, LIST_COUNT);
        browserSlider.setEnabled(max > LIST_COUNT);
    }


    // ----------------------------------------------------------
    /**
     *
     */
    private void updateLists(int firstWidgetToUpdate)
    {
        int firstIndex = browserSlider.getSelection();

        for (int i = firstWidgetToUpdate; i < LIST_COUNT; i++)
        {
            browserLists[i].removeAll();

            if (firstIndex + i <= lastListIndex())
            {
                SegmentInfo segment = segments.get(firstIndex + i);

                if (keyProvider.hasKeys(segment.getClassName()))
                {
                    String[] keys = keyProvider.getKeys(segment.getClassName());

                    addKeysToList(i, segment.getClassName(), keys);

                    for (int j = 0; j < keys.length; j++)
                    {
                        if (keys[j].equals(segment.getKey()))
                        {
                            browserLists[i].select(j);
                            browserLists[i].showSelection();
                            break;
                        }
                    }
                }
            }
        }
    }


    // ----------------------------------------------------------
    private int lastListIndex()
    {
        int lastIndex = segments.size() - 1;
        SegmentInfo lastSegment = segments.get(lastIndex);

        if (keyProvider.hasKeys(lastSegment.getClassName()))
            return lastIndex;
        else
            return lastIndex - 1;
    }


    // ----------------------------------------------------------
    /**
     * Initializes the contents of the lists based on the current root class.
     */
    private void initializeLists()
    {
        if (keyProvider != null && keyLabelProvider != null
                && rootClassName != null)
        {
            updateScroller(0, 0, 0);

            for (int i = 0; i < LIST_COUNT; i++)
                browserLists[i].removeAll();

            String[] rootKeys = keyProvider.getKeys(rootClassName);
            addKeysToList(0, rootClassName, rootKeys);
        }
    }


    // ----------------------------------------------------------
    private void addKeysToList(int listIndex, String className, String[] keys)
    {
        if (keys != null)
        {
            browserLists[listIndex].setRedraw(false);

            for (int i = 0; i < keys.length; i++)
            {
                String key = keys[i];

                TableItem item = new TableItem(browserLists[listIndex],
                        SWT.NONE);

                item.setData("className", className); // $NON_NLS_1$ //$NON-NLS-1$
                item.setData("key", key); // $NON_NLS_1$ //$NON-NLS-1$

                Color fgColor = keyLabelProvider.getForegroundColor(className,
                        key);

                if (fgColor != null)
                    item.setForeground(fgColor);

                item.setText(keyLabelProvider.getLabel(className, key));
                item.setImage(keyLabelProvider.getImage(className, key));
            }

            browserLists[listIndex].setRedraw(true);
        }
    }


    // ----------------------------------------------------------
    public void setKeyProvider(IKeyProvider provider)
    {
        keyProvider = provider;
        initializeLists();
    }


    // ----------------------------------------------------------
    public IKeyProvider getKeyProvider()
    {
        return keyProvider;
    }


    // ----------------------------------------------------------
    public void setKeyLabelProvider(IKeyLabelProvider provider)
    {
        keyLabelProvider = provider;
        initializeLists();
    }


    // ----------------------------------------------------------
    public IKeyLabelProvider getKeyLabelProvider()
    {
        return keyLabelProvider;
    }


    // ----------------------------------------------------------
    public void setRootClassName(String className)
    {
        rootClassName = className;

        segments.clear();
        segments.add(new SegmentInfo(rootClassName, null));

        initializeLists();
    }


    // ----------------------------------------------------------
    public String getRootClassName()
    {
        return rootClassName;
    }


    // ----------------------------------------------------------
    public void setSelectedKeyPath(String keyPath)
    {
        // TODO implement
    }


    // ----------------------------------------------------------
    public String getSelectedKeyPath()
    {
        if (segments.isEmpty() || segments.get(0).getKey() == null)
            return ""; //$NON-NLS-1$

        StringBuilder buffer = new StringBuilder();

        buffer.append(segments.get(0).getKey());

        for (int i = 1; i < segments.size(); i++)
        {
            SegmentInfo segment = segments.get(i);
            if (segment.getKey() != null)
            {
                buffer.append("."); //$NON-NLS-1$
                buffer.append(segment.getKey());
            }
        }

        return buffer.toString();
    }


    //~ Nested classes ........................................................

    // ----------------------------------------------------------
    private class SegmentInfo
    {
        //~ Constructor .......................................................

        // ----------------------------------------------------------
        public SegmentInfo(String className, String key)
        {
            this.className = className;
            this.key = key;
        }


        //~ Methods ...........................................................

        // ----------------------------------------------------------
        public String getClassName()
        {
            return className;
        }


        // ----------------------------------------------------------
        public void setClassName(String value)
        {
            className = value;
        }


        // ----------------------------------------------------------
        public String getKey()
        {
            return key;
        }


        // ----------------------------------------------------------
        public void setKey(String value)
        {
            key = value;
        }


        //~ Static/instance variables .........................................

        private String className;
        private String key;
    }


    //~ Static/instance variables .............................................

    private static final int LIST_COUNT = 3;
    private static final String LIST_INDEX_KEY = "listIndex"; // $NON_NLS_1$ //$NON-NLS-1$

    private String rootClassName;
    private IKeyProvider keyProvider;
    private IKeyLabelProvider keyLabelProvider;
    private java.util.List<SelectionListener> selectionListeners;
    private boolean currentlyInSelection = false;
    private Table[] browserLists;
    private Slider browserSlider;
    private java.util.List<SegmentInfo> segments;
    private Image arrowImage;
}
