package student.web.internal.tests.support;

import java.util.ArrayList;
import java.util.List;

public class NoDefaultConst
{
    List<Object> foo = new ArrayList<Object>();
    public NoDefaultConst(String blah)
    {
        foo.add( null );
        foo.add( null );
    }
}
