/*==========================================================================*\
 |  $Id: ContentAssistIOUtils.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

//------------------------------------------------------------------------
/**
 * Methods that read and write cached content assist information to the disk.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ContentAssistIOUtils.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class ContentAssistIOUtils
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Prevent instantiation.
     */
    private ContentAssistIOUtils()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Reads the versions of the Web-CAT subsystems from the specified reader.
     *
     * @param subsystemVersions
     *            the map that will contain the subsystem names and versions
     * @param reader
     *            the reader from which the data will be read
     *
     * @throws IOException
     *             if an I/O error occurred
     */
    public static void readSubsystemVersions(
            Map<String, String> subsystemVersions, BufferedReader reader)
            throws IOException
    {
        subsystemVersions.clear();

        String line;
        while ((line = reader.readLine()) != null)
        {
            String[] parts = line.split(":"); //$NON-NLS-1$

            if (parts.length > 0)
            {
                if (parts[0].equals("version")) //$NON-NLS-1$
                {
                    String[] versionParts = parts[1].split(","); //$NON-NLS-1$

                    if (versionParts.length == 2)
                    {
                        subsystemVersions.put(versionParts[0], versionParts[1]);
                    }
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Reads the descriptions of the Web-CAT entities and subsystem version
     * information from the specified reader.
     *
     * @param subsystemVersions
     *            the map that will contain the subsystem versions
     * @param entityDescriptions
     *            the map that will contain the entity descriptions
     * @param reader
     *            the reader from which the data will be read
     *
     * @throws IOException
     *             is an I/O error occurs
     */
    public static void readEntityDescriptions(
            Map<String, String> subsystemVersions,
            Map<String, List<ContentAssistAttributeInfo>> entityDescriptions,
            BufferedReader reader) throws IOException
    {
        subsystemVersions.clear();
        entityDescriptions.clear();

        try
        {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);
            
            JSONArray subsystemArray = root.getJSONArray("subsystems");
            for (int i = 0; i < subsystemArray.length(); i++)
            {
                JSONObject subsObj = subsystemArray.getJSONObject(i);
                subsystemVersions.put(subsObj.getString("name"),
                        subsObj.getString("version"));
            }
            
            JSONArray entityArray = root.getJSONArray("entities");
            for (int i = 0; i < entityArray.length(); i++)
            {
                JSONObject entityObj = entityArray.getJSONObject(i);
                
                String name = entityObj.getString("name");
                JSONArray attrArray = entityObj.getJSONArray("attributes");

                ArrayList<ContentAssistAttributeInfo> attrs =
                    new ArrayList<ContentAssistAttributeInfo>();

                for (int j = 0; j < attrArray.length(); j++)
                {
                    JSONObject attrObj = attrArray.getJSONObject(j);
                    
                    String attrName = attrObj.getString("name");
                    String type = attrObj.getString("type");
                    JSONObject props = attrObj.getJSONObject("properties");
                    
                    ContentAssistAttributeInfo attrInfo =
                        new ContentAssistAttributeInfo(attrName, type, props);
                    
                    attrs.add(attrInfo);
                }
                
                entityDescriptions.put(name, attrs);
            }
        }
        catch (JSONException e)
        {
            // Do nothing for now.
        }
    }


    // ----------------------------------------------------------
    /**
     * Writes the Web-CAT entity descriptions and subsystem version information
     * to the specified writer.
     *
     * @param subsystemVersions
     *            the map that contains the subsystem version information
     * @param entityDescriptions
     *            the map that contains the entity descriptions
     * @param writer
     *            the writer to which the data will be written
     *
     * @throws IOException
     *             if an I/O error occurred
     */
    public static void writeEntityDescriptions(
            Map<String, String> subsystemVersions,
            Map<String, List<ContentAssistAttributeInfo>> entityDescriptions,
            BufferedWriter writer) throws IOException
    {
        try
        {
            JSONObject root = new JSONObject();
    
            JSONArray subsystemArray = new JSONArray();
            
            for (String subsystem : subsystemVersions.keySet())
            {
                JSONObject subsObj = new JSONObject();
                subsObj.put("name", subsystem);
                subsObj.put("version", subsystemVersions.get(subsystem));
                subsystemArray.put(subsObj);
            }
            
            root.put("subsystems", subsystemArray);
    
            JSONArray entityArray = new JSONArray();
    
            for (String entity : entityDescriptions.keySet())
            {
                JSONObject entityObj = new JSONObject();
                entityObj.put("name", entity);

                JSONArray attributeArray = new JSONArray();

                for (ContentAssistAttributeInfo attrInfo :
                    entityDescriptions.get(entity))
                {
                    JSONObject attrObj = new JSONObject();
                    attrObj.put("name", attrInfo.name());
                    attrObj.put("type", attrInfo.type());
                    attrObj.put("properties", attrInfo.allPropertyValues());
                    
                    attributeArray.put(attrObj);
                }
                
                entityObj.put("attributes", attributeArray);
                
                entityArray.put(entityObj);
            }
            
            root.put("entities", entityArray);
            
            writer.write(root.toString(4));
        }
        catch (JSONException e)
        {
            // Do nothing.
        }
    }


    // ----------------------------------------------------------
    /**
     * Reads the descriptions of the currently active EO objects on Web-CAT from
     * the specified reader.
     *
     * @param objectDescriptions
     *            the map that will contain the object descriptions
     * @param reader
     *            the reader from which the data will be read
     *
     * @throws IOException
     *             if an I/O error occurred
     */
    public static void readObjectDescriptions(
            Map<String, List<ContentAssistObjectDescription>> objectDescriptions,
            BufferedReader reader) throws IOException
    {
        objectDescriptions.clear();

        try
        {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);

            JSONArray entities = root.getJSONArray("entities");
            
            for (int i = 0; i < entities.length(); i++)
            {
                JSONObject entityObj = entities.getJSONObject(i);
                
                String name = entityObj.getString("name");
                JSONArray objectArray = entityObj.getJSONArray("objects");
                
                ArrayList<ContentAssistObjectDescription> objects =
                    new ArrayList<ContentAssistObjectDescription>();

                for (int j = 0; j < objectArray.length(); j++)
                {
                    JSONObject objectObj = objectArray.getJSONObject(j);
                    
                    int id = objectObj.getInt("id");
                    String representation =
                        objectObj.getString("representation");
                    
                    objects.add(new ContentAssistObjectDescription(
                            name, id, representation));
                }
                
                objectDescriptions.put(name, objects);
            }
        }
        catch (JSONException e)
        {
            // Ignore for now.
        }
    }


    // ----------------------------------------------------------
    /**
     * Writes the descriptions of the currently active EO objects on Web-CAT to
     * the specified writer.
     *
     * @param objectDescriptions
     *            the map that contains the object descriptions
     * @param writer
     *            the writer to which the data will be writter
     *
     * @throws IOException
     *             if an I/O error occurred
     */
    public static void writeObjectDescriptions(
            Map<String, List<ContentAssistObjectDescription>> objectDescriptions,
            BufferedWriter writer) throws IOException
    {
        try
        {
            JSONObject root = new JSONObject();
    
            JSONArray entityArray = new JSONArray();
    
            for (String entityName : objectDescriptions.keySet())
            {
                JSONObject entityObj = new JSONObject();
                entityObj.put("name", entityName);
    
                JSONArray objectArray = new JSONArray();
    
                for (ContentAssistObjectDescription objDesc : objectDescriptions
                        .get(entityName))
                {
                    JSONObject objectObj = new JSONObject();
                    objectObj.put("id", objDesc.id());
                    objectObj.put("representation", objDesc.description());
    
                    objectArray.put(objectObj);
                }
                
                entityObj.put("objects", objectArray);
                
                entityArray.put(entityObj);
            }
    
            root.put("entities", entityArray);
            
            writer.write(root.toString(4));
        }
        catch (JSONException e)
        {
            // Do nothing.
        }
    }
}
