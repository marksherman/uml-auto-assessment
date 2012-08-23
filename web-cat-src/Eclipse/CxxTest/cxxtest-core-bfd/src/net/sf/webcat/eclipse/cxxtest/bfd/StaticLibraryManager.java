/*==========================================================================*\
 |  $Id: StaticLibraryManager.java,v 1.5 2009/09/21 14:16:50 aallowat Exp $
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.webcat.eclipse.cxxtest.CxxTestPlugin;
import net.sf.webcat.eclipse.cxxtest.bfd.i18n.Messages;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.envvar.IEnvironmentVariable;
import org.eclipse.cdt.core.envvar.IEnvironmentVariableManager;
import org.eclipse.cdt.managedbuilder.gnu.cygwin.GnuCygwinConfigurationEnvironmentSupplier;
import org.eclipse.cdt.utils.spawner.ProcessFactory;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *  
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.5 $ $Date: 2009/09/21 14:16:50 $
 */
public class StaticLibraryManager
{
	private StaticLibraryManager()
	{
		isWindows = System.getProperty("os.name").toLowerCase().startsWith( //$NON-NLS-1$
				"windows "); //$NON-NLS-1$
	}


	public static StaticLibraryManager getInstance()
	{
		if (instance == null)
		{
			instance = new StaticLibraryManager();
		}
		
		return instance;
	}


