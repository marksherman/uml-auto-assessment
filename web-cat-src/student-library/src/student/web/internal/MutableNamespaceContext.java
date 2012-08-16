/*==========================================================================*\
 |  $Id: MutableNamespaceContext.java,v 1.2 2010/02/23 17:06:36 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student.web.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;


//-------------------------------------------------------------------------
/**
 * There is a bug in the JDK which omits the setNamespace declaration
 * from implementations of NamespaceContext. We have to create our
 * own implementation to work around it. Documented here:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5101859

 * @author Oliver Roup <oroup@oroup.com>
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/02/23 17:06:36 $
 */
public class MutableNamespaceContext
    implements NamespaceContext
{
    private Map<String, String> map;

    // ----------------------------------------------------------
    /**
     * Default constructor.
     */
    public MutableNamespaceContext()
    {
        map = new HashMap<String, String>();
    }


    // ----------------------------------------------------------
    /**
     * Add a namespace to the context.
     * @param prefix The prefix to associate.
     * @param namespaceURI The URI to associate with the given prefix.
     */
    public void setNamespace(String prefix, String namespaceURI)
    {
        map.put(prefix, namespaceURI);
    }


    // ----------------------------------------------------------
    public String getNamespaceURI(String prefix)
    {
        return map.get(prefix);
    }


    // ----------------------------------------------------------
    public String getPrefix(String namespaceURI)
    {
        for (String prefix : map.keySet())
        {
            if (map.get(prefix).equals(namespaceURI))
            {
                return prefix;
            }
        }
        return null;
    }


    // ----------------------------------------------------------
    public Iterator<String> getPrefixes(String namespaceURI)
    {
        List<String> prefixes = new ArrayList<String>();
        for (String prefix : map.keySet())
        {
            if (map.get(prefix).equals(namespaceURI))
            {
                prefixes.add(prefix);
            }
        }
        return prefixes.iterator();
    }
}
