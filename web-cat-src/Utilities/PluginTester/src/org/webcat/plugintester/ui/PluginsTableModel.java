/*==========================================================================*\
 |  $Id: PluginsTableModel.java,v 1.2 2011/03/18 11:31:32 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
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

package org.webcat.plugintester.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.configuration.ConfigurationException;
import org.webcat.plugintester.AppConstants;
import org.webcat.plugintester.util.PluginConfiguration;

//-------------------------------------------------------------------------
/**
 * The table model used to maintain the list of plugins that will be executed.
 * 
 * @author Tony Allevato
 * @version $Id: PluginsTableModel.java,v 1.2 2011/03/18 11:31:32 aallowat Exp $
 */
public class PluginsTableModel extends AbstractTableModel
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public PluginsTableModel()
    {
        plugins = new ArrayList<String>();
    }
    

    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public int getColumnCount()
    {
        return types.length;
    }


    // ----------------------------------------------------------
    public String getColumnName(int columnIndex)
    {
        return columns[columnIndex];
    }
    
    
    // ----------------------------------------------------------
    public int getRowCount()
    {
        return plugins.size();
    }


    // ----------------------------------------------------------
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return plugins.get(rowIndex);
    }


    // ----------------------------------------------------------
    public Class<?> getColumnClass(int columnIndex)
    {
        return types[columnIndex];
    }

    
    // ----------------------------------------------------------
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return canEdit[columnIndex];
    }


    // ----------------------------------------------------------
    public void addPlugin(String path)
    {
        plugins.add(path);
        fireTableDataChanged();
    }
    
    
    // ----------------------------------------------------------
    public void removePluginAtIndex(int index)
    {
        plugins.remove(index);
        fireTableDataChanged();
    }


    // ----------------------------------------------------------
    public void updatePropertiesFromModel(Properties props)
    {
        StringBuffer buffer = new StringBuffer();

        if (plugins.size() > 0)
        {
            buffer.append(plugins.get(0));
            
            for (int i = 1; i < plugins.size(); i++)
            {
                buffer.append(File.pathSeparatorChar);
                buffer.append(plugins.get(i));
            }
        }
        
        props.setProperty(AppConstants.PROP_LAST_PLUGIN_PATHS,
                buffer.toString());
    }
    
    
    // ----------------------------------------------------------
    public void updateModelFromProperties(Properties props)
    {
        plugins.clear();

        String pluginsString = props.getProperty(
                AppConstants.PROP_LAST_PLUGIN_PATHS);
        
        if (pluginsString == null || pluginsString.length() == 0)
        {
            return;
        }

        String[] pluginNames = pluginsString.split(File.pathSeparator);
        
        for (String plugin : pluginNames)
        {
            try
            {
                new PluginConfiguration(new File(plugin));
                plugins.add(plugin);
            }
            catch (ConfigurationException e)
            {
                // Do nothing.
            }
        }
        
        fireTableDataChanged();
    }


    // ----------------------------------------------------------
    public String[] getPlugins()
    {
        return plugins.toArray(new String[plugins.size()]);
    }

    
    //~ Instance/static variables .............................................

    /** The list of plugins. */
    private List<String> plugins;

    private static String[] columns = new String[] { "Plug-in path" };
    private static Class<?>[] types = new Class[] { java.lang.String.class };
    private static boolean[] canEdit = new boolean[] { false };

    private static final long serialVersionUID = -2292025334003681111L;
}
