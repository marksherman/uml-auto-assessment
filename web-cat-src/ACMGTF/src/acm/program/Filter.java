package acm.program;

public abstract class Filter
{

    protected String description;   

    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description += description;
    }
}
