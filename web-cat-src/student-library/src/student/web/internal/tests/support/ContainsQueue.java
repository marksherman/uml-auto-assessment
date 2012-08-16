package student.web.internal.tests.support;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class ContainsQueue
{
    public Queue<Integer> internalQueue = new PriorityQueue<Integer>();
    public ContainsQueue()
    {
        internalQueue.add( 1 );
        internalQueue.add( 2 );
        internalQueue.add( 3 );
    }
}
