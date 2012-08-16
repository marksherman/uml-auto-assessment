/*==========================================================================*\
 |  $Id: PreviewQueryManager.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.preview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import org.eclipse.core.runtime.IPath;
import org.webcat.oda.designer.DesignerActivator;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewQueryManager.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class PreviewQueryManager
{
    public PreviewQueryManager()
    {
        dataSetQueries = new Hashtable<String, PreviewQueryClause[]>();

        loadFromState();
    }


    private File getDbFile()
    {
        IPath statePath = DesignerActivator.getDefault().getStateLocation();
        return statePath.append(PREVIEW_QUERY_FILE).toFile();
    }


    private void loadFromState()
    {
        File dbFile = getDbFile();

        if (!dbFile.exists())
            return;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(dbFile));

            String line;
            while ((line = reader.readLine()) != null)
            {
                String uuid = line;

                ArrayList<PreviewQueryClause> clauses = new ArrayList<PreviewQueryClause>();

                while (!(line = reader.readLine()).equals(END_OF_QUERY_MARKER))
                {
                    clauses.add(PreviewQueryClause.read(reader));
                }

                PreviewQueryClause[] clauseArray = new PreviewQueryClause[clauses
                        .size()];
                clauses.toArray(clauseArray);

                dataSetQueries.put(uuid, clauseArray);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void saveToState()
    {
        File dbFile = getDbFile();

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbFile));

            for (String uuid : dataSetQueries.keySet())
            {
                writer.write(uuid);
                writer.newLine();
                writer.newLine();

                writeQuery(uuid, writer);

                writer.write(END_OF_QUERY_MARKER);
                writer.newLine();
            }

            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void writeQuery(String uuid, BufferedWriter writer)
            throws IOException
    {
        PreviewQueryClause[] clauses = dataSetQueries.get(uuid);

        if (clauses == null)
            return;

        for (int i = 0; i < clauses.length; i++)
        {
            clauses[i].write(writer);

            if (i != clauses.length - 1)
                writer.newLine();
        }
    }


    public void addQuery(String uuid, PreviewQueryClause[] clauses)
    {
        dataSetQueries.put(uuid, clauses);
    }


    public PreviewQueryClause[] getQuery(String uuid)
    {
        return dataSetQueries.get(uuid);
    }


    private static final String PREVIEW_QUERY_FILE = "previewQueries.txt"; //$NON-NLS-1$

    private static final String END_OF_QUERY_MARKER = "%%%% END OF QUERY %%%%"; //$NON-NLS-1$

    private Map<String, PreviewQueryClause[]> dataSetQueries;
}
