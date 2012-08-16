/*==========================================================================*\
 |  $Id: ProcessClosure.java,v 1.2 2009/09/13 12:59:13 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.bfd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *  
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:13 $
 */
public class ProcessClosure
{
	protected static class ReaderThread extends Thread
	{
		public ReaderThread(ThreadGroup group, String name, InputStream in, OutputStream out)
		{
			super(group, name);
			
			outputStream = out;
			inputStream = in;
			setDaemon(true);
			lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
		}
		
		@Override
		public void run()
		{
			try
			{
				try
				{
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					String line;
					
					while ((line = reader.readLine()) != null)
					{
						line += lineSeparator;
						outputStream.write(line.getBytes());
					}
				}
				catch (IOException e)
				{
					
				}
				finally
				{
					try
					{
						outputStream.flush();
					}
					catch (IOException e)
					{
						
					}
					
					try
					{
						inputStream.close();
					}
					catch (IOException e)
					{
						
					}
				}
			}
			finally
			{
				complete();
			}
		}
		
		
		public synchronized boolean finished()
		{
			return finished;
		}
		
		
		public synchronized void waitFor()
		{
			while (!finished)
			{
				try
				{
					wait();
				}
				catch (InterruptedException e)
				{
					
				}
			}
		}
		
		
		public synchronized void complete()
		{
			finished = true;
			notify();
		}
		
		
		public void close()
		{
			try
			{
				outputStream.close();
			}
			catch (IOException e)
			{
				
			}
		}

		private InputStream inputStream;
		private OutputStream outputStream;
		private boolean finished = false;
		private String lineSeparator;
	}
	
	
	public ProcessClosure(Process process)
	{
		this(process, new NullOutputStream(), new NullOutputStream());
	}


	public ProcessClosure(Process process, OutputStream outputStream, OutputStream errorStream)
	{
		this.process = process;
		this.output = outputStream;
		this.error = errorStream;
	}
	
	
	public void runNonBlocking()
	{
		ThreadGroup group = new ThreadGroup("CBuilder" + counter++); //$NON-NLS-1$
		
		InputStream stdout = process.getInputStream();
		InputStream stderr = process.getErrorStream();
		
		outputReader = new ReaderThread(group, "OutputReader", stdout, output); //$NON-NLS-1$
		errorReader = new ReaderThread(group, "ErrorReader", stderr, error); //$NON-NLS-1$
		
		outputReader.start();
		errorReader.start();
	}


	public void runBlocking()
	{
		runNonBlocking();
		
		boolean finished = false;
		
		while (!finished)
		{
			try
			{
				process.waitFor();
			}
			catch (InterruptedException e)
			{
				
			}
			
			try
			{
				process.exitValue();
				finished = true;
			}
			catch (IllegalStateException e)
			{
				
			}
		}
		
		if (!outputReader.finished())
		{
			outputReader.waitFor();
		}
		
		if (!errorReader.finished())
		{
			errorReader.waitFor();
		}
		
		outputReader.close();
		errorReader.close();
		
		process = null;
		outputReader = null;
		errorReader = null;
	}


	public boolean isAlive()
	{
		if (process != null)
		{
			if (outputReader.isAlive() || errorReader.isAlive())
			{
				return true;
			}
			
			process = null;
			outputReader.close();
			errorReader.close();
			outputReader = null;
			errorReader = null;
		}
		
		return false;
	}


	public boolean isRunning()
	{
		if (process != null)
		{
			if (outputReader.isAlive() || errorReader.isAlive())
			{
				return true;
			}
			
			process = null;
		}
		
		return false;
	}
	
	
	public void terminate()
	{
		if (process != null)
		{
			process.destroy();
			process = null;
		}
		
		if (!outputReader.finished())
		{
			outputReader.waitFor();
		}
		
		if (!errorReader.finished())
		{
			errorReader.waitFor();
		}
		
		outputReader.close();
		errorReader.close();
		outputReader = null;
		errorReader = null;
	}


	protected static int counter = 0;
	
	protected Process process;
	
	protected ReaderThread outputReader;
	protected ReaderThread errorReader;
	protected OutputStream output;
	protected OutputStream error;
}
