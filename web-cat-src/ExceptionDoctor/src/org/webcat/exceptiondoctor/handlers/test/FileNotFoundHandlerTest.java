package org.webcat.exceptiondoctor.handlers.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.webcat.exceptiondoctor.handlers.FileNotFoundExceptionHandler;

import junit.framework.TestCase;

public class FileNotFoundHandlerTest extends TestCase
{

	public void testGetErrorType()
	{
		FileNotFoundExceptionHandler handle = new FileNotFoundExceptionHandler();
		FileNotFoundException wrapped = null;
		try
		{
			String fileName = "blah.blah";
			File f = new File(fileName);
			@SuppressWarnings("unused")
			Scanner s = new Scanner(f);
		}
		catch (FileNotFoundException e)
		{
			try
			{
				wrapped = (FileNotFoundException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("blah.blah"));
	}

	public void testGetErrorType2()
	{
		FileNotFoundExceptionHandler handle = new FileNotFoundExceptionHandler();
		FileNotFoundException wrapped = null;
		try
		{
			String fileName = "/home/mike/blah.blah";
			File f = new File(fileName);
			@SuppressWarnings("unused")
			Scanner s = new Scanner(f);
		}
		catch (FileNotFoundException e)
		{
			try
			{
				wrapped = (FileNotFoundException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("blah.blah"));
	}

	public void testGetErrorType3()
	{
		FileNotFoundExceptionHandler handle = new FileNotFoundExceptionHandler();
		FileNotFoundException wrapped = null;
		try
		{
			String fileName = "/home/mike/notreal/blah.blah";
			File f = new File(fileName);
			@SuppressWarnings("unused")
			Scanner s = new Scanner(f);
		}
		catch (FileNotFoundException e)
		{
			try
			{
				wrapped = (FileNotFoundException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			wrapped.printStackTrace();
		}
		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("blah.blah"));
	}
}
