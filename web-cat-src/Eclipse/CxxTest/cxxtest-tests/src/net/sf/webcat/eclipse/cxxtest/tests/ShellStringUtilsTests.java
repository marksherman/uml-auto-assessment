package net.sf.webcat.eclipse.cxxtest.tests;

import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.webcat.eclipse.cxxtest.internal.options.ShellStringUtils;

public class ShellStringUtilsTests
{
	@Test public void splitEmpty()
	{
		String[] array = ShellStringUtils.split("");
		assertEquals(0, array.length);
	}
	
	@Test public void splitOne()
	{
		String[] array = ShellStringUtils.split("one");
		assertEquals(1, array.length);
		assertEquals("one", array[0]);
	}
	
	@Test public void splitOneQuoted()
	{
		String[] array = ShellStringUtils.split("\"one\"");
		assertEquals(1, array.length);
		assertEquals("\"one\"", array[0]);
	}
	
	@Test public void splitMultiple()
	{
		String[] array = ShellStringUtils.split("one two three");
		assertEquals(3, array.length);
		assertEquals("one", array[0]);
		assertEquals("two", array[1]);
		assertEquals("three", array[2]);
	}

	@Test public void splitQuotedWithSpaces()
	{
		String[] array = ShellStringUtils.split("\"one two three\"");
		assertEquals(1, array.length);
		assertEquals("\"one two three\"", array[0]);
	}

	@Test public void splitSingleQuotedWithSpaces()
	{
		String[] array = ShellStringUtils.split("'one two three'");
		assertEquals(1, array.length);
		assertEquals("'one two three'", array[0]);
	}
	
	@Test public void quoteIfNecessaryEmpty()
	{
		assertEquals("", ShellStringUtils.quoteIfNecessary(""));
	}

	@Test public void quoteIfNecessaryUnnecessary()
	{
		assertEquals("one", ShellStringUtils.quoteIfNecessary("one"));
	}

	@Test public void quoteIfNecessaryQuoted()
	{
		assertEquals("\\\"one\\\"", ShellStringUtils.quoteIfNecessary("\"one\""));
	}

	@Test public void quoteIfNecessaryNecessary()
	{
		assertEquals("\"one two\"", ShellStringUtils.quoteIfNecessary("one two"));
	}

	@Test public void joinEmpty()
	{
		String str = ShellStringUtils.join(new String[0]);
		assertEquals("", str);
	}
	
	@Test public void joinOne()
	{
		String str = ShellStringUtils.join(
				new String[] { "one" });
		assertEquals("one", str);
	}

	@Test public void joinOneWithSpaces()
	{
		String str = ShellStringUtils.join(
				new String[] { "one two three" });
		assertEquals("\"one two three\"", str);
	}
	
	@Test public void joinMultiple()
	{
		String str = ShellStringUtils.join(
				new String[] { "one", "two", "three" });
		assertEquals("one two three", str);
	}
	
	@Test public void joinMultipleWithSpaces()
	{
		String str = ShellStringUtils.join(
				new String[] { "foo bar", "baz", "bar foo" });
		assertEquals("\"foo bar\" baz \"bar foo\"", str);
	}
	
	@Test public void versionPatternDeploymentNoQualifier()
	{
		String path = "/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.5.0/path/to/foo";
		String regex = ShellStringUtils.patternForAnyVersionOfPluginRelativePath(path, true);
		
		assertTrue(path.matches(regex));
		assertTrue("/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.6.0/path/to/foo".matches(regex));
		assertTrue("/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.6.0/path/to/foo/".matches(regex));
		assertFalse("/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.6.0/path/to/foo/child".matches(regex));
		assertFalse("/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.6.0/path/to/something/else".matches(regex));
		assertFalse("/other/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.5.0/path/to/foo".matches(regex));

		assertEquals(
				"/path/to/plugins/net\\.sf\\.webcat\\.eclipse\\.cxxtest_[^.]+\\.[^.]+\\.[^./]+(\\.[^/]+)?/path/to/foo/?",
				regex);
	}


	@Test public void versionPatternDeploymentQualifier()
	{
		String path = "/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.5.0.v12345-6789/path/to/foo";
		String regex = ShellStringUtils.patternForAnyVersionOfPluginRelativePath(path, true);
		
		assertTrue(path.matches(regex));
		assertTrue("/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.6.0.v12345-9876/path/to/foo".matches(regex));
		assertTrue("/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.6.0.v12345-9876/path/to/foo/".matches(regex));
		assertFalse("/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.6.0.v12345-9876/path/to/foo/child".matches(regex));
		assertFalse("/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.6.0.v12345-9876/path/to/something/else".matches(regex));
		assertFalse("/other/path/to/plugins/net.sf.webcat.eclipse.cxxtest_1.5.0.v12345-6789/path/to/foo".matches(regex));

		assertEquals(
				"/path/to/plugins/net\\.sf\\.webcat\\.eclipse\\.cxxtest_[^.]+\\.[^.]+\\.[^./]+(\\.[^/]+)?/path/to/foo/?",
				regex);
	}


	@Test public void versionPatternDevelopment()
	{
		String path = "/path/to/workspace/cxxtest-core/path/to/foo";
		String regex = ShellStringUtils.patternForAnyVersionOfPluginRelativePath(path, true);
		
		assertTrue("/path/to/workspace/cxxtest-core/path/to/foo".matches(regex));
		assertTrue("/path/to/workspace/cxxtest-core/path/to/foo/".matches(regex));
		assertFalse("/other/path/to/workspace/cxxtest-core/path/to/foo".matches(regex));

		assertEquals(
				"/path/to/workspace/cxxtest-core/path/to/foo/?",
				regex);
	}
}
