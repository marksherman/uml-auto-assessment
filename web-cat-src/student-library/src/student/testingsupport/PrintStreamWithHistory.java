/*==========================================================================*\
 |  $Id: PrintStreamWithHistory.java,v 1.4 2011/04/22 17:04:05 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student.testingsupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

//-------------------------------------------------------------------------
/**
 *  An enhanced version of {@link PrintStream} that provides for a history
 *  recall function and some other features making I/O testing a bit
 *  easier to perform.  See the documentation for {@link PrintStream} for
 *  more thorough details on what methods are provided.
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.4 $, $Date: 2011/04/22 17:04:05 $
 */
public class PrintStreamWithHistory
    extends PrintStream
{
    //~ Instance/static variables .............................................

    private StringWriter  history = new StringWriter();
    private final String LINE_SEPARATOR = java.security.AccessController
        .doPrivileged(new java.security.PrivilegedAction<String>()
        {
            public String run()
            {
                return System.getProperty("line.separator");
            }
        });
    private final byte[] LINE_SEPARATOR_BYTES = LINE_SEPARATOR.getBytes();


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Create a new print stream.  This stream will not flush automatically.
     *
     * @param  out        The output stream to which values and objects will be
     *                    printed
     *
     * @see java.io.PrintWriter#PrintWriter(java.io.OutputStream)
     */
    public PrintStreamWithHistory(OutputStream out)
    {
        super(out, false);
    }


    // ----------------------------------------------------------
    /**
     * Create a new print stream.
     *
     * @param  out        The output stream to which values and objects will be
     *                    printed
     * @param  autoFlush  A boolean; if true, the output buffer will be flushed
     *                    whenever a byte array is written, one of the
     *                    <code>println</code> methods is invoked, or a newline
     *                    character or byte (<code>'\n'</code>) is written
     *
     * @see java.io.PrintWriter#PrintWriter(java.io.OutputStream, boolean)
     */
    public PrintStreamWithHistory(OutputStream out, boolean autoFlush)
    {
        super(out, autoFlush);
    }


    // ----------------------------------------------------------
    /**
     * Create a new print stream.
     *
     * @param  out        The output stream to which values and objects will be
     *                    printed
     * @param  autoFlush  A boolean; if true, the output buffer will be flushed
     *                    whenever a byte array is written, one of the
     *                    <code>println</code> methods is invoked, or a newline
     *                    character or byte (<code>'\n'</code>) is written
     * @param  encoding   The name of a supported
     *                    <a href="../lang/package-summary.html#charenc">
     *                    character encoding</a>
     *
     * @exception  UnsupportedEncodingException
     *             If the named encoding is not supported
     */
    public PrintStreamWithHistory(
        OutputStream out, boolean autoFlush, String encoding)
        throws UnsupportedEncodingException
    {
        super(out, autoFlush, encoding);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new print stream, without automatic line flushing, with the
     * specified file name.  This convenience constructor creates
     * the necessary intermediate {@link java.io.OutputStreamWriter
     * OutputStreamWriter}, which will encode characters using the
     * {@linkplain java.nio.charset.Charset#defaultCharset default charset}
     * for this instance of the Java virtual machine.
     *
     * @param  fileName
     *         The name of the file to use as the destination of this print
     *         stream.  If the file exists, then it will be truncated to
     *         zero size; otherwise, a new file will be created.  The output
     *         will be written to the file and is buffered.
     *
     * @throws  FileNotFoundException
     *          If the given file object does not denote an existing, writable
     *          regular file and a new regular file of that name cannot be
     *          created, or if some other error occurs while opening or
     *          creating the file
     *
     * @throws  SecurityException
     *          If a security manager is present and {@link
     *          SecurityManager#checkWrite checkWrite(fileName)} denies write
     *          access to the file
     *
     * @since  1.5
     */
    public PrintStreamWithHistory(String fileName)
        throws FileNotFoundException
    {
        super(fileName);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new print stream, without automatic line flushing, with the
     * specified file name and charset.  This convenience constructor creates
     * the necessary intermediate {@link java.io.OutputStreamWriter
     * OutputStreamWriter}, which will encode characters using the provided
     * charset.
     *
     * @param  fileName
     *         The name of the file to use as the destination of this print
     *         stream.  If the file exists, then it will be truncated to
     *         zero size; otherwise, a new file will be created.  The output
     *         will be written to the file and is buffered.
     *
     * @param  csn
     *         The name of a supported {@linkplain java.nio.charset.Charset
     *         charset}
     *
     * @throws  FileNotFoundException
     *          If the given file object does not denote an existing, writable
     *          regular file and a new regular file of that name cannot be
     *          created, or if some other error occurs while opening or
     *          creating the file
     *
     * @throws  SecurityException
     *          If a security manager is present and {@link
     *          SecurityManager#checkWrite checkWrite(fileName)} denies write
     *          access to the file
     *
     * @throws  UnsupportedEncodingException
     *          If the named charset is not supported
     *
     * @since  1.5
     */
    public PrintStreamWithHistory(String fileName, String csn)
        throws FileNotFoundException, UnsupportedEncodingException
    {
        super(fileName, csn);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new print stream, without automatic line flushing, with the
     * specified file.  This convenience constructor creates the necessary
     * intermediate {@link java.io.OutputStreamWriter OutputStreamWriter},
     * which will encode characters using the {@linkplain
     * java.nio.charset.Charset#defaultCharset default charset} for this
     * instance of the Java virtual machine.
     *
     * @param  file
     *         The file to use as the destination of this print stream.  If the
     *         file exists, then it will be truncated to zero size; otherwise,
     *         a new file will be created.  The output will be written to the
     *         file and is buffered.
     *
     * @throws  FileNotFoundException
     *          If the given file object does not denote an existing, writable
     *          regular file and a new regular file of that name cannot be
     *          created, or if some other error occurs while opening or
     *          creating the file
     *
     * @throws  SecurityException
     *          If a security manager is present and {@link
     *          SecurityManager#checkWrite checkWrite(file.getPath())}
     *          denies write access to the file
     *
     * @since  1.5
     */
    public PrintStreamWithHistory(File file)
        throws FileNotFoundException
    {
        super(file);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new print stream, without automatic line flushing, with the
     * specified file and charset.  This convenience constructor creates
     * the necessary intermediate {@link java.io.OutputStreamWriter
     * OutputStreamWriter}, which will encode characters using the provided
     * charset.
     *
     * @param  file
     *         The file to use as the destination of this print stream.  If the
     *         file exists, then it will be truncated to zero size; otherwise,
     *         a new file will be created.  The output will be written to the
     *         file and is buffered.
     *
     * @param  csn
     *         The name of a supported {@linkplain java.nio.charset.Charset
     *         charset}
     *
     * @throws  FileNotFoundException
     *          If the given file object does not denote an existing, writable
     *          regular file and a new regular file of that name cannot be
     *          created, or if some other error occurs while opening or
     *          creating the file
     *
     * @throws  SecurityException
     *          If a security manager is presentand {@link
     *          SecurityManager#checkWrite checkWrite(file.getPath())}
     *          denies write access to the file
     *
     * @throws  UnsupportedEncodingException
     *          If the named charset is not supported
     *
     * @since  1.5
     */
    public PrintStreamWithHistory(File file, String csn)
        throws FileNotFoundException, UnsupportedEncodingException
    {
        super(file, csn);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Retrieve the text history of what has been sent to this PrintStream.
     * This will include all text printed through this object.  The
     * {@link #clearHistory()} method resets the history to be empty, just
     * as when the object was first created.  Note that newline characters
     * in the history are always represented by '\n', regardless of what
     * value the system <code>line.separator</code> property has.
     * @return all the text sent to this PrintStream
     */
    public String getHistory()
    {
        return history.toString();
    }


    // ----------------------------------------------------------
    /**
     * Reset this object's history to be empty, just as when the object was
     * first created.  You can access the history using {@link #getHistory()}.
     */
    public void clearHistory()
    {
        getHistoryBuffer().setLength(0);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the StringBuffer object used to store this object's text
     * history.
     * @return The history as a string buffer
     */
    public StringBuffer getHistoryBuffer()
    {
        return history.getBuffer();
    }


    // ----------------------------------------------------------
    /**
     * Write the specified byte to this stream.  If the byte is a newline and
     * automatic flushing is enabled then the <code>flush</code> method will be
     * invoked.
     *
     * <p> Note that the byte is written as given; to write a character that
     * will be translated according to the platform's default character
     * encoding, use the <code>print(char)</code> or <code>println(char)</code>
     * methods.
     *
     * @param  b  The byte to be written
     * @see #print(char)
     * @see #println(char)
     */
    public void write(int b)
    {
        synchronized (history)
        {
            super.write(b);
            history.write(b);
        }
    }


    // ----------------------------------------------------------
    /**
     * Write <code>len</code> bytes from the specified byte array starting at
     * offset <code>off</code> to this stream.  If automatic flushing is
     * enabled then the <code>flush</code> method will be invoked.
     *
     * <p> Note that the bytes will be written as given; to write characters
     * that will be translated according to the platform's default character
     * encoding, use the <code>print(char)</code> or <code>println(char)</code>
     * methods.
     *
     * @param  buf   A byte array
     * @param  off   Offset from which to start taking bytes
     * @param  len   Number of bytes to write
     */
    public void write(byte buf[], int off, int len)
    {
        synchronized (history)
        {
            super.write(buf, off, len);
            int startPos = off;
            int end = off + len;
            for (int i = off; i < end; i++)
            {
                if (matches(buf, i, end, LINE_SEPARATOR_BYTES))
                {
                    if (i > startPos)
                    {
                        history.write(new String(buf, startPos, i - startPos));
                    }
                    startPos = i + LINE_SEPARATOR_BYTES.length;
                    i = startPos - 1;
                    history.write('\n');
                }
            }
            if (startPos < end)
            {
                history.write(new String(buf, startPos, end - startPos));
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Returns true if the subsequence of elements in buf beginning at
     * index start matches the entire target array.  If target has length
     * len, then this method compares the subranges buf[start..start+len-1]
     * and target[0..len-1], returning true iff they contain the same bytes.
     * If either range extends off the corresponding array, the result is
     * false.
     * @param buf The (possibly larger) array to look in.
     * @param start The starting index in buf to compare against.
     * @param target The target sequence of bytes to look for.
     */
    private boolean matches(byte buf[], int start, int end, byte target[])
    {
        int len = target.length;
        if (start + len > end)
        {
            return false;
        }
        for (int i = 0; i < len; i++)
        {
            if (buf[start + i] != target[i])
            {
                return false;
            }
        }
        return true;
    }
}
