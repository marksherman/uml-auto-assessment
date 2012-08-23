/*==========================================================================*\
 |  $Id: PathMatcherTests.java,v 1.1 2010/12/06 21:06:48 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.webcat.submitter.internal.utility.PathMatcher;

//--------------------------------------------------------------------------
/**
 * Tests the Ant-style pattern matcher.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/12/06 21:06:48 $
 */
public class PathMatcherTests
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Tests a matcher using a simple filename.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void matchSimpleFilename() throws Exception
    {
    	PathMatcher matcher = new PathMatcher("foo.java");

    	assertTrue(matcher.matches("foo.java"));
    	assertTrue(matcher.matches("src/foo.java"));
    	assertTrue(matcher.matches("/src/foo.java"));
    	assertTrue(matcher.matches("src/org/foo/foo.java"));
    	assertTrue(matcher.matches("/src/org/foo/foo.java"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a matcher using an absolute pattern.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void matchAbsolutePattern() throws Exception
    {
    	PathMatcher matcher = new PathMatcher("/foo.java");

    	assertTrue(matcher.matches("/foo.java"));
    	assertFalse(matcher.matches("/src/foo.java"));
    	assertFalse(matcher.matches("/src/org/foo/foo.java"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a matcher using an absolute pattern with "*" inside it.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void matchAbsolutePatternStar() throws Exception
    {
    	PathMatcher matcher = new PathMatcher("/*/foo.java");

    	assertFalse(matcher.matches("/foo.java"));
    	assertTrue(matcher.matches("/src/foo.java"));
    	assertFalse(matcher.matches("/src/org/foo/foo.java"));
    	assertFalse(matcher.matches("/src/org/foo/bar.java"));
    }

    
    // ----------------------------------------------------------
    /**
     * Tests a matcher using an absolute pattern with "**" inside it.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void matchAbsolutePatternStarStar() throws Exception
    {
    	PathMatcher matcher = new PathMatcher("/**/foo.java");

    	assertTrue(matcher.matches("/foo.java"));
    	assertTrue(matcher.matches("/src/foo.java"));
    	assertTrue(matcher.matches("/src/org/foo/foo.java"));
    	assertFalse(matcher.matches("/src/org/foo/bar.java"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a matcher using an absolute pattern with "**" inside it.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void matchRelativePatternStarStar() throws Exception
    {
    	PathMatcher matcher = new PathMatcher("**/foo.java");

    	assertTrue(matcher.matches("/foo.java"));
    	assertTrue(matcher.matches("foo.java"));
    	assertTrue(matcher.matches("/src/foo.java"));
    	assertTrue(matcher.matches("src/foo.java"));
    	assertTrue(matcher.matches("/src/org/foo/foo.java"));
    	assertTrue(matcher.matches("src/org/foo/foo.java"));
    	assertFalse(matcher.matches("/src/org/foo/bar.java"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a matcher using an absolute pattern with "**" inside it.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void matchOther() throws Exception
    {
    	PathMatcher matcher = new PathMatcher("bin/**/*.png");

    	assertFalse(matcher.matches("/Project 3/src/test.png"));
    	assertFalse(matcher.matches("/Project 3/src/test.png"));
    	assertTrue(matcher.matches("/Project 3/bin/test.png"));
    	assertTrue(matcher.matches("/Project 3/bin/images/test.png"));
    	assertTrue(matcher.matches("/Project 3/bin/images/etc/test.png"));
    	assertFalse(matcher.matches("/Project 3/bin/test.jpg"));
    	assertFalse(matcher.matches("/Project 3/bin/images/test.jpg"));
    	assertFalse(matcher.matches("/Project 3/bin/images/etc/test.jpg"));
    	assertTrue(matcher.matches("/bin/test.png"));
    	assertTrue(matcher.matches("/bin/images/test.png"));
    	assertTrue(matcher.matches("/bin/images/etc/test.png"));
    	assertTrue(matcher.matches("bin/test.png"));
    	assertTrue(matcher.matches("bin/images/test.png"));
    	assertTrue(matcher.matches("bin/images/etc/test.png"));
    }
}
