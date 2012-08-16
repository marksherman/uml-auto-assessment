/*==========================================================================*\
 |  $Id: Application.java,v 1.1 2010/05/10 16:15:19 aallowat Exp $
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

package org.webcat.plugintester;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.webcat.plugintester.ui.MainFrameBuilder;
import org.webcat.plugintester.util.ImmediateProperties;
import org.webcat.plugintester.util.WebCATConfiguration;

//-------------------------------------------------------------------------
/**
 * Handles application startup.
 * 
 * @author Tony Allevato
 * @version $Id: Application.java,v 1.1 2010/05/10 16:15:19 aallowat Exp $
 */
public class Application
{
    //~ Entry point ...........................................................

    // ----------------------------------------------------------
    /**
     * The main entry point for the application.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(final String[] args)
    {
        doOSSpecificInitialization();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Application(args);
            }
        });
    }

    
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the application.
     * 
     * @param args command line arguments passed to the application
     */
    public Application(String[] args)
    {
        currentSettings = new ImmediateProperties(AppConstants.CONFIG_FILE);

        if (currentSettings.getProperty(AppConstants.PROP_WEBCAT_HOME) == null)
        {
            boolean success = askUserForWebCATHome();
            
            if (!success)
            {
                System.exit(0);
            }
        }
        
        webcatConfig = new WebCATConfiguration(currentSettings.getProperty(
                AppConstants.PROP_WEBCAT_HOME));

        MainFrameBuilder builder = new MainFrameBuilder(
                webcatConfig, currentSettings);
        builder.openFrame();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Performs any OS-specific initialization that is required to improve the
     * usability of the application, such as setting the application title that
     * will appear in the menu bar on Mac OS X.
     */
    private static void doOSSpecificInitialization()
    {
        System.setProperty(
                "com.apple.mrj.application.apple.menu.about.name",
                "Web-CAT Plug-in Tester");
    }


    // ----------------------------------------------------------
    /**
     * Asks the user to specify the location of the Web-CAT server software if
     * it is their first time running this tool.
     * 
     * @return true if the user specified a location; false if they canceled.
     */
    private boolean askUserForWebCATHome()
    {
        JOptionPane.showMessageDialog(null,
                "This appears to be the first time you've run the Web-CAT\n" +
                "Plug-in Tester. This application must be running on a system\n" +
                "that has a copy of the Web-CAT server software. When you click\n" +
                "OK you will be asked to specify the location of Web-CAT.\n\n" +
                "If you do not have a copy of Web-CAT, you can download it by\n" +
                "going to http://web-cat.org, clicking the \"SourceForge tab\", then\n" +
                "\"Downloads\", and downloading the latest version of the\n" +
                "\"Web-CAT Servlet\" package. Once the WAR file is downloaded,\n" +
                "it does not need to be installed; simply extract it (you may need\n" +
                "to rename it from .war to .zip) and specify that location in the\n" +
                "next dialog.",
                "Web-CAT Plug-in Tester",
                JOptionPane.INFORMATION_MESSAGE);
        
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            String homePath = chooser.getSelectedFile().getAbsolutePath();
            currentSettings.setProperty(AppConstants.PROP_WEBCAT_HOME, homePath);
            return true;
        }
        else
        {
            return false;
        }
    }


    //~ Instance/static variables .............................................
    
    private WebCATConfiguration webcatConfig;
    private ImmediateProperties currentSettings;
}
