package student.web.internal.tests.support;

import java.util.HashMap;
import java.util.Map;

public class MapTestClass {
	public Map<Integer, String> toPersist = new HashMap<Integer, String>();
	public MapTestClass()
	{
		toPersist.put(0,"0");
		toPersist.put(1, "1");
		toPersist.put(2, "2");
	}
}
