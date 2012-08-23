/*==========================================================================*\
 |  $Id: WCServletContext.java,v 1.10 2010/09/26 22:31:30 stedwar2 Exp $
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

package net.sf.webcat;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;

// -------------------------------------------------------------------------
/**
 *  This is a custom subclass of javax.servlet.ServletContext that overrides
 *  the lookup of the WOClasspath init parameter with a custom value supplied
 *  by the {@link WCServletAdaptor}.  It is implemented as a wrapper around
 *  a system-supplied ServletContext, and overrides the
 *  {@link #getInitParameter(String)} method to supply a custom value when
 *  the WOClasspath parameter is queried.
 *
 *  @author  stedwar2
 *  @version $Id: WCServletContext.java,v 1.10 2010/09/26 22:31:30 stedwar2 Exp $
 */
public class WCServletContext
    implements ServletContext
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new context object.
     * @param innerContext the actual ServerContext object to be wrapped
     * @param woClasspath the WOClasspath value to use: a newline-separated
     *        list of jar files or directory names representing the
     *        application's classpath
     */
    public WCServletContext( ServletContext innerContext,
                             String         woClasspath )
    {
        this.innerContext = innerContext;
        this.woClasspath  = woClasspath;
    }

    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Returns a <code>String</code> containing the value of the named
     * context-wide initialization parameter, or <code>null</code> if the
     * parameter does not exist.  This version fixes up the WOClasspath
     * parameter to account for dynamically added subsystems.
     *
     * @param name a <code>String</code> containing the name of the parameter
     *             whose value is requested
     * @return a <code>String</code> containing the parameter's value, or
     *         <code>null</code> if no such parameter exists.
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public String getInitParameter( String name )
    {
        String result = null;
        if ( "WOClasspath".equals( name ) )
        {
            if ( woClasspath == null )
            {
                woClasspath = innerContext.getInitParameter( name );
            }
            result = woClasspath;
        }
        else
        {
            result = innerContext.getInitParameter( name );
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Set the internal value used for the WOClasspath init parameter.
     * @param newClasspath the new value to use for WOClasspath
     */
    public void setWOClasspath( String newClasspath )
    {
        woClasspath = newClasspath;
    }


    // ----------------------------------------------------------
//    /**
//     * Splice the subsystem class path specified in the constructor into
//     * a WOClasspath-style value.  Searches the initial classpath value
//     * for the application's main Contents/Resources/Java directory, and
//     * adds the subsystem class path right after this entry.
//     *
//     * @param classpath the initial classpath, as specified for the WOClasspath
//     *        initial parameter value entry in the web.xml file for
//     *        this application; this is a newline-separated list of
//     *        jar file or directory names
//     * @return the modified classpath
//     */
//    public String fixUpClassPath( String classpath )
//    {
//        System.out.println( "WCServletContext.fixUpClassPath(): "
//            + subsystemClassPath );
//        String[] entries = classpath.split( "\\s+" );
//        StringBuffer buffer = new StringBuffer( 10 * entries.length );
//        boolean found = false;
//        for ( int i = 0; i < entries.length; i++ )
//        {
//            buffer.append( entries[i] );
//            buffer.append( "\n" );
//            if ( !found && entries[i].matches( RESOURCES_JAVA_SUBDIR ) )
//            {
//                buffer.append( subsystemClassPath );
//                found = true;
//            }
//        }
//        if ( !found )
//        {
//            buffer.append( subsystemClassPath );
//        }
//        return buffer.toString();
//    }


    //~ Wrapper Methods .......................................................
    // All of the methods in this section are implemented using pure
    // delegation to the innerContext object.

    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    public ServletContext getContext( String arg0 )
    {
        return innerContext.getContext( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public int getMajorVersion()
    {
        return innerContext.getMajorVersion();
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public int getMinorVersion()
    {
        return innerContext.getMinorVersion();
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public String getMimeType( String arg0 )
    {
        return innerContext.getMimeType( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public Set getResourcePaths( String arg0 )
    {
        return innerContext.getResourcePaths( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    public URL getResource( String arg0 )
        throws MalformedURLException
    {
        return innerContext.getResource( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream( String arg0 )
    {
        return innerContext.getResourceAsStream( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher( String arg0 )
    {
        return innerContext.getRequestDispatcher( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public RequestDispatcher getNamedDispatcher( String arg0 )
    {
        return innerContext.getNamedDispatcher( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    @Deprecated
    public Servlet getServlet( String arg0 )
        throws ServletException
    {
        return innerContext.getServlet( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServlets()
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public Enumeration getServlets()
    {
        return innerContext.getServlets();
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServletNames()
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public Enumeration getServletNames()
    {
        return innerContext.getServletNames();
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public void log( String arg0 )
    {
        innerContext.log( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
     */
    @Deprecated
    public void log( Exception arg0, String arg1 )
    {
        innerContext.log( arg0, arg1 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
     */
    public void log( String arg0, Throwable arg1 )
    {
        innerContext.log( arg0, arg1 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath( String arg0 )
    {
        return innerContext.getRealPath( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo()
    {
        return innerContext.getServerInfo();
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    @SuppressWarnings("unchecked")
    public Enumeration getInitParameterNames()
    {
        return innerContext.getInitParameterNames();
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute( String arg0 )
    {
        return innerContext.getAttribute( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    @SuppressWarnings("unchecked")
    public Enumeration getAttributeNames()
    {
        return innerContext.getAttributeNames();
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute( String arg0, Object arg1 )
    {
        innerContext.setAttribute( arg0, arg1 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute( String arg0 )
    {
        innerContext.removeAttribute( arg0 );
    }


    // ----------------------------------------------------------
    /* (non-Javadoc)
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName()
    {
        return innerContext.getServletContextName();
    }


    // ----------------------------------------------------------
	public String getContextPath()
    {
        if (getContextPath == null)
        {
            try
            {
                getContextPath = innerContext.getClass()
                    .getMethod("getContextPath", (Class<?>[])null);
            }
            catch (NoSuchMethodException e)
            {
                throw new RuntimeException(e);
            }
        }
        String result = null;
        if (getContextPath != null)
        {
            try
            {
                result = (String)getContextPath.invoke(null);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            catch (java.lang.reflect.InvocationTargetException e)
            {
                throw new RuntimeException(e);
            }
        }
        return result;
	}


	//~ Instance/static variables .............................................

    private ServletContext innerContext;
    private String         woClasspath = null;
    private Method         getContextPath;

//    private static final String RESOURCES_JAVA_SUBDIR
//        = "[/\\\\]Resources[/\\\\]Java([/\\\\]?)$";
}
