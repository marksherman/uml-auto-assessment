package student.web.internal.tests.support;

public class ArrayTestClass {
	public InnerClass[] internalArray = new InnerClass[10];
	public int[] internalPrimitiveArray = new int[10];
	
	public ArrayTestClass()
	{
		internalArray[1] = new InnerClass("first",0);
		for(int i = 0; i < internalPrimitiveArray.length; i++)
		{
			internalPrimitiveArray[i] = i+1;
		}
	}
}
