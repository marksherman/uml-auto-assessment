package student.web.internal.tests.support;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DataStructClass {
	public List<InnerClass> internalDataStruct;
	public List<InnerClass> internalDataStruct2;
	
	public DataStructClass()
	{
		internalDataStruct = new ArrayList<InnerClass>();
		internalDataStruct2 = new ArrayList<InnerClass>();
		internalDataStruct2.add(new InnerClass("foo",99));
	}
	public void add(InnerClass internal)
	{
		internalDataStruct.add(internal);
	}
	public void remove(int i)
	{
		internalDataStruct.remove(i);
	}
	public InnerClass get(int i)
	{
		return internalDataStruct.get(i);
	}
}
