/*==========================================================================*\
 |  $Id: ExtraOptionsUpdater.java,v 1.5 2009/09/13 21:57:15 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.internal.options;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.webcat.eclipse.cxxtest.CxxTestPlugin;
import net.sf.webcat.eclipse.cxxtest.options.IExtraOptionsUpdater;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionConverter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * Maintains the table of extra options handlers defined by all plug-ins
 * currently loaded in Eclipse.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.5 $ $Date: 2009/09/13 21:57:15 $
 */
public class ExtraOptionsUpdater implements IExtraOptionsUpdater
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	/**
	 * Creates a new instance of the ExtraOptionsRegistry class.
	 */
	public ExtraOptionsUpdater()
	{
		isWindows = System.getProperty("os.name").toLowerCase().startsWith( //$NON-NLS-1$
			"windows "); //$NON-NLS-1$

		optionSets =
		        new HashMap<String, SortedMap<Version, IConfigurationElement>>();

		loadExtensions();
	}


	// ------------------------------------------------------------------------
	public boolean isUpdateNeeded(IProject project)
	{
		Properties versionProps = loadVersionProperties(project);

		// If there are a different number of option handlers loaded than there
		// are in the properties file for the project, then something is
		// mismatched and it needs an update.

		if(versionProps.size() != optionSets.size())
		{
			return true;
		}

		for(String loadedId : optionSets.keySet())
		{
			Version latestVersion = getLatestVersion(loadedId);
			Version projectVersion =
			        Version.parseVersion(versionProps.getProperty(loadedId));

			if(projectVersion.compareTo(latestVersion) < 0)
			{
				return true;
			}
		}

		return false;
	}


	// ------------------------------------------------------------------------
	public void updateOptions(IProject project)
	{
		Properties versionProps = loadVersionProperties(project);

		for(String loadedId : optionSets.keySet())
		{
			if(versionProps.containsKey(loadedId))
			{
				Version version = Version.parseVersion(
						versionProps.getProperty(loadedId));
				removeOptions(project, loadedId, version);
			}

			addLatestOptions(project, loadedId);

			Version latestVersion = getLatestVersion(loadedId);
			versionProps.setProperty(loadedId, latestVersion.toString());
		}

		storeVersionProperties(project, versionProps);
	}


	// ------------------------------------------------------------------------
	public String[] getLatestCxxTestRunnerIncludes(IConfiguration configuration)
	{
		ArrayList<String> includeList = new ArrayList<String>();

		for(String loadedId : optionSets.keySet())
		{
			SortedMap<Version, IConfigurationElement> optionsForId =
		        optionSets.get(loadedId);

			if(optionsForId == null)
			{
				break;
			}
	
			Version latestVersion = getLatestVersion(loadedId);
			if(latestVersion == null)
			{
				break;
			}
	
			IConfigurationElement optionSets = optionsForId.get(latestVersion);
			IConfigurationElement[] optionSetElems = optionSets.getChildren("optionSet"); //$NON-NLS-1$

			for (IConfigurationElement optionSet : optionSetElems)
			{
				boolean enabled = evaluateOptionSetEnablement(
						optionSet, configuration);
				
				if (enabled)
				{
					// Add the runner include paths to the configuration.

					IConfigurationElement[] includeElems =
						optionSet.getChildren("runnerIncludes"); //$NON-NLS-1$

					for(IConfigurationElement includeElem : includeElems)
					{
						IConfigurationElement[] pathElems =
							includeElem.getChildren("includeFile"); //$NON-NLS-1$
						
						for(IConfigurationElement pathElem : pathElems)
						{
							String path = pathElem.getAttribute("path"); //$NON-NLS-1$
							includeList.add(path);
						}
					}
				}
			}
		}
		
		return includeList.toArray(new String[includeList.size()]);
	}
	

	// ------------------------------------------------------------------------
	public void removeAllOptions(IProject project)
	{
		Properties versionProps = loadVersionProperties(project);

		for(String loadedId : optionSets.keySet())
		{
			if(versionProps.containsKey(loadedId))
			{
				Version version = Version.parseVersion(
						versionProps.getProperty(loadedId));
				removeOptions(project, loadedId, version);
			}
		}

		versionProps.clear();
		storeVersionProperties(project, versionProps);
	}


	// ------------------------------------------------------------------------
	/**
	 * Gets the latest version number in use by option handlers with a
	 * particular unique identifier.
	 */
	private Version getLatestVersion(String id)
	{
		if(optionSets.containsKey(id))
			return optionSets.get(id).lastKey();
		else
			return null;
	}


	// ------------------------------------------------------------------------
	private boolean evaluateOptionSetEnablement(IConfigurationElement optionSet,
			IConfiguration configuration)
	{
		IConfigurationElement[] enablementElems =
			optionSet.getChildren("enablement"); //$NON-NLS-1$

		boolean enabled = true;

		if (enablementElems != null && enablementElems.length > 0)
		{
			try 
			{
				Expression expression = ExpressionConverter.getDefault().perform(
						enablementElems[0]);

				EvaluationContext context = new EvaluationContext(
						null, configuration);
				
				EvaluationResult result = expression.evaluate(context);
				enabled = (result == EvaluationResult.TRUE);
			}
			catch (CoreException e)
			{
				enabled = false;
			}
		}
		
		return enabled;
	}


	// ------------------------------------------------------------------------
	private void addLatestOptions(IProject project, String id)
	{
		SortedMap<Version, IConfigurationElement> optionsForId =
		        optionSets.get(id);

		if(optionsForId == null)
		{
			return;
		}

		Version latestVersion = getLatestVersion(id);

		if(latestVersion == null)
		{
			return;
		}

		IConfigurationElement optionSets = optionsForId.get(latestVersion);
		IConfigurationElement[] optionSetElems = optionSets.getChildren("optionSet"); //$NON-NLS-1$

		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
		IConfiguration[] configs =
		        buildInfo.getManagedProject().getConfigurations();

		for (IConfiguration config : configs)
		{
			for (IConfigurationElement optionSet : optionSetElems)
			{
				boolean enabled = evaluateOptionSetEnablement(
						optionSet, config);
				
				if (enabled)
				{
					// Add the runner include paths to the configuration.

					IConfigurationElement[] includeElems =
						optionSet.getChildren("runnerIncludes"); //$NON-NLS-1$
					
					for(IConfigurationElement includeElem : includeElems)
					{
						String pluginId = includeElem.getAttribute("pluginId"); //$NON-NLS-1$
						String path = includeElem.getAttribute("path"); //$NON-NLS-1$

						if(pluginId == null)
						{
							pluginId = optionSet.getContributor().getName();
						}

						String includePath =
							getBundleEntryPath(pluginId, path);

						try
						{
							addCxxTestRunnerInclude(config, includePath);
						}
						catch (BuildException e)
						{
							e.printStackTrace();
						}
					}
					
					IConfigurationElement[] toolElems =
				        optionSet.getChildren("tool"); //$NON-NLS-1$

					for(IConfigurationElement toolElem : toolElems)
					{
						try
						{
							addOptionsForTool(config, toolElem);
						}
						catch(BuildException e)
						{
							e.printStackTrace();
						}
					}					
				}
			}
		}

		ManagedBuildManager.saveBuildInfo(project, true);
	}


	// ------------------------------------------------------------------------
	private void addOptionsForTool(IConfiguration config,
	        IConfigurationElement toolElem) throws BuildException
	{
		String superClassId = toolElem.getAttribute("superClassId"); //$NON-NLS-1$
		ITool[] tools = config.getToolsBySuperClassId(superClassId);
		IConfigurationElement[] optionElems = toolElem.getChildren();

		for(ITool tool : tools)
		{
			for(IConfigurationElement optionElem : optionElems)
			{
				String optionType = optionElem.getName();
				String optionId = optionElem.getAttribute("id"); //$NON-NLS-1$

				if(optionType.equals("includesOption")) //$NON-NLS-1$
				{
					String[] newEntries = getPathsForOption(optionElem, true);
					ProjectOptionsUtil.addToIncludes(tool, optionId, newEntries);
				}
				else if(optionType.equals("librariesOption")) //$NON-NLS-1$
				{
					String[] newEntries = getItemsForOption(optionElem);
					ProjectOptionsUtil.addToLibraries(tool, optionId,
					        newEntries);
				}
				else if(optionType.equals("definedSymbolsOption")) //$NON-NLS-1$
				{
					String[] newEntries = getItemsForOption(optionElem);
					ProjectOptionsUtil.addToDefinedSymbols(tool, optionId,
					        newEntries);
				}
				else if(optionType.equals("stringListOption")) //$NON-NLS-1$
				{
					String[] newEntries = getItemsForOption(optionElem);
					ProjectOptionsUtil.addToStringList(tool, optionId,
					        newEntries);
				}
				else if(optionType.equals("pathListOption")) //$NON-NLS-1$
				{
					String[] newEntries = getPathsForOption(optionElem, true);
					ProjectOptionsUtil.addToStringList(tool, optionId,
					        newEntries);
				}
				else if(optionType.equals("splitStringOption")) //$NON-NLS-1$
				{
					String[] newEntries =
					        getItemsAndPathsForOption(optionElem, true);
					ProjectOptionsUtil.addToString(tool, optionId, newEntries);
				}
				else if(optionType.equals("booleanOption")) //$NON-NLS-1$
				{
					Boolean newValue =
					        Boolean.parseBoolean(optionElem.getAttribute("value")); //$NON-NLS-1$
					ProjectOptionsUtil.setBoolean(tool, optionId, newValue);
				}
				else if(optionType.equals("stringOption")) //$NON-NLS-1$
				{
				}
			}
		}
	}


	// ------------------------------------------------------------------------
	private void addCxxTestRunnerInclude(IConfiguration config,
			String path) throws BuildException
	{
		String superClassId = "cdt.managedbuild.tool.gnu.cpp.compiler"; //$NON-NLS-1$
		ITool[] tools = config.getToolsBySuperClassId(superClassId);

		String optionId = "gnu.cpp.compiler.option.include.paths"; //$NON-NLS-1$

		for(ITool tool : tools)
		{
			path = "\"" + path.replace('\\', '/') + "\""; //$NON-NLS-1$ //$NON-NLS-2$

			String[] newEntries = new String[] { path };
			ProjectOptionsUtil.addToIncludes(tool, optionId, newEntries);
		}
	}


	// ------------------------------------------------------------------------
	private void removeOptionsForTool(IProject project, IConfiguration config,
	        IConfigurationElement toolElem) throws BuildException
	{
		String superClassId = toolElem.getAttribute("superClassId"); //$NON-NLS-1$
		ITool[] tools = config.getToolsBySuperClassId(superClassId);
		IConfigurationElement[] optionElems = toolElem.getChildren();

		for(ITool tool : tools)
		{
			for(IConfigurationElement optionElem : optionElems)
			{
				String optionType = optionElem.getName();
				String optionId = optionElem.getAttribute("id"); //$NON-NLS-1$

				if(optionType.equals("includesOption")) //$NON-NLS-1$
				{
					String[] newEntries = getPathPatternsForOption(optionElem, true);
					ProjectOptionsUtil.removeFromIncludesIf(tool, optionId,
					        new RegexOptionPredicate(newEntries));
				}
				else if(optionType.equals("librariesOption")) //$NON-NLS-1$
				{
					String[] newEntries = getItemsForOption(optionElem);
					ProjectOptionsUtil.removeFromLibrariesIf(tool, optionId,
					        new ChoiceOptionPredicate(newEntries));
				}
				else if(optionType.equals("definedSymbolsOption")) //$NON-NLS-1$
				{
					String[] newEntries = getItemsForOption(optionElem);
					ProjectOptionsUtil.removeFromDefinedSymbolsIf(tool,
					        optionId, new ChoiceOptionPredicate(newEntries));
				}
				else if(optionType.equals("stringListOption")) //$NON-NLS-1$
				{
					String[] newEntries = getItemsForOption(optionElem);
					ProjectOptionsUtil.removeFromStringListIf(tool, optionId,
					        new ChoiceOptionPredicate(newEntries));
				}
				else if(optionType.equals("pathListOption")) //$NON-NLS-1$
				{
					String[] newEntries = getItemsForOption(optionElem);
					ProjectOptionsUtil.removeFromStringListIf(tool, optionId,
							new ChoiceOptionPredicate(newEntries));

					newEntries = getPathPatternsForOption(optionElem, true);
					ProjectOptionsUtil.removeFromStringListIf(tool, optionId,
					        new RegexOptionPredicate(newEntries));
				}
				else if(optionType.equals("splitStringOption")) //$NON-NLS-1$
				{
					String[] newEntries = getItemsForOption(optionElem);
					ProjectOptionsUtil.removeFromStringIf(tool, optionId,
					        new ChoiceOptionPredicate(newEntries));
				}
				else if(optionType.equals("booleanOption")) //$NON-NLS-1$
				{
					Boolean newValue =
					        Boolean.parseBoolean(optionElem.getAttribute("value")); //$NON-NLS-1$
					String ignore = optionElem.getAttribute("ignoreOnRemove"); //$NON-NLS-1$
					if(ignore != null && !Boolean.parseBoolean(ignore))
						ProjectOptionsUtil.setBoolean(tool, optionId, !newValue);
				}
				
				ManagedBuildManager.saveBuildInfo(project, true);
			}
		}
	}


	// ------------------------------------------------------------------------
	private String[] getItemsForOption(IConfigurationElement optionElem)
	{
		IConfigurationElement[] itemElems = optionElem.getChildren("item"); //$NON-NLS-1$
		ArrayList<String> items = new ArrayList<String>();

		for(IConfigurationElement itemElem : itemElems)
			items.add(itemElem.getAttribute("value")); //$NON-NLS-1$

		String[] itemArray = new String[items.size()];
		items.toArray(itemArray);
		return itemArray;
	}


	// ------------------------------------------------------------------------
	private String[] getItemsAndPathsForOption(
	        IConfigurationElement optionElem, boolean quoted)
	{
		IConfigurationElement[] itemElems = optionElem.getChildren();
		ArrayList<String> items = new ArrayList<String>();

		for(IConfigurationElement itemElem : itemElems)
		{
			if(itemElem.getName().equals("item")) //$NON-NLS-1$
			{
				items.add(itemElem.getAttribute("value")); //$NON-NLS-1$
			}
			else if(itemElem.getName().equals("path")) //$NON-NLS-1$
			{
				String fullPath = getPathFromElement(itemElem, quoted);
				if(fullPath != null)
					items.add(fullPath);
			}
		}

		String[] itemArray = new String[items.size()];
		items.toArray(itemArray);
		return itemArray;
	}


	// ------------------------------------------------------------------------
	private String getBundleEntryPath(String pluginId, String relativePath)
	{
		String path = null;

		try
		{
			Bundle bundle = Platform.getBundle(pluginId);
			if(bundle == null)
				return null;

			URL entryURL =
			        FileLocator.find(bundle, new Path(relativePath), null);
			URL url = FileLocator.resolve(entryURL);
			path = url.getFile();

			// This special check is somewhat shady, but it looks like it's
			// the only way to handle a Windows path properly, since Eclipse
			// returns a string like "/C:/folder/...". The Cygwin make tools
			// don't like paths with colons in them, so we convert them to
			// the "/cygdrive/c/..." format instead.

			if(isWindows && path.charAt(2) == ':')
			{
				char letter = Character.toLowerCase(path.charAt(1));
				path = "/cygdrive/" + letter + path.substring(3); //$NON-NLS-1$
			}

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


	// ------------------------------------------------------------------------
	private String getPathFromElement(IConfigurationElement pathElem,
	        boolean quoted)
	{
		String prefix = pathElem.getAttribute("prefix"); //$NON-NLS-1$
		if(prefix == null)
			prefix = ""; //$NON-NLS-1$

		String pluginId = pathElem.getAttribute("pluginId"); //$NON-NLS-1$
		String relPath = pathElem.getAttribute("relativePath"); //$NON-NLS-1$

		String fullPath;

		if(pluginId == null)
			fullPath = relPath;
		else
			fullPath = getBundleEntryPath(pluginId, relPath);

		if(fullPath != null)
		{
			fullPath = fullPath.replace('\\', '/');
			
			if(quoted)
				return (prefix + "\"" + fullPath + "\""); //$NON-NLS-1$ //$NON-NLS-2$
			else
				return (prefix + fullPath);
		}

		return null;
	}


	// ------------------------------------------------------------------------
	private String[] getPathsForOption(IConfigurationElement optionElem,
	        boolean quoted)
	{
		IConfigurationElement[] pathElems = optionElem.getChildren("path"); //$NON-NLS-1$
		ArrayList<String> paths = new ArrayList<String>();

		for(IConfigurationElement pathElem : pathElems)
		{
			String fullPath = getPathFromElement(pathElem, quoted);
			if(fullPath != null)
				paths.add(fullPath);
		}

		String[] pathArray = new String[paths.size()];
		paths.toArray(pathArray);
		return pathArray;
	}


	// ------------------------------------------------------------------------
	private String[] getPathPatternsForOption(IConfigurationElement optionElem,
			boolean quoted)
	{
		IConfigurationElement[] pathElems = optionElem.getChildren("path"); //$NON-NLS-1$
		ArrayList<String> patterns = new ArrayList<String>();

		for(IConfigurationElement pathElem : pathElems)
		{
			String fullPath = getPathFromElement(pathElem, quoted);
			if(fullPath != null)
			{
				boolean isDir = new File(fullPath).isDirectory();
				String pattern =
					ShellStringUtils.patternForAnyVersionOfPluginRelativePath(
							fullPath, isDir);
				
				patterns.add(pattern);
			}
		}

		String[] patternArray = new String[patterns.size()];
		patterns.toArray(patternArray);
		return patternArray;
	}


	// ------------------------------------------------------------------------
	private void removeOptions(IProject project, String id, Version version)
	{
		SortedMap<Version, IConfigurationElement> optionsForId =
		        optionSets.get(id);
		if(optionsForId == null)
		{
			return;
		}

		Version latestVersion = getLatestVersion(id);
		if(latestVersion == null)
		{
			return;
		}

		IConfigurationElement optionSets = optionsForId.get(latestVersion);
		IConfigurationElement[] optionSetElems = optionSets.getChildren("optionSet"); //$NON-NLS-1$

		IManagedBuildInfo buildInfo = ManagedBuildManager.getBuildInfo(project);
		IConfiguration[] configs =
		        buildInfo.getManagedProject().getConfigurations();

		for (IConfiguration config : configs)
		{
			for (IConfigurationElement optionSet : optionSetElems)
			{
				// TODO this needs to be fixed; we need to remember the
				// preferences that were previously enabled and use THAT
				// information to remove the correct options, somehow
				
				boolean enabled = true; //evaluateOptionSetEnablement(
						//optionSet, config);
				
				if (enabled)
				{
					IConfigurationElement[] toolElems =
				        optionSet.getChildren("tool"); //$NON-NLS-1$
					
					for(IConfigurationElement toolElem : toolElems)
					{
						try
						{
							removeOptionsForTool(project, config, toolElem);
						}
						catch(BuildException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}

		ManagedBuildManager.saveBuildInfo(project, true);
	}


	// ------------------------------------------------------------------------
	/**
	 * Loads the extra options handlers from all currently loaded plug-ins.
	 */
	private void loadExtensions()
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint =
		        registry.getExtensionPoint(CxxTestPlugin.PLUGIN_ID
		                + ".extraProjectOptions"); //$NON-NLS-1$

		IConfigurationElement[] elements =
		        extensionPoint.getConfigurationElements();

		for(IConfigurationElement element : elements)
			loadExtraProjectOptions(element);
	}


	// ------------------------------------------------------------------------
	/**
	 * A helper function to populate the option handler table.
	 */
	private void loadExtraProjectOptions(IConfigurationElement element)
	{
		String optionsId = element.getAttribute("id"); //$NON-NLS-1$

		IConfigurationElement[] optionSetsElems =
		        element.getChildren("optionSets"); //$NON-NLS-1$

		SortedMap<Version, IConfigurationElement> optionSetsForVersion =
		        new TreeMap<Version, IConfigurationElement>();

		for(IConfigurationElement optionSetsElem : optionSetsElems)
		{
			Version version =
			        new Version(optionSetsElem.getAttribute("version")); //$NON-NLS-1$

			optionSetsForVersion.put(version, optionSetsElem);
		}

		if(optionSetsForVersion.size() != 0)
		{
			optionSets.put(optionsId, optionSetsForVersion);
		}
	}


	// ------------------------------------------------------------------------
	private Properties loadVersionProperties(IProject project)
	{
		Properties properties = new Properties();

		try
		{
			IFile propIFile = project.getFile(PROPERTIES_FILE);
			File propFile = propIFile.getRawLocation().toFile();

			if(propFile.exists())
				properties.load(new FileInputStream(propFile));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return properties;
	}


	// ------------------------------------------------------------------------
	private void storeVersionProperties(IProject project, Properties properties)
	{
		try
		{
			IFile propIFile = project.getFile(PROPERTIES_FILE);
			File propFile = propIFile.getRawLocation().toFile();
			properties.store(new FileOutputStream(propFile), PROPERTIES_COMMENT);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}


	// === Static Variables ===================================================

	/**
	 * The name of the properties file that keeps track of the version of each
	 * option handler's settings.
	 */
	private static final String PROPERTIES_FILE =
	        ".cxxtest.versions.properties"; //$NON-NLS-1$

	/**
	 * A comment added to the top of the option version properties file.
	 */
	private static final String PROPERTIES_COMMENT =
	        "Automatically generated file -- DO NOT MODIFY"; //$NON-NLS-1$


	// === Instance Variables =================================================

	/**
	 * A table that holds all of the extra options handlers registered in loaded
	 * plug-ins, keyed by their unique identifier. For each version key, the
	 * value of that key is a sorted map that contains all of the
	 * IExtraProjectOptions instances available for that option set, keyed by
	 * their version number.
	 */
	private Map<String, SortedMap<Version, IConfigurationElement>> optionSets;
	
	private boolean isWindows;
}
