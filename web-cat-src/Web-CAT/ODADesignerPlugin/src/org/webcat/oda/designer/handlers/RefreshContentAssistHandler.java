/*==========================================================================*\
 |  $Id: RefreshContentAssistHandler.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.contentassist.ContentAssistManager;

//------------------------------------------------------------------------
/**
 * An Eclipse command handler that forces the content assist information to be
 * refreshed from the Web-CAT server.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: RefreshContentAssistHandler.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class RefreshContentAssistHandler extends AbstractHandler
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        ContentAssistManager manager = DesignerActivator.getDefault()
                .getContentAssistManager();

        if (manager != null)
        {
            manager.update(true);
        }

        return null;
    }
}
