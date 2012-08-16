/*
 * Diff Match and Patch
 *
 * Copyright 2006 Google Inc.
 * http://code.google.com/p/google-diff-match-patch/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.webcat.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This class performs a diff between two lists or arrays and returns a list of
 * Diff objects that describe the differences.
 *
 * @param <T> the type of object in the list
 *
 * @author fraser@google.com (Neil Fraser)
 * @author Tony Allevato
 */
public class Differ<T>
{

    //~ Static/instance variables .............................................

    private Comparator<T> comparator;
    private DiffList<T> differences;
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public Differ(T[] text1, T[] text2)
    {
        this(Arrays.asList(text1), Arrays.asList(text2));
    }


    // ----------------------------------------------------------
    public Differ(T[] text1, T[] text2, Comparator<T> comp)
    {
        this(Arrays.asList(text1), Arrays.asList(text2), comp);
    }


    // ----------------------------------------------------------
    public Differ(List<T> text1, List<T> text2)
    {
        this(text1, text2, null);
    }


    // ----------------------------------------------------------
    public Differ(List<T> text1, List<T> text2, Comparator<T> comp)
    {
        this.comparator = comp;
        differences = doDiff(text1, text2);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public DiffList<T> getDifferences()
    {
        return differences;
    }


    // ----------------------------------------------------------
    /**
     * Find the differences between two texts. Simplifies the problem by
     * stripping any common prefix or suffix off the texts before diffing.
     *
     * @param text1
     *            Old List<T> to be diffed.
     * @param text2
     *            New List<T> to be diffed.
     * @param checklines
     *            Speedup flag. If false, then don't run a line-level diff first
     *            to identify the changed areas. If true, then run a faster
     *            slightly less optimal diff.
     * @return Linked List of Diff objects.
     */
    private DiffList<T> doDiff(List<T> text1, List<T> text2)
    {
        // Check for null inputs.
        if (text1 == null || text2 == null)
        {
            throw new IllegalArgumentException("Null inputs. (diff_main)");
        }

        // Check for equality (speedup).
        DiffList<T> diffs;
        if (text1.equals(text2))
        {
            diffs = new DiffList<T>();
            if (text1.size() != 0)
            {
                diffs.add(new Diff<T>(Diff.Operation.EQUAL, text1));
            }
            return diffs;
        }

        // Trim off common prefix (speedup).
        int commonlength = findCommonPrefix(text1, text2);
        List<T> commonprefix = text1.subList(0, commonlength);
        text1 = text1.subList(commonlength, text1.size());
        text2 = text2.subList(commonlength, text2.size());

        // Trim off common suffix (speedup).
        commonlength = findCommonSuffix(text1, text2);
        List<T> commonsuffix = text1.subList(text1.size() - commonlength, text1.size());
        text1 = text1.subList(0, text1.size() - commonlength);
        text2 = text2.subList(0, text2.size() - commonlength);

        // Compute the diff on the middle block.
        diffs = compute(text1, text2);

        // Restore the prefix and suffix.
        if (commonprefix.size() != 0)
        {
            diffs.addFirst(new Diff<T>(Diff.Operation.EQUAL, commonprefix));
        }
        if (commonsuffix.size() != 0)
        {
            diffs.addLast(new Diff<T>(Diff.Operation.EQUAL, commonsuffix));
        }

        //diff_cleanupMerge(diffs);
        return diffs;
    }


    // ----------------------------------------------------------
    /**
     * Find the differences between two texts. Assumes that the texts do not
     * have any common prefix or suffix.
     *
     * @param text1
     *            Old List<T> to be diffed.
     * @param text2
     *            New List<T> to be diffed.
     * @param checklines
     *            Speedup flag. If false, then don't run a line-level diff first
     *            to identify the changed areas. If true, then run a faster
     *            slightly less optimal diff.
     * @param deadline
     *            Time when the diff should be complete by.
     * @return Linked List of Diff objects.
     */
    private DiffList<T> compute(List<T> text1, List<T> text2)
    {
        DiffList<T> diffs = new DiffList<T>();

        if (text1.size() == 0)
        {
            // Just add some text (speedup).
            diffs.add(new Diff<T>(Diff.Operation.INSERT, text2));
            return diffs;
        }

        if (text2.size() == 0)
        {
            // Just delete some text (speedup).
            diffs.add(new Diff<T>(Diff.Operation.DELETE, text1));
            return diffs;
        }

        List<T> longtext = text1.size() > text2.size() ? text1 : text2;
        List<T> shorttext = text1.size() > text2.size() ? text2 : text1;
        int i = longtext.indexOf(shorttext);
        if (i != -1)
        {
            // Shorter text is inside the longer text (speedup).
            Diff.Operation op = (text1.size() > text2.size()) ? Diff.Operation.DELETE
                    : Diff.Operation.INSERT;
            diffs.add(new Diff<T>(op, longtext.subList(0, i)));
            diffs.add(new Diff<T>(Diff.Operation.EQUAL, shorttext));
            diffs.add(new Diff<T>(op, longtext.subList(i + shorttext.size(), longtext.size())));
            return diffs;
        }

        if (shorttext.size() == 1)
        {
            // Single character List<T>.
            // After the previous speedup, the character can't be an equality.
            diffs.add(new Diff<T>(Diff.Operation.DELETE, text1));
            diffs.add(new Diff<T>(Diff.Operation.INSERT, text2));
            return diffs;
        }
        longtext = shorttext = null; // Garbage collect.

        // Check to see if the problem can be split in two.
        List<T>[] hm = halfMatch(text1, text2);
        if (hm != null)
        {
            // A half-match was found, sort out the return data.
            List<T> text1_a = hm[0];
            List<T> text1_b = hm[1];
            List<T> text2_a = hm[2];
            List<T> text2_b = hm[3];
            List<T> mid_common = hm[4];
            // Send both pairs off for separate processing.
            DiffList<T> diffs_a = doDiff(text1_a, text2_a);
            DiffList<T> diffs_b = doDiff(text1_b, text2_b);
            // Merge the results.
            diffs = diffs_a;
            diffs.add(new Diff<T>(Diff.Operation.EQUAL, mid_common));
            diffs.addAll(diffs_b);
            return diffs;
        }

        return bisect(text1, text2);
    }


    // ----------------------------------------------------------
    /**
     * Find the 'middle snake' of a diff, split the problem in two and return
     * the recursively constructed diff. See Myers 1986 paper: An O(ND)
     * Difference Algorithm and Its Variations.
     *
     * @param text1
     *            Old List<T> to be diffed.
     * @param text2
     *            New List<T> to be diffed.
     * @param deadline
     *            Time at which to bail if not yet complete.
     * @return LinkedList of Diff objects.
     */
    private DiffList<T> bisect(List<T> text1, List<T> text2)
    {
        // Cache the text lengths to prevent multiple calls.
        int text1_length = text1.size();
        int text2_length = text2.size();
        int max_d = (text1_length + text2_length + 1) / 2;
        int v_offset = max_d;
        int v_length = 2 * max_d;
        int[] v1 = new int[v_length];
        int[] v2 = new int[v_length];
        for (int x = 0; x < v_length; x++)
        {
            v1[x] = -1;
            v2[x] = -1;
        }
        v1[v_offset + 1] = 0;
        v2[v_offset + 1] = 0;
        int delta = text1_length - text2_length;
        // If the total number of characters is odd, then the front path will
        // collide with the reverse path.
        boolean front = (delta % 2 != 0);
        // Offsets for start and end of k loop.
        // Prevents mapping of space beyond the grid.
        int k1start = 0;
        int k1end = 0;
        int k2start = 0;
        int k2end = 0;
        for (int d = 0; d < max_d; d++)
        {
            // Walk the front path one step.
            for (int k1 = -d + k1start; k1 <= d - k1end; k1 += 2)
            {
                int k1_offset = v_offset + k1;
                int x1;
                if (k1 == -d || k1 != d
                        && v1[k1_offset - 1] < v1[k1_offset + 1])
                {
                    x1 = v1[k1_offset + 1];
                }
                else
                {
                    x1 = v1[k1_offset - 1] + 1;
                }
                int y1 = x1 - k1;
                while (x1 < text1_length && y1 < text2_length
                        && itemsEqual(text1.get(x1), text2.get(y1)))
                {
                    x1++;
                    y1++;
                }
                v1[k1_offset] = x1;
                if (x1 > text1_length)
                {
                    // Ran off the right of the graph.
                    k1end += 2;
                }
                else if (y1 > text2_length)
                {
                    // Ran off the bottom of the graph.
                    k1start += 2;
                }
                else if (front)
                {
                    int k2_offset = v_offset + delta - k1;
                    if (k2_offset >= 0 && k2_offset < v_length
                            && v2[k2_offset] != -1)
                    {
                        // Mirror x2 onto top-left coordinate system.
                        int x2 = text1_length - v2[k2_offset];
                        if (x1 >= x2)
                        {
                            // Overlap detected.
                            return bisectSplit(text1, text2, x1, y1);
                        }
                    }
                }
            }

            // Walk the reverse path one step.
            for (int k2 = -d + k2start; k2 <= d - k2end; k2 += 2)
            {
                int k2_offset = v_offset + k2;
                int x2;
                if (k2 == -d || k2 != d
                        && v2[k2_offset - 1] < v2[k2_offset + 1])
                {
                    x2 = v2[k2_offset + 1];
                }
                else
                {
                    x2 = v2[k2_offset - 1] + 1;
                }
                int y2 = x2 - k2;
                while (x2 < text1_length
                        && y2 < text2_length
                        && itemsEqual(text1.get(text1_length - x2 - 1),
                                text2.get(text2_length - y2 - 1)))
                {
                    x2++;
                    y2++;
                }
                v2[k2_offset] = x2;
                if (x2 > text1_length)
                {
                    // Ran off the left of the graph.
                    k2end += 2;
                }
                else if (y2 > text2_length)
                {
                    // Ran off the top of the graph.
                    k2start += 2;
                }
                else if (!front)
                {
                    int k1_offset = v_offset + delta - k2;
                    if (k1_offset >= 0 && k1_offset < v_length
                            && v1[k1_offset] != -1)
                    {
                        int x1 = v1[k1_offset];
                        int y1 = v_offset + x1 - k1_offset;
                        // Mirror x2 onto top-left coordinate system.
                        x2 = text1_length - x2;
                        if (x1 >= x2)
                        {
                            // Overlap detected.
                            return bisectSplit(text1, text2, x1, y1);
                        }
                    }
                }
            }
        }
        // Diff took too long and hit the deadline or
        // number of diffs equals number of characters, no commonality at all.
        DiffList<T> diffs = new DiffList<T>();
        diffs.add(new Diff<T>(Diff.Operation.DELETE, text1));
        diffs.add(new Diff<T>(Diff.Operation.INSERT, text2));
        return diffs;
    }


    // ----------------------------------------------------------
    /**
     * Given the location of the 'middle snake', split the diff in two parts and
     * recurse.
     *
     * @param text1
     *            Old List<T> to be diffed.
     * @param text2
     *            New List<T> to be diffed.
     * @param x
     *            Index of split point in text1.
     * @param y
     *            Index of split point in text2.
     * @param deadline
     *            Time at which to bail if not yet complete.
     * @return LinkedList of Diff objects.
     */
    private DiffList<T> bisectSplit(List<T> text1, List<T> text2,
            int x, int y)
    {
        List<T> text1a = text1.subList(0, x);
        List<T> text2a = text2.subList(0, y);
        List<T> text1b = text1.subList(x, text1.size());
        List<T> text2b = text2.subList(y, text2.size());

        // Compute both diffs serially.
        DiffList<T> diffs = doDiff(text1a, text2a);
        DiffList<T> diffsb = doDiff(text1b, text2b);

        diffs.addAll(diffsb);
        return diffs;
    }


    // ----------------------------------------------------------
    /**
     * Determine the common prefix of two List<T>s
     *
     * @param text1
     *            First List<T>.
     * @param text2
     *            Second List<T>.
     * @return The number of characters common to the start of each List<T>.
     */
    private int findCommonPrefix(List<T> text1, List<T> text2)
    {
        // Performance analysis: http://neil.fraser.name/news/2007/10/09/
        int n = Math.min(text1.size(), text2.size());
        for (int i = 0; i < n; i++)
        {
            if (!itemsEqual(text1.get(i), text2.get(i)))
            {
                return i;
            }
        }
        return n;
    }


    // ----------------------------------------------------------
    /**
     * Determine the common suffix of two List<T>s
     *
     * @param text1
     *            First List<T>.
     * @param text2
     *            Second List<T>.
     * @return The number of characters common to the end of each List<T>.
     */
    private int findCommonSuffix(List<T> text1, List<T> text2)
    {
        // Performance analysis: http://neil.fraser.name/news/2007/10/09/
        int text1_length = text1.size();
        int text2_length = text2.size();
        int n = Math.min(text1_length, text2_length);
        for (int i = 1; i <= n; i++)
        {
            if (!itemsEqual(text1.get(text1_length - i),
                    text2.get(text2_length - i)))
            {
                return i - 1;
            }
        }
        return n;
    }


    // ----------------------------------------------------------
    /**
     * Determine if the suffix of one List<T> is the prefix of another.
     *
     * @param text1
     *            First List<T>.
     * @param text2
     *            Second List<T>.
     * @return The number of characters common to the end of the first List<T>
     *         and the start of the second List<T>.
     */
    @SuppressWarnings("unused")
    private int findCommonOverlap(List<T> text1, List<T> text2)
    {
        // Cache the text lengths to prevent multiple calls.
        int text1_length = text1.size();
        int text2_length = text2.size();
        // Eliminate the null case.
        if (text1_length == 0 || text2_length == 0)
        {
            return 0;
        }
        // Truncate the longer List<T>.
        if (text1_length > text2_length)
        {
            text1 = text1.subList(text1_length - text2_length, text1.size());
        }
        else if (text1_length < text2_length)
        {
            text2 = text2.subList(0, text1_length);
        }
        int text_length = Math.min(text1_length, text2_length);
        // Quick check for the worst case.
        if (text1.equals(text2))
        {
            return text_length;
        }

        // Start by looking for a single character match
        // and increase length until no match is found.
        // Performance analysis: http://neil.fraser.name/news/2010/11/04/
        int best = 0;
        int length = 1;
        while (true)
        {
            List<T> pattern = text1.subList(text_length - length, text1.size());
            int found = text2.indexOf(pattern);
            if (found == -1)
            {
                return best;
            }
            length += found;
            if (found == 0 || listsEqual(
                    text1.subList(text_length - length, text1.size()),
                    text2.subList(0, length)))
            {
                best = length;
                length++;
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Do the two texts share a subList which is at least half the length of
     * the longer text? This speedup can produce non-minimal diffs.
     *
     * @param text1
     *            First List<T>.
     * @param text2
     *            Second List<T>.
     * @return Five element List<T> array, containing the prefix of text1, the
     *         suffix of text1, the prefix of text2, the suffix of text2 and the
     *         common middle. Or null if there was no match.
     */
    private List<T>[] halfMatch(List<T> text1, List<T> text2)
    {
        List<T> longtext = text1.size() > text2.size() ? text1 : text2;
        List<T> shorttext = text1.size() > text2.size() ? text2 : text1;
        if (longtext.size() < 4 || shorttext.size() * 2 < longtext.size())
        {
            return null; // Pointless.
        }

        // First check if the second quarter is the seed for a half-match.
        List<T>[] hm1 = halfMatchI(longtext, shorttext,
                (longtext.size() + 3) / 4);
        // Check again based on the third quarter.
        List<T>[] hm2 = halfMatchI(longtext, shorttext,
                (longtext.size() + 1) / 2);
        List<T>[] hm;
        if (hm1 == null && hm2 == null)
        {
            return null;
        }
        else if (hm2 == null)
        {
            hm = hm1;
        }
        else if (hm1 == null)
        {
            hm = hm2;
        }
        else
        {
            // Both matched.  Select the longest.
            hm = hm1[4].size() > hm2[4].size() ? hm1 : hm2;
        }

        // A half-match was found, sort out the return data.
        if (text1.size() > text2.size())
        {
            return hm;
            //return new List<T>[]{hm[0], hm[1], hm[2], hm[3], hm[4]};
        }
        else
        {
            return new List[] { hm[2], hm[3], hm[0], hm[1], hm[4] };
        }
    }


    // ----------------------------------------------------------
    /**
     * Does a subList of shorttext exist within longtext such that the
     * subList is at least half the length of longtext?
     *
     * @param longtext
     *            Longer List<T>.
     * @param shorttext
     *            Shorter List<T>.
     * @param i
     *            Start index of quarter length subList within longtext.
     * @return Five element List<T> array, containing the prefix of longtext, the
     *         suffix of longtext, the prefix of shorttext, the suffix of
     *         shorttext and the common middle. Or null if there was no match.
     */
    private List<T>[] halfMatchI(List<T> longtext, List<T> shorttext, int i)
    {
        // Start with a 1/4 length subList at position i as a seed.
        List<T> seed = longtext.subList(i, i + longtext.size() / 4);
        int j = -1;
        List<T> best_common = new ArrayList<T>();
        List<T> best_longtext_a = new ArrayList<T>();
        List<T> best_longtext_b = new ArrayList<T>();
        List<T> best_shorttext_a = new ArrayList<T>();
        List<T> best_shorttext_b = new ArrayList<T>();
        while ((j = listIndexOf(shorttext, seed, j + 1)) != -1)
        {
            int prefixLength = findCommonPrefix(longtext.subList(i, longtext.size()),
                    shorttext.subList(j, shorttext.size()));
            int suffixLength = findCommonSuffix(longtext.subList(0, i),
                    shorttext.subList(0, j));
            if (best_common.size() < suffixLength + prefixLength)
            {
                best_common = addLists(shorttext.subList(j - suffixLength, j),
                        shorttext.subList(j, j + prefixLength));
                best_longtext_a = longtext.subList(0, i - suffixLength);
                best_longtext_b = longtext.subList(i + prefixLength, longtext.size());
                best_shorttext_a = shorttext.subList(0, j - suffixLength);
                best_shorttext_b = shorttext.subList(j + prefixLength, shorttext.size());
            }
        }
        if (best_common.size() * 2 >= longtext.size())
        {
            return new List[] { best_longtext_a, best_longtext_b,
                    best_shorttext_a, best_shorttext_b, best_common };
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    private boolean itemsEqual(T item1, T item2)
    {
        return DiffUtils.itemsEqual(item1, item2, comparator);
    }


    // ----------------------------------------------------------
    private List<T> addLists(List<T>... lists)
    {
        ArrayList<T> newList = new ArrayList<T>();

        for (List<T> list : lists)
        {
            newList.addAll(list);
        }

        return newList;
    }


    // ----------------------------------------------------------
    private boolean listsEqual(List<T> list1, List<T> list2)
    {
        return DiffUtils.listsEqual(list1, list2, comparator);
    }


    // ----------------------------------------------------------
    private int listIndexOf(List<T> source, List<T> target, int start)
    {
        return DiffUtils.listIndexOf(source, target, start, comparator);
    }



}
