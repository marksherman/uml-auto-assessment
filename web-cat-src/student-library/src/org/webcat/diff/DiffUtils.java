package org.webcat.diff;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

public class DiffUtils
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static <T> boolean itemsEqual(T item1, T item2,
                                         Comparator<T> comparator)
    {
        if (item1 == null && item2 == null)
        {
            return true;
        }
        else if (item1 == null || item2 == null)
        {
            return false;
        }

        if (comparator != null)
        {
            return comparator.compare(item1, item2) == 0;
        }
        else
        {
            return item1.equals(item2);
        }
    }


    // ----------------------------------------------------------
    public static <T> boolean listsEqual(List<T> list1, List<T> list2,
            Comparator<T> comparator)
    {
        if (list1.size() != list2.size())
        {
            return false;
        }
        else
        {
            for (int i = 0; i < list1.size(); i++)
            {
                if (!itemsEqual(list1.get(i), list2.get(i), comparator))
                {
                    return false;
                }
            }
        }

        return true;
    }


    // ----------------------------------------------------------
    public static <T> int listIndexOf(List<T> source, List<T> target, int start,
            Comparator<T> comparator)
    {
        source = source.subList(start, source.size());

        int sourceSize = source.size();
        int targetSize = target.size();
        int maxCandidate = sourceSize - targetSize;

        ListIterator<T> si = source.listIterator();
    nextCand:
        for (int candidate = 0; candidate <= maxCandidate; candidate++)
        {
            ListIterator<T> ti = target.listIterator();
            for (int i=0; i < targetSize; i++)
            {
                if (!itemsEqual(ti.next(), si.next(), comparator))
                {
                    // Back up source iterator to next candidate
                    for (int j = 0; j<i; j++)
                    {
                        si.previous();
                    }
                    continue nextCand;
                }
            }
            return candidate;
        }
        return -1;  // No candidate matched the target
    }


    // ----------------------------------------------------------
    public static <T> int listLastIndexOf(List<T> source, List<T> target,
            int start, Comparator<T> comparator)
    {
        source = source.subList(0,
                Math.min(start + 1 + target.size(), source.size()));

        int sourceSize = source.size();
        int targetSize = target.size();
        int maxCandidate = sourceSize - targetSize;

        if (maxCandidate < 0)
            return -1;
        ListIterator<T> si = source.listIterator(maxCandidate);
    nextCand:
        for (int candidate = maxCandidate; candidate >= 0; candidate--)
        {
            ListIterator<T> ti = target.listIterator();
            for (int i=0; i<targetSize; i++) {
                if (!itemsEqual(ti.next(), si.next(), comparator))
                {
                    if (candidate != 0)
                    {
                        // Back up source iterator to next candidate
                        for (int j=0; j<=i+1; j++)
                        {
                            si.previous();
                        }
                    }
                    continue nextCand;
                }
            }
            return candidate;
        }

        return -1;  // No candidate matched the target
    }


    // ----------------------------------------------------------
    public static <T> boolean listStartsWith(List<T> list, List<T> subList,
            Comparator<T> comparator)
    {
        return listIndexOf(list, subList, 0, comparator) == 0;
    }


    // ----------------------------------------------------------
    public static <T> boolean listEndsWith(List<T> list, List<T> subList,
            Comparator<T> comparator)
    {
        return listIndexOf(list, subList, 0, comparator)
            == list.size() - subList.size();
    }
}
