/*==========================================================================*\
 |  $Id: TestRunnerGenerator.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
 |
 |  This file is part of the Web-CAT CxxTest Distribution.
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

package net.sf.webcat.cxxtest.generator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.AngleBracketTemplateLexer;

//--------------------------------------------------------------------------
/**
 * Manages the options for generating the CxxTest test runner and invokes
 * StringTemplate to generate it.
 * 
 * @author Tony Allevato
 * @version $Id: TestRunnerGenerator.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 */
public class TestRunnerGenerator
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Instantiates an instance of the CxxTestDriverGenerator for the specified
     * project and test suite collection.
     * 
     * @param path
     *            the path of the source file to be generated; if null, output
     *            will be sent to stdout
     * @param suites
     *            the collection of test suites to be generated
     * @param testsToRun
     *            a map indicating which test cases should actually be run
     * @param listenerClass
     *            the name of the CxxTest listener class to use
     */
    public TestRunnerGenerator(String path, TestSuiteCollection suites,
            Map<String, Boolean> testsToRun, List<String> listeners)
    {
        this.suites = suites;

        if (listeners.isEmpty())
        {
            listeners.add("StdioPrinter");
        }

        // Create a proxy object to manage the tests to run. Any tests
        // not in this map are assumed to be true. (Currently, all tests are
        // run.)

        this.testsToRunProxy = new TestsToRunProxy();

        // Load the template from the JAR's resources.

        InputStream stream = getClass().getResourceAsStream(
                RunnerTemplateResourcePath);

        StringTemplateGroup templateGroup = new StringTemplateGroup(
                new InputStreamReader(stream), AngleBracketTemplateLexer.class);

        templateGroup.registerRenderer(String.class,
                new TestRunnerStringRenderer(path));

        template = templateGroup.getInstanceOf("runAllTestsFile");

        // Initialize the default options that will be passed into the template.

        options = new Hashtable<String, Object>();
        options.put("platformIsMSVC", false);
        options.put("trapSignals", true);
        options.put("traceStack", true);
        options.put("noStaticInit", true);
        options.put("root", true);
        options.put("part", false);
        options.put("abortOnFail", true);
        options.put("mainProvided", suites.doesMainFunctionExist());
        options.put("listeners", listeners);
        options.put("testsToRun", testsToRunProxy);

        try
        {
            if (path != null)
            {
                writer = new FileWriter(path);
            }
            else
            {
                writer = new OutputStreamWriter(System.out);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * Sets the value of an option that will control how the test runner is
     * generated.
     * 
     * @param name the name of the option
     * @param value the value of the option
     */
    public void setOption(String name, Object value)
    {
        if (value == null)
        {
            options.remove(name);
        }
        else
        {
            options.put(name, value);
        }
    }


    // ----------------------------------------------------------
    /**
     * Generates the test case runner.
     */
    public void generate()
    {
        template.setAttribute("options", options);
        template.setAttribute("suites", suites.getSuites());

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


    //~ Private classes .......................................................
    
    // ----------------------------------------------------------
    /**
     * Used by the test generation template to determine which test cases
     * should be executed and which should be generated but not executed.
     * Currently returns true for all test cases, but a future version might
     * support selectively running tests.
     */
    private class TestsToRunProxy extends Hashtable<String, Boolean>
    {
        //~ Methods ...........................................................
        
        // ------------------------------------------------------
        public boolean containsKey(Object key)
        {
            return true;
        }
        
        
        // ------------------------------------------------------
        public Boolean get(Object key)
        {
            return true;
        }


        //~ Static/instance variables .........................................

        private static final long serialVersionUID = 1L;
    }


    //~ Static/instance variables .............................................

    private static final String RunnerTemplateResourcePath = "runner.stg";

    private TestSuiteCollection suites;
    private TestsToRunProxy testsToRunProxy;
    private Hashtable<String, Object> options;
    private StringTemplate template;
    private Writer writer;
}
