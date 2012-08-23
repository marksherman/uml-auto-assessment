/*==========================================================================*\
 |  $Id: ContentAssistManager.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
 |
 |  This file is part of Web-CAT.
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

package org.webcat.oda.designer.contentassist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IPath;
import org.webcat.oda.designer.DesignerActivator;

//------------------------------------------------------------------------
/**
 * A class that manages the content assist information that is retrieved from
 * the Web-CAT server in order to ease the editing of data sets for the user.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ContentAssistManager.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class ContentAssistManager
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public ContentAssistManager()
    {
        this.subsystemVersions = new Hashtable<String, String>();

        this.entityDescriptions = new Hashtable<String, List<ContentAssistAttributeInfo>>();

        this.objectDescriptions = new Hashtable<String, List<ContentAssistObjectDescription>>();

        loadFromState();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void setServerCredentials(String newUrl, String newUser,
            String newPass)
    {
        if ((newUrl != null && !newUrl.equals(serverUrl))
                || (newUser != null && !newUser.equals(username))
                || (newPass != null && !newPass.equals(password)))
        {
            this.serverUrl = newUrl;
            this.username = newUser;
            this.password = newPass;

            update(false);
        }
    }


    // ----------------------------------------------------------
    private File getEntityDbFile()
    {
        IPath statePath = DesignerActivator.getDefault().getStateLocation();
        return statePath.append(ENTITY_DESCRIPTIONS_FILE).toFile();
    }


    // ----------------------------------------------------------
    private File getObjectDbFile()
    {
        IPath statePath = DesignerActivator.getDefault().getStateLocation();
        return statePath.append(OBJECT_DESCRIPTIONS_FILE).toFile();
    }


    // ----------------------------------------------------------
    private void loadFromState()
    {
        loadEntityDescriptionsFromState();
        loadObjectDescriptionsFromState();
    }


    // ----------------------------------------------------------
    private void loadEntityDescriptionsFromState()
    {
        File dbFile = getEntityDbFile();

        if (!dbFile.exists())
            return;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(dbFile));
            ContentAssistIOUtils.readEntityDescriptions(subsystemVersions,
                    entityDescriptions, reader);
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    private void loadObjectDescriptionsFromState()
    {
        File dbFile = getObjectDbFile();

        if (!dbFile.exists())
            return;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(dbFile));
            ContentAssistIOUtils.readObjectDescriptions(objectDescriptions,
                    reader);
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /* package */void writeToState()
    {
        writeEntityDescriptionsToState();
        writeObjectDescriptionsToState();
    }


    // ----------------------------------------------------------
    private void writeEntityDescriptionsToState()
    {
        File dbFile = getEntityDbFile();

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile));
            ContentAssistIOUtils.writeEntityDescriptions(subsystemVersions,
                    entityDescriptions, writer);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    private void writeObjectDescriptionsToState()
    {
        File dbFile = getObjectDbFile();

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile));
            ContentAssistIOUtils.writeObjectDescriptions(objectDescriptions,
                    writer);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    public void update(boolean forceEvenIfVersionsIdentical)
    {
        if (serverUrl != null)
        {
            ContentAssistReaderJob job = new ContentAssistReaderJob(this,
                    forceEvenIfVersionsIdentical);

            job.schedule();
        }

        writeToState();
    }


    // ----------------------------------------------------------
    public boolean isEntity(String name)
    {
        if (name == null)
            return false;
        else
            return entityDescriptions.containsKey(name);
    }


    // ----------------------------------------------------------
    public String[] getEntities()
    {
        String[] array = new String[entityDescriptions.size()];
        entityDescriptions.keySet().toArray(array);

        Arrays.sort(array, String.CASE_INSENSITIVE_ORDER);
        return array;
    }


    // ----------------------------------------------------------
    public String[] getAttributeNames(String entity)
    {
        if (isEntity(entity))
        {
            List<ContentAssistAttributeInfo> attrs = entityDescriptions
                    .get(entity);

            String[] names = new String[attrs.size()];

            int i = 0;
            for (ContentAssistAttributeInfo attrInfo : attrs)
            {
                names[i++] = attrInfo.name();
            }

            Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
            return names;
        }

        return null;
    }


    // ----------------------------------------------------------
    public String getAttributeType(String entity, String attribute)
    {
        if (isEntity(entity))
        {
            List<ContentAssistAttributeInfo> attrs = entityDescriptions
                    .get(entity);

            for (ContentAssistAttributeInfo attrInfo : attrs)
            {
                if (attrInfo.name().equals(attribute))
                {
                    return attrInfo.type();
                }
            }
        }

        return null;
    }


    // ----------------------------------------------------------
    public ContentAssistAttributeInfo getAttributeInfo(String entity,
            String attribute)
    {
        if (isEntity(entity))
        {
            List<ContentAssistAttributeInfo> attrs = entityDescriptions
                    .get(entity);

            for (ContentAssistAttributeInfo attrInfo : attrs)
            {
                if (attrInfo.name().equals(attribute))
                {
                    return attrInfo;
                }
            }
        }

        return null;
    }

    
    // ----------------------------------------------------------
    public String getKeyPathType(String rootClass, String keyPath)
    {
        String[] parts = keyPath.split("\\."); //$NON-NLS-1$

        for (String part : parts)
        {
            String partType = getAttributeType(rootClass, part);

            if (partType == null)
                return null;

            rootClass = partType;
        }

        return rootClass;
    }


    // ----------------------------------------------------------
    public ContentAssistObjectDescription[] getObjectDescriptions(String entity)
    {
        List<ContentAssistObjectDescription> list = objectDescriptions
                .get(entity);

        if (list != null)
        {
            ContentAssistObjectDescription[] array = new ContentAssistObjectDescription[list
                    .size()];
            list.toArray(array);

            Arrays.sort(array, new Comparator<ContentAssistObjectDescription>()
            {
                public int compare(ContentAssistObjectDescription arg0,
                        ContentAssistObjectDescription arg1)
                {
                    return String.CASE_INSENSITIVE_ORDER.compare(arg0
                            .description(), arg1.description());
                }
            });

            return array;
        }
        else
        {
            return new ContentAssistObjectDescription[0];
        }
    }


    //~ Static/instance variables .............................................

    private static final String ENTITY_DESCRIPTIONS_FILE = "entityDescriptions.txt"; //$NON-NLS-1$
    private static final String OBJECT_DESCRIPTIONS_FILE = "objectDescriptions.txt"; //$NON-NLS-1$

    /* package */Map<String, String> subsystemVersions;
    /* package */Map<String, List<ContentAssistAttributeInfo>> entityDescriptions;
    /* package */Map<String, List<ContentAssistObjectDescription>> objectDescriptions;
    /* package */String serverUrl;
    /* package */String username;
    /* package */String password;
}
