/*==========================================================================*\
 |  $Id: SubmissionManifest.java,v 1.4 2010/12/06 21:06:48 aallowat Exp $
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

package org.webcat.submitter;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.webcat.submitter.targets.AssignmentTarget;

//--------------------------------------------------------------------------
/**
 * <p>
 * This class collects references to a number of objects that are required in
 * various places during the submission process, so that they can be easily
 * passed between functions.
 * </p><p>
 * Any function below that takes an {@link IStringEncoder} as an argument can
 * take null in its place; this will cause no encoding to be done to the
 * parameter values (they will be passed as-is, unmodified).
 * </p>
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.4 $ $Date: 2010/12/06 21:06:48 $
 */
public class SubmissionManifest
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Initializes a new submission manifest.
	 */
	public SubmissionManifest()
	{
		parameters = new HashMap<String, String>();
	}
	
	
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the assignment referred to by this object.
     *
     * @return an AssignmentTarget representing the assignment to submit
     */
    public AssignmentTarget getAssignment()
    {
        return assignment;
    }


    // ----------------------------------------------------------
    /**
     * Sets the assignment referred to by this object.
     *
     * @param value an AssignmentTarget representing the assignment to submit
     */
    public void setAssignment(AssignmentTarget value)
    {
        assignment = value;
    }


    // ----------------------------------------------------------
    /**
     * Gets the submittable items that represent the resources to be submitted.
     *
     * @return An array of ISubmittableItems representing the files to submit
     */
    public ISubmittableItem[] getSubmittableItems()
    {
        return submittables;
    }


    // ----------------------------------------------------------
    /**
     * Sets the submittable items that represent the resources to be submitted.
     *
     * @param value an array of ISubmittableItems representing the files to
     *     submit
     */
    public void setSubmittableItems(ISubmittableItem[] value)
    {
        submittables = value;
    }


    // ----------------------------------------------------------
    /**
     * A convenience method for setting the array of submittable items to a
     * single item.
     *
     * @param value a single ISubmittableItem representing the files to
     *     submit
     */
    public void setSubmittableItems(ISubmittableItem value)
    {
        setSubmittableItems(new ISubmittableItem[] { value });
    }


    // ----------------------------------------------------------
    /**
     * Gets the username referred to by this object.
     *
     * @return a String containing the username
     */
    public String getUsername()
    {
        return username;
    }


    // ----------------------------------------------------------
    /**
     * Sets the username referred to by this object.
     *
     * @param value a String containing the username
     */
    public void setUsername(String value)
    {
        username = value;
    }


    // ----------------------------------------------------------
    /**
     * Gets the password referred to by this object.
     *
     * @return a String containing the password
     */
    public String getPassword()
    {
        return password;
    }


    // ----------------------------------------------------------
    /**
     * Sets the password referred to by this object.
     *
     * @param value a String containing the password
     */
    public void setPassword(String value)
    {
        password = value;
    }


    // ----------------------------------------------------------
    /**
     * Gets the value of the parameter with the specified key.
     *
     * @param key a String representing the name of the parameter to get
     * @return a String containing the value of the parameter, or null if the
     *     parameter has not yet been set
     */
    public String getParameter(String key)
    {
        return parameters.get(key);
    }
    
    
    // ----------------------------------------------------------
    /**
     * Gets the value of the parameter with the specified key.
     *
     * @param key a String representing the name of the parameter to get
     * @param protectPassword true to protect the password, otherwise false
     * @return a String containing the value of the parameter, or null if the
     *     parameter has not yet been set
     */
    private String internalGetParameter(String key, IStringEncoder encoder,
    		boolean protectPassword)
    {
    	String value = null;

    	if ("user".equals(key))
    	{
    		value = username;
    	}
    	else if ("pw".equals(key))
    	{
    		value = (protectPassword ? "<PASSWORD>" : password);
    	}
    	else if ("assignment.name".equals(key))
    	{
    		value = assignment.getName();
    	}
    	else
    	{
    		value = parameters.get(key);
    	}

    	if (value != null)
    	{
    		if (encoder == null)
    		{
    			return value;
    		}
    		else
    		{
    			return encoder.encodeString(value);
    		}
    	}
    	else
    	{
    		return null;
    	}
    }
    
    
    // ----------------------------------------------------------
    /**
     * Gets a set containing the names of all of the parameters that are
     * currently set in the manifest.
     * 
     * @return a Set containing the names of all the currently set parameters
     */
    public Set<String> getParameterNames()
    {
    	return parameters.keySet();
    }


    // ----------------------------------------------------------
    /**
     * Sets the value of the parameter with the specified key. To clear the
     * value of a parameter, you can pass null as the value.
     *
     * @param key a String representing the name of the parameter to set
     * @param value a String containing the value of the parameter, or null to
     *     clear its value
     */
    public void setParameter(String key, String value)
    {
    	if (value != null)
    	{
    		parameters.put(key, value);
    	}
    	else
    	{
    		parameters.remove(key);
    	}
    }


    // ----------------------------------------------------------
    /**
     * Resolves the specified parameter string by replacing any variable
     * placeholders with their actual values. The following fixed
     * placeholders are supported:
     *
     * <ul>
     * <li>${user} - the username</li>
     * <li>${pw} - the password</li>
     * <li>${assignment.name} - the name of the assignment</li>
     * </ul>
     * 
     * You can also use any other key inside the ${x} notation, which will
     * look up the value of a parameter that was set by a call to
     * {@link #setParameter(String)}.
     *
     * @param text the String containing placeholders to be resolved
     * @param encoder the encoder to use to encode the parameter values
     * @return a copy of the original string with the placeholders replaced by
     *     their actual values
     */
    public String resolveParameters(String text, IStringEncoder encoder)
    {
        return resolveParameters(text, encoder, false);
    }


    // ----------------------------------------------------------
    /**
     * Identical to {@link #resolveParameters(String, IStringEncoder)}, but
     * with the added option that the password can be protected to display
     * merely "&lt;PASSWORD&gt;" instead of the user's actual password. This
     * method should be used if a parameter value is to be displayed to the
     * user or logged.
     *
     * @param text the String containing placeholders to be resolved
     * @param encoder the encoder to use to encode the parameter values
     * @param protectPassword true to protect the password, otherwise false
     * @return a copy of the original string with the placeholders replaced by
     *     their actual values
     */
    private String resolveParameters(String text,
    		IStringEncoder encoder, boolean protectPassword)
    {
        if (text == null || text.length() == 0)
        {
            return text;
        }

        // Get the index of the first constant, if any.

        StringBuffer buffer = new StringBuffer(text.length());
        int beginIndex = 0;
        int startName = text.indexOf(PARAMETER_START, beginIndex);

        while (startName >= 0)
        {
            int endName = text.indexOf(PARAMETER_END, startName);

            if (endName == -1)
            {
                // Terminating symbol not found, so return the value as is.
                break;
            }

            if (startName > beginIndex)
            {
                buffer.append(text.substring(beginIndex, startName));
                beginIndex = startName;
            }

            String constName  =
                text.substring(startName + PARAMETER_START.length(), endName);
            String constValue = internalGetParameter(constName, encoder,
            		protectPassword);

            if (constValue == null)
            {
                // Property name not found.
                buffer.append(text.substring(beginIndex,
                                endName + PARAMETER_END.length()));
            }
            else
            {
                // Insert the constant value into the original property value.
                buffer.append(constValue);
            }

            beginIndex = endName + PARAMETER_END.length();

            // Look for the next constant.
            startName = text.indexOf(PARAMETER_START, beginIndex);
        }

        buffer.append(text.substring(beginIndex, text.length()));

        return buffer.toString();
    }


    // ----------------------------------------------------------
    /**
     * Gets a URI that represents the transport to use for submitting the
     * package, once any parameter placeholders have been resolved.
     *
     * @param encoder the encoder to use to encode any parameter values
     * @return the URL to submit the package to
     * @throws SubmissionTargetException if an exception occurs
     */
    public URI getResolvedTransport(IStringEncoder encoder)
    throws SubmissionTargetException
    {
        String uriString = resolveParameters(assignment.getTransport(),
        		encoder);

        try
        {
            return new URI(uriString);
        }
        catch (URISyntaxException e)
        {
            throw new SubmissionTargetException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets a URI that represents the transport to use for submitting the
     * package, once any parameter placeholders have been resolved, while also
     * protecting the user's password from being displayed. This method is
     * useful for logging.
     *
     * @param encoder the encoder to use to encode any parameter values
     * @return the URL to submit the package to
     * @throws SubmissionTargetException if an exception occurs
     */
    public URI getResolvedTransportWithoutPassword(IStringEncoder encoder)
    throws SubmissionTargetException
    {
        String uriString = resolveParameters(assignment.getTransport(),
        		encoder, true);

        try
        {
            return new URI(uriString);
        }
        catch (URISyntaxException e)
        {
            throw new SubmissionTargetException(e);
        }
    }


    //  ---------------------------------------------------------
    /**
     * Takes the forest of submittable items and sends those that should be
     * included (based on the target assignment) to the specified stream,
     * using the packager defined for the target assignment.
     *
     * Users implementing a user interface for this submitter core need
     * not call this method; rather, it is exposed as public so that
     * custom protocol handlers can call it from within their
     * {@link IProtocol#submit(SubmissionManifest, ILongRunningTask)} method in
     * order to transfer the final submitted package to its destination.
     *
     * @param stream the OutputStream to write the package contents to
     * @param task the long-running task to be used to notify the user of the
     *     progress of the operation
     * @param encoder the encoder to use to encode any parameters before they
     *     are passed to the packager
     *
     * @throws IOException if an I/O exception occurred
     */
    public void packageContentsIntoStream(OutputStream stream,
            ILongRunningTask task, IStringEncoder encoder) throws IOException
    {
        task.beginSubtask(3);

        final IPackager packager =
            PackagerRegistry.getInstance().createPackagerInstance(
                    assignment.getPackager());

        if (packager == null)
        {
            throw new PackagerNotRegisteredException(assignment.getPackager());
        }

        Map<String, String> parameters = assignment.getPackagerParameters();
        Map<String, String> resolvedParameters =
            new Hashtable<String, String>();

        for (String key : parameters.keySet())
        {
            String value = parameters.get(key);
            String resolved = resolveParameters(value, encoder);

            resolvedParameters.put(key, resolved);
        }

        packager.startPackage(stream, resolvedParameters);
        task.doWork(1);

        LongRunningSubmittableItemVisitor visitor =
            new LongRunningSubmittableItemVisitor(task)
        {
            protected void accept(ISubmittableItem item)
            throws InvocationTargetException
            {
                try
                {
                    if (!assignment.isFileExcluded(item.getFilename()))
                    {
                        packager.addSubmittableItem(item);
                    }
                }
                catch (Exception e)
                {
                    throw new InvocationTargetException(e);
                }
            }
        };

        try
        {
            // This visitation acts as the second unit of work so manually
            // incrementing the task here is not required.

            visitor.visit(submittables);
        }
        catch (InvocationTargetException e)
        {
            if (e.getCause() instanceof IOException)
            {
                throw (IOException) e.getCause();
            }
            else
            {
                e.getCause().printStackTrace();
            }
        }

        packager.endPackage();
        task.doWork(1);

        task.finishSubtask();
    }


    //~ Static/instance variables .............................................

    private static final String PARAMETER_START = "${";

    private static final String PARAMETER_END = "}";

    /* The assignment to which the user is submitting. */
    private AssignmentTarget assignment;

    /* The submittable items to be packaged and submitted. */
    private ISubmittableItem[] submittables;

    /* The ID of the user. */
    private String username;

    /* The password used to log into the submission target system, if
       required. */
    private String password;

    /* Other parameters to include in the submission manifest. It is up to
       the client code using the submitter to designate what these are, and
       for the receiving system to do something appropriate with them. */
    private Map<String, String> parameters;
}
