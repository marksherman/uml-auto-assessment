/*==========================================================================*\
 |  $Id: OgnlPartitionScanner.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OgnlPartitionScanner.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OgnlPartitionScanner extends RuleBasedPartitionScanner
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new OgnlPartitionScanner object.
     */
    public OgnlPartitionScanner()
    {
        List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

        rules.add(new SingleLineRule("\"", "\"", TOKEN_STRING, '\\')); //$NON-NLS-1$ //$NON-NLS-2$
        rules.add(new SingleLineRule("'", "'", TOKEN_STRING, '\\')); //$NON-NLS-1$ //$NON-NLS-2$

        PredicateWordRule keywordRule = new PredicateWordRule(
                new IWordDetector()
                {
                    public boolean isWordStart(char c)
                    {
                        return Character.isJavaIdentifierStart(c);
                    }


                    public boolean isWordPart(char c)
                    {
                        return Character.isJavaIdentifierPart(c);
                    }
                }, TOKEN_DEFAULT);

        keywordRule.addWords(keywordTokens, TOKEN_KEYWORD);
        rules.add(keywordRule);

        PredicateWordRule staticMethodRule = new PredicateWordRule(
                new IWordDetector()
                {
                    public boolean isWordStart(char c)
                    {
                        return c == '@';
                    }


                    public boolean isWordPart(char c)
                    {
                        return c == '@' || c == '.'
                                || Character.isJavaIdentifierPart(c);
                    }
                }, TOKEN_STATIC_METHOD);

        rules.add(staticMethodRule);

        PredicateWordRule variableRule = new PredicateWordRule(
                new IWordDetector()
                {
                    public boolean isWordStart(char c)
                    {
                        return c == '#';
                    }


                    public boolean isWordPart(char c)
                    {
                        return Character.isJavaIdentifierStart(c)
                                || Character.isJavaIdentifierPart(c);
                    }
                }, TOKEN_VARIABLE);

        rules.add(variableRule);

        setRuleList(rules);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    private void setRuleList(List<IPredicateRule> rules)
    {
        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }


    // ----------------------------------------------------------
    protected void addWords(WordRule rule, String[] tokens, IToken token)
    {
        for (int i = 0; i < tokens.length; i++)
        {
            rule.addWord(tokens[i], token);
        }
    }


    //~ Nested classes ........................................................

    // ----------------------------------------------------------
    private class PredicateWordRule extends WordRule implements IPredicateRule
    {
        //~ Constructor .......................................................

        // ----------------------------------------------------------
        public PredicateWordRule(IWordDetector detector, IToken defaultToken)
        {
            super(detector, defaultToken);
        }


        //~ Methods ...........................................................

        // ----------------------------------------------------------
        public IToken getSuccessToken()
        {
            return Token.UNDEFINED;
        }


        // ----------------------------------------------------------
        public IToken evaluate(ICharacterScanner scanner, boolean resume)
        {
            return null;
        }


        // ----------------------------------------------------------
        private void addWords(String[] tokens, IToken token)
        {
            for (int i = 0; i < tokens.length; i++)
            {
                addWord(tokens[i], token);
            }
        }
    }


    //~ Static/instance variables .............................................

    public final static String OGNL_DEFAULT = "__ognl_default"; //$NON-NLS-1$
    public final static String OGNL_KEYWORD = "__ognl_keyword"; //$NON-NLS-1$
    public final static String OGNL_STRING = "__ognl_string"; //$NON-NLS-1$
    public final static String OGNL_STATIC_METHOD = "__ognl_static_method"; //$NON-NLS-1$
    public final static String OGNL_VARIABLE = "__ognl_variable"; //$NON-NLS-1$

    public final static IToken TOKEN_STRING = new Token(OGNL_STRING);
    public final static IToken TOKEN_DEFAULT = new Token(OGNL_DEFAULT);
    public final static IToken TOKEN_KEYWORD = new Token(OGNL_KEYWORD);
    public final static IToken TOKEN_STATIC_METHOD = new Token(
            OGNL_STATIC_METHOD);
    public final static IToken TOKEN_VARIABLE = new Token(OGNL_VARIABLE);

    /** Array of keyword token strings. */
    private static String[] keywordTokens = { "and", //$NON-NLS-1$
            "band", //$NON-NLS-1$
            "bor", //$NON-NLS-1$
            "eq", //$NON-NLS-1$
            "false", //$NON-NLS-1$
            "gt", //$NON-NLS-1$
            "gte", //$NON-NLS-1$
            "in", //$NON-NLS-1$
            "instanceof", //$NON-NLS-1$
            "lt", //$NON-NLS-1$
            "lte", //$NON-NLS-1$
            "neq", //$NON-NLS-1$
            "new", //$NON-NLS-1$
            "not", //$NON-NLS-1$
            "null", //$NON-NLS-1$
            "or", //$NON-NLS-1$
            "shl", //$NON-NLS-1$
            "shr", //$NON-NLS-1$
            "ushr", //$NON-NLS-1$
            // "this", //$NON-NLS-1$
            "true", //$NON-NLS-1$
            "xor", //$NON-NLS-1$
    };
}
