/*==========================================================================*\
 |  $Id: OutcomesMeasurementDatabaseUpdates.java,v 1.3 2010/11/15 03:27:12 ringenmt Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
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

package org.webcat.outcomesmeasurement;

import org.webcat.dbupdate.UpdateSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

//-------------------------------------------------------------------------
/**
 * This class captures the SQL database schema for the database tables
 * underlying the OutcomesMeasurement subsystem and the
 * OutcomesMeasurement.eomodeld.  Logging output for this class uses its
 * parent class' logger.
 *
 * @author  Stephen Edwards
 * @author Last changed by $Author: ringenmt $
 * @version $Revision: 1.3 $, $Date: 2010/11/15 03:27:12 $
 */
public class OutcomesMeasurementDatabaseUpdates
extends UpdateSet
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * The default constructor uses the name "grader" as the unique
	 * identifier for this subsystem and EOModel.
	 */
	public OutcomesMeasurementDatabaseUpdates()
	{
		super("outcomesmeasurement");
	}


	//~ Public Methods ........................................................

	// ----------------------------------------------------------
	/**
	 * Creates all tables in their baseline configuration, as needed.
	 * @throws SQLException on error
	 */
	public void updateIncrement0() throws SQLException
	{
		createAccreditingBodyTable();
		createAccreditingBodyOutcomeSetTable();
		createExternalOutcomeTable();
		createFacultyReflectionTable();
		createMeasureTable();
		createMeasureChangeTable();
		createMeasureCourseTable();
		createMeasureOfOfferingTable();
		createMeasureOutcomePairTable();
		createMeasureProgramOutcomeTable();
		createOutcomePairTable();
		createOutcomeTable();
		createOutcomeSetTable();
		createOutcomeSetOutcomeTable();
		createOutcomeToOutcomeTable();
		createProgramTable();
		createProgramOutcomeTable();
		createProgramOutcomeSetTable();
	}

	public void updateIncrement1() throws SQLException {
		createCourseWorkTable();
		createStudentAnswerTable();
	}
	
	public void updateIncrement2() throws SQLException {
		alterCourseWorkTable();
	}


	//~ Private Methods .......................................................

	// ----------------------------------------------------------
	/**
	 * Create the AccreditingBody table, if needed.
	 * @throws SQLException on error
	 */
	private void createAccreditingBodyTable() throws SQLException
	{
		if ( !database().hasTable( "AccreditingBody" ) )
		{
			log.info( "creating table AccreditingBody" );
			database().executeSQL(
					"CREATE TABLE AccreditingBody "
					+ "(name TINYTEXT NOT NULL, OID INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE AccreditingBody ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the AccreditingBodyOutcomeSet table, if needed.
	 * @throws SQLException on error
	 */
	private void createAccreditingBodyOutcomeSetTable() throws SQLException
	{
		if ( !database().hasTable( "AccreditingBodyOutcomeSet" ) )
		{
			log.info( "creating table AccreditingBodyOutcomeSet" );
			database().executeSQL(
					"CREATE TABLE AccreditingBodyOutcomeSet "
					+ "(accreditingBodyOid INTEGER NOT NULL, "
					+ "outcomeSetId INTEGER NOT NULL)");
			database().executeSQL(
					"ALTER TABLE AccreditingBodyOutcomeSet ADD PRIMARY KEY "
					+ "(accreditingBodyOid, outcomeSetId)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the ExternalOutcome table, if needed.
	 * @throws SQLException on error
	 */
	private void createExternalOutcomeTable() throws SQLException
	{
		if ( !database().hasTable( "ExternalOutcome" ) )
		{
			log.info( "creating table ExternalOutcome" );
			database().executeSQL(
					"CREATE TABLE ExternalOutcome "
					+ "(accreditingBodyId INTEGER NOT NULL, "
					+ "OID INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE ExternalOutcome ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the FacultyReflection table, if needed.
	 * @throws SQLException on error
	 */
	private void createFacultyReflectionTable() throws SQLException
	{
		if ( !database().hasTable( "FacultyReflection" ) )
		{
			log.info( "creating table FacultyReflection" );
			database().executeSQL(
					"CREATE TABLE FacultyReflection "
					+ "(courseOfferingId INTEGER NOT NULL, lastChange DATETIME, "
					+ "modifications LONGTEXT, OID INTEGER NOT NULL, "
					+ "proposedActions LONGTEXT, reflection LONGTEXT, "
					+ "studentFeedback LONGTEXT, userId INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE FacultyReflection ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the Measure table, if needed.
	 * @throws SQLException on error
	 */
	private void createMeasureTable() throws SQLException
	{
		if ( !database().hasTable( "Measure" ) )
		{
			log.info( "creating table Measure" );
			database().executeSQL(
					"CREATE TABLE Measure "
					+ "(description MEDIUMTEXT, keyPhrase TINYTEXT, "
					+ "label TINYTEXT, OID INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE Measure ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the MeasureChange table, if needed.
	 * @throws SQLException on error
	 */
	private void createMeasureChangeTable() throws SQLException
	{
		if ( !database().hasTable( "MeasureChange" ) )
		{
			log.info( "creating table MeasureChange" );
			database().executeSQL(
					"CREATE TABLE MeasureChange "
					+ "(changes BLOB, measureOfOfferingOid INTEGER NOT NULL, "
					+ "time DATETIME NOT NULL, CUPDATEMUTABLEFIELDS BIT NOT NULL, "
					+ "userId INTEGER NOT NULL, OID INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE MeasureChange ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the MeasureCourse table, if needed.
	 * @throws SQLException on error
	 */
	private void createMeasureCourseTable() throws SQLException
	{
		if ( !database().hasTable( "MeasureCourse" ) )
		{
			log.info( "creating table MeasureCourse" );
			database().executeSQL(
					"CREATE TABLE MeasureCourse "
					+ "(courseOid INTEGER NOT NULL, measureOid INTEGER NOT NULL)");
			database().executeSQL(
					"ALTER TABLE MeasureCourse ADD PRIMARY KEY "
					+ "(courseOid, measureOid)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the MeasureOfOffering table, if needed.
	 * @throws SQLException on error
	 */
	private void createMeasureOfOfferingTable() throws SQLException
	{
		if ( !database().hasTable( "MeasureOfOffering" ) )
		{
			log.info( "creating table MeasureOfOffering" );
			database().executeSQL(
					"CREATE TABLE MeasureOfOffering "
					+ "(adequate INTEGER, adequateCriteria MEDIUMTEXT, "
					+ "courseOfferingId INTEGER, dataDescription MEDIUMTEXT, "
					+ "excellent INTEGER, excellentCriteria MEDIUMTEXT, "
					+ "measureId INTEGER NOT NULL, OID INTEGER NOT NULL, "
					+ "unsatisfactory INTEGER, "
					+ "unsatisfactoryCriteria MEDIUMTEXT)");
			database().executeSQL(
			"ALTER TABLE MeasureOfOffering ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the MeasureOutcomePair table, if needed.
	 * @throws SQLException on error
	 */
	private void createMeasureOutcomePairTable() throws SQLException
	{
		if ( !database().hasTable( "MeasureOutcomePair" ) )
		{
			log.info( "creating table MeasureOutcomePair" );
			database().executeSQL(
					"CREATE TABLE MeasureOutcomePair "
					+ "(measureOid INTEGER NOT NULL, outcomePairOid INTEGER NOT NULL)");
			database().executeSQL(
					"ALTER TABLE MeasureOutcomePair ADD PRIMARY KEY "
					+ "(measureOid, outcomePairOid)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the MeasureProgramOutcome table, if needed.
	 * @throws SQLException on error
	 */
	private void createMeasureProgramOutcomeTable() throws SQLException
	{
		if ( !database().hasTable( "MeasureProgramOutcome" ) )
		{
			log.info( "creating table MeasureProgramOutcome" );
			database().executeSQL(
					"CREATE TABLE MeasureProgramOutcome "
					+ "(measureOid INTEGER NOT NULL, "
					+ "programOutcomeOid INTEGER NOT NULL)");
			database().executeSQL(
					"ALTER TABLE MeasureProgramOutcome ADD PRIMARY KEY "
					+ "(measureOid, programOutcomeOid)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the Outcome table, if needed.
	 * @throws SQLException on error
	 */
	private void createOutcomeTable() throws SQLException
	{
		if ( !database().hasTable( "Outcome" ) )
		{
			log.info( "creating table Outcome" );
			database().executeSQL(
					"CREATE TABLE Outcome "
					+ "(description MEDIUMTEXT, keyPhrase TINYTEXT, "
					+ "label TINYTEXT, OID INTEGER NOT NULL, "
					+ "microLabel TINYTEXT)");
			database().executeSQL(
			"ALTER TABLE Outcome ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the OutcomePair table, if needed.
	 * @throws SQLException on error
	 */
	private void createOutcomePairTable() throws SQLException
	{
		if ( !database().hasTable( "OutcomePair" ) )
		{
			log.info( "creating table OutcomePair" );
			database().executeSQL(
					"CREATE TABLE OutcomePair "
					+ "(externalOutcomeId INTEGER NOT NULL, OID INTEGER NOT NULL, "
					+ "programOutcomeId INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE OutcomePair ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the OutcomeSet table, if needed.
	 * @throws SQLException on error
	 */
	private void createOutcomeSetTable() throws SQLException
	{
		if ( !database().hasTable( "OutcomeSet" ) )
		{
			log.info( "creating table OutcomeSet" );
			database().executeSQL(
					"CREATE TABLE OutcomeSet "
					+ "(OID INTEGER NOT NULL, semesterId INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE OutcomeSet ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the OutcomeSetOutcome table, if needed.
	 * @throws SQLException on error
	 */
	private void createOutcomeSetOutcomeTable() throws SQLException
	{
		if ( !database().hasTable( "OutcomeSetOutcome" ) )
		{
			log.info( "creating table OutcomeSetOutcome" );
			database().executeSQL(
					"CREATE TABLE OutcomeSetOutcome "
					+ "(outcomeOid INTEGER NOT NULL, "
					+ "outcomeSetId INTEGER NOT NULL)");
			database().executeSQL(
					"ALTER TABLE OutcomeSetOutcome ADD PRIMARY KEY "
					+ "(outcomeOid, outcomeSetId)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the OutcomeToOutcome table, if needed.
	 * @throws SQLException on error
	 */
	private void createOutcomeToOutcomeTable() throws SQLException
	{
		if ( !database().hasTable( "OutcomeToOutcome" ) )
		{
			log.info( "creating table OutcomeToOutcome" );
			database().executeSQL(
					"CREATE TABLE OutcomeToOutcome "
					+ "(outcomeOid INTEGER NOT NULL, "
					+ "outcomeOid1 INTEGER NOT NULL)");
			database().executeSQL(
					"ALTER TABLE OutcomeToOutcome ADD PRIMARY KEY "
					+ "(outcomeOid, outcomeOid1)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the Program table, if needed.
	 * @throws SQLException on error
	 */
	private void createProgramTable() throws SQLException
	{
		if ( !database().hasTable( "Program" ) )
		{
			log.info( "creating table Program" );
			database().executeSQL(
					"CREATE TABLE Program "
					+ "(departmentId INTEGER NOT NULL, name TINYTEXT, "
					+ "OID INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE Program ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the ProgramOutcome table, if needed.
	 * @throws SQLException on error
	 */
	private void createProgramOutcomeTable() throws SQLException
	{
		if ( !database().hasTable( "ProgramOutcome" ) )
		{
			log.info( "creating table ProgramOutcome" );
			database().executeSQL(
					"CREATE TABLE ProgramOutcome "
					+ "(OID INTEGER NOT NULL, programId INTEGER NOT NULL)");
			database().executeSQL(
			"ALTER TABLE ProgramOutcome ADD PRIMARY KEY (OID)" );
		}
	}


	// ----------------------------------------------------------
	/**
	 * Create the ProgramOutcomeSet table, if needed.
	 * @throws SQLException on error
	 */
	private void createProgramOutcomeSetTable() throws SQLException
	{
		if ( !database().hasTable( "ProgramOutcomeSet" ) )
		{
			log.info( "creating table ProgramOutcomeSet" );
			database().executeSQL(
					"CREATE TABLE ProgramOutcomeSet "
					+ "(outcomeSetId INTEGER NOT NULL, "
					+ "programOid INTEGER NOT NULL)");
			database().executeSQL(
					"ALTER TABLE ProgramOutcomeSet ADD PRIMARY KEY "
					+ "(outcomeSetId, programOid)" );
		}
	}

	private void createStudentAnswerTable() throws SQLException {
		if (!database().hasTable("studentanswer")){
			log.info("creating table StudentAnswer");
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE studentanswer (");
			sb.append("courseworkId INTEGER NOT NULL,");
			sb.append("userId INTEGER NOT NULL,");
			sb.append("pointsEarned INTEGER,");
			sb.append("percentEarned FLOAT, ");
			sb.append("response text)");
			database().executeSQL(sb.toString());
			database().executeSQL(
					"ALTER TABLE studentanswer ADD PRIMARY KEY "
					+ "(courseworkId, userId)" );
		}
	}

	private void createCourseWorkTable() throws SQLException {
		if (!database().hasTable("coursework")){
			log.info("Creating table Coursework");
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE Coursework (");
			sb.append("OID INTEGER NOT NULL,");
			sb.append("max_result FLOAT,");
			sb.append("dateCompleted DATETIME, ");
			sb.append("courseOfferingId INTEGER,");
			sb.append("outcomePairId INTEGER,");
			sb.append("description MEDIUMTEXT)");
			database().executeSQL(sb.toString());
			database().executeSQL(
					"ALTER TABLE Coursework ADD PRIMARY KEY "
					+ "(OID)" );
		}
	}

// ****************************************************************
// This set of tables is used to break the question and coursework 
// into two separate database tables, where the questions can be 
// tied to an assignment and outcome mappings could be made to
// either the coursework or to the question. 
// ****************************************************************	
//	private void createCourseworkQuestionTable() throws SQLException {
//		if (!database().hasTable("courseworkquestion")){
//			log.info("creating table CourseworkQuestion");
//			StringBuilder sb = new StringBuilder();
//			sb.append("CREATE TABLE CourseworkQuestion (");
//			sb.append("questionOid INTEGER NOT NULL,");
//			sb.append("courseworkOid INTEGER NOT NULL,");
//			sb.append("question_text TEXT,");
//			sb.append("max_result INTEGER)");
//			database().executeSQL(sb.toString());
//			database().executeSQL(
//					"ALTER TABLE CourseworkQuestion ADD PRIMARY KEY "
//					+ "(questionOid, courseworkOid)" );
//		}
//	}
//
//	private void createCourseWorkTable() throws SQLException {
//		if (!database().hasTable("Coursework")){
//			log.info("creating table Coursework");
//			StringBuilder sb = new StringBuilder();
//			sb.append("CREATE TABLE Coursework (");
//			sb.append("OID INTEGER NOT NULL,");
//			sb.append("question_num INTEGER,");
//			sb.append("description text,");
//			sb.append("type text,");
//			sb.append("dateCompleted DATETIME, ");
//			sb.append("group_project bit NOT NULL,");
//			sb.append("courseOfferingId INTEGER)");
//			database().executeSQL(sb.toString());
//			database().executeSQL(
//					"ALTER TABLE Coursework ADD PRIMARY KEY "
//					+ "(OID)" );
//		}
//	}
// ****************************************************************
	public void createCourseworkOutcomeTable() throws SQLException {
		if (!database().hasTable("outcometocoursework")){
			log.info("creating table OutcomeToCoursework");
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE OutcomeToCoursework (");
			sb.append("Oid INT NOT NULL,");
			sb.append("courseworkOid INT NOT NULL,");
			sb.append("externalOutcomeOid INT NOT NULL,");
			sb.append("programOutcomeOid INT NOT NULL)");
			database().executeSQL(sb.toString());
			database().executeSQL("alter table outcometocoursework " + 
					"add primary key (OID)");
		}
	}

	public void createStudentProgramTable() throws SQLException {
		if (!database().hasTable("StudentProgram")){
			log.info("creating table StudentProgram");
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE StudentProgram (");
			sb.append("userOID INT NOT NULL,");
			sb.append("programOID INT NOT NULL,");
			sb.append("startDate DATE,");
			sb.append("endDate DATE,");
			sb.append("transfer bit,");
			sb.append("dropped bit)");
			database().executeSQL(sb.toString());
			database().executeSQL(
					"ALTER TABLE StudentProgram ADD PRIMARY KEY "
					+ "(userOID, programOID");
		}
	}

	public void alterCourseWorkTable() throws SQLException {
		log.info("altering table coursework");
		database().executeSQL("alter table Coursework add excellentCutoff FLOAT" );
		database().executeSQL("alter table Coursework add moderateCutoff FLOAT" );
	}
	
	//~ Instance/static variables .............................................

	static Logger log =
		Logger.getLogger(OutcomesMeasurementDatabaseUpdates.class);
}
