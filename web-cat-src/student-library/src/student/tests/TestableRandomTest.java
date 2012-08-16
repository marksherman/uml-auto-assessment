package student.tests;

import java.util.Random;
import junit.framework.TestCase;
import student.TestableRandom;

//-------------------------------------------------------------------------
/**
 *  Test class for student.TestableRandom.
 *
 *  @author  Stephen Edwards
 *  @version 2010.02.02
 */
public class TestableRandomTest
    extends TestCase
{
    //~ Instance/static variables .............................................

    Random random;


    //~ Public instance methods ...............................................

    // ----------------------------------------------------------
    public void setUp()
    {
        random = new TestableRandom();
    }


    // ----------------------------------------------------------
    public void testNextInt()
    {
        TestableRandom.setNextInts(1, 2, 3, 4, 5);
        assertEquals(1, random.nextInt());
        assertEquals(2, random.nextInt());
        assertEquals(3, random.nextInt());
        assertEquals(4, random.nextInt());
        assertEquals(5, random.nextInt());

        // The odds are approximately 1 in 2^32 that this will be true
        assertFalse(6 == random.nextInt());
    }


    // ----------------------------------------------------------
    public void testNextInt2()
    {
        TestableRandom.setNextInts(new int[] { 1, 2, 3, 4, 5 });
        assertEquals(1, random.nextInt());
        assertEquals(2, random.nextInt());
        assertEquals(3, random.nextInt());
        assertEquals(4, random.nextInt());
        assertEquals(5, random.nextInt());

        // The odds are approximately 1 in 2^32 that this will be true
        assertFalse(6 == random.nextInt());
    }


    // ----------------------------------------------------------
    public void testNextInt3()
    {
        TestableRandom.setNextInts(1, 2, 3, 4, 5);
        assertEquals(1, random.nextInt(4));
        assertEquals(2, random.nextInt(4));
        assertEquals(3, random.nextInt(4));
        assertEquals(0, random.nextInt(4));
        assertEquals(1, random.nextInt(4));
    }


    // ----------------------------------------------------------
    public void testNextInt4()
    {
        TestableRandom.setNextInts(1, 2, 3, 4, 5);
        assertEquals(1, random.nextInt());
        assertEquals(2, random.nextInt());

        // Reset the sequence
        TestableRandom.setNextInts();

        // The odds are approximately 1 in 2^32 that this will be true
        assertFalse(3 == random.nextInt());
    }


}
