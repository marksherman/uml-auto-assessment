/*==========================================================================*\
 |  $Id: PreviewQueryClause.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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
import java.io.IOException;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewQueryClause.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class PreviewQueryClause
{
    public static final int COMPARAND_LITERAL = 0;

    public static final int COMPARAND_KEYPATH = 1;


    public String keyPath()
    {
        return keyPath;
    }


    public void setKeyPath(String kp)
    {
        keyPath = kp;
    }


    public PreviewQueryComparison comparison()
    {
        return comparison;
    }


    public void setComparison(PreviewQueryComparison c)
    {
        comparison = c;
    }


    public int comparandType()
    {
        return comparandType;
    }


    public void setComparandType(int ct)
    {
        comparandType = ct;
    }


    public String valueRepresentation()
    {
        return valueRepresentation;
    }


    public void setValueRepresentation(String vr)
    {
        valueRepresentation = vr;
    }


    public static PreviewQueryClause read(BufferedReader reader)
            throws IOException
    {
        PreviewQueryClause clause = new PreviewQueryClause();
        String line;

        line = reader.readLine();
        if (line == null)
            return null;
        clause.setKeyPath(line);

        line = reader.readLine();
        if (line == null)
            return null;
        clause.setComparison(Enum.valueOf(PreviewQueryComparison.class, line));

        line = reader.readLine();
        if (line == null)
            return null;
        clause.setComparandType(Integer.parseInt(line));

        line = reader.readLine();
        if (line == null)
            return null;
        clause.setValueRepresentation(line);

        return clause;
    }


    public void write(BufferedWriter writer) throws IOException
    {
        writer.write(keyPath);
        writer.newLine();
        writer.write(comparison.name());
        writer.newLine();
        writer.write(Integer.toString(comparandType));
        writer.newLine();
        writer.write(valueRepresentation);
        writer.newLine();
    }


    private String keyPath;

    private PreviewQueryComparison comparison;

    private int comparandType;

    private String valueRepresentation;
}
