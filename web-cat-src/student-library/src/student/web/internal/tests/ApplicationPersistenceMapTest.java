package student.web.internal.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import student.web.ApplicationPersistentMap;
import student.web.SharedPersistentMap;
import student.web.internal.tests.support.CircularClass;

public class ApplicationPersistenceMapTest
{
	private static final String TEST_ELEMENT = "TestElement";
	Stub stub = new Stub();
	private static class Stub
	{
		public Object toPersist = "Foo";
		public boolean equals(Object o)
		{
			if(o instanceof Stub)
			{
				if(((Stub)o).toPersist.equals(toPersist))
					return true;
			}
			return false;
		}
		public int hashCode()
		{
			return toPersist.hashCode();
			
		}
	}
	ApplicationPersistentMap<Stub> localAppStore;
	@Before
	public void setupLocalAppStore() throws InterruptedException
	{
		localAppStore = new ApplicationPersistentMap<Stub>("LocalUnitTestApp",Stub.class);
	}
	@After
	public void clearLocalAppStore()
	{
		localAppStore.clear();
		assertTrue(localAppStore.isEmpty());
		assertEquals(localAppStore.size(),0);
		assertEquals(localAppStore.keySet().size(),0);
		assertEquals(localAppStore.values().size(),0);
		assertEquals(localAppStore.entrySet().size(),0);
	}
	@Test
	public void testApplicationPersistenceMap()
	{
		   assertNotNull(localAppStore);
	}

	@Test
	public void testSize()
	{
		localAppStore.put(TEST_ELEMENT, stub);
		assertEquals(localAppStore.size(),1);
		assertTrue(localAppStore.keySet().contains(TEST_ELEMENT));
		assertEquals(localAppStore.entrySet().size(),1);
	}

	@Test
	public void testIsEmpty()
	{
		localAppStore.put(TEST_ELEMENT, stub);
		assertFalse(localAppStore.isEmpty());
	}

	@Test
	public void testContainsKey()
	{
		assertFalse(localAppStore.containsKey(TEST_ELEMENT));
		localAppStore.put(TEST_ELEMENT, stub);
		assertTrue(localAppStore.containsKey(TEST_ELEMENT));
	}

	@Test
	public void testContainsValue()
	{
		localAppStore.put(TEST_ELEMENT, stub);
		assertTrue(localAppStore.containsValue(stub));

	}

	@Test
	public void testGet()
	{
		localAppStore.put(TEST_ELEMENT, stub);
		Stub localStub = localAppStore.get(TEST_ELEMENT);
		assertEquals(stub,localStub);
	}

	@Test
	public void testPut()
	{
		localAppStore.put(TEST_ELEMENT, stub);
		assertTrue(localAppStore.containsKey(TEST_ELEMENT));
		assertTrue(localAppStore.containsValue(stub));
	}

	@Test
	public void testRemove()
	{
		localAppStore.put(TEST_ELEMENT, stub);
		localAppStore.remove(TEST_ELEMENT);
		assertFalse(localAppStore.containsKey(TEST_ELEMENT));
		assertFalse(localAppStore.containsValue(stub));
		assertEquals(localAppStore.size(),0);
	}

	@Test
	public void testPutAll()
	{
		Map<String,Stub> toInsert = new HashMap<String, Stub>();
		toInsert.put("test1",stub);
		toInsert.put("test2", stub);
		toInsert.put("test3", stub);
		
		localAppStore.putAll(toInsert);
		assertEquals(localAppStore.size(),3);
	}

	@Test
	public void testClear()
	{
		localAppStore.put("test1", stub);
		localAppStore.put("test2", stub);
		assertEquals(localAppStore.size(),2);
		localAppStore.clear();
		assertEquals(localAppStore.size(),0);
	}

	@Test
	public void testKeySet()
	{
		localAppStore.put("test1", stub);
		localAppStore.put("test2", stub);
		localAppStore.put("test2", stub);
		assertEquals(localAppStore.keySet().size(),2);
		assertTrue(localAppStore.containsKey("test1"));
		assertTrue(localAppStore.containsKey("test2"));
		
	}

	@Test
	public void testValues()
	{
		localAppStore.put("test1", stub);
		localAppStore.put("test2", stub);
		localAppStore.put("test2", stub);
		assertEquals(localAppStore.keySet().size(),2);
		assertTrue(localAppStore.containsValue(stub));
		assertTrue(localAppStore.values().contains(stub));
		assertEquals(localAppStore.values().size(),1);

	}

