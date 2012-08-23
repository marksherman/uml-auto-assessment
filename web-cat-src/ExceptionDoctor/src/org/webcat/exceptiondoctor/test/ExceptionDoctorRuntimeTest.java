package org.webcat.exceptiondoctor.test;

import org.webcat.exceptiondoctor.runtime.ExceptionDoctorRuntime;
import junit.framework.TestCase;

public class ExceptionDoctorRuntimeTest extends TestCase
{
	ExceptionDoctorRuntime br;

	@Override
	public void tearDown()
	{
		// System.out.println("***********************END OF TEST***************");
		// System.out.println();
		// System.out.println();
	}

	@Override
	public void setUp()
	{
		br = new ExceptionDoctorRuntime(false);
	}

	public void testStart()
	{

		String[] args = { "testPrograms.NullPointerProgram" };
		br.start(args);

	}

	public void testCustomExcep()
	{
		String[] args = { "testPrograms.UserDefinedProgram" };
		br.start(args);
	}

	public void testCustom2()
	{
		String[] args = { "testPrograms.UserDefinedProgram2" };
		br.start(args);
		// br.printMappings();
	}
}
