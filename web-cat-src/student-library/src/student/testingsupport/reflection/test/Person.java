package student.testingsupport.reflection.test;

public class Person
{
    private String firstName;
    private String lastName;

    protected int personProtected;

    public Person()
    {
        // nothing to do
    }

    public Person(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void doit()
    {
        System.out.println(this);
    }

    public String toString()
    {
        return firstName + " " + lastName;
    }
}
