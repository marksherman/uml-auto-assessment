package org.webcat.exceptiondoctor;

import org.webcat.exceptiondoctor.handlers.test.ArithmeticHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.ArrayIndexHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.ArrayStoreHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.ClassCastHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.FileNotFoundHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.IOHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.IllegalArgumentHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.IndexBoundHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.InputMismatchHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.LinkageHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.NegativeArraySizeHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.NoClassDefFoundHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.NoSuchElementHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.NullPointerHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.NumberFormatHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.OutOfMemoryHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.StackOverflowHandlerTest;
import org.webcat.exceptiondoctor.handlers.test.StringBoundsHandlerTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ExceptionHandlersTestSuite
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for exceptionHandlers");
		// $JUnit-BEGIN$
		suite.addTestSuite(InputMismatchHandlerTest.class);
		suite.addTestSuite(NoSuchElementHandlerTest.class);
		suite.addTestSuite(FileNotFoundHandlerTest.class);
		suite.addTestSuite(NegativeArraySizeHandlerTest.class);
		suite.addTestSuite(LinkageHandlerTest.class);
		suite.addTestSuite(NumberFormatHandlerTest.class);
		suite.addTestSuite(ArrayIndexHandlerTest.class);
		suite.addTestSuite(OutOfMemoryHandlerTest.class);
		suite.addTestSuite(StringBoundsHandlerTest.class);
		suite.addTestSuite(NoClassDefFoundHandlerTest.class);
		suite.addTestSuite(ClassCastHandlerTest.class);
		suite.addTestSuite(ArithmeticHandlerTest.class);
		suite.addTestSuite(IOHandlerTest.class);
		suite.addTestSuite(NullPointerHandlerTest.class);
		suite.addTestSuite(IllegalArgumentHandlerTest.class);
		suite.addTestSuite(ArrayStoreHandlerTest.class);
		suite.addTestSuite(StackOverflowHandlerTest.class);
		suite.addTestSuite(IndexBoundHandlerTest.class);
		// $JUnit-END$
		return suite;
	}

}
