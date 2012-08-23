/*==========================================================================*\
 |  $Id: GeneratedReport.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

package org.webcat.reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.webcat.core.MutableArray;
import org.webcat.core.User;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOObjectStore;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;

// -------------------------------------------------------------------------
/**
 * Represents a report that is being generated or has been generated from a
 * report template.
 *
 * @author Tony Allevato
 * @version $Id: GeneratedReport.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class GeneratedReport
    extends _GeneratedReport
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new GeneratedReport object.
     */
    public GeneratedReport()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public ReportGenerationJob reportGenerationJob()
    {
        NSArray<ReportGenerationJob> jobs = reportGenerationJobs();

        if (jobs.count() > 0)
        {
            return jobs.objectAtIndex(0);
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    private static String generatedReportDirForUser(User user)
    {

        StringBuffer dir = new StringBuffer( 50 );
        dir.append( org.webcat.core.Application
            .configurationProperties().getProperty( "grader.submissiondir" ) );
        dir.append( '/' );
        dir.append( user.authenticationDomain().subdirName() );
        dir.append( '/' );
        dir.append( GENERATED_REPORTS_SUBDIR_NAME );
        dir.append( '/' );
        dir.append( user.userName() );
        return dir.toString();
    }


    // ----------------------------------------------------------
    private static String generatedReportFilePathForUser(
        User user, Number id)
    {
        return generatedReportDirForUser(user) + "/" + id.toString() +
            REPORT_EXTENSION;
    }


    // ----------------------------------------------------------
    public String renderedResourcesDir()
    {
        StringBuffer dir = new StringBuffer( 50 );
        dir.append( org.webcat.core.Application
            .configurationProperties().getProperty( "grader.submissiondir" ) );
        dir.append( "/" );
        dir.append( RENDERED_REPORTS_SUBDIR_NAME );
        dir.append( "/" );
        dir.append( id().toString() );

        return dir.toString();
    }


    // ----------------------------------------------------------
    /**
     * Gets rid of the rendered files for this report (everything except for
     * the rendering.properties file). This can be used in a cronjob to free up
     * space if necessary, since the pages can always be re-rendered on demand.
     */
    public void clearRenderedFiles()
    {
        File dir = new File(renderedResourcesDir());
        for (File file : dir.listFiles())
        {
            if (!file.getName().equals(RENDER_PROPERTIES_FILE))
            {
                file.delete();
            }
        }
    }


    // ----------------------------------------------------------
    public String renderedHTMLPagePath(int pageNumber)
    {
        return renderedResourcePath("page" + pageNumber + ".html");
    }


    // ----------------------------------------------------------
    public String renderedResourcePath(String filename)
    {
        return renderedResourcesDir() + "/" + filename;
    }


    // ----------------------------------------------------------
    public synchronized Properties renderingProperties()
    {
        Properties props = new Properties();
        FileInputStream stream = null;

        try
        {
            stream = new FileInputStream(
                    renderedResourcePath(RENDER_PROPERTIES_FILE));

            props.load(stream);
        }
        catch (FileNotFoundException e)
        {
            // Do nothing.
        }
        catch (IOException e)
        {
            log.error("An error occurred loading the render properties for"
                    + " the report \"" + description() + "\" (id=" + id()
                    + "): ", e);
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch (IOException e2)
                {
                    // Do nothing.
                }
            }
        }

        return props;
    }


    // ----------------------------------------------------------
    public synchronized void setRenderingProperties(Properties props)
    {
        FileOutputStream stream = null;

        try
        {
            stream = new FileOutputStream(
                    renderedResourcePath(RENDER_PROPERTIES_FILE));

            props.store(stream, null);
        }
        catch (IOException e)
        {
            log.error("An error occurred saving the render properties for"
                    + " the report \"" + description() + "\" (id=" + id()
                    + "): ", e);
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch (IOException e2)
                {
                    // Do nothing.
                }
            }
        }
    }


    // ----------------------------------------------------------
    public void prepareRenderingArea()
    {
        new File(renderedResourcesDir()).mkdirs();
        new File(renderedResourcePath(RENDER_PROPERTIES_FILE)).delete();
    }


    // ----------------------------------------------------------
    public boolean hasRenderingErrors()
    {
        File renderDir = new File(renderedResourcesDir());
        if (renderDir.exists())
        {
            File renderErrorFile = new File(renderDir, RENDER_ERRORS_FILE);
            if (renderErrorFile.exists())
            {
                return true;
            }
        }

        return false;
    }


    // ----------------------------------------------------------
    public MutableArray renderingErrors()
    {
        MutableArray errors = null;

        File renderDir = new File(renderedResourcesDir());
        if (renderDir.exists())
        {
            File renderErrorFile = new File(renderDir, RENDER_ERRORS_FILE);
            if (renderErrorFile.exists())
            {
                try
                {
                    FileInputStream stream = new FileInputStream(renderErrorFile);
                    NSData data = new NSData(stream, 1024);
                    errors = MutableArray.objectWithArchiveData(data);
                    stream.close();
                }
                catch (IOException e)
                {
                    log.error("Error reading rendering error info: ", e);
                }
            }
        }

        return errors;
    }


    // ----------------------------------------------------------
    public void setRenderingErrors(MutableArray errors)
    {
        File renderDir = new File(renderedResourcesDir());
        if (renderDir.exists())
        {
            try
            {
                File renderErrorFile = new File(renderDir, RENDER_ERRORS_FILE);
                FileOutputStream stream = new FileOutputStream(renderErrorFile);
                errors.archiveData().writeToStream(stream);
                stream.close();
            }
            catch (IOException e)
            {
                log.error("Error writing rendering error info: ", e);
            }
        }
    }


    // ----------------------------------------------------------
    public String generatedReportDir()
    {
        return generatedReportDirForUser(user());
    }


    // ----------------------------------------------------------
    public String generatedReportFile()
    {
        return generatedReportFilePathForUser(user(), id());
    }


    // ----------------------------------------------------------
    public IReportDocument openReportDocument()
    {
        String path = generatedReportFile();
        return Reporter.getInstance().openReportDocument(path);
    }


    // ----------------------------------------------------------
    @Override
    public void mightDelete()
    {
        log.debug("mightDelete()");
        if (isNewObject()) return;

        reportDocToDelete = generatedReportFile();
        renderedDirToDelete = renderedResourcesDir();

        super.mightDelete();
    }


    // ----------------------------------------------------------
    @Override
    public void didDelete( EOEditingContext context )
    {
        log.debug("didDelete()");
        super.didDelete( context );

        // should check to see if this is a child ec
        EOObjectStore parent = context.parentObjectStore();
        if (parent == null || !(parent instanceof EOEditingContext))
        {
            if (reportDocToDelete != null)
            {
                File file = new File(reportDocToDelete);

                if (file.exists())
                {
                    file.delete();
                }
            }

            if (renderedDirToDelete != null)
            {
                File dir = new File(renderedDirToDelete);
                if (dir.exists())
                {
                    org.webcat.core.FileUtilities.deleteDirectory(dir);
                }
            }
        }
    }


    //~ Static/instance variables .............................................

    public static final String REPORT_EXTENSION = ".rptdocument";

    private static final String RENDER_ERRORS_FILE = ".renderingErrors";

    private static final String GENERATED_REPORTS_SUBDIR_NAME =
        "GeneratedReports";

    private static final String RENDERED_REPORTS_SUBDIR_NAME =
        "RenderedReports";

    private static final String RENDER_PROPERTIES_FILE = "rendering.properties";

    private String reportDocToDelete;

    private String renderedDirToDelete;

    private static final Logger log = Logger.getLogger(GeneratedReport.class);
}
