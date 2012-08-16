package student.web.internal.tests.support;

public class ComplexClass
    extends PlainClass
    implements Comparable<Object>
{
	public String complexStuff = "!!!!";

	public int compareTo(Object o) {
		return 0;
	}
}
