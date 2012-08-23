package student.testingsupport.junit4;

import java.lang.reflect.Method;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

//-------------------------------------------------------------------------
/**
 * A modified version of
 * {@link org.junit.internal.runners.statements.InvokeMethod}
 * that allows test classes to add custom hooks to the test method
 * invocation machinery by defining/overriding a method with this
 * signature:
 * <pre>
 * protected void runTestMethod(Statement statement) { ... }
 * </pre>
 * <p>
 * If this method is defined, a Statement object representing the test
 * method to execute (minus any After/Before decorations) is passed in,
 * and execution of the test method is delegated to runTestMethod().  If
 * this method is absent, the test method is invoked directly.
 * </p>
 *
 * @author Stephen Edwards
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.1 $, $Date: 2011/06/09 15:35:28 $
 */
public class RunTestMethodWrapper
    extends org.junit.runners.model.Statement
{
    private final Statement fNext;
    private Object fTarget;

    // ----------------------------------------------------------
    public RunTestMethodWrapper(Statement next, Object target)
    {
        fNext = next;
        fTarget = target;
    }

    // ----------------------------------------------------------
    @Override
    public void evaluate()
        throws Throwable
    {
        Method wrapper = null;
        try
        {
            Class<?> cls = fTarget.getClass();
            while (wrapper == null && cls != null)
            {
                try
                {
                    wrapper = cls.getDeclaredMethod("runTestMethod",
                        Statement.class);
                }
                catch (Exception e)
                {
                    // ignore
                }
                cls = cls.getSuperclass();
            }
            if (wrapper != null)
            {
                wrapper.setAccessible(true);
            }
        }
        catch (Exception e)
        {
            wrapper = null;
        }
        if (wrapper == null)
        {
            fNext.evaluate();
        }
        else
        {
            (new org.junit.runners.model.FrameworkMethod(wrapper))
                .invokeExplosively(fTarget, fNext);
        }
    }
}
