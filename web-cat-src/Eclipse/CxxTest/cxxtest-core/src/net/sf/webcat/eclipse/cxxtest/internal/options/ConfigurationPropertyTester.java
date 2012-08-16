/*==========================================================================*\
 |  $Id: ConfigurationPropertyTester.java,v 1.1 2009/09/13 12:59:29 aallowat Exp $
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

import net.sf.webcat.eclipse.cxxtest.CxxTestPlugin;
import net.sf.webcat.eclipse.cxxtest.options.IExtraOptionsEnablement;

import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2009/09/13 12:59:29 $
 */
public class ConfigurationPropertyTester extends PropertyTester
{
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue)
	{
		IConfiguration configuration = (IConfiguration) receiver;
		
		if (PROP_CONFIGURATION_NAME.equals(property))
		{
			return configuration.getName().equals(expectedValue);
		}
		else if (PROP_PREFERENCE.equals(property))
		{
			IPreferenceStore store = CxxTestPlugin.getDefault().getPreferenceStore();
			String key = CxxTestPlugin.PLUGIN_ID + ".preferences." + args[0]; //$NON-NLS-1$
			String type = (String) args[1];
			Object value = null;

			if ("boolean".equals(type)) //$NON-NLS-1$
			{
				value = store.getBoolean(key);
			}
			else if ("int".equals(type)) //$NON-NLS-1$
			{
				value = store.getInt(key);
			}
			else if ("long".equals(type)) //$NON-NLS-1$
			{
				value = store.getLong(key);
			}
			else if ("string".equals(type)) //$NON-NLS-1$
			{
				value = store.getString(type);
			}
			else if ("float".equals(type)) //$NON-NLS-1$
			{
				value = store.getFloat(type);
			}
			else if ("double".equals(type)) //$NON-NLS-1$
			{
				value = store.getDouble(type);
			}
			
			return expectedValue.equals(value);
		}
		else if (PROP_EVALUATE_PREDICATE.equals(property))
		{
			String typename = (String) args[0];
			
			try
			{
				@SuppressWarnings("unchecked")
				Class<? extends IExtraOptionsEnablement> klass =
					(Class<? extends IExtraOptionsEnablement>)
					Class.forName(typename);
				
				IExtraOptionsEnablement predicate = klass.newInstance();
				boolean value = predicate.shouldProcessOptions(
						configuration.getOwner().getProject(), configuration);
				
				return expectedValue.equals(value);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return false;
		}
		else
		{
			assert false;
			return false;
		}
	}
	
	
	private static final String PROP_CONFIGURATION_NAME = "configurationName"; //$NON-NLS-1$
	private static final String PROP_PREFERENCE = "preference"; //$NON-NLS-1$
	private static final String PROP_EVALUATE_PREDICATE = "evaluatePredicate"; //$NON-NLS-1$
}
