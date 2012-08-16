/*==========================================================================*\
 |  $Id: ReporterDatabaseUpdates.java,v 1.2 2010/10/14 18:45:10 stedwar2 Exp $
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

import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.webcat.dbupdate.UpdateSet;
import org.webcat.jobqueue.JobQueueDatabaseUpdates;

//-------------------------------------------------------------------------
/**
 * This class captures the SQL database schema for the database tables
 * underlying the Reporter subsystem and the Reporter.eomodeld.  Logging
 * output for this class uses its parent class' logger.
 *
 * @author Tony Allevato
 * @version $Id: ReporterDatabaseUpdates.java,v 1.2 2010/10/14 18:45:10 stedwar2 Exp $
 */
public class ReporterDatabaseUpdates
    extends UpdateSet
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * The default constructor uses the name "reporter" as the unique
     * identifier for this subsystem and EOModel.
     */
    public ReporterDatabaseUpdates()
    {
        super("reporter");
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Creates all tables in their baseline configuration, as needed.
     * @throws SQLException on error
     */
    @Override
    public void updateIncrement0() throws SQLException
    {
        createReportTemplateTable();
        createReportDataSetTable();
        createReportQueryTable();
        createReportDataSetQueryTable();
        createEnqueuedReportGenerationJobTable();
        createEnqueuedReportRenderJobTable();
        createGeneratedReportTable();
    }


    // ----------------------------------------------------------
    /**
     * Add support for input parameters to report templates.
     *
     * @throws SQLException on error
     */
    public void updateIncrement1() throws SQLException
    {
        database().executeSQL("ALTER TABLE TREPORTTEMPLATE ADD COLUMN "
                + "CPARAMETERS BLOB");
        database().executeSQL("ALTER TABLE TREPORTTEMPLATE ADD COLUMN "
                + "CUPDATEMUTABLEFIELDS BIT NOT NULL");
    }


    // ----------------------------------------------------------
    /**
     * Drops the no-longer-used EnqueuedReportRenderJob table from the
     * database.
     *
     * @throws SQLException on error
     */
    public void updateIncrement2() throws SQLException
    {
        database().executeSQL("DROP TABLE TENQUEUEDREPORTRENDERJOB");
    }


    // ----------------------------------------------------------
    /**
     * Creates the new ReportGenerationJob table (based on the JobQueue
     * subsystem) and fixes various other relationships to use the new table.
     *
     * @throws SQLException on error
     */
    public void updateIncrement3() throws SQLException
    {
        createReportGenerationJobTable();

        database().executeSQL("ALTER TABLE TREPORTDATASETQUERY DROP "
                + "CENQUEUEDREPORTJOBID");

        database().executeSQL("DROP TABLE TENQUEUEDREPORTGENERATIONJOB");
    }


    // ----------------------------------------------------------
    /**
     * Truncates existing generated reports since the report query mechanism
     * has been generalized and moved into Core.
     *
     * @throws SQLException on error
     */
    public void updateIncrement4() throws SQLException
    {
        database().executeSQL("TRUNCATE TABLE TREPORTDATASETQUERY");
        database().executeSQL("TRUNCATE TABLE TGENERATEDREPORT");

        database().executeSQL("DROP TABLE TREPORTQUERY");
    }


    // ----------------------------------------------------------
    /**
     * Add indexes for better performance.
     * @throws SQLException on error
     */
    public void updateIncrement5() throws SQLException
    {
        // Indices for GeneratedReport
        createIndexFor("TGENERATEDREPORT", "CUSERID");
        createIndexFor("TGENERATEDREPORT", "CREPORTTEMPLATEID");

        // Indices for ReportDataSet
        createIndexFor("TREPORTDATASET", "CREPORTTEMPLATEID");

        // Indices for ReportDataSetQuery
        createIndexFor("TREPORTDATASETQUERY", "CDATASETID");
        createIndexFor("TREPORTDATASETQUERY", "CGENERATEDREPORTID");
        createIndexFor("TREPORTDATASETQUERY", "CREPORTQUERYID");

        // Indices for ReportGenerationJob
        // None

        // Indices for ReportTemplate
        createIndexFor("TREPORTTEMPLATE", "CBRANCHEDFROMTEMPLATEID");
        createIndexFor("TREPORTTEMPLATE", "CPREDECESSORTEMPLATEID");
        createIndexFor("TREPORTTEMPLATE", "CROOTTEMPLATEID");
        createIndexFor("TREPORTTEMPLATE", "CUSERID");
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Create the TREPORTTEMPLATE table, if needed.
     * @throws SQLException on error
     */
    private void createReportTemplateTable() throws SQLException
    {
        if ( !database().hasTable( "TREPORTTEMPLATE" ) )
        {
            log.info( "creating table TREPORTTEMPLATE" );

            database().executeSQL("CREATE TABLE TREPORTTEMPLATE ("
                + "OID INTEGER NOT NULL , "
                + "CNAME TINYTEXT NOT NULL , "
                + "CDESCRIPTION MEDIUMTEXT , "
                + "CUSERID INTEGER , "
                + "CISPUBLISHED BIT NOT NULL , "
                + "CUPLOADEDTIME DATETIME , "
                + "CVERSION TINYTEXT , "
                + "CCHANGEHISTORY MEDIUMTEXT , "
                + "CROOTTEMPLATEID INTEGER , "
                + "CBRANCHEDFROMTEMPLATEID INTEGER , "
                + "CPREDECESSORTEMPLATEID INTEGER , "
                + "CCHECKSUM TINYTEXT , "
                + "CDESIGNELEMENTS MEDIUMTEXT , "
                + "CLANGUAGE TINYTEXT , "
                + "CPREFERREDRENDERER TINYTEXT )" );
            database().executeSQL(
                "ALTER TABLE TREPORTTEMPLATE ADD PRIMARY KEY (OID)" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the TREPORTDATASET table, if needed.
     * @throws SQLException on error
     */
    private void createReportDataSetTable() throws SQLException
    {
        if ( !database().hasTable( "TREPORTDATASET" ) )
        {
            log.info( "creating table TREPORTDATASET" );

            database().executeSQL("CREATE TABLE TREPORTDATASET ("
                + "OID INTEGER NOT NULL , "
                + "CNAME TINYTEXT , "
                + "CDESCRIPTION MEDIUMTEXT , "
                + "CENTITYNAME TINYTEXT NOT NULL , "
                + "CREPORTTEMPLATEID INTEGER , "
                + "CREFERENCECOUNT INTEGER , "
                + "CCONSTRAINTS BLOB , "
                + "CUPDATEMUTABLEFIELDS BIT NOT NULL )" );
            database().executeSQL(
                "ALTER TABLE TREPORTDATASET ADD PRIMARY KEY (OID)" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the TREPORTQUERY table, if needed.
     * @throws SQLException on error
     */
    private void createReportQueryTable() throws SQLException
    {
        if ( !database().hasTable( "TREPORTQUERY" ) )
        {
            log.info( "creating table TREPORTQUERY" );

            database().executeSQL("CREATE TABLE TREPORTQUERY ("
                + "OID INTEGER NOT NULL , "
                + "CDESCRIPTION MEDIUMTEXT , "
                + "CUSERID INTEGER NOT NULL ,"
                + "CENTITYNAME TINYTEXT NOT NULL , "
                + "CQUERYINFO BLOB , "
                + "CUPDATEMUTABLEFIELDS BIT NOT NULL)");
            database().executeSQL(
                "ALTER TABLE TREPORTQUERY ADD PRIMARY KEY (OID)");
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the TREPORTDATASETQUERY table, if needed.
     * @throws SQLException on error
     */
    private void createReportDataSetQueryTable() throws SQLException
    {
        if ( !database().hasTable( "TREPORTDATASETQUERY" ) )
        {
            log.info( "creating table TREPORTDATASETQUERY" );

            database().executeSQL("CREATE TABLE TREPORTDATASETQUERY ("
                + "OID INTEGER NOT NULL , "
                + "CENQUEUEDREPORTJOBID INTEGER , "
                + "CGENERATEDREPORTID INTEGER , "
                + "CDATASETID INTEGER NOT NULL , "
                + "CREPORTQUERYID INTEGER NOT NULL)");
            database().executeSQL(
                "ALTER TABLE TREPORTDATASETQUERY ADD PRIMARY KEY (OID)");
        }
    }

    // ----------------------------------------------------------
    /**
     * Create the TENQUEUEDREPORTGENERATIONJOB table, if needed.
     * @throws SQLException on error
     */
    private void createEnqueuedReportGenerationJobTable() throws SQLException
    {
        if ( !database().hasTable( "TENQUEUEDREPORTGENERATIONJOB" ) )
        {
            log.info( "creating table TENQUEUEDREPORTGENERATIONJOB" );

            database().executeSQL("CREATE TABLE TENQUEUEDREPORTGENERATIONJOB ("
                + "OID INTEGER NOT NULL , "
                + "CUSERID INTEGER NOT NULL , "
                + "CQUEUETIME DATETIME , "
                + "CREPORTTEMPLATEID INTEGER , "
                + "CDESCRIPTION MEDIUMTEXT ) " );
            database().executeSQL(
                "ALTER TABLE TENQUEUEDREPORTGENERATIONJOB "
                    + "ADD PRIMARY KEY (OID)" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the TENQUEUEDREPORTRENDERJOB table, if needed.
     * @throws SQLException on error
     */
    private void createEnqueuedReportRenderJobTable() throws SQLException
    {
        if ( !database().hasTable( "TENQUEUEDREPORTRENDERJOB" ) )
        {
            log.info( "creating table TENQUEUEDREPORTRENDERJOB" );

            database().executeSQL("CREATE TABLE TENQUEUEDREPORTRENDERJOB ("
                + "OID INTEGER NOT NULL , "
                + "CUSERID INTEGER NOT NULL , "
                + "CQUEUETIME DATETIME , "
                + "CGENERATEDREPORTID INTEGER , "
                + "CRENDEREDRESOURCEACTIONURL MEDIUMTEXT , "
                + "CRENDERINGMETHOD TINYTEXT )" );
            database().executeSQL(
                "ALTER TABLE TENQUEUEDREPORTRENDERJOB ADD PRIMARY KEY (OID)" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the TGENERATEDREPORT table, if needed.
     * @throws SQLException on error
     */
    private void createGeneratedReportTable() throws SQLException
    {
        if ( !database().hasTable( "TGENERATEDREPORT" ) )
        {
            log.info( "creating table TGENERATEDREPORT" );

            database().executeSQL("CREATE TABLE TGENERATEDREPORT ("
                + "OID INTEGER NOT NULL , "
                + "CUSERID INTEGER NOT NULL , "
                + "CDESCRIPTION MEDIUMTEXT , "
                + "CREPORTTEMPLATEID INTEGER NOT NULL , "
                + "CCOMPLETE BIT NOT NULL , "
                + "CGENERATEDTIME DATETIME , "
                + "CERRORS BLOB , "
                + "CUPDATEMUTABLEFIELDS BIT NOT NULL )" );
            database().executeSQL(
                "ALTER TABLE TGENERATEDREPORT ADD PRIMARY KEY (OID)" );
        }
    }


    // ----------------------------------------------------------
    /**
     * Create the TReportGenerationJob table, if needed.
     * @throws SQLException on error
     */
    private void createReportGenerationJobTable() throws SQLException
    {
        if ( !database().hasTable( "TReportGenerationJob" ) )
        {
            log.info( "creating table TReportGenerationJob" );

            database().executeSQL("CREATE TABLE TReportGenerationJob ("
                + "OID INTEGER NOT NULL , "
                + "generatedReportId INTEGER )");
            database().executeSQL(
                "ALTER TABLE TReportGenerationJob ADD PRIMARY KEY (OID)" );
        }
    }


    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger( UpdateSet.class );
}
