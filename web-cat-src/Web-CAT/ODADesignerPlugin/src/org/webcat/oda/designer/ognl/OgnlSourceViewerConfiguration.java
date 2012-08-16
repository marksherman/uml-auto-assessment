/*==========================================================================*\
 |  $Id: OgnlSourceViewerConfiguration.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OgnlSourceViewerConfiguration.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OgnlSourceViewerConfiguration extends SourceViewerConfiguration
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public OgnlSourceViewerConfiguration(OgnlSyntaxContext context)
    {
        this.context = context;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see SourceViewerConfiguration#getConfiguredContentTypes(ISourceViewer)
     */
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
    {
        return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
                OgnlPartitionScanner.OGNL_KEYWORD,
                OgnlPartitionScanner.OGNL_STRING,
                OgnlPartitionScanner.OGNL_STATIC_METHOD,
                OgnlPartitionScanner.OGNL_VARIABLE };
    }


    // ----------------------------------------------------------
    /**
     * Gets default scanner
     *
     * @return scanner
     */
    protected RuleBasedScanner getDefaultScanner()
    {
        if (scanner == null)
        {
            scanner = new OgnlScanner();
            scanner.setDefaultReturnToken(new Token(new TextAttribute(
                    DEFAULT_COLOR)));
        }

        return scanner;
    }


    // ----------------------------------------------------------
    /**
     * @see SourceViewerConfiguration#getPresentationReconciler(ISourceViewer)
     */
    public IPresentationReconciler getPresentationReconciler(
            ISourceViewer sourceViewer)
    {
        PresentationReconciler reconciler = new PresentationReconciler();

        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(
                getDefaultScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

        NonRuleBasedDamagerRepairer stringRepairer = new NonRuleBasedDamagerRepairer(
                new TextAttribute(STRING_COLOR, null, SWT.NONE));
        reconciler.setDamager(stringRepairer, OgnlPartitionScanner.OGNL_STRING);
        reconciler
                .setRepairer(stringRepairer, OgnlPartitionScanner.OGNL_STRING);

        NonRuleBasedDamagerRepairer keywordRepairer = new NonRuleBasedDamagerRepairer(
                new TextAttribute(KEYWORD_COLOR, null, SWT.BOLD));
        reconciler.setDamager(keywordRepairer,
                OgnlPartitionScanner.OGNL_KEYWORD);
        reconciler.setRepairer(keywordRepairer,
                OgnlPartitionScanner.OGNL_KEYWORD);

        NonRuleBasedDamagerRepairer staticMethodRepairer = new NonRuleBasedDamagerRepairer(
                new TextAttribute(STATIC_METHOD_COLOR, null, SWT.BOLD));
        reconciler.setDamager(staticMethodRepairer,
                OgnlPartitionScanner.OGNL_STATIC_METHOD);
        reconciler.setRepairer(staticMethodRepairer,
                OgnlPartitionScanner.OGNL_STATIC_METHOD);

        NonRuleBasedDamagerRepairer variableRepairer = new NonRuleBasedDamagerRepairer(
                new TextAttribute(VARIABLE_COLOR, null, SWT.BOLD));
        reconciler.setDamager(variableRepairer,
                OgnlPartitionScanner.OGNL_VARIABLE);
        reconciler.setRepairer(variableRepairer,
                OgnlPartitionScanner.OGNL_VARIABLE);

        return reconciler;
    }


    // ----------------------------------------------------------
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer)
    {
        ContentAssistant assistant = new ContentAssistant();
        assistant.setContentAssistProcessor(
                new OgnlCompletionProcessor(context),
                IDocument.DEFAULT_CONTENT_TYPE);
        assistant.enableAutoActivation(true);
        assistant.setAutoActivationDelay(500);
        assistant
                .setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
        return assistant;
    }


    //~ Static/instance variables .............................................

    private static final Color STRING_COLOR = new Color(Display.getDefault(),
            42, 0, 255);
    private static final Color KEYWORD_COLOR = new Color(Display.getDefault(),
            127, 0, 85);
    private static final Color STATIC_METHOD_COLOR = new Color(Display
            .getDefault(), 0, 0, 128);
    private static final Color VARIABLE_COLOR = new Color(Display.getDefault(),
            0, 128, 64);
    private static final Color DEFAULT_COLOR = new Color(Display.getDefault(),
            0, 0, 0);

    private RuleBasedScanner scanner;
    private OgnlSyntaxContext context;
}
