package student.web.internal.tests;

public class Person
{
    private String firstName;
    private String lastName;

    public Person(String first, String last)
    {
        setFirstName(first);
        setLastName(last);
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

    public String toString()
    {
        return "Person(" + firstName + ", " + lastName + ")";
    }
}
