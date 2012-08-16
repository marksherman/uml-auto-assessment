/*==========================================================================*\
 |  $Id: MixRunner.java,v 1.3 2012/03/05 14:17:13 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2011 Virginia Tech
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

package student.testingsupport.junit4;

import java.lang.reflect.Method;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

//-------------------------------------------------------------------------
/**
 * A custom JUnit runner which uses reflection to run both JUnit3 as well as
 * JUnit4 tests. The usefulness of this is that it can be used with a
 * {@code @RunWith} annotation in a parent class, and the resulting subclasses
 * can be written as if they are JUnit3 tests, but advanced users can use
 * annotations as well, and any functionality dictated by the superclass, for
 * instance {@code @Rule} annotations, will be applied to the children as
 * well.
 *
 * It also looks for JUnit3 setUp() and tearDown() methods and performs them
 * as if they are JUnit4 {@code @Before}s and {@code @After}s.
 *
 * @author Craig Estep
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2012/03/05 14:17:13 $
 */
public class MixRunner
    extends BlockJUnit4ClassRunner
{
    //~ Instance/static variables .............................................

	private List<FrameworkMethod> befores = null;
	private boolean junit3methodsAdded = false;
	private boolean junit3aftersAdded = false;


	//~ Constructors ..........................................................

    // ----------------------------------------------------------
	/**
	 * Creates a JUnitMixRunner to run {@code klass}
	 *
	 * @param  klass The test class to run
	 * @throws InitializationError if the test class is malformed.
	 */
	public MixRunner(Class<?> klass)
	    throws InitializationError
	{
		super(klass);
	}


	//~ Methods ...............................................................

    // ----------------------------------------------------------
	/**
	 * Returns a {@link Statement}: run all non-overridden {@code @Before}
	 * methods on this class and superclasses, as well as any JUnit3 setUp
	 * methods, before running {@code next}; if any throws an Exception, stop
	 * execution and pass the exception on.
	 *
	 * Note that in BlockJUnit4ClassRunner this method is deprecated.
	 */
	@Override
	protected Statement withBefores(
	    FrameworkMethod method, Object target, Statement statement)
	{
		List<FrameworkMethod> annotatedBefores =
		    getTestClass().getAnnotatedMethods(Before.class);

		if (befores != annotatedBefores)
		{
            befores = annotatedBefores;
		    // FIXME: This code only finds setUp() if it is public,
		    // when the inherited method is protected.
			Method[] methods = getTestClass().getJavaClass().getMethods();
			for (Method m : methods)
			{
			    // Need to check for correct signature
			    // Need to ensure it isn't annotated as @Before already
				if (m.getName().equals("setUp"))
				{
					FrameworkMethod fm = new FrameworkMethod(m);
					// add at the end, so it will be executed last, after
					// all other @Before methods
					befores.add(fm);
				}
			}
		}

		return befores.isEmpty()
		    ? statement
		    : new RunBefores(statement, befores, target);
	}


    // ----------------------------------------------------------
	/**
	 * Returns a {@link Statement}: run all non-overridden {@code @After}
	 * methods, as well as any JUnit3 tearDown methods, on this class and
	 * superclasses before running {@code next}; all After methods are always
	 * executed: exceptions thrown by previous steps are combined, if
	 * necessary, with exceptions from After methods into a
	 * {@link MultipleFailureException}.
	 *
	 * Note that in BlockJUnit4ClassRunner this method is deprecated.
	 */
	@Override
	protected Statement withAfters(
	    FrameworkMethod method, Object target, Statement statement)
	{
		List<FrameworkMethod> afters =
		    getTestClass().getAnnotatedMethods(After.class);

		if (!junit3aftersAdded)
		{
			Method[] methods = getTestClass().getJavaClass().getMethods();
			for (Method m : methods)
			{
                // Need to check for correct signature
                // Need to ensure it isn't annotated as @Before already
				if (m.getName().equals("tearDown"))
				{
					FrameworkMethod fm = new FrameworkMethod(m);
					// Add at position zero, so it will be executed first,
					// before all other @After methods
					afters.add(0, fm);
				}
			}
			junit3aftersAdded = true;
		}

		return afters.isEmpty()
		    ? statement
		    : new RunAfters(statement, afters, target);
	}


    // ----------------------------------------------------------
	/**
	 * Gathers all JUnit4 and JUnit3 test methods from this class and its
	 * superclasses.
	 *
	 * @return the list of test methods to run.
	 */
	@Override
	protected List<FrameworkMethod> getChildren()
	{
		List<FrameworkMethod> children = super.computeTestMethods();

		if (!junit3methodsAdded)
		{
			Method[] methods = getTestClass().getJavaClass().getMethods();
			for (Method method : methods)
			{
				FrameworkMethod fm = new FrameworkMethod(method);
				if (method.getName().startsWith("test")
				    && !children.contains(fm))
				{
					children.add(fm);
				}
			}
			junit3methodsAdded = true;
		}

		return children;
	}


    // ----------------------------------------------------------
	/**
     * Adds to {@code errors} a throwable for each problem noted with the
     * test class (available from {@link #getTestClass()}).  Default
     * implementation adds an error for each method annotated with
     * {@code @BeforeClass} or {@code @AfterClass} that is not
     * {@code public static void} with no arguments.
     */
    protected void collectInitializationErrors(List<Throwable> errors)
    {
        super.collectInitializationErrors(errors);
        for (int i = 0; i < errors.size(); i++)
        {
            if (errors.get(i).getMessage().equals("No runnable methods"))
            {
                errors.remove(i);
                break;
            }
        }
    }


    // ----------------------------------------------------------
    @SuppressWarnings("deprecation")
    protected Statement methodBlock(FrameworkMethod method)
    {
        Object test;
        try
        {
            test = new ReflectiveCallable() {
                @Override
                protected Object runReflectiveCall() throws Throwable
                {
                    return createTest();
                }
            }.run();
        }
        catch (Throwable e)
        {
            return new Fail(e);
        }

        Statement statement = methodInvoker(method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = withAfters(method, test, statement);
        statement = withRules(method, test, statement);
        statement = new RunTestMethodWrapper(statement, test);
        return statement;
    }


    // ----------------------------------------------------------
    /**
     * This method was declared private in the parent class, when it should
     * have been protected (sigh)--it takes a {@link Statement}, and decorates
     * it with all the {@link MethodRule}s in the test class.
     * @param method The test method itself.
     * @param target The instance of the test class, on which the method will
     *               be called.
     * @param statement The decorated, executable representation of the method
     *               call that has all supplementary behaviors added on.
     * @return A new statement that represents the incoming statement with
     * any method rules added to it.
     */
    protected Statement withRules(
        FrameworkMethod method, Object target, Statement statement)
    {
        Statement result = statement;
        for (MethodRule each : getTestClass()
            .getAnnotatedFieldValues(target, Rule.class, MethodRule.class))
        {
            result = each.apply(result, method, target);
        }
        return result;
    }
}
