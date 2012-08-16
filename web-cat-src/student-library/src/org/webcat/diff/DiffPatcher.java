package org.webcat.diff;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiffPatcher<T>
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
//    public DiffPatcher(List<T> list1, DiffList<T> diffs)
//    {
//        this(list1, diffs, null);
//    }


    // ----------------------------------------------------------
    public DiffPatcher(List<T> list1, DiffList<T> diffs,
                       Comparator<T> comp)
    {
        this.comparator = comp;
        this.patches = makePatch(list1, diffs);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public PatchList<T> getPatches()
    {
        return patches;
    }


    // ----------------------------------------------------------
    /**
     * Increase the context until it is unique, but don't let the pattern expand
     * beyond Match_MaxBits.
     *
     * @param patch
     *            The patch to grow.
     * @param list
     *            Source list.
     */
    private void addContext(Patch<T> patch, List<T> list)
    {
        if (list.size() == 0)
        {
            return;
        }
        List<T> pattern = list.subList(patch.start2, patch.start2
                + patch.length1);
        int padding = 0;

        // Look for the first and last matches of pattern in list.  If two different
        // matches are found, increase the pattern length.
        while (list.indexOf(pattern) != list.lastIndexOf(pattern)
                && pattern.size() < Match_MaxBits - Patch_Margin
                        - Patch_Margin)
        {
            padding += Patch_Margin;
            pattern = list
                    .subList(Math.max(0, patch.start2 - padding), Math.min(
                            list.size(), patch.start2 + patch.length1
                                    + padding));
        }
        // Add one chunk for good luck.
        padding += Patch_Margin;

        // Add the prefix.
        List<T> prefix = list.subList(Math.max(0, patch.start2 - padding),
                patch.start2);
        if (prefix.size() != 0)
        {
            patch.diffs.addFirst(new Diff<T>(Diff.Operation.EQUAL, prefix));
        }
        // Add the suffix.
        List<T> suffix = list.subList(patch.start2 + patch.length1, Math.min(
                list.size(), patch.start2 + patch.length1 + padding));
        if (suffix.size() != 0)
        {
            patch.diffs.addLast(new Diff<T>(Diff.Operation.EQUAL, suffix));
        }

        // Roll back the start points.
        patch.start1 -= prefix.size();
        patch.start2 -= prefix.size();
        // Extend the lengths.
        patch.length1 += prefix.size() + suffix.size();
        patch.length2 += prefix.size() + suffix.size();
    }


    // ----------------------------------------------------------
    private static <T> List<T> addLists(List<T>... lists)
    {
        ArrayList<T> newList = new ArrayList<T>();

        for (List<T> list : lists)
        {
            newList.addAll(list);
        }

        return newList;
    }


    // ----------------------------------------------------------
    /**
     * Compute a list of patches to turn list1 into list2. list2 is not
     * provided, diffs are the delta between list1 and list2.
     *
     * @param list1
     *            Old list.
     * @param diffs
     *            Array of diff tuples for list1 to list2.
     * @return LinkedList of Patch objects.
     */
    private PatchList<T> makePatch(List<T> list1, DiffList<T> diffs)
    {
        if (list1 == null || diffs == null)
        {
            throw new IllegalArgumentException("Null inputs. (patch_make)");
        }

        PatchList<T> patches = new PatchList<T>();
        if (diffs.isEmpty())
        {
            return patches; // Get rid of the null case.
        }
        Patch<T> patch = new Patch<T>();
        int char_count1 = 0; // Number of characters into the list1 List<T>.
        int char_count2 = 0; // Number of characters into the list2 List<T>.
        // Start with list1 (prepatch_list) and apply the diffs until we arrive at
        // list2 (postpatch_list). We recreate the patches one by one to determine
        // context info.
        List<T> prepatch_list = list1;
        List<T> postpatch_list = list1;
        for (Diff<T> aDiff : diffs)
        {
            if (patch.diffs.isEmpty() && aDiff.operation != Diff.Operation.EQUAL)
            {
                // A new patch starts here.
                patch.start1 = char_count1;
                patch.start2 = char_count2;
            }

            switch (aDiff.operation)
            {
            case INSERT:
                patch.diffs.add(aDiff);
                patch.length2 += aDiff.list.size();
                postpatch_list = addLists(postpatch_list.subList(0, char_count2),
                        aDiff.list, postpatch_list.subList(char_count2, postpatch_list.size()));
                break;
            case DELETE:
                patch.length1 += aDiff.list.size();
                patch.diffs.add(aDiff);
                postpatch_list =
                    addLists(postpatch_list.subList(0, char_count2),
                        postpatch_list.subList(char_count2
                                + aDiff.list.size(), postpatch_list.size()));
                break;
            case EQUAL:
                if (aDiff.list.size() <= 2 * Patch_Margin
                        && !patch.diffs.isEmpty() && aDiff != diffs.getLast())
                {
                    // Small equality inside a patch.
                    patch.diffs.add(aDiff);
                    patch.length1 += aDiff.list.size();
                    patch.length2 += aDiff.list.size();
                }

                if (aDiff.list.size() >= 2 * Patch_Margin)
                {
                    // Time for a new patch.
                    if (!patch.diffs.isEmpty())
                    {
                        addContext(patch, prepatch_list);
                        patches.add(patch);
                        patch = new Patch<T>();
                        // Unlike Unidiff, our patch lists have a rolling conlist.
                        // http://code.google.com/p/google-diff-match-patch/wiki/Unidiff
                        // Update prepatch list & pos to reflect the application of the
                        // just completed patch.
                        prepatch_list = postpatch_list;
                        char_count1 = char_count2;
                    }
                }
                break;
            }

            // Update the current character count.
            if (aDiff.operation != Diff.Operation.INSERT)
            {
                char_count1 += aDiff.list.size();
            }
            if (aDiff.operation != Diff.Operation.DELETE)
            {
                char_count2 += aDiff.list.size();
            }
        }
        // Pick up the leftover patch if not empty.
        if (!patch.diffs.isEmpty())
        {
            addContext(patch, prepatch_list);
            patches.add(patch);
        }

        return patches;
    }


    // ----------------------------------------------------------
    /**
     * Merge a set of patches onto the list. Return a patched list, as well as
     * an array of true/false values indicating which patches were applied.
     *
     * @param patches
     *            Array of patch objects
     * @param list
     *            Old list.
     * @return Two element Object array, containing the new list and an array of
     *         boolean values.
     */
    public PatchApplication<T> apply(List<T> list)
    {
        if (patches.isEmpty())
        {
            return new PatchApplication<T>(list, new boolean[0]);
        }

        // Deep copy the patches so that no changes are made to originals.
        patches = patches.clone();

        List<T> nullPadding = addPadding(patches, Patch_Margin);
        list = addLists(nullPadding, list, nullPadding);
        splitMax(patches);

        int x = 0;
        // delta keeps track of the offset between the expected and actual location
        // of the previous patch.  If there are patches expected at positions 10 and
        // 20, but the first patch was found at 12, delta is 2 and the second patch
        // has an effective expected position of 22.
        int delta = 0;
        boolean[] results = new boolean[patches.size()];
        for (Patch<T> aPatch : patches)
        {
            int expected_loc = aPatch.start2 + delta;
            List<T> list1 = aPatch.diffs.computeFirstList();
            int start_loc;
            int end_loc = -1;
            if (list1.size() > this.Match_MaxBits)
            {
                // patch_splitMax will only provide an oversized pattern in the case of
                // a monster delete.
                start_loc = new DiffMatcher<T>(list, list1.subList(0,
                        this.Match_MaxBits), expected_loc, comparator).getBestMatchIndex();
                if (start_loc != -1)
                {
                    end_loc = new DiffMatcher<T>(list, list1.subList(list1.size()
                            - this.Match_MaxBits, list1.size()), expected_loc
                            + list1.size() - this.Match_MaxBits, comparator).getBestMatchIndex();
                    if (end_loc == -1 || start_loc >= end_loc)
                    {
                        // Can't find valid trailing conlist.  Drop this patch.
                        start_loc = -1;
                    }
                }
            }
            else
            {
                start_loc = new DiffMatcher<T>(list, list1, expected_loc, comparator).getBestMatchIndex();
            }
            if (start_loc == -1)
            {
                // No match found.  :(
                results[x] = false;
                // Subtract the delta for this failed patch from subsequent patches.
                delta -= aPatch.length2 - aPatch.length1;
            }
            else
            {
                // Found a match.  :)
                results[x] = true;
                delta = start_loc - expected_loc;
                List<T> list2;
                if (end_loc == -1)
                {
                    list2 = list.subList(start_loc, Math.min(start_loc
                            + list1.size(), list.size()));
                }
                else
                {
                    list2 = list.subList(start_loc, Math.min(end_loc
                            + this.Match_MaxBits, list.size()));
                }
                if (DiffUtils.listsEqual(list1, list2, comparator))
                {
                    // Perfect match, just shove the replacement list in.
                    list = addLists(list.subList(0, start_loc),
                            aPatch.diffs.computeSecondList(),
                            list.subList(start_loc + list1.size(), list.size()));
                }
                else
                {
                    // Imperfect match.  Run a diff to get a framework of equivalent
                    // indices.
                    DiffList<T> diffs = new Differ<T>(list1, list2, comparator).getDifferences();
                    if (list1.size() > this.Match_MaxBits
                            && diffs.getLevenshteinDistance() / (float) list1.size() > this.Patch_DeleteThreshold)
                    {
                        // The end points match, but the content is unacceptably bad.
                        results[x] = false;
                    }
                    else
                    {
                        //diff_cleanupSemanticLossless(diffs);
                        int index1 = 0;
                        for (Diff<T> aDiff : aPatch.diffs)
                        {
                            if (aDiff.operation != Diff.Operation.EQUAL)
                            {
                                int index2 = diffs.translateIndex(index1);
                                if (aDiff.operation == Diff.Operation.INSERT)
                                {
                                    // Insertion
                                    list = addLists(
                                            list.subList(0, start_loc + index2),
                                            aDiff.list,
                                            list.subList(start_loc + index2, list.size()));
                                }
                                else if (aDiff.operation == Diff.Operation.DELETE)
                                {
                                    // Deletion
                                    list = addLists(list.subList(0, start_loc + index2),
                                            list.subList(start_loc + diffs.translateIndex(index1 + aDiff.list.size()), list.size()));
                                }
                            }
                            if (aDiff.operation != Diff.Operation.DELETE)
                            {
                                index1 += aDiff.list.size();
                            }
                        }
                    }
                }
            }
            x++;
        }
        // Strip the padding off.
        list = list.subList(nullPadding.size(), list.size()
                - nullPadding.size());
        return new PatchApplication<T>(list, results);
    }


    // ----------------------------------------------------------
    /**
     * Add some padding on list start and end so that edges can match something.
     * Intended to be called only from within patch_apply.
     *
     * @param patches
     *            Array of patch objects.
     * @return The padding List<T> added to each side.
     */
    private static <T> List<T> addPadding(PatchList<T> patches, short margin)
    {
        short paddingLength = margin;
        List<T> nullPadding = new ArrayList<T>();
        for (short x = 1; x <= paddingLength; x++)
        {
            //nullPadding += String.valueOf((char) x);
            nullPadding.add(null);
        }

        // Bump all the patches forward.
        for (Patch<T> aPatch : patches)
        {
            aPatch.start1 += paddingLength;
            aPatch.start2 += paddingLength;
        }

        // Add some padding on start of first diff.
        Patch<T> patch = patches.getFirst();
        DiffList<T> diffs = patch.diffs;
        if (diffs.isEmpty() || diffs.getFirst().operation != Diff.Operation.EQUAL)
        {
            // Add nullPadding equality.
            diffs.addFirst(new Diff<T>(Diff.Operation.EQUAL, nullPadding));
            patch.start1 -= paddingLength; // Should be 0.
            patch.start2 -= paddingLength; // Should be 0.
            patch.length1 += paddingLength;
            patch.length2 += paddingLength;
        }
        else if (paddingLength > diffs.getFirst().list.size())
        {
            // Grow first equality.
            Diff<T> firstDiff = diffs.getFirst();
            int extraLength = paddingLength - firstDiff.list.size();
            firstDiff.list = addLists(
                    nullPadding.subList(firstDiff.list.size(), nullPadding.size()),
                    firstDiff.list);
            patch.start1 -= extraLength;
            patch.start2 -= extraLength;
            patch.length1 += extraLength;
            patch.length2 += extraLength;
        }

        // Add some padding on end of last diff.
        patch = patches.getLast();
        diffs = patch.diffs;
        if (diffs.isEmpty() || diffs.getLast().operation != Diff.Operation.EQUAL)
        {
            // Add nullPadding equality.
            diffs.addLast(new Diff<T>(Diff.Operation.EQUAL, nullPadding));
            patch.length1 += paddingLength;
            patch.length2 += paddingLength;
        }
        else if (paddingLength > diffs.getLast().list.size())
        {
            // Grow last equality.
            Diff<T> lastDiff = diffs.getLast();
            int extraLength = paddingLength - lastDiff.list.size();
            lastDiff.list = addLists(lastDiff.list, nullPadding.subList(0, extraLength));
            patch.length1 += extraLength;
            patch.length2 += extraLength;
        }

        return nullPadding;
    }


    // ----------------------------------------------------------
    /**
     * Look through the patches and break up any which are longer than the
     * maximum limit of the match algorithm. Intended to be called only from
     * within patch_apply.
     *
     * @param patches
     *            LinkedList of Patch objects.
     */
    private void splitMax(PatchList<T> patches)
    {
        short patch_size = Match_MaxBits;
        List<T> precontext, postcontext;
        Patch<T> patch;
        int start1, start2;
        boolean empty;
        Diff.Operation diff_type;
        List<T> diff_text;
        ListIterator<Patch<T>> pointer = patches.listIterator();
        Patch<T> bigpatch = pointer.hasNext() ? pointer.next() : null;
        while (bigpatch != null)
        {
            if (bigpatch.length1 <= Match_MaxBits)
            {
                bigpatch = pointer.hasNext() ? pointer.next() : null;
                continue;
            }
            // Remove the big old patch.
            pointer.remove();
            start1 = bigpatch.start1;
            start2 = bigpatch.start2;
            precontext = new ArrayList<T>();
            while (!bigpatch.diffs.isEmpty())
            {
                // Create one of several smaller patches.
                patch = new Patch<T>();
                empty = true;
                patch.start1 = start1 - precontext.size();
                patch.start2 = start2 - precontext.size();
                if (precontext.size() != 0)
                {
                    patch.length1 = patch.length2 = precontext.size();
                    patch.diffs.add(new Diff<T>(Diff.Operation.EQUAL, precontext));
                }
                while (!bigpatch.diffs.isEmpty()
                        && patch.length1 < patch_size - Patch_Margin)
                {
                    diff_type = bigpatch.diffs.getFirst().operation;
                    diff_text = bigpatch.diffs.getFirst().list;
                    if (diff_type == Diff.Operation.INSERT)
                    {
                        // Insertions are harmless.
                        patch.length2 += diff_text.size();
                        start2 += diff_text.size();
                        patch.diffs.addLast(bigpatch.diffs.removeFirst());
                        empty = false;
                    }
                    else if (diff_type == Diff.Operation.DELETE
                            && patch.diffs.size() == 1
                            && patch.diffs.getFirst().operation == Diff.Operation.EQUAL
                            && diff_text.size() > 2 * patch_size)
                    {
                        // This is a large deletion.  Let it pass in one chunk.
                        patch.length1 += diff_text.size();
                        start1 += diff_text.size();
                        empty = false;
                        patch.diffs.add(new Diff<T>(diff_type, diff_text));
                        bigpatch.diffs.removeFirst();
                    }
                    else
                    {
                        // Deletion or equality.  Only take as much as we can stomach.
                        diff_text = diff_text.subList(0, Math.min(diff_text
                                .size(), patch_size - patch.length1
                                - Patch_Margin));
                        patch.length1 += diff_text.size();
                        start1 += diff_text.size();
                        if (diff_type == Diff.Operation.EQUAL)
                        {
                            patch.length2 += diff_text.size();
                            start2 += diff_text.size();
                        }
                        else
                        {
                            empty = false;
                        }
                        patch.diffs.add(new Diff<T>(diff_type, diff_text));
                        if (DiffUtils.listsEqual(diff_text, bigpatch.diffs.getFirst().list, comparator)/*diff_text.equals(bigpatch.diffs.getFirst().list)*/)
                        {
                            bigpatch.diffs.removeFirst();
                        }
                        else
                        {
                            bigpatch.diffs.getFirst().list =
                                bigpatch.diffs.getFirst().list.subList(
                                        diff_text.size(),
                                        bigpatch.diffs.getFirst().list.size());
                        }
                    }
                }
                // Compute the head context for the next patch.
                precontext = patch.diffs.computeSecondList();
                precontext = precontext.subList(Math.max(0, precontext
                        .size() - Patch_Margin), precontext.size());

                List<T> firstList = bigpatch.diffs.computeFirstList();
                // Append the end context for this patch.
                if (firstList.size() > Patch_Margin)
                {
                    postcontext = firstList.subList(0, Patch_Margin);
                }
                else
                {
                    postcontext = firstList;
                }
                if (postcontext.size() != 0)
                {
                    patch.length1 += postcontext.size();
                    patch.length2 += postcontext.size();
                    if (!patch.diffs.isEmpty()
                            && patch.diffs.getLast().operation == Diff.Operation.EQUAL)
                    {
                        patch.diffs.getLast().list =
                            addLists(patch.diffs.getLast().list, postcontext);
                    }
                    else
                    {
                        patch.diffs.add(new Diff<T>(Diff.Operation.EQUAL, postcontext));
                    }
                }
                if (!empty)
                {
                    pointer.add(patch);
                }
            }

            bigpatch = pointer.hasNext() ? pointer.next() : null;
        }
    }


    //~ Static/instance variables .............................................

    // When deleting a large block of text (over ~64 characters), how close does
    // the contents have to match the expected contents. (0.0 = perfection, 1.0
    // = very loose). Note that Match_Threshold controls how closely the end
    // points of a delete need to match.
    private float Patch_DeleteThreshold = 0.5f;

    // Chunk size for context length.
    private short Patch_Margin = 4;

    // The number of bits in an int.
    private short Match_MaxBits = 32;

    private Comparator<T> comparator;
    private PatchList<T> patches;
}
