/*==========================================================================*\
 |  $Id: MultipartBuilder.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter.internal.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Random;

//--------------------------------------------------------------------------
/**
 * A utility class to aid in the construction of an HTTP POST multipart
 * request.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class MultipartBuilder
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Generates a new multipart request builder attached to the specified
     * connection.
     *
     * @param con the connection that to which the multipart request will be
     *     sent
     * @throws IOException if any I/O errors occur
     */
    public MultipartBuilder(HttpURLConnection con) throws IOException
    {
        connection = con;

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundaryString);

        outStream = connection.getOutputStream();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Writes a boundary separator between parts of the request.
     *
     * @throws IOException if any I/O errors occur
     */
    private void writeBoundary() throws IOException
    {
        write("--");
        write(boundaryString);
    }


    // ----------------------------------------------------------
    /**
     * Writes a character to the request.
     *
     * @param c the character to write to the request
     * @throws IOException if any I/O exceptions occur
     */
    protected void write(char c) throws IOException
    {
        outStream.write(c);
    }


    // ----------------------------------------------------------
    /**
     * Writes a string to the request.
     *
     * @param s the String to write to the request
     * @throws IOException if any I/O exceptions occur
     */
    protected void write(String s) throws IOException
    {
        outStream.write(s.getBytes());
    }


    // ----------------------------------------------------------
    /**
     * Writes a carriage return/line feed pair to the request.
     *
     * @throws IOException if any I/O errors occur.
     */
    protected void newline() throws IOException
    {
        write("\r\n");
    }


    // ----------------------------------------------------------
    /**
     * Writes a string to the request, followed by a newline.
     *
     * @param s the String to write to the request
     * @throws IOException if any I/O errors occur
     */
    protected void writeln(String s) throws IOException
    {
        write(s);
        newline();
    }


    // ----------------------------------------------------------
    /**
     * Writes the name of a part of this request.
     *
     * @param name the name of the part of the request
     * @throws IOException if any I/O errors occur
     */
    private void writeName(String name) throws IOException
    {
        newline();
        write("Content-Disposition: form-data; name=\"");
        write(name);
        write('"');
    }


    // ----------------------------------------------------------
    /**
     * Adds a parameter name/value pair to the request.
     *
     * @param name the name of the parameter
     * @param value the value of the parameter
     * @throws IOException if any I/O errors occur
     */
    public void writeParameter(String name, String value) throws IOException
    {
        writeBoundary();
        writeName(name);
        newline();
        newline();
        writeln(value);
    }


    // ----------------------------------------------------------
    /**
     * Adds the appropriate headers for a file attachment to the request.
     *
     * @param name the name of the request parameter
     * @param filename the name of the file attachment
     * @param contentType the MIME content type of the attachment
     * @return the OutputStream to which the file can be written
     * @throws IOException if any I/O errors occur
     */
    public OutputStream beginWriteFile(String name, String filename,
            String contentType) throws IOException
    {
        writeBoundary();
        writeName(name);
        write("; filename=\"");
        write(filename);
        write('"');
        newline();
        write("Content-Type: ");
        writeln(contentType);
        newline();
        return outStream;
    }


    // ----------------------------------------------------------
    /**
     * Completes the file attachment operation begun by
     * {@link #beginWriteFile(String, String, String)}.
     *
     * @throws IOException if any I/O errors occur
     */
    public void endWriteFile() throws IOException
    {
        newline();
    }


    // ----------------------------------------------------------
    /**
     * Completes the multipart request and closes the stream.
     *
     * @throws IOException if any I/O errors occur
     */
    public void close() throws IOException
    {
        writeBoundary();
        writeln("--");
        outStream.close();
    }


    // ----------------------------------------------------------
    /**
     * Generates a string from a random long value converted to base 36 (0-9,
     * A-Z).
     *
     * @return a String generated from a random long value.
     */
    private static String randomString()
    {
        return Long.toString(random.nextLong(), 36);
    }


    //~ Static/instance variables .............................................

    /* The connection to which the request will be sent. */
    private HttpURLConnection connection;

    /* The stream to which the request will be written. */
    private OutputStream outStream;

    /* Random number generator. */
    private static Random random = new Random();

    /* Boundary string that separates different parts of the multipart
       request. */
    private static final String boundaryString = "---------------------------"
            + randomString() + randomString() + randomString();
}
