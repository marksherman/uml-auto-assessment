/*==========================================================================*\
 |  $Id: MutableStringBufferInputStream.java,v 1.3 2010/02/23 17:06:38 stedwar2 Exp $
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

import java.io.IOException;
import java.io.InputStream;

//-------------------------------------------------------------------------
/**
 *  A custom input stream that takes its contents from a provided string
 *  literal, and that also supports dynamically resetting of its own contents.
 *  This class is designed to help write test cases involving input,
 *  particularly from <code>System.in</code>.  It can be used as a
 *  "smart" replacement for <code>System.in</code> that allows the console
 *  input to be provided by string contents, and allows the console
 *  input to be reset whenever needed.
 *  <p>
 *  Note that it is possible to create a
 *  <code>MutableStringBufferInputStream</code> with null contents.  Such
 *  a stream is in an "unusable" state where no {@link InputStream} methods
 *  can be called until actual contents have been provided at some later point.
 *  Any calls to <code>InputStream</code> methods when a stream is unusable
 *  are instead preempted, and a protected error handling method called
 *  {@link #handleMissingContents()} is invoked instead, usually producing
 *  an {@link IllegalStateException}.  The <code>handleMissingContents()</code>
 *  method is protected, so that subclasses can override it to provide
 *  customized error responses if desired.
 *  </p><p>
 *  At any point in time, {@link #resetContents(String)} (or
 *  {@link #resetContents(InputStream)}) can be used to change the stream's
 *  contents to a new value.  This completely resets the stream's state.
 *  </p>
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.3 $, $Date: 2010/02/23 17:06:38 $
 */
@SuppressWarnings("deprecation")
public class MutableStringBufferInputStream
    extends InputStream
{
    //~ Instance/static variables .............................................

    private InputStream src;
    private String name = "an input stream";

    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Create a new input stream with the specified contents.  If null is
     * provided, the stream is created in an "unusable" state where
     * {@link InputStream} methods cannot be called until actual contents
     * have been provided at some later point.  Any attempt to access an
     * unusable stream is preempted by a call to
     * {@link #handleMissingContents()}, which typically produces an
     * exception.  Contents can be changed at any time using
     * <code>resetContents()</code>.
     * @param contents The complete contents of this input stream, provided
     *                 as a <code>String</code>.
     */
    public MutableStringBufferInputStream(String contents)
    {
        resetContents(contents);
    }


    // ----------------------------------------------------------
    /**
     * Create a new input stream with the specified contents.  If null is
     * provided, the stream is created in an "unusable" state where
     * {@link InputStream} methods cannot be called until actual contents
     * have been provided at some later point.  Any attempt to access an
     * unusable stream is preempted by a call to
     * {@link #handleMissingContents()}, which typically produces an
     * exception.  Contents can be changed at any time using
     * <code>resetContents()</code>.
     * @param contents A second input stream to draw contents from.  All
     *                 stream operations will be delegated to this object,
     *                 until <code>resetContents()</code> is called.
     */
    public MutableStringBufferInputStream(InputStream contents)
    {
        resetContents(contents);
    }


    // ----------------------------------------------------------
    /**
     * Get the human-readable name of this input stream, for diagnostic or
     * debugging purposes.  The default name for each newly-created
     * stream is simply "an input stream", but a specific name can be
     * provided using {@link #setName(String)} where desired.
     * @return This stream's name, for debugging purposes.
     */
    public String getName()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * Set the human-readable name of this input stream, for diagnostic or
     * debugging purposes.  The default name is simply "an input stream".
     * @param name The new name to use for this stream.
     */
    public void setName(String name)
    {
        this.name = name;
    }


    // ----------------------------------------------------------
    /**
     * Reset this stream and replace its contents.  If null is
     * provided, the stream becomes "unusable" where
     * {@link InputStream} methods cannot be called until actual contents
     * have been provided at some later point.  Any attempt to access an
     * unusable stream is preempted by a call to
     * {@link #handleMissingContents()}, which typically produces an
     * exception.  Contents can be changed again at any time using
     * <code>resetContents()</code>.
     * @param newContents The new contents (possibly null) for this stream.
     */
    public void resetContents(String newContents)
    {
        if (newContents == null)
        {
            src = null;
        }
        else
        {
            src = new java.io.StringBufferInputStream(newContents);
        }
    }


    // ----------------------------------------------------------
    /**
     * Reset this stream and replace its contents.  If null is
     * provided, the stream becomes "unusable" where
     * {@link InputStream} methods cannot be called until actual contents
     * have been provided at some later point.  Any attempt to access an
     * unusable stream is preempted by a call to
     * {@link #handleMissingContents()}, which typically produces an
     * exception.  Contents can be changed again at any time using
     * <code>resetContents()</code>.
     * @param newContents The new contents (possibly null) for this stream.
     */
    public void resetContents(InputStream newContents)
    {
        src = newContents;
    }


    // ----------------------------------------------------------
    /**
     * Handle access to an input stream that has not had its contents
     * defined.  The default behavior is to throw an
     * <code>IllegalStateException</code> indicating that the stream
     * has not had its contents set.  Subclasses can override this
     * method to provide custom error handling behaviors as needed.
     */
    protected void handleMissingContents()
    {
        throw new IllegalStateException("The program attempted to read from "
            + getName()
            + ", but no contents have been set yet.");
    }


    // ----------------------------------------------------------
    @Override
    public int read()
        throws IOException
    {
        if (src == null)
        {
            handleMissingContents();
        }
        return src.read();
    }


    // ----------------------------------------------------------
    @Override
    public int read(byte[] b, int off, int len)
        throws IOException
    {
        if (src == null)
        {
            handleMissingContents();
        }
        return src.read(b, off, len);
    }


    // ----------------------------------------------------------
    @Override
    public long skip(long n)
        throws IOException
    {
        if (src == null)
        {
            handleMissingContents();
        }
        return src.skip(n);
    }


    // ----------------------------------------------------------
    @Override
    public int available()
        throws IOException
    {
        if (src == null)
        {
            handleMissingContents();
        }
        return src.available();
    }


    // ----------------------------------------------------------
    @Override
    public void reset()
        throws IOException
    {
        if (src == null)
        {
            handleMissingContents();
        }
        src.reset();
    }
}
