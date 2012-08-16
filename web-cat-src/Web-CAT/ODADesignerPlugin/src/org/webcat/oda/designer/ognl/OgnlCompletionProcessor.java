/*==========================================================================*\
 |  $Id: OgnlCompletionProcessor.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OgnlCompletionProcessor.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OgnlCompletionProcessor implements IContentAssistProcessor
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public OgnlCompletionProcessor(OgnlSyntaxContext context)
    {
        this.context = context;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
            int offset)
    {
        this.currentWord = null;
        try
        {
            String expression = supposeCurrentExpression(viewer.getDocument(),
                    viewer.getTopIndexStartOffset(), offset);

            if (expression.endsWith(".")) //$NON-NLS-1$
                expression = expression.substring(0, expression
                        .lastIndexOf('.'));

            // Find the final type.
            // TODO generalize
            String[] segments = expression.split("\\."); //$NON-NLS-1$
            String finalType = context.getRootClassName();
            for (String segment : segments)
            {
                finalType = context.getKeyProvider().getKeyType(finalType,
                        segment);
            }

            return getCompletionProposals(finalType, offset);
        }
        catch (BadLocationException e)
        {
            // ignore
        }

        return null;
    }


    // ----------------------------------------------------------
    private String supposeCurrentExpression(IDocument document, int topOffset,
            int offset) throws BadLocationException
    {
        if (offset < 0)
        {
            offset = 0;
        }

        int startOffset = offset, endOffset = offset;
        char currentChar;
        int bracket = 0;
        while (startOffset > topOffset)
        {
            startOffset--;
            currentChar = document.getChar(startOffset);

            if (currentWord == null && currentChar == '.')
            {
                // if behind char is '.', ignore.
                while ((currentChar = document.getChar(--startOffset)) == '.')
                {
                    // Do nothing but read characters.
                }

                // else reset start offset.
                currentChar = document.getChar(++startOffset);
                currentWord = document.get(startOffset + 1, endOffset
                        - startOffset - 1);// ignore '.', because replacement
                // don't have'.'
                endOffset = startOffset + 1;// include '.' for expression parse
                // use
            }
            if (currentChar == ')' || currentChar == ']')
                ++bracket;
            if (currentChar == '(' || currentChar == '[')
                --bracket;
            if (bracket == 0
                    && (currentChar == '\n' || currentChar == ' '
                            || currentChar == '=' || currentChar == '+'
                            || currentChar == '-' || currentChar == '*'
                            || currentChar == '/' || currentChar == '<'
                            || currentChar == '>' || currentChar == '&'
                            || currentChar == '|' || currentChar == ';'))
            {
                startOffset++;
                break;
            }
        }

        if (currentWord == null)
        {
            return currentWord = document.get(startOffset, endOffset
                    - startOffset);
        }

        return document.get(startOffset, endOffset - startOffset);
    }


    // ----------------------------------------------------------
    private CompletionProposal[] getCompletionProposals(String finalType,
            int offset)
    {
        List<CompletionProposal> proposals = new ArrayList<CompletionProposal>();

        int wordLength = currentWord == null ? 0 : currentWord.length();

        String[] keys = context.getKeyProvider().getKeys(finalType);
        if (keys != null)
        {
            for (int i = 0; i < keys.length; i++)
            {
                if (currentWord == null
                        || currentWord.equals("") //$NON-NLS-1$
                        || keys[i].toLowerCase().startsWith(
                                currentWord.toLowerCase()))
                {
                    proposals.add(new CompletionProposal(keys[i], offset
                            - wordLength, wordLength, keys[i].length(),
                            context.getKeyLabelProvider().getImage(finalType,
                                    keys[i]), context.getKeyLabelProvider()
                                    .getLabel(finalType, keys[i]), null, null));
                }
            }
        }

        return proposals.toArray(new CompletionProposal[proposals.size()]);
    }


    // ----------------------------------------------------------
    public IContextInformation[] computeContextInformation(ITextViewer viewer,
            int offset)
    {
        return null;
    }


    // ----------------------------------------------------------
    public char[] getCompletionProposalAutoActivationCharacters()
    {
        return new char[] { '.' };
    }


    // ----------------------------------------------------------
    public char[] getContextInformationAutoActivationCharacters()
    {
        return null;
    }


    // ----------------------------------------------------------
    public IContextInformationValidator getContextInformationValidator()
    {
        return null;
    }


    // ----------------------------------------------------------
    public String getErrorMessage()
    {
        return null;
    }


    //~ Static/instance variables .............................................

    private OgnlSyntaxContext context;
    private String currentWord = ""; //$NON-NLS-1$
}
