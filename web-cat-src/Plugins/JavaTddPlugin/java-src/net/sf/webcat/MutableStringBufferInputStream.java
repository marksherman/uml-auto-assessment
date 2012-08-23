package net.sf.webcat;

import java.io.InputStream;


//-------------------------------------------------------------------------
/**
 *  Use {@link student.testingsupport.MutableStringBufferInputStream}
 *  instead.
 *
 *  @deprecated
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/02/23 19:47:08 $
 */
public class MutableStringBufferInputStream
    extends student.testingsupport.MutableStringBufferInputStream
{
    // ----------------------------------------------------------
    /**
     * Use {@link student.testingsupport.MutableStringBufferInputStream}
     * instead.
     * @param contents The complete contents of this input stream, provided
     *                 as a <code>String</code>.
     * @deprecated
     */
    public MutableStringBufferInputStream(String contents)
    {
        super(contents);
    }


    // ----------------------------------------------------------
    /**
     * Use {@link student.testingsupport.MutableStringBufferInputStream}
     * instead.
     * @param contents A second input stream to draw contents from.  All
     *                 stream operations will be delegated to this object,
     *                 until <code>resetContents()</code> is called.
     * @deprecated
     */
    public MutableStringBufferInputStream(InputStream contents)
    {
        super(contents);
    }
}
