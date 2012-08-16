package student.web.internal.tests.support;

import java.util.Map;

import student.web.SharedPersistentMap;

public class InceptionClass
{
    Map<String,Object> map = new SharedPersistentMap<Object>(Object.class);
}