	public String getMissingLibraryString()
	{
		ArrayList<String> list = new ArrayList<String>();

		if (!hasBfd)
		{
			list.add("libbfd"); //$NON-NLS-1$
		}
		
		if (!hasIntl)
		{
			list.add("libintl"); //$NON-NLS-1$
		}
		
		if (!hasIberty)
		{
			list.add("libiberty"); //$NON-NLS-1$
		}
		
		if (list.isEmpty())
		{
			return null;
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append(Messages.StaticLibraryManager_MissingLibrariesMsgStart);

		buffer.append(list.get(0));
		for (int i = 1; i < list.size(); i++)
		{
			buffer.append(", "); //$NON-NLS-1$
			buffer.append(list.get(i));
		}
		
		return buffer.toString();
	}


	public void checkForDependencies(IProgressMonitor monitor)
	{
		monitor.beginTask(Messages.StaticLibraryManager_CheckingLibraryReqs, 6);

		// First make an attempt to just build with bfd.  We might not need to
		// consider any other dependencies.
		
		monitor.subTask(Messages.StaticLibraryManager_LookingForBfd);
		hasBfd = tryToCompile("check-libbfd.c", "bfd"); //$NON-NLS-1$
		monitor.worked(1);

		if (hasBfd)
		{
			hasIntl = true;
			needsLinkToIntl = false;
			hasIberty = true;
			needsLinkToIberty = false;

			monitor.done();
			return;
		}

		// Check for libintl functions, first without explicitly linking
		// (in case they're part of glibc), and then by linking directly.

		monitor.subTask(Messages.StaticLibraryManager_LookingForIntlBuiltIn);
		boolean hasIntlBuiltIn = tryToCompile("check-libintl.c"); //$NON-NLS-1$
		monitor.worked(1);

		if (!hasIntlBuiltIn)
		{
			monitor.subTask(Messages.StaticLibraryManager_LookingForIntlSeparate);
			hasIntl = tryToCompile("check-libintl.c", "intl"); //$NON-NLS-1$ //$NON-NLS-2$
			needsLinkToIntl = true;
		}
		else
		{
			hasIntl = true;
			needsLinkToIntl = false;
		}
		
		monitor.worked(1);

		// Check for libiberty functions, first without explicitly linking
		// (in case they're part of glibc), and then by linking directly.

		monitor.subTask(Messages.StaticLibraryManager_LookingForIbertyBuiltIn);
		boolean hasIbertyBuiltIn = tryToCompile("check-libiberty.c"); //$NON-NLS-1$
		monitor.worked(1);

		if (!hasIbertyBuiltIn)
		{
			monitor.subTask(Messages.StaticLibraryManager_LookingForIbertySeparate);
			hasIberty = tryToCompile("check-libiberty.c", "iberty"); //$NON-NLS-1$ //$NON-NLS-2$
			needsLinkToIberty = true;
		}
		else
		{
			hasIberty = true;
			needsLinkToIberty = false;
		}

		monitor.worked(1);
		
		// Now use what we know to try search for libbfd successfully. For
		// this to be successful we need to have support for several libintl
		// and libiberty functions, so use the information collected above to
		// link to them explicitly if necessary.

		monitor.subTask(Messages.StaticLibraryManager_LookingForBfd);
		hasBfd = tryToCompile("check-libbfd.c", librariesNeededForBfd()); //$NON-NLS-1$
		monitor.worked(1);
		
		monitor.done();
	}


	private String[] librariesNeededForBfd()
	{
		ArrayList<String> libs = new ArrayList<String>();
		libs.add("bfd"); //$NON-NLS-1$
		
		if (shouldAddIntlToBuild())
		{
			libs.add("intl"); //$NON-NLS-1$
		}
		
		if (shouldAddIbertyToBuild())
		{
			libs.add("iberty"); //$NON-NLS-1$
		}

		return libs.toArray(new String[libs.size()]);
	}


	private String[] calculateEnvironment()
	{
		TreeMap<String, String> envMap;
		if (isWindows)
		{
			envMap = new TreeMap<String, String>(new Comparator<String>() {
				public int compare(String lhs, String rhs)
				{
					return lhs.compareToIgnoreCase(rhs);
				}
			});
		}
		else
		{
			envMap = new TreeMap<String, String>();
		}
		
		IEnvironmentVariableManager mngr =
			CCorePlugin.getDefault().getBuildEnvironmentManager();
		IEnvironmentVariable[] vars = mngr.getVariables(null, true);
		
		if (vars != null)
		{
			for(int i = 0; i < vars.length; i++)
			{
				envMap.put(vars[i].getName(), vars[i].getValue());
			}
		}

		GnuCygwinConfigurationEnvironmentSupplier gnu =
			new GnuCygwinConfigurationEnvironmentSupplier();
		vars = gnu.getVariables(null, null);
		
		if (vars != null)
		{
			for(int i = 0; i < vars.length; i++)
			{
				String oldValue = envMap.get(vars[i].getName());
				
				if (oldValue != null)
				{
					oldValue = vars[i].getValue() + vars[i].getDelimiter() +
						oldValue;
				}
				else
				{
					oldValue = vars[i].getValue();
				}
	
				envMap.put(vars[i].getName(), oldValue);
			}
		}

		List<String> strings = new ArrayList<String>(envMap.size());
		for (Map.Entry<String, String> entry : envMap.entrySet())
		{
			strings.add(entry.getKey() + "=" + entry.getValue()); //$NON-NLS-1$
		}
		
		return (String[]) strings.toArray(new String[strings.size()]);
	}


	private boolean tryToCompile(String sourceFile, String... linkLibraries)
	{
		String[] envp = calculateEnvironment();
		sourceFile = getLibraryCheckPath(sourceFile);

		File tempOut = null;
		try
		{
			tempOut = File.createTempFile("libchkexe", null); //$NON-NLS-1$
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}

		ArrayList<String> argList = new ArrayList<String>();
		argList.add("sh"); //$NON-NLS-1$
		argList.add("-c"); //$NON-NLS-1$

		StringBuffer cmdLine = new StringBuffer();
		
		cmdLine.append("gcc -o "); //$NON-NLS-1$
		
		if (isWindows)
		{
			cmdLine.append('"');
			cmdLine.append(tempOut.getAbsolutePath().replace(
					File.separatorChar, '/'));
			cmdLine.append('"');
			cmdLine.append(" "); //$NON-NLS-1$
			cmdLine.append('"');
			cmdLine.append(sourceFile.replace(File.separatorChar, '/'));
			cmdLine.append('"');
		}
		else
		{
			cmdLine.append('"');
			cmdLine.append(tempOut.getAbsolutePath());
			cmdLine.append('"');
			cmdLine.append(" "); //$NON-NLS-1$
			cmdLine.append('"');
			cmdLine.append(sourceFile);
			cmdLine.append('"');
		}

		if (linkLibraries != null)
		{
			for (String lib : linkLibraries)
			{
				cmdLine.append(" -l"); //$NON-NLS-1$
				cmdLine.append(lib);
			}
		}
		
		argList.add(cmdLine.toString());

		String[] args = argList.toArray(new String[argList.size()]);

		boolean success = false;

		Process proc;
		try
		{
//			System.out.println(Arrays.toString(args));
			proc = ProcessFactory.getFactory().exec(args, envp);
			ProcessClosure closure = new ProcessClosure(proc);
//					new NonClosingOutputStreamWrapper(System.out),
//					new NonClosingOutputStreamWrapper(System.out));
			closure.runBlocking();

			success = (proc.exitValue() == 0);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		tempOut.delete();

		return success;
	}


	private String getLibraryCheckPath(String file)
	{
		String path = null;

		try
		{
			URL entry = FileLocator.find(
					CxxTestPlugin.getDefault().getBundle(),
					new Path("/library-checks/" + file), null); //$NON-NLS-1$
			URL url = FileLocator.resolve(entry);
			path = url.getFile();

			// This special check is somewhat shady, but it looks like it's
			// the only way to handle a Windows path properly, since Eclipse
			// returns a string like "/C:/folder/...".
			if(path.charAt(2) == ':')
				path = path.substring(1);
			
			path = new Path(path).toOSString();
			if(path.charAt(path.length() - 1) == File.separatorChar)
				path = path.substring(0, path.length() - 1);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return path;
	}


	public boolean hasBfd()
	{
		return hasBfd;
	}


	public boolean shouldAddIntlToBuild()
	{
		return needsLinkToIntl && hasIntl;
	}


	public boolean shouldAddIbertyToBuild()
	{
		return needsLinkToIberty && hasIberty;
	}


	private boolean isWindows;

	private boolean hasBfd;
	private boolean hasIntl;
	private boolean needsLinkToIntl;
	private boolean hasIberty;
	private boolean needsLinkToIberty;

	private static StaticLibraryManager instance;
}