	@Test
	public void testEntrySet()
	{
		localAppStore.put("test1", stub);
		localAppStore.put("test2", stub);
		for(Entry<String,Stub> entry : localAppStore.entrySet())
		{
			assertTrue(entry.getKey().startsWith("test"));
			assertEquals(entry.getValue(),stub);
		}
		assertEquals(localAppStore.entrySet().size(),2);
		
	}
	private static class Stub2
	{
		public Object toPersist = "Bar";
		public boolean equals(Object o)
		{
			if(o instanceof Stub)
			{
				if(((Stub)o).toPersist.equals(toPersist))
					return true;
			}
			return false;
		}
		public int hashCode()
		{
			return toPersist.hashCode();
			
		}
	}
	private Stub2 stub2 = new Stub2();
	@Test
	public void testMultipleMaps()
	{
		ApplicationPersistentMap<Stub2> stub2Map = new ApplicationPersistentMap<Stub2>("LocalUnitTestApp",Stub2.class);
		stub2Map.put("test1", stub2);
		localAppStore.put("test2", stub);
		assertEquals(localAppStore.size(),2);
		assertEquals(stub2Map.size(),2);
		assertEquals(localAppStore.get("test1"),null);
		assertEquals(stub2Map.get("test2"),null);
		
		
	}
	@Test
	public void testMultipleSnapshotKeySet()
	{
		ApplicationPersistentMap<Stub2> stub2Map = new ApplicationPersistentMap<Stub2>("LocalUnitTestApp",Stub2.class);
		stub2Map.put("test1", stub2);
		localAppStore.put("test2", stub);
		Set<String> keyset1 = localAppStore.keySet();
		Set<String> keyset2 = stub2Map.keySet();
		localAppStore.clear();
		stub2Map.clear();
		assertEquals(keyset1.size(),2);
		assertEquals(keyset2.size(),2);
		assertEquals(localAppStore.size(),0);
		assertEquals(stub2Map.size(),0);
		assertEquals(localAppStore.keySet().size(),0);
		assertEquals(stub2Map.keySet().size(),0);
	}
	public void restoreTestData( String fileName, String oldFileName )
    throws IOException
{
    // FileUtils.copyFileToDirectory(new File("data/test/"+fileName), new
    // File("data/shared"));
    // FileUtils.copyFile(new File("data/test/" + oldFileName), new File(
    // "data/shared/" + fileName));
    try
    {
        File f1 = new File( "data/test/" + oldFileName );
        File f2 = new File( "data/app/testApp/" + fileName );
        InputStream in = new FileInputStream( f1 );

        // For Append the file.
        // OutputStream out = new FileOutputStream(f2,true);

        // For Overwrite the file.
        OutputStream out = new FileOutputStream( f2 );

        byte[] buf = new byte[1024];
        int len;
        while ( ( len = in.read( buf ) ) > 0 )
        {
            out.write( buf, 0, len );
        }
        in.close();
        out.close();
    }
    catch ( FileNotFoundException ex )
    {
        System.out.println( ex.getMessage()
            + " in the specified directory." );
        System.exit( 0 );
    }
    catch ( IOException e )
    {
        System.out.println( e.getMessage() );
    }
}
    @Test
    public void circularReferencePut()
    {
        ApplicationPersistentMap<CircularClass> pMap = new ApplicationPersistentMap<CircularClass>( "testApp",CircularClass.class );
        CircularClass class1 = new CircularClass();
        CircularClass class2 = new CircularClass();
        pMap.put( "class1", class1 );
        pMap.put( "class2", class2 );
        class1.ref = class2;
        class2.ref = class1;
        pMap.put( "class1", class1 );
        pMap.put( "class2", class2 );
        assertEquals( class1.ref, class2 );
        assertEquals( class2.ref, class1 );
        pMap.clear();
    }


    @Test
    public void circularReferenceGet()
    {
        try
        {
            restoreTestData( "class1-00.dataxml","class1-00-app.dataxml" );
            restoreTestData( "class2-00.dataxml", "class2-00-app.dataxml" );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            assertTrue( false );
        }
        ApplicationPersistentMap<CircularClass> pMap = new ApplicationPersistentMap<CircularClass>( "testApp",CircularClass.class );
        CircularClass class1 = pMap.get( "class1" );
        CircularClass class2 = pMap.get( "class2" );
        assertEquals(class2,class1.ref);
        assertEquals(class1,class2.ref);
        pMap.clear();

    }

}
