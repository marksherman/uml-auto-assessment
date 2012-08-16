/*==========================================================================*\
 |  $Id: DeriveTransformedDataSetHandler.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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


import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.editors.ReportEditorProxy;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.webcat.oda.designer.transform.CreateTransformedDataSetDialog;

//------------------------------------------------------------------------
/**
 * An Eclipse command handler that opens a dialog that can be used to create
 * scripted data sets derived from other data sets.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DeriveTransformedDataSetHandler.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class DeriveTransformedDataSetHandler extends AbstractHandler
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IEditorPart part = HandlerUtil.getActiveEditor(event);

        if (part instanceof ReportEditorProxy)
        {
            ReportEditorProxy proxy = (ReportEditorProxy) part;
            part = proxy.getEditorPart();

            if(part instanceof MultiPageReportEditor)
            {
                MultiPageReportEditor editor = (MultiPageReportEditor) part;

                ModuleHandle model = editor.getModel();

                CreateTransformedDataSetDialog dialog =
                    new CreateTransformedDataSetDialog(
                            HandlerUtil.getActiveShell(event), model);

                if (dialog.open() == Window.OK)
                {
                    // TODO set dirty bit on editor
                }
            }
        }

        return null;
    }

}
