/*==========================================================================*\
 |  $Id: SubmissionTarget.java,v 1.3 2010/12/06 21:06:48 aallowat Exp $
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

package org.webcat.submitter.targets;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;
import org.webcat.submitter.AmbiguityResolutionPolicy;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.SubmissionTargetException;
import org.webcat.submitter.internal.Xml;
import org.webcat.submitter.internal.utility.PathMatcher;

//--------------------------------------------------------------------------
/**
 * An abstract base class from which all the objects in the submission target
 * tree are derived.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2010/12/06 21:06:48 $
 */
public abstract class SubmissionTarget
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes fields in the SubmissionTarget.
     *
     * @param parent the SubmissionTarget that represents this node's parent in
     *     the tree
     */
    protected SubmissionTarget(SubmissionTarget parent)
    {
        packager = DEFAULT_PACKAGER;

        this.parent = parent;
        name = null;
        hidden = false;

        ambiguityResolution = AmbiguityResolutionPolicy.EXCLUDE;

        transportParams = new LinkedHashMap<String, String>();
        packagerParams = new LinkedHashMap<String, String>();
        
        otherAttributes = new LinkedHashMap<String, String>();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the parent node to this node in the tree.
     *
     * @return the SubmissionTarget that is the parent of this target
     */
    public SubmissionTarget parent()
    {
        return parent;
    }


    // ----------------------------------------------------------
    /**
     * Gets the root of the tree that this object is contained in.
     *
     * @return the root of the tree, or null if it could not be found (or if
     *     the top-level target was not a {@link RootTarget})
     */
    public RootTarget getRoot()
    {
        SubmissionTarget par = this;

        while (par.parent() != null)
        {
            par = par.parent();
        }

        return (par instanceof RootTarget) ? (RootTarget) par : null;
    }


    // ----------------------------------------------------------
    /**
     * Overridden by derived classes to specify whether the node may contain
     * children.
     *
     * @return true if the node may contain children; otherwise, false
     */
    public abstract boolean isContainer();


    // ----------------------------------------------------------
    /**
     * Overridden by derived classes to specify whether the node should be
     * displayed at the same level as its parent, or if it should be nested at
     * a lower level. Typically a target that is a container will not be nested
     * if it does not have a name, but subclasses can provide their own
     * behavior.
     *
     * @return true if the node should be nested at a lower level in the tree;
     *     false if it should be displayed at the same level as its parent
     */
    public abstract boolean isNested();


    // ----------------------------------------------------------
    /**
     * Overridden by derived classes to specify whether an action can be taken
     * on this node. In a wizard, for example, this would enable the
     * Next/Finish button so the user can continue with the submission.
     *
     * @return true if the node is actionable; otherwise, false
     */
    public abstract boolean isActionable();


    // ----------------------------------------------------------
    /**
     * Overridden by derived classes to specify whether the node has been
     * loaded into local memory. This is always true for most nodes except
     * imported groups, which return true only if the external XML file has
     * already been processed.
     *
     * @return true if the node is local or if it has been delay-loaded; false
     *     if the node is an imported group that has not let been expanded
     */
    public abstract boolean isLoaded();


    // ----------------------------------------------------------
    /**
     * Gets the name of the target.
     *
     * @return the name of the target
     */
    public String getName()
    {
        return name;
    }

    // ----------------------------------------------------------
    /**
     * Sets the name of the target.
     *
     * @param value the name of the target
     */
    public void setName(String value)
    {
        name = value;
    }


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether the target should be hidden in the user
     * interface.
     *
     * @return true if the target should be hidden; otherwise, false
     */
    public boolean isHidden()
    {
        return hidden;
    }


    // ----------------------------------------------------------
    /**
     * Sets a value indicating whether the target should be hidden in the user
     * interface.
     *
     * @param value true if the target should be hidden; otherwise, false
     */
    public void setHidden(boolean value)
    {
        hidden = value;
    }


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether file filter ambiguities are resolved by
     * including the file or excluding it.
     *
     * @return one of the {@link AmbiguityResolutionPolicy} values indicating
     *     what to do when an ambiguity occurs
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public AmbiguityResolutionPolicy getAmbiguityResolution()
    throws SubmissionTargetException
    {
        return ambiguityResolution;
    }


    // ----------------------------------------------------------
    /**
     * Sets a value indicating whether file filter ambiguities are resolved by
     * including the file or excluding it.
     *
     * @param value one of the {@link AmbiguityResolutionPolicy} values
     *     indicating what to do when an ambiguity occurs
     */
    public void setAmbiguityResolution(AmbiguityResolutionPolicy value)
    {
        ambiguityResolution = value;
    }


    // ----------------------------------------------------------
    /**
     * Gets the included file patterns for this node.
     *
     * @return an array of Strings that represent the file patterns
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String[] getIncludedFiles() throws SubmissionTargetException
    {
        return (includes != null) ? includes : new String[0];
    }


    // ----------------------------------------------------------
    /**
     * Sets the included file patterns for this node.
     *
     * @param array an array of Strings that represent the file patterns
     */
    public void setIncludedFiles(String[] array)
    {
        includes = (array != null) ? array.clone() : null;
    }


    // ----------------------------------------------------------
    /**
     * Gets the excluded file patterns for this node.
     *
     * @return an array of Strings that represent the file patterns
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String[] getExcludedFiles() throws SubmissionTargetException
    {
        return (excludes != null) ? excludes : new String[0];
    }


    // ----------------------------------------------------------
    /**
     * Sets the excluded file patterns for this node.
     *
     * @param array an array of Strings that represent the file patterns
     */
    public void setExcludedFiles(String[] array)
    {
        excludes = (array != null) ? array.clone() : null;
    }


    // ----------------------------------------------------------
    /**
     * Gets the required file patterns for this node.
     *
     * @return an array of Strings that represent the file patterns
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String[] getRequiredFiles() throws SubmissionTargetException
    {
        return (required != null) ? required : new String[0];
    }


    // ----------------------------------------------------------
    /**
     * Sets the required file patterns for this node.
     *
     * @param array an array of Strings that represent the file patterns
     */
    public void setRequiredFiles(String[] array)
    {
        required = (array != null) ? array.clone() : null;
    }


    // ----------------------------------------------------------
    /**
     * Recursively walks up the tree from the current node and collects a list
     * of all the required file patterns for a submission at this level.
     *
     * @return an array of Strings representing all the required files
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String[] getAllRequiredFiles() throws SubmissionTargetException
    {
        List<String> list = new ArrayList<String>();
        getAllRequiredFiles(list);

        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }


    // ----------------------------------------------------------
    /**
     * This helper function manages the actual recursion for
     * {@link #getAllRequiredFiles()}.
     *
     * @param list a list that will build up the results
     */
    private void getAllRequiredFiles(List<String> list)
    throws SubmissionTargetException
    {
        list.addAll(Arrays.asList(getRequiredFiles()));

        if (parent != null)
        {
            parent.getAllRequiredFiles(list);
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the value of an attribute for submission targets at this level in
     * the tree. This function walks up the tree to find an inherited
     * attribute, if necessary.
     *
     * @param attribute the name of the attribute
     * @return a String containing the value of the attribute, or null if it
     *     is not present
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String getAttribute(String attribute)
    	throws SubmissionTargetException
    {
        String localAttribute = getLocalAttribute(attribute);

        if (localAttribute != null)
        {
            return localAttribute;
        }
        else if (parent != null)
        {
            return parent.getAttribute(attribute);
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the value of an attribute for submission targets at this level in
     * the tree. This function does not walk up the tree to find an inherited
     * attribute--it returns the attribute specified for this node only.
     *
     * @param attribute the name of the attribute
     * @return a String containing the value of the attribute, or null if it
     *     is not present
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String getLocalAttribute(String attribute)
        throws SubmissionTargetException
    {
        return otherAttributes.get(attribute);
    }


    // ----------------------------------------------------------
    /**
     * Sets an attribute for this node.
     *
     * @param attribute the name of the attribute
     * @param value the value for the attribute
     */
    public void setAttribute(String attribute, String value)
    {
    	if (value != null)
    	{
    		otherAttributes.put(attribute, value);
    	}
    	else
    	{
    		otherAttributes.remove(attribute);
    	}
    }


    // ----------------------------------------------------------
    /**
     * Gets the transport URI for submission targets at this level in the tree.
     * This function walks up the tree to find an inherited transport, if
     * necessary.
     *
     * @return a String containing the transport URI
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String getTransport() throws SubmissionTargetException
    {
        String localTransport = getLocalTransport();

        if (localTransport != null)
        {
            return localTransport;
        }
        else if (parent != null)
        {
            return parent.getTransport();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the transport URI for submission targets at this level in the tree.
     * This function does not walk up the tree to find an inherited
     * transport--it returns the transport specified for this node only.
     *
     * @return a String containing the transport URI
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String getLocalTransport() throws SubmissionTargetException
    {
        return transport;
    }


    // ----------------------------------------------------------
    /**
     * Sets the transport URI for this node.
     *
     * @param uri a String containing the transport URI
     */
    public void setTransport(String uri)
    {
        transport = uri;
    }


    // ----------------------------------------------------------
    /**
     * Gets a map containing the transport parameter name/value pairs for this
     * node. This function walks up the tree to find an inherited transport, if
     * necessary.
     *
     * @return a Map containing the parameter names and values
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public Map<String, String> getTransportParameters()
    throws SubmissionTargetException
    {
        String localTransport = getLocalTransport();

        if (localTransport != null)
        {
            return getLocalTransportParameters();
        }
        else if (parent != null)
        {
            return parent.getTransportParameters();
        }
        else
        {
            return new LinkedHashMap<String, String>();
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets a map containing the transport parameter name/value pairs for this
     * node. This function does not walk up the tree to find an inherited
     * transport--it returns the transport specified for this node only.
     *
     * @return a Map containing the parameter names and values
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public Map<String, String> getLocalTransportParameters()
    throws SubmissionTargetException
    {
        return transportParams;
    }


    // ----------------------------------------------------------
    /**
     * Sets the transport parameter name/value pairs for this node.
     *
     * @param params a Map containing the parameter names and values
     */
    public void setTransportParameters(Map<String, String> params)
    {
        transportParams = new LinkedHashMap<String, String>(params);
    }


    // ----------------------------------------------------------
    /**
     * Gets the identifier of the packager used to submit the project. This
     * function walks up the tree to find an inherited packager, if necessary.
     *
     * @return a String containing the packager identifier
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String getPackager() throws SubmissionTargetException
    {
        String localPackager = getLocalPackager();

        if (localPackager != null)
        {
            return localPackager;
        }
        else if (parent != null)
        {
            return parent.getPackager();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the identifier of the packager used to submit the project. This
     * function does not walk up the tree to find an inherited packager--it
     * returns the packager specified for this node only.
     *
     * @return a String containing the packager identifier
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public String getLocalPackager() throws SubmissionTargetException
    {
        return packager;
    }


    // ----------------------------------------------------------
    /**
     * Sets the packager identifier for this node.
     *
     * @param id a String containing the unique identifier of a packager
     */
    public void setPackager(String id)
    {
        packager = id;
    }


    // ----------------------------------------------------------
    /**
     * Gets a map containing the packager parameter name/value pairs for this
     * node.
     *
     * @return a Map containing the packager parameter names and values
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public Map<String, String> getPackagerParameters()
    throws SubmissionTargetException
    {
        String localPackager = getLocalPackager();

        if (localPackager != null)
        {
            return getLocalPackagerParameters();
        }
        else if (parent != null)
        {
            return parent.getPackagerParameters();
        }
        else
        {
            return new LinkedHashMap<String, String>();
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets a map containing the packager parameter name/value pairs for this
     * node. This function does not walk up the tree to find an inherited
     * packager--it returns the transport specified for this node only.
     *
     * @return A Map containing the parameter names and values
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public Map<String, String> getLocalPackagerParameters()
    throws SubmissionTargetException
    {
        return packagerParams;
    }


    // ----------------------------------------------------------
    /**
     * Sets the packager parameter name/value pairs for this node.
     *
     * @param params a Map containing the parameter names and values
     */
    public void setPackagerParameters(Map<String, String> params)
    {
        packagerParams = new LinkedHashMap<String, String>(params);
    }


    // ----------------------------------------------------------
    /**
     * Gets the children of this node. This function only considers the
     * link structure of the target tree, not the nested state of any of the
     * nodes.
     *
     * @return an array of SubmissionTargets that represent the children of the
     *     node
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public SubmissionTarget[] getChildren() throws SubmissionTargetException
    {
        return (children != null) ? children : new SubmissionTarget[0];
    }


    // ----------------------------------------------------------
    /**
     * Gets the "logical" children of this node, respecting the nested state of
     * any children (so that children of a non-nested child are "pushed up"
     * into the parent). This method is appropriate for determining the
     * children of a node as they should be displayed in a user interface.
     *
     * @return an array of SubmissionTargets that represent the logical
     *     children of the node
     * @throws SubmissionTargetException if an exception occurs
     */
    public SubmissionTarget[] getLogicalChildren()
    throws SubmissionTargetException
    {
       List<SubmissionTarget> childList = new ArrayList<SubmissionTarget>();

       computeLogicalChildren(this, childList);

       SubmissionTarget[] array = new SubmissionTarget[childList.size()];
       childList.toArray(array);
       return array;
    }


    // ----------------------------------------------------------
    /**
     * Recursively computes the children for the specified target, taking into
     * account the nested state of each target.
     *
     * @param target the target whose children should be computed
     * @param list a list that will hold the children upon returning
     * @throws SubmissionTargetException
     */
    private static void computeLogicalChildren(SubmissionTarget target,
            List<SubmissionTarget> list) throws SubmissionTargetException
    {
        SubmissionTarget[] children = target.getChildren();

        for (SubmissionTarget child : children)
        {
            if (!child.isHidden())
            {
                if (child.isContainer() && !child.isNested())
                {
                    computeLogicalChildren(child, list);
                }
                else
                {
                    list.add(child);
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Sets the children of this node.
     *
     * @param array an array of SubmissionTargets that represent the new
     *     children of the node
     */
    public void setChildren(SubmissionTarget[] array)
    {
        children = (array != null) ? array.clone() : null;
    }


    // ----------------------------------------------------------
    /**
     * A helper function to ease initializing the included files from a list.
     *
     * @param list the list to set the included files from
     */
    protected void setIncludedFiles(List<String> list)
    {
        includes = new String[list.size()];
        list.toArray(includes);
    }


    // ----------------------------------------------------------
    /**
     * A helper function to ease initializing the excluded files from a list.
     *
     * @param list the list to set the excluded files from
     */
    protected void setExcludedFiles(List<String> list)
    {
        excludes = new String[list.size()];
        list.toArray(excludes);
    }


    // ----------------------------------------------------------
    /**
     * A helper function to ease initializing the required files from a list.
     *
     * @param list the list to set the required files from
     */
    protected void setRequiredFiles(List<String> list)
    {
        required = new String[list.size()];
        list.toArray(required);
    }


    // ----------------------------------------------------------
    /**
     * A helper function to ease initializing the children from a list.
     *
     * @param list the list to set the children from
     */
    protected void setChildren(List<SubmissionTarget> list)
    {
        children = new SubmissionTarget[list.size()];
        list.toArray(children);
    }


    // ----------------------------------------------------------
    /**
     * Recursively walks up the submission target tree to determine if a file
     * with the specified path should be excluded from the submission.
     *
     * @param packageRelativePath the package-relative path to check for
     *     exclusion
     * @return true if the file should be excluded; otherwise, false
     * @throws SubmissionTargetException if an error occurs while delay-loading
     *     the node
     */
    public boolean isFileExcluded(String packageRelativePath)
    throws SubmissionTargetException
    {
        boolean localExclude = false;
        boolean localInclude = false;

        // Check to see if the file is excluded locally.

        String[] exc = getExcludedFiles();
        for (String patternString : exc)
        {
            if (new PathMatcher(patternString).matches(packageRelativePath))
            {
                localExclude = true;
                break;
            }
        }

        // Check to see if the file is explicitly included locally.

        String[] inc = getIncludedFiles();
        for (String patternString : inc)
        {
            if (new PathMatcher(patternString).matches(packageRelativePath))
            {
                localInclude = true;
                break;
            }
        }

        if (localInclude && localExclude)
        {
            if (ambiguityResolution == AmbiguityResolutionPolicy.EXCLUDE)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if (localExclude)
        {
            return true;
        }
        else if (localInclude)
        {
            return false;
        }

        // If no explicit mention of the file was found,
        // try going up the assignment tree.

        if (parent != null)
        {
            return parent.isFileExcluded(packageRelativePath);
        }
        else
        {
            // If we're at the top level and the file has still not matched a
            // pattern, we include it unless the top level node has any
            // explicit inclusions, in which case we exclude it.

            if (inc.length == 0)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Used by delay-loading nodes, this function copies the entire tree rooted
     * at the specified node into the current tree at the position represented
     * by the node on which this method is invoked.
     *
     * @param target the tree from which to copy
     * @throws SubmissionTargetException if there are any errors during
     *     delay-loading
     */
    protected void copyFrom(SubmissionTarget target)
    throws SubmissionTargetException
    {
        // Used for delay-loading. Takes the specified definition object
        // and copies it into the current object.

        setAmbiguityResolution(target.getAmbiguityResolution());
        setChildren(target.getChildren());
        setIncludedFiles(target.getIncludedFiles());
        setExcludedFiles(target.getExcludedFiles());
        setRequiredFiles(target.getRequiredFiles());

        setTransport(target.getTransport());
        setTransportParameters(target.getTransportParameters());

        setPackager(target.getPackager());
        setPackagerParameters(target.getPackagerParameters());
    }
    
    
    // ----------------------------------------------------------
    /**
     * Gets a set of attribute names that should not be included in the
     * attribute set used by {@link #getAttribute(String)} and related
     * methods.
     * 
     * @return the set of attribute names that should not be included in the
     *     target's attribute set
     */
    protected Set<String> getIgnoredAttributes()
    {
    	Set<String> ignored = new HashSet<String>();
    	ignored.add("name");
    	ignored.add("hidden");

    	return ignored;
    }


    // ----------------------------------------------------------
    /**
     * Subdivides the next unit of work in the specified long-running task into
     * a subtask containing as many units of work as there are children in an
     * XML node.
     *
     * @param parentNode the node whose children will determine the length of
     *     the subtask
     * @param task the long-running task to subdivide
     */
    protected void startSubtaskForChildNodes(Node parentNode,
            ILongRunningTask task)
    {
        task.beginSubtask(parentNode.getChildNodes().getLength());
    }


    // ----------------------------------------------------------
    /**
     * Parses the specified XML node and builds a subtree from the data.
     *
     * @param node the XML document node to parse from
     * @param task the long-running task to run under
     * @throws SubmissionTargetException if any errors occurred during parsing
     */
    public abstract void parse(Node node, ILongRunningTask task)
    throws SubmissionTargetException;


    // ----------------------------------------------------------
    /**
     * Parses the common attributes for the specified XML node and adds them
     * to the target.
     * 
     * @param node the XML document node to parse from
     * @param task the long-running task to run under
     * @throws SubmissionTargetException if any errors occurred during parsing
     */
    protected void parseCommonAttributes(Node node, ILongRunningTask task)
    throws SubmissionTargetException
    {
        Node nameNode = node.getAttributes().getNamedItem(
                Xml.Attributes.NAME);
        Node hiddenNode = node.getAttributes().getNamedItem(
                Xml.Attributes.HIDDEN);

        String hiddenString = null;

        if (nameNode != null)
        {
            setName(nameNode.getNodeValue());
        }

        if (hiddenNode != null)
        {
            hiddenString = hiddenNode.getNodeValue();
        }

        setHidden(Boolean.parseBoolean(hiddenString));

        Set<String> ignored = getIgnoredAttributes();

        for (int i = 0; i < node.getAttributes().getLength(); i++)
        {
        	Node attribute = node.getAttributes().item(i);
        	String attributeName = attribute.getNodeName();
        	
        	if (!ignored.contains(attributeName))
        	{
        		String value = attribute.getNodeValue();
        		otherAttributes.put(attributeName, value);
        	}
        }
    }


    // ----------------------------------------------------------
    /**
     * Parses a transport element and initializes the appropriate fields.
     *
     * @param parentNode the XML node representing the transport element
     * @param task the long-running task to run under
     * @throws SubmissionTargetException if there are any parsing errors
     */
    protected void parseTransport(Node parentNode, ILongRunningTask task)
    throws SubmissionTargetException
    {
        startSubtaskForChildNodes(parentNode, task);

        Node uriNode = parentNode.getAttributes().getNamedItem(
                Xml.Attributes.URI);

        if (uriNode != null)
        {
            transport = uriNode.getNodeValue();
        }

        // Parse the parameter tags.
        Node node = parentNode.getFirstChild();
        while (node != null)
        {
            String nodeName = node.getLocalName();

            if (Xml.Elements.PARAM.equals(nodeName))
            {
                parseTransportParameter(node, task);
            }
            else if (Xml.Elements.FILE_PARAM.equals(nodeName))
            {
                parseTransportFileParameter(node, task);
            }
            else
            {
                task.doWork(1);
            }

            node = node.getNextSibling();
        }

        task.finishSubtask();
    }


    // ----------------------------------------------------------
    /**
     * Parses a packager element and initializes the appropriate fields.
     *
     * @param parentNode the XML node representing the packager element
     * @param task the long-running task to run under
     * @throws SubmissionTargetException if there are any parsing errors
     */
    protected void parsePackager(Node parentNode, ILongRunningTask task)
    throws SubmissionTargetException
    {
        startSubtaskForChildNodes(parentNode, task);

        Node idNode = parentNode.getAttributes().getNamedItem(
                Xml.Attributes.ID);
        if (idNode != null)
        {
            packager = idNode.getNodeValue();
        }

        // Parse the parameter tags.
        Node node = parentNode.getFirstChild();
        while (node != null)
        {
            String nodeName = node.getLocalName();

            if (Xml.Elements.PARAM.equals(nodeName))
            {
                parsePackagerParameter(node, task);
            }
            else
            {
                task.doWork(1);
            }

            node = node.getNextSibling();
        }

        task.finishSubtask();
    }


    // ----------------------------------------------------------
    /**
     * Parses a packager param element and initializes the appropriate fields.
     *
     * @param node the XML node representing the param element
     * @param task the long-running task to run under
     * @throws SubmissionTargetException if there are any parsing errors
     */
    protected void parsePackagerParameter(Node node, ILongRunningTask task)
    throws SubmissionTargetException
    {
        Node nameNode = node.getAttributes().getNamedItem(
                Xml.Attributes.NAME);
        Node valueNode = node.getAttributes().getNamedItem(
                Xml.Attributes.VALUE);

        if (nameNode != null && valueNode != null)
        {
            packagerParams.put(nameNode.getNodeValue(),
                    valueNode.getNodeValue());
        }

        task.doWork(1);
    }


    // ----------------------------------------------------------
    /**
     * Parses a transport param element and initializes the appropriate fields.
     *
     * @param node the XML node representing the param element
     * @param task the long-running task to run under
     * @throws SubmissionTargetException if there are any parsing errors
     */
    protected void parseTransportParameter(Node node, ILongRunningTask task)
    throws SubmissionTargetException
    {
        Node nameNode = node.getAttributes().getNamedItem(
                Xml.Attributes.NAME);
        Node valueNode = node.getAttributes().getNamedItem(
                Xml.Attributes.VALUE);

        if (nameNode != null && valueNode != null)
        {
            transportParams.put(nameNode.getNodeValue(),
                    valueNode.getNodeValue());
        }

        task.doWork(1);
    }


    // ----------------------------------------------------------
    /**
     * Parses a transport file-param element and initializes the appropriate
     * fields.
     *
     * @param node the XML node representing the file-param element
     * @param task the long-running task to run under
     * @throws SubmissionTargetException if there are any parsing errors
     */
    protected void parseTransportFileParameter(Node node, ILongRunningTask task)
    throws SubmissionTargetException
    {
        Node nameNode = node.getAttributes().getNamedItem(
                Xml.Attributes.NAME);
        Node valueNode = node.getAttributes().getNamedItem(
                Xml.Attributes.VALUE);

        if (nameNode != null && valueNode != null)
        {
            transportParams.put("$file." + nameNode.getNodeValue(),
                    valueNode.getNodeValue());
        }
    }


    // ----------------------------------------------------------
    /**
     * Parses the file pattern from an include, exclude, or required element
     * and initializes the appropriate fields.
     *
     * @param node the XML node representing the file pattern element
     * @param task the long-running task to run under
     * @return the file pattern
     * @throws SubmissionTargetException if there are any parsing errors
     */
    protected String parseFilePattern(Node node, ILongRunningTask task)
    throws SubmissionTargetException
    {
        Node patternNode = node.getAttributes().getNamedItem(
                Xml.Attributes.PATTERN);

        task.doWork(1);

        if (patternNode != null)
        {
            return patternNode.getNodeValue();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Parses the filter ambiguity resolution policy and initializes the
     * appropriate fields.
     *
     * @param node the XML node representing the filter-ambiguity element
     * @param task the long-running task to run under
     * @throws SubmissionTargetException if there are any parsing errors
     */
    protected void parseFilterAmbiguity(Node node, ILongRunningTask task)
    throws SubmissionTargetException
    {
        Node choiceNode = node.getAttributes().getNamedItem(
                Xml.Attributes.CHOICE);

        if (choiceNode != null)
        {
            String value = choiceNode.getNodeValue();

            if ("include".equals(value))
            {
                setAmbiguityResolution(AmbiguityResolutionPolicy.INCLUDE);
            }
            else
            {
                setAmbiguityResolution(AmbiguityResolutionPolicy.EXCLUDE);
            }
        }

        task.doWork(1);
    }


    // ----------------------------------------------------------
    /**
     * Overridden by subclasses to write this target in XML format.
     *
     * @param writer the PrintWriter to write the output to
     * @param indentLevel the number of levels to indent the output
     */
    public abstract void writeToXML(PrintWriter writer, int indentLevel);


    // ----------------------------------------------------------
    /**
     * Writes three spaces for each indentation level to the specified writer.
     *
     * @param indentLevel an integer specifying the indentation level of the
     *     element
     * @param writer the writer that will store the XML code
     */
    protected void padToIndent(int indentLevel, PrintWriter writer)
    {
        for (int i = 0; i < indentLevel; i++)
        {
            writer.print("   ");
        }
    }


    // ----------------------------------------------------------
    /**
     * Writes the given string to the writer, first converting any special XML
     * characters (angle brackets, quotation mark, ampersand) to XML entities.
     *
     * @param string the string to convert and write
     * @param writer the writer that will store the XML code
     */
    protected void writeXMLString(String string, PrintWriter writer)
    {
        for (int i = 0; i < string.length(); i++)
        {
            char ch = string.charAt(i);

            switch (ch)
            {
            case '<': writer.print("&lt;"); break;
            case '>': writer.print("&gt;"); break;
            case '&': writer.print("&amp;"); break;
            case '"': writer.print("&quot;"); break;
            default:  writer.print(ch); break;
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Writes the file patterns, transport, and packager elements for the
     * current node to the specified writer.
     *
     * @param indentLevel an integer specifying the indentation level of the
     *     element
     * @param writer the writer that will store the XML code
     */
    protected void writeSharedProperties(int indentLevel, PrintWriter writer)
    {
        // Write file patterns.
        writeFilePatterns(indentLevel, writer);

        // Write packager.
        writePackager(indentLevel, writer);

        // Write transport.
        writeTransport(indentLevel, writer);
    }


    // ----------------------------------------------------------
    /**
     * Writes the file patterns for the current node to the specified writer.
     *
     * @param indentLevel an integer specifying the indentation level of the
     *     element
     * @param writer the writer that will store the XML code
     */
    protected void writeFilePatterns(int indentLevel, PrintWriter writer)
    {
        if (ambiguityResolution == AmbiguityResolutionPolicy.INCLUDE)
        {
            padToIndent(indentLevel, writer);
            writer.print("<filter-ambiguity choice=\"include\"/>");
        }

        if (includes.length > 0)
        {
            for (String pattern : includes)
            {
                writePatternTag(indentLevel, "include", pattern, writer);
            }
        }

        if (excludes.length > 0)
        {
            for (String pattern : excludes)
            {
                writePatternTag(indentLevel, "exclude", pattern, writer);
            }
        }

        if (required.length > 0)
        {
            for (String pattern : required)
            {
                writePatternTag(indentLevel, "required", pattern, writer);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Writes a single file pattern element to the specified writer.
     *
     * @param indentLevel an integer specifying the indentation level of the
     *     element
     * @param type the element type name
     * @param value the file pattern
     * @param writer the writer that will store the XML code
     */
    protected void writePatternTag(int indentLevel, String type, String value,
            PrintWriter writer)
    {
        padToIndent(indentLevel, writer);
        writer.print("<");
        writer.print(type);
        writer.print(" pattern=\"");
        writeXMLString(value, writer);
        writer.println("\"/>");
    }


    // ----------------------------------------------------------
    /**
     * Writes the transport element for the current node to the specified
     * writer.
     *
     * @param indentLevel an integer specifying the indentation level of the
     *     element
     * @param writer the writer that will store the XML code
     */
    protected void writeTransport(int indentLevel, PrintWriter writer)
    {
        if (transport != null)
        {
            padToIndent(indentLevel, writer);

            writer.print("<transport uri=\"");
            writeXMLString(transport, writer);
            writer.print("\"");

            if (transportParams.isEmpty())
            {
                writer.println("/>");
            }
            else
            {
                writer.println(">");

                for (String name : transportParams.keySet())
                {
                    String value = transportParams.get(name);

                    padToIndent(indentLevel + 1, writer);

                    if (name.startsWith("$file."))
                    {
                        writer.print("<file-param name=\"");
                        writeXMLString(name.substring(6), writer);
                        writer.print("\" value=\"");
                        writeXMLString(value, writer);
                        writer.println("\"/>");
                    }
                    else
                    {
                        writer.print("<param name=\"");
                        writeXMLString(name, writer);
                        writer.print("\" value=\"");
                        writeXMLString(value, writer);
                        writer.println("\"/>");
                    }
                }

                padToIndent(indentLevel, writer);
                writer.println("</transport>");
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Writes the packager element for the current node to the specified
     * writer.
     *
     * @param indentLevel an integer specifying the indentation level of the
     *     element
     * @param writer the writer that will store the XML code
     */
    protected void writePackager(int indentLevel, PrintWriter writer)
    {
        if (packager.equals(DEFAULT_PACKAGER)
                && (packagerParams == null || packagerParams.size() == 0))
        {
            return;
        }

        if (packager != null)
        {
            padToIndent(indentLevel, writer);

            writer.print("<packager id=\"");
            writeXMLString(packager, writer);
            writer.print("\"");

            if (packagerParams.isEmpty())
            {
                writer.println("/>");
            }
            else
            {
                writer.println(">");

                for (String name : packagerParams.keySet())
                {
                    String value = packagerParams.get(name);

                    padToIndent(indentLevel + 1, writer);

                    writer.print("<param name=\"");
                    writeXMLString(name, writer);
                    writer.print("\" value=\"");
                    writeXMLString(value, writer);
                    writer.println("\"/>");
                }

                padToIndent(indentLevel, writer);
                writer.println("</packager>");
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Writes the children of the current node to the specified writer.
     *
     * @param indentLevel an integer specifying the indentation level of the
     *     element
     * @param writer the writer that will store the XML code
     */
    protected void writeChildren(int indentLevel, PrintWriter writer)
    {
        for (SubmissionTarget child : children)
        {
            child.writeToXML(writer, indentLevel);
        }
    }


    //~ Static/instance variables .............................................

    /* The default packager to use when none is specified. */
    private static String DEFAULT_PACKAGER =
        "org.webcat.submitter.packagers.zip";

    /* The parent object to this object in the tree. */
    private SubmissionTarget parent;

    /* The name of the target. */
    private String name;

    /* Indicates whether the target should be hidden. */
    private boolean hidden;

    /* The list of file patterns that represent files that are to be included
       in a submission. */
    private String[] includes;

    /* The list of file patterns that represent files that are to be excluded
       from a submission. */
    private String[] excludes;

    /* The list of file patterns that represent files that must be included in
       a submission. */
    private String[] required;

    /* Indicates whether exclusion should be preferred over inclusion in the
       event that two filters at the same level of the tree conflict for a
       particular file. */
    private AmbiguityResolutionPolicy ambiguityResolution;

    /* The string that contains the submission protocol and destination
       location. */
    private String transport;

    /* A table of name-value pairs that represent additional parameters that
       are included with the submission. */
    private LinkedHashMap<String, String> transportParams;

    /* The globally unique identifier of the packager used to submit the
       project. The packager should already have been registered with the
       submission core plug-in. */
    private String packager;

    /* A table of name-value pairs that represent additional parameters that
       are passed to the packager. */
    private LinkedHashMap<String, String> packagerParams;

    /* Other attributes associated with a submission target (such as file
       size limits). */
    private LinkedHashMap<String, String> otherAttributes;

    /* The list of child nodes to this node in the submission target tree. */
    private SubmissionTarget[] children;
}
