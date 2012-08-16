package student.web.internal.tests.support;

public class InnerClass {

	public String data0;
	public int data1;

	public InnerClass(String data0, int data1) {
		this.data0 = data0;
		this.data1 = data1;
	}

	public String getData0() {
		return data0;
	}

	public int getData1() {
		return data1;
	}
	public String toString()
	{
		return data0+":"+data1;
	}
}
