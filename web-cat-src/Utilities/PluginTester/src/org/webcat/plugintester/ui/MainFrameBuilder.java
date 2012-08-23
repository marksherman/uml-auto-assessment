/*==========================================================================*\
 |  $Id: MainFrameBuilder.java,v 1.2 2011/03/18 11:31:32 aallowat Exp $
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.webcat.plugintester.AppConstants;
import org.webcat.plugintester.PluginRunner;
import org.webcat.plugintester.util.PluginConfiguration;
import org.webcat.plugintester.util.WebCATConfiguration;

//-------------------------------------------------------------------------
/**
 * Manages the main frame window for the application.
 *
 * @author Tony Allevato
 * @version $Id: MainFrameBuilder.java,v 1.2 2011/03/18 11:31:32 aallowat Exp $
 */
public class MainFrameBuilder
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new instances of the MainFrameBuilder class.
     *
     * @param wcConfig the object that represents the Web-CAT application
     *     configuration
     * @param settings the Properties object that contains the settings that
     *     are specified in the user interface
     */
    public MainFrameBuilder(WebCATConfiguration wcConfig, Properties settings)
    {
        webcatConfig = wcConfig;
        currentSettings = settings;

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Web-CAT Plug-in Tester");

        constructFrame(frame);
        initializeFields();

        frame.setLocationRelativeTo(null);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Opens the main frame window.
     */
    public void openFrame()
    {
        frame.setVisible(true);
    }


    // ----------------------------------------------------------
    /**
     * Creates and lays out the Swing components for the window.
     *
     * @param frame the JFrame instance that will contain the components
     */
    private void constructFrame(JFrame frame)
    {
        GridBagConstraints gridBagConstraints;

        fileChooser = new JFileChooser();
        bottomPanel = new JPanel();
        runButton = new JButton();
        mainPanel = new JPanel();
        JLabel webCatHomeLabel = new JLabel();
        webCatHomeField = new JTextField();
        webCatHomeBrowseButton = new JButton();
        JSeparator jSeparator0 = new JSeparator();
        JLabel submissionLabel = new JLabel();
        submissionField = new JTextField();
        submissionBrowseButton = new JButton();
        JSeparator jSeparator1 = new JSeparator();
        JLabel pluginsLabel = new JLabel();
        JPanel pluginsPanel = new JPanel();
        JScrollPane pluginsScrollPane = new JScrollPane();
        pluginsTable = new JTable();
        pluginAddButton = new JButton();
        pluginRemoveButton = new JButton();
        JSeparator jSeparator2 = new JSeparator();
        JTabbedPane tabPane = new JTabbedPane();
        propertiesPanel = new JPanel();
        JScrollPane propertiesScrollPane = new JScrollPane();
        propertiesEditor = new JEditorPane();
        documentationPanel = new JPanel();
        JScrollPane documentationScrollPane = new JScrollPane();
        documentationEditor = new JEditorPane();

        // File chooser
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        // Bottom panel
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));

        // Run button
        runButton.setText("Run Plug-ins");
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(runButton);

        frame.getContentPane().add(bottomPanel, BorderLayout.PAGE_END);

        // Main panel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        mainPanel.setLayout(new GridBagLayout());

        // Web-CAT Home field
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        webCatHomeLabel.setText("Web-CAT Home:");
        mainPanel.add(webCatHomeLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        webCatHomeField.setTransferHandler(new WebCATHomeTransferHandler());
        mainPanel.add(webCatHomeField, gridBagConstraints);

        // Web-CAT Home browse button
        webCatHomeBrowseButton.setText("Browse...");
        webCatHomeBrowseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                webCatHomeBrowseButtonActionPerformed(evt);
            }
        });
        mainPanel.add(webCatHomeBrowseButton, new GridBagConstraints());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(jSeparator0, gridBagConstraints);

        // Submission field
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        submissionLabel.setText("Submission:");
        mainPanel.add(submissionLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        submissionField.setTransferHandler(new SubmissionTransferHandler());
        mainPanel.add(submissionField, gridBagConstraints);

        // Submission browse button
        submissionBrowseButton.setText("Browse...");
        submissionBrowseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                submissionBrowseButtonActionPerformed(evt);
            }
        });
        mainPanel.add(submissionBrowseButton, new GridBagConstraints());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(jSeparator1, gridBagConstraints);

        // Plug-ins area
        pluginsLabel.setText("Plug-ins to run:");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(pluginsLabel, gridBagConstraints);

        pluginsPanel.setPreferredSize(new Dimension(400, 80));
        pluginsPanel.setLayout(new GridBagLayout());

        pluginsModel = new PluginsTableModel();
        pluginsTable.setModel(pluginsModel);
        pluginsTable.setColumnSelectionAllowed(true);
        pluginsTable.getTableHeader().setReorderingAllowed(false);
        pluginsScrollPane.setViewportView(pluginsTable);
        pluginsTable.getColumnModel().getSelectionModel().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        pluginsTable.getColumnModel().getColumn(0).setResizable(false);
        pluginsScrollPane.setTransferHandler(new PluginsTransferHandler());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pluginsPanel.add(pluginsScrollPane, gridBagConstraints);

        pluginAddButton.setText("Add...");
        pluginAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pluginAddButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        pluginsPanel.add(pluginAddButton, gridBagConstraints);

        pluginRemoveButton.setText("Remove");

        pluginRemoveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                pluginRemoveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
        pluginsPanel.add(pluginRemoveButton, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.25;
        mainPanel.add(pluginsPanel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(jSeparator2, gridBagConstraints);

        tabPane.setPreferredSize(new Dimension(466, 150));

        propertiesPanel.setLayout(new GridBagLayout());

        propertiesEditor.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                propertiesTimer.restart();
            }
        });

        propertiesTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateGradingProperties();
            }
        });

        propertiesScrollPane.setViewportView(propertiesEditor);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        propertiesPanel.add(propertiesScrollPane, gridBagConstraints);

        tabPane.addTab("Properties", propertiesPanel);

        documentationPanel.setLayout(new GridBagLayout());

        documentationEditor.setContentType("text/html");
        documentationEditor.setEditable(false);
        documentationScrollPane.setViewportView(documentationEditor);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        documentationPanel.add(documentationScrollPane, gridBagConstraints);

        tabPane.addTab("Documentation", documentationPanel);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        mainPanel.add(tabPane, gridBagConstraints);

        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

        frame.pack();
    }


    // ----------------------------------------------------------
    /**
     * Initializes the frame components with values obtained from the current
     * settings.
     */
    private void initializeFields()
    {
        String homePath = currentSettings.getProperty(
                AppConstants.PROP_WEBCAT_HOME);

        if (homePath != null)
        {
            webCatHomeField.setText(homePath);
        }

        String subPath = currentSettings.getProperty(
                AppConstants.PROP_LAST_SUBMISSION_PATH);

        if (subPath != null)
        {
            submissionField.setText(subPath);
        }

        pluginsModel.updateModelFromProperties(currentSettings);

        String props = currentSettings.getProperty(
                AppConstants.PROP_USER_GRADING_PROPERTIES);

        if (props != null)
        {
            propertiesEditor.setText(props);
        }

        updateDocumentationPane();
    }


    // ----------------------------------------------------------
    /**
     * Sets the Web-CAT home location to the specified directory.
     *
     * @param file a File object that represents the directory containing the
     *     Web-CAT server
     */
    private void setWebCATHome(File file)
    {
        String path;

        if (file.isDirectory())
        {
            path = file.getAbsolutePath();
        }
        else
        {
            path = null;
            JOptionPane.showMessageDialog(frame,
                    "The selection you have made does not appear to be\n" +
                    "a valid submission. You should select the directory\n" +
                    "that represents the expanded submission contents.\n",
                    "Not a valid submission",
                    JOptionPane.ERROR_MESSAGE);
        }

        webCatHomeField.setText(path);

        if (path == null)
        {
            currentSettings.remove(AppConstants.PROP_WEBCAT_HOME);
        }
        else
        {
            currentSettings.setProperty(AppConstants.PROP_WEBCAT_HOME, path);
            webcatConfig = new WebCATConfiguration(path);
        }
    }


    // ----------------------------------------------------------
    /**
     * Sets the current submission to the specified directory.
     *
     * @param file a File object that represents the directory containing the
     *     submission to use for testing
     */
    private void setSubmission(File file)
    {
        String path;

        if (file.isDirectory())
        {
            path = file.getAbsolutePath();
        }
        else
        {
            path = null;
            JOptionPane.showMessageDialog(frame,
                    "The selection you have made does not appear to be\n" +
                    "a valid submission. You should select the directory\n" +
                    "that represents the expanded submission contents.\n",
                    "Not a valid submission",
                    JOptionPane.ERROR_MESSAGE);
        }

        submissionField.setText(path);

        if (path == null)
        {
            currentSettings.remove(AppConstants.PROP_LAST_SUBMISSION_PATH);
        }
        else
        {
            currentSettings.setProperty(AppConstants.PROP_LAST_SUBMISSION_PATH, path);
        }
    }


    // ----------------------------------------------------------
    /**
     * Adds the plugin represented by the specified file or directory to the
     * testing chain.
     *
     * @param file a File object that represents either the directory
     *     containing the grading plugin or the config.plist file inside the
     *     plugin's directory
     */
    private void addPlugin(File file)
    {
        String path;

        if (file.isDirectory())
        {
            File configFile = new File(file, "config.plist");

            if (configFile.exists())
            {
                path = file.getAbsolutePath();
            }
            else
            {
                path = null;
                JOptionPane.showMessageDialog(frame,
                        "The selection you have made does not appear to be\n" +
                        "a valid grading plug-in. You should either select the\n" +
                        "directory that represents the plug-in contents, or\n" +
                        "the config.plist file inside that directory.",
                        "Not a valid plug-in",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            if (file.getName().equalsIgnoreCase("config.plist"))
            {
                path = file.getParentFile().getAbsolutePath();
            }
            else
            {
                path = null;
                JOptionPane.showMessageDialog(frame,
                        "The selection you have made does not appear to be\n" +
                        "a valid grading plug-in. You should either select the\n" +
                        "directory that represents the plug-in contents, or\n" +
                        "the config.plist file inside that directory.",
                        "Not a valid plug-in",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        if (path != null)
        {
            try
            {
                new PluginConfiguration(new File(path));
                pluginsModel.addPlugin(path);
                pluginsModel.updatePropertiesFromModel(currentSettings);
                updateDocumentationPane();
            }
            catch (ConfigurationException e)
            {
                JOptionPane.showMessageDialog(frame,
                        "An error occurred while parsing the plug-in's "
                        + "config.plist:\n\n"
                        + e.getMessage(),
                        "Not a valid plug-in",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Update the user's grading properties in the current settings based on
     * the text entered in the property editor pane.
     */
    private void updateGradingProperties()
    {
        String props = propertiesEditor.getText();

        if (props == null)
        {
            props = "";
        }

        currentSettings.setProperty(AppConstants.PROP_USER_GRADING_PROPERTIES,
                props);
    }


    // ----------------------------------------------------------
    /**
     * Called when the "Browse..." button is pressed to let the user choose the
     * Web-CAT home location.
     *
     * @param evt the event
     */
    private void webCatHomeBrowseButtonActionPerformed(ActionEvent evt)
    {
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
        {
            setWebCATHome(fileChooser.getSelectedFile());
        }
    }


    // ----------------------------------------------------------
    /**
     * Called when the "Browse..." button is pressed to let the user choose the
     * submission to be used for testing.
     *
     * @param evt the event
     */
    private void submissionBrowseButtonActionPerformed(ActionEvent evt)
    {
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
        {
            setSubmission(fileChooser.getSelectedFile());
        }
    }


    // ----------------------------------------------------------
    /**
     * Called when the "Add..." button is pressed to let the user choose a
     * plugin to be added to the testing chain.
     *
     * @param evt the event
     */
    private void pluginAddButtonActionPerformed(ActionEvent evt)
    {
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
        {
            addPlugin(fileChooser.getSelectedFile());
        }
    }


    // ----------------------------------------------------------
    /**
     * Called when the "Remove" button is pressed to let the user remove the
     * currently selected plugin from the testing chain.
     *
     * @param evt the event
     */
    private void pluginRemoveButtonActionPerformed(ActionEvent evt)
    {
        int row = pluginsTable.getSelectedRow();

        if (row != -1)
        {
            pluginsModel.removePluginAtIndex(row);
            pluginsModel.updatePropertiesFromModel(currentSettings);
            updateDocumentationPane();
        }
    }


    // ----------------------------------------------------------
    /**
     * Called when the "Run Plug-ins" button is pressed to begin the grading
     * process.
     *
     * @param evt the event
     */
    private void runButtonActionPerformed(ActionEvent evt)
    {
        PluginRunner runner = new PluginRunner(webcatConfig, currentSettings);

        runner.run();
    }


    // ----------------------------------------------------------
    /**
     * Updates the documentation pane with property descriptions when the
     * current plugin chain is modified.
     */
    private void updateDocumentationPane()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>");

        String[] plugins = pluginsModel.getPlugins();

        for (String pluginDir : plugins)
        {
            PluginConfiguration pluginConfig = null;

            try
            {
                pluginConfig = new PluginConfiguration(new File(pluginDir));
            }
            catch (ConfigurationException e)
            {
                // Do nothing.
            }

            Map<String, PropertyListConfiguration> option =
                pluginConfig.getOptions();

            for (String optionName : option.keySet())
            {
                PropertyListConfiguration plist = option.get(optionName);

                appendPropertyInfoToBuffer(plist, buffer);
            }
        }

        buffer.append("</body></html>");

        documentationEditor.setText(buffer.toString());
    }


    // ----------------------------------------------------------
    /**
     * Appends a description of the specified property as HTML to a string
     * buffer.
     *
     * @param plist the property list containing information about the property
     * @param buffer the buffer to which the description will be appended
     */
    private void appendPropertyInfoToBuffer(PropertyListConfiguration plist,
            StringBuffer buffer)
    {
        buffer.append("<font face=\"Arial\">");
        buffer.append("<b>");
        buffer.append(plist.getString("property"));
        buffer.append("</b> <i>(type: ");
        buffer.append(plist.getString("type"));
        buffer.append(")</i>");
        buffer.append("</font>");
        buffer.append("<br>");
        buffer.append("<font face=\"Arial\" size=\"-1\">");
        buffer.append(plist.getString("description"));
        buffer.append("</font>");
        buffer.append("<hr>");
    }


    //~ Private classes .......................................................

    // ----------------------------------------------------------
    /**
     * Handles drop operations of a file onto the current submission text
     * field.
     */
    private class SubmissionTransferHandler extends TransferHandler
    {
        //~ Methods ...........................................................

        // ----------------------------------------------------------
        /**
         * Returns true if the dropped content is a file; otherwise, false.
         *
         * @param comp the component receiving the drop
         * @param transferFlavors the data flavors of the content being dropped
         * @return true if the dropped content is a file; otherwise, false.
         */
        public boolean canImport(JComponent comp,
                DataFlavor[] transferFlavors)
        {
            for (DataFlavor flavor : transferFlavors)
            {
                if (flavor.isFlavorJavaFileListType())
                {
                    return true;
                }
            }

            return false;
        }


        // ----------------------------------------------------------
        /**
         * Sets the current submission for testing to the dropped file, if
         * possible.
         *
         * @param comp the component receiving the drop
         * @param t the data being dropped
         * @return true if the content was successfully dropped; otherwise,
         *     false.
         */
        public boolean importData(JComponent comp, Transferable t)
        {
            try
            {
                @SuppressWarnings("unchecked")
                List<File> files = (List<File>) t.getTransferData(
                        DataFlavor.javaFileListFlavor);

                if (files.size() == 1)
                {
                    setSubmission(files.get(0));
                    return true;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }


        //~ Instance/static variables .........................................

        private static final long serialVersionUID = -6870865104793930201L;
    }


    // ----------------------------------------------------------
    /**
     * Handles drop operations of a file onto the current submission text
     * field.
     */
    private class WebCATHomeTransferHandler extends TransferHandler
    {
        //~ Methods ...........................................................

        // ----------------------------------------------------------
        /**
         * Returns true if the dropped content is a file; otherwise, false.
         *
         * @param comp the component receiving the drop
         * @param transferFlavors the data flavors of the content being dropped
         * @return true if the dropped content is a file; otherwise, false.
         */
        public boolean canImport(JComponent comp,
                DataFlavor[] transferFlavors)
        {
            for (DataFlavor flavor : transferFlavors)
            {
                if (flavor.isFlavorJavaFileListType())
                {
                    return true;
                }
            }

            return false;
        }


        // ----------------------------------------------------------
        /**
         * Sets the current submission for testing to the dropped file, if
         * possible.
         *
         * @param comp the component receiving the drop
         * @param t the data being dropped
         * @return true if the content was successfully dropped; otherwise,
         *     false.
         */
        public boolean importData(JComponent comp, Transferable t)
        {
            try
            {
                @SuppressWarnings("unchecked")
                List<File> files = (List<File>) t.getTransferData(
                        DataFlavor.javaFileListFlavor);

                if (files.size() == 1)
                {
                    setWebCATHome(files.get(0));
                    return true;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }


        //~ Instance/static variables .........................................

        private static final long serialVersionUID = 771815718413890191L;
    }


    // ----------------------------------------------------------
    private class PluginsTransferHandler extends TransferHandler
    {
        //~ Methods ...........................................................

        // ----------------------------------------------------------
        /**
         * Returns true if the dropped content is a file; otherwise, false.
         *
         * @param comp the component receiving the drop
         * @param transferFlavors the data flavors of the content being dropped
         * @return true if the dropped content is a file; otherwise, false.
         */
        public boolean canImport(JComponent comp,
                DataFlavor[] transferFlavors)
        {
            for (DataFlavor flavor : transferFlavors)
            {
                if (flavor.isFlavorJavaFileListType())
                {
                    return true;
                }
            }

            return false;
        }


        // ----------------------------------------------------------
        /**
         * Adds the dropped file to the plugin chain, if possible.
         *
         * @param comp the component receiving the drop
         * @param t the data being dropped
         * @return true if the content was successfully dropped; otherwise,
         *     false.
         */
        public boolean importData(JComponent comp, Transferable t)
        {
            try
            {
                @SuppressWarnings("unchecked")
                List<File> files = (List<File>) t.getTransferData(
                        DataFlavor.javaFileListFlavor);

                if (files.size() == 1)
                {
                    addPlugin(files.get(0));
                    return true;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        }


        //~ Instance/static variables .........................................

        private static final long serialVersionUID = -99073136818056014L;
    }


    //~ Instance/static variables .............................................

    private JFrame frame;
    private JFileChooser fileChooser;
    private JPanel mainPanel;
    private JTextField webCatHomeField;
    private JButton webCatHomeBrowseButton;
    private JTextField submissionField;
    private JButton submissionBrowseButton;
    private JPanel propertiesPanel;
    private JEditorPane propertiesEditor;
    private JPanel documentationPanel;
    private JEditorPane documentationEditor;
    private JTable pluginsTable;
    private JButton pluginAddButton;
    private JButton pluginRemoveButton;
    private JPanel bottomPanel;
    private JButton runButton;

    private PluginsTableModel pluginsModel;
    private Timer propertiesTimer;

    private WebCATConfiguration webcatConfig;
    private Properties currentSettings;
}
