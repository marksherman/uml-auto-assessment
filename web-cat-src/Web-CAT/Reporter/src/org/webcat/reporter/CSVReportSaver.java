/*==========================================================================*\
 |  $Id: CSVReportSaver.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

package org.webcat.reporter;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.webcat.core.DeliverFile;
import org.webcat.core.NSMutableDataOutputStream;

//-------------------------------------------------------------------------
/**
 * Renders and saves a generated report in CSV format.
 *
 * @author  Tony Allevato
 * @version $Id: CSVReportSaver.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class CSVReportSaver extends AbstractReportSaver
{
    //~ Constructors ..........................................................
    
    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the CSV report saver.
     * 
     * @param report the generated report to be saved
     * @param resultSetName the name of the result set to export, or null to
     *     export all result sets in a zipped archive
     */
    public CSVReportSaver(GeneratedReport report, String resultSetName)
    {
        super(report);

        this.resultSetName = resultSetName;
    }

    
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Saves the rendered report to the specified DeliverFile component, which
     * the caller will return back to the user.
     * 
     * @param file the DeliverFile component to save the file into
     * @return a Throwable if an error occurred (which the caller should
     *     display to the user), or null if the operation was successful
     */
    public Throwable deliverTo(DeliverFile file)
    {
        try
        {
            NSMutableDataOutputStream outputStream =
                new NSMutableDataOutputStream();

            IDataExtractionTask task =
                reportEngine().createDataExtractionTask(reportDocument());

            if (resultSetName != null)
            {
                writeResultSetToOutputStream(task, resultSetName, outputStream);

                file.setContentType("application/csv");
                file.setDeliveredName(generatedReport().description() + "-" +
                        resultSetName + ".csv");
            }
            else
            {
                zipResultSetsToOutputStream(task, outputStream);

                file.setContentType("application/zip");
                file.setDeliveredName(generatedReport().description() +
                        "-csvs.zip");
            }

            task.close();
            outputStream.close();
            close();

            file.setFileData(outputStream.data());
            file.setStartDownload(true);
        }
        catch (Exception e)
        {
            return e;
        }
        
        return null;
    }
    
    
    // ----------------------------------------------------------
    /**
     * Writes the specified result set as a CSV file to the output stream.
     * 
     * @param resultSet the name of the result set to export
     * @param outputStream the output stream to write the CSV file to
     * @throws BirtException if there was an error extracting the data
     */
    private void writeResultSetToOutputStream(IDataExtractionTask task,
            String resultSet, OutputStream stream) throws BirtException
    {
        task.selectResultSet(resultSet);
        IExtractionResults extraction = task.extract();

        if (extraction != null)
        {
            // Create the CSV file.
            PrintWriter writer = new PrintWriter(stream);

            // Write the column names to the first line of the file.
            IResultMetaData metadata = extraction.getResultMetaData();
            int columnCount = metadata.getColumnCount();
            writer.print('"');
            writer.print(metadata.getColumnLabel(0));
            writer.print('"');
            for (int i = 1; i < columnCount; i++)
            {
                writer.print(",\"");
                writer.print(metadata.getColumnLabel(i));
                writer.print('"');
            }
            writer.println();

            // Write the values for each row into the CSV file.
            IDataIterator it = extraction.nextResultIterator();
            if (it != null)
            {
                while (it.next())
                {
                    writer.print(getColumnValue(it, 0));

                    for (int i = 1; i < columnCount; i++)
                    {
                        writer.print(',');
                        writer.print(getColumnValue(it, i));
                    }

                    writer.println();
                }

                it.close();
            }

            writer.flush();
            extraction.close();
        }
    }

    
    // ----------------------------------------------------------
    /**
     * Writes a zip archive containing all of the result sets in the report
     * to the specified output stream.
     * 
     * @param outputStream the output stream to write the zip file to
     * @throws BirtException if there was an error extracting the data
     */
    private void zipResultSetsToOutputStream(IDataExtractionTask task,
            OutputStream stream) throws BirtException, IOException
    {
        List<IResultSetItem> resultSets = task.getResultSetList();

        ZipOutputStream zipStream = new ZipOutputStream(stream);

        for (IResultSetItem resultSet : resultSets)
        {
            String name = resultSet.getResultSetName();

            zipStream.putNextEntry(new ZipEntry(name + ".csv"));
            writeResultSetToOutputStream(task, name, zipStream);
            zipStream.closeEntry();
        }
        
        zipStream.finish();
    }
    
    
    // ----------------------------------------------------------
    /**
     * Gets the specified column value as a quoted string to be output to the
     * CSV file.
     * 
     * @param it the data iterator
     * @param index the index of the column to retrieve
     * @return the quoted string value of the column
     */
    private String getColumnValue(IDataIterator it, int index)
    {
        String value;

        try
        {
            value = it.getValue(index).toString();
            value = "\"" + value.replaceAll("\\\"", "\\\"\\\"") + "\"";
        }
        catch (Exception e)
        {
            value = "";
        }

        return value;
    }


    //~ Static/instance variables .............................................
    
    private String resultSetName;
}
