/*==========================================================================*\
 |  $Id: TestRunnerGenerator.java,v 1.5 2009/09/13 16:13:15 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech 
 |
 |	This file is part of Web-CAT Eclipse Plugins.
 |
 |	Web-CAT is free software; you can redistribute it and/or modify
 |	it under the terms of the GNU General Public License as published by
 |	the Free Software Foundation; either version 2 of the License, or
 |	(at your option) any later version.
 |
 |	Web-CAT is distributed in the hope that it will be useful,
 |	but WITHOUT ANY WARRANTY; without even the implied warranty of
 |	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |	GNU General Public License for more details.
 |
 |	You should have received a copy of the GNU General Public License
 |	along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.cxxtest.internal.generator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import net.sf.webcat.eclipse.cxxtest.CxxTestPlugin;
import net.sf.webcat.eclipse.cxxtest.ICxxTestConstants;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.5 $ $Date: 2009/09/13 16:13:15 $
 */
public class TestRunnerGenerator
{
    /**
     * Instantiates an instance of the CxxTestDriverGenerator for the specified
     * project and test suite collection.
     * 
     * @param project
     *            the ICProject associated with this generator
     * @param path
     *            the path of the source file to be generated
     * @param suites
     *            the collection of test suites to be generated
     * 
     * @throws IOException
     *             if an I/O error occurs during generation
     */
    public TestRunnerGenerator(ICProject project, String path,
    		TestSuiteCollection suites, Map<String, Boolean> testsToRun)
    throws IOException
    {
        this.suites = suites;

        // Create a proxy object to manage the tests to run. Any tests
        // not in this map are assumed to be true (so that if tests
        // have been added, but not refreshed in the tool window, they
        // will be run until they are explicitly disabled).

        this.testsToRunProxy = new TestsToRunProxy();

        // Load the template from the embedded assembly resources.

        InputStream stream = FileLocator.openStream(
        		CxxTestPlugin.getDefault().getBundle(),
        		new Path(RunnerTemplateResourcePath), true);

        StringTemplateGroup templateGroup = new StringTemplateGroup(
                new InputStreamReader(stream), AngleBracketTemplateLexer.class);

        templateGroup.registerRenderer(String.class,
                new TestRunnerStringRenderer(path));

        template = templateGroup.getInstanceOf("runAllTestsFile"); //$NON-NLS-1$

        // Initialize the options that will be passed into the template.

        options = new Hashtable<String, Object>();
        options.put("platformIsMSVC", false); //$NON-NLS-1$
        options.put("trapSignals", true); //$NON-NLS-1$
        options.put("traceStack", true); //$NON-NLS-1$
        options.put("noStaticInit", true); //$NON-NLS-1$
        options.put("root", true); //$NON-NLS-1$
        options.put("part", false); //$NON-NLS-1$
        options.put("abortOnFail", true); //$NON-NLS-1$
        options.put("mainProvided", suites.doesMainFunctionExist()); //$NON-NLS-1$
        options.put("testResultsFilename", ICxxTestConstants.TEST_RESULTS_FILE); //$NON-NLS-1$
        options.put("testsToRun", testsToRunProxy); //$NON-NLS-1$

        ArrayList<String> listeners = new ArrayList<String>();
        listeners.add("XmlStdioPrinter"); //$NON-NLS-1$
        options.put("listeners", listeners); //$NON-NLS-1$
        
        try
        {
            writer = new FileWriter(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


	public boolean isTrackingHeap()
	{
		return trackHeap;
	}

	public void setTrackHeap(boolean value)
	{
		trackHeap = value;
	}

	public boolean isTrappingSignals()
	{
		return trapSignals;
	}

	public void setTrapSignals(boolean value)
	{
		options.put("trapSignals", value);
		trapSignals = value;
	}

	public boolean isTracingStack()
	{
		return traceStack;
	}
	
	public void setTraceStack(boolean value)
	{
		options.put("traceStack", value);
		traceStack = value;
	}

	public String[] getExtraIncludes()
	{
		return extraIncludes;
	}

	public void setExtraIncludes(String[] files)
	{
		extraIncludes = files;
	}
	
	public String[] getPossibleTestFiles()
	{
		return possibleTestFiles;
	}

	public void setPossibleTestFiles(String[] files)
	{
		possibleTestFiles = files;
	}
	
	public void generate()
    {
        template.setAttribute("options", options); //$NON-NLS-1$
        template.setAttribute("suites", suites.getSuites()); //$NON-NLS-1$

        if (possibleTestFiles != null && possibleTestFiles.length > 0)
        {
        	template.setAttribute("possibleTestFiles", possibleTestFiles); //$NON-NLS-1$
        }

        if (extraIncludes != null && extraIncludes.length > 0)
        {
        	options.put("extraIncludes", extraIncludes); //$NON-NLS-1$
        }

        try
        {
            template.write(new AutoIndentWriter(writer));
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private class TestsToRunProxy extends Hashtable<String, Boolean>
    {
        private static final long serialVersionUID = 1L;

        public boolean containsKey(Object key)
        {
            return true;
        }
        
        public Boolean get(Object key)
        {
            return true;
        }
    }


    private static final String RunnerTemplateResourcePath =
    	"/generator-templates/runner.stg"; //$NON-NLS-1$

    private TestSuiteCollection suites;
    private TestsToRunProxy testsToRunProxy;
    private Hashtable<String, Object> options;
    private StringTemplate template;
    private Writer writer;
    
	private boolean trackHeap;
	private boolean trapSignals;
	private boolean traceStack;

	private String[] possibleTestFiles;
	private String[] extraIncludes;
}
