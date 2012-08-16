package student.testingsupport.reflection.test;

public class Employee
    extends Person
    implements MyMarker
{
    private double salary;

    private byte b = 1;
    private short s = 10;
    private int i = 100;
    private long l = 1000;
    private float f = 20.0f;
    private boolean y = true;
    private char c = 'c';

    private static String msg = "hello";
    public String wrong;
    public static String alsoWrong;

    public Employee()
    {
        super();
    }

    public Employee(String firstName, String lastName, double salary)
    {
        super(firstName, lastName);
        this.salary = salary;
    }

    public void setSalary(double salary)
    {
        this.salary = salary;
    }

    public double getSalary()
    {
        return salary;
    }

    public String toString()
    {
        return super.toString() + "[" + salary + "]";
    }

    public void setB(byte b)
    {
        this.b = b;
    }

    public byte getB()
    {
        return b;
    }

    public void setS(short s)
    {
        this.s = s;
    }

    public short getS()
    {
        return s;
    }

    public void setI(int i)
    {
        this.i = i;
    }

    public int getI()
    {
        return i;
    }

    public void setL(long l)
    {
        this.l = l;
    }

    public long getL()
    {
        return l;
    }

    public void setF(float f)
    {
        this.f = f;
    }

    public float getF()
    {
        return f;
    }

    public void setY(boolean y)
    {
        this.y = y;
    }

    public boolean isY()
    {
        return y;
    }

    public void setC(char c)
    {
        this.c = c;
    }

    public char getC()
    {
        return c;
    }

    public void setInteger(Integer i)
    {
        this.i = i;
    }

    public Integer getIInteger()
    {
        return i;
    }
}
