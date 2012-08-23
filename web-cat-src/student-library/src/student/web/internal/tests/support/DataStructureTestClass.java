package student.web.internal.tests.support;

import java.util.*;

public class DataStructureTestClass {
	private NotPersistedClass[] blah = new NotPersistedClass[10];
	private Map<String,Object> foo = new HashMap<String,Object>();
	private TreeMap treeMap = new TreeMap();
	private TreeSet treeSet = new TreeSet();
	private Properties prop = new Properties();
	public DataStructureTestClass()
	{
		foo.put("foo", new NotPersistedClass());
		foo.put("bar", new NotPersistedClass());
		prop.put("foo", new NotPersistedClass());
		prop.put("bar", new NotPersistedClass());
		treeMap.put("foo", new NotPersistedComplexClass());
		treeMap.put("bar", new NotPersistedComplexClass());
		treeSet.add(new NotPersistedComplexClass());
		treeSet.add(new NotPersistedComplexClass());
	}
}
