/*==========================================================================*\
 |  $Id: OgnlSyntaxContext.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.ognl;

import org.webcat.oda.designer.widgets.IKeyLabelProvider;
import org.webcat.oda.designer.widgets.IKeyProvider;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OgnlSyntaxContext.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OgnlSyntaxContext
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public OgnlSyntaxContext(String rootClass, IKeyProvider keyProv,
            IKeyLabelProvider keyLabelProv)
    {
        rootClassName = rootClass;
        keyProvider = keyProv;
        keyLabelProvider = keyLabelProv;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public String getRootClassName()
    {
        return rootClassName;
    }


    // ----------------------------------------------------------
    public IKeyProvider getKeyProvider()
    {
        return keyProvider;
    }


    // ----------------------------------------------------------
    public IKeyLabelProvider getKeyLabelProvider()
    {
        return keyLabelProvider;
    }


    //~ Static/instance variables .............................................

    private String rootClassName;
    private IKeyProvider keyProvider;
    private IKeyLabelProvider keyLabelProvider;
}
