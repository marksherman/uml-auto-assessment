package org.webcat.diff;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiffMatcher<T>
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public DiffMatcher(List<T> text, List<T> pattern, int loc)
    {
        this(text, pattern, loc, null);
    }


    // ----------------------------------------------------------
    public DiffMatcher(List<T> text, List<T> pattern, int loc,
                       Comparator<T> comp)
    {
        this.comparator = comp;
        bestMatchIndex = doMatch(text, pattern, loc);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public int getBestMatchIndex()
    {
        return bestMatchIndex;
    }


    // ----------------------------------------------------------
    /**
     * Locate the best instance of 'pattern' in 'text' near 'loc'. Returns -1 if
     * no match found.
     *
     * @param text
     *            The text to search.
     * @param pattern
     *            The pattern to search for.
     * @param loc
     *            The location to search around.
     * @return Best match index or -1.
     */
    private int doMatch(List<T> text, List<T> pattern, int loc)
    {
        // Check for null inputs.
        if (text == null || pattern == null)
        {
            throw new IllegalArgumentException("Null inputs. (match_main)");
        }

        loc = Math.max(0, Math.min(loc, text.size()));
        if (DiffUtils.listsEqual(text, pattern, comparator)/*text.equals(pattern)*/)
        {
            // Shortcut (potentially not guaranteed by the algorithm)
            return 0;
        }
        else if (text.size() == 0)
        {
            // Nothing to match.
            return -1;
        }
        else if (loc + pattern.size() <= text.size()
                && DiffUtils.listsEqual(text.subList(loc, loc + pattern.size()), pattern, comparator)/*text.subList(loc, loc + pattern.size()).equals(pattern)*/)
        {
            // Perfect match at the perfect spot!  (Includes case of null pattern)
            return loc;
        }
        else
        {
            // Do a fuzzy compare.
            return bitap(text, pattern, loc);
        }
    }


    // ----------------------------------------------------------
    /**
     * Locate the best instance of 'pattern' in 'text' near 'loc' using the
     * Bitap algorithm. Returns -1 if no match found.
     *
     * @param text
     *            The text to search.
     * @param pattern
     *            The pattern to search for.
     * @param loc
     *            The location to search around.
     * @return Best match index or -1.
     */
    private int bitap(List<T> text, List<T> pattern, int loc)
    {
        assert (Match_MaxBits == 0 || pattern.size() <= Match_MaxBits) : "Pattern too long for this application.";

        // Initialize the alphabet.
        Map<T, Integer> s = makeAlphabet(pattern);

        // Highest score beyond which we give up.
        double score_threshold = Match_Threshold;
        // Is there a nearby exact match? (speedup)
        int best_loc = DiffUtils.listIndexOf(text, pattern, loc, comparator);
        if (best_loc != -1)
        {
            score_threshold = Math.min(bitapScore(0, best_loc, loc,
                    pattern), score_threshold);
            // What about in the other direction? (speedup)
            best_loc = DiffUtils.listLastIndexOf(text, pattern, loc + pattern.size(), comparator);
            if (best_loc != -1)
            {
                score_threshold = Math.min(bitapScore(0, best_loc, loc,
                        pattern), score_threshold);
            }
        }

        // Initialize the bit arrays.
        int matchmask = 1 << (pattern.size() - 1);
        best_loc = -1;

        int bin_min, bin_mid;
        int bin_max = pattern.size() + text.size();
        // Empty initialization added to appease Java compiler.
        int[] last_rd = new int[0];
        for (int d = 0; d < pattern.size(); d++)
        {
            // Scan for the best match; each iteration allows for one more error.
            // Run a binary search to determine how far from 'loc' we can stray at
            // this error level.
            bin_min = 0;
            bin_mid = bin_max;
            while (bin_min < bin_mid)
            {
                if (bitapScore(d, loc + bin_mid, loc, pattern) <= score_threshold)
                {
                    bin_min = bin_mid;
                }
                else
                {
                    bin_max = bin_mid;
                }
                bin_mid = (bin_max - bin_min) / 2 + bin_min;
            }
            // Use the result from this iteration as the maximum for the next.
            bin_max = bin_mid;
            int start = Math.max(1, loc - bin_mid + 1);
            int finish = Math.min(loc + bin_mid, text.size())
                    + pattern.size();

            int[] rd = new int[finish + 2];
            rd[finish + 1] = (1 << d) - 1;
            for (int j = finish; j >= start; j--)
            {
                int charMatch;
                if (text.size() <= j - 1
                        || !s.containsKey(text.get(j - 1)))
                {
                    // Out of range.
                    charMatch = 0;
                }
                else
                {
                    charMatch = s.get(text.get(j - 1));
                }
                if (d == 0)
                {
                    // First pass: exact match.
                    rd[j] = ((rd[j + 1] << 1) | 1) & charMatch;
                }
                else
                {
                    // Subsequent passes: fuzzy match.
                    rd[j] = ((rd[j + 1] << 1) | 1) & charMatch
                            | (((last_rd[j + 1] | last_rd[j]) << 1) | 1)
                            | last_rd[j + 1];
                }
                if ((rd[j] & matchmask) != 0)
                {
                    double score = bitapScore(d, j - 1, loc, pattern);
                    // This match will almost certainly be better than any existing
                    // match.  But check anyway.
                    if (score <= score_threshold)
                    {
                        // Told you so.
                        score_threshold = score;
                        best_loc = j - 1;
                        if (best_loc > loc)
                        {
                            // When passing loc, don't exceed our current distance from loc.
                            start = Math.max(1, 2 * loc - best_loc);
                        }
                        else
                        {
                            // Already passed loc, downhill from here on in.
                            break;
                        }
                    }
                }
            }
            if (bitapScore(d + 1, loc, loc, pattern) > score_threshold)
            {
                // No hope for a (better) match at greater error levels.
                break;
            }
            last_rd = rd;
        }
        return best_loc;
    }


    // ----------------------------------------------------------
    /**
     * Compute and return the score for a match with e errors and x location.
     *
     * @param e
     *            Number of errors in match.
     * @param x
     *            Location of match.
     * @param loc
     *            Expected location of match.
     * @param pattern
     *            Pattern being sought.
     * @return Overall score for match (0.0 = good, 1.0 = bad).
     */
    private double bitapScore(int e, int x, int loc, List<T> pattern)
    {
        float accuracy = (float) e / pattern.size();
        int proximity = Math.abs(loc - x);
        if (Match_Distance == 0)
        {
            // Dodge divide by zero error.
            return proximity == 0 ? accuracy : 1.0;
        }
        return accuracy + (proximity / (float) Match_Distance);
    }


    // ----------------------------------------------------------
    /**
     * Initialize the alphabet for the Bitap algorithm.
     *
     * @param pattern
     *            The text to encode.
     * @return Hash of character locations.
     */
    private Map<T, Integer> makeAlphabet(List<T> pattern)
    {
        Map<T, Integer> s = new HashMap<T, Integer>();

        for (T c : pattern)
        {
            s.put(c, 0);
        }
        int i = 0;
        for (T c : pattern)
        {
            s.put(c, s.get(c) | (1 << (pattern.size() - i - 1)));
            i++;
        }
        return s;
    }


    //~ Static/instance variables .............................................

    // At what point is no match declared (0.0 = perfection, 1.0 = very loose).
    private float Match_Threshold = 0.5f;

    // How far to search for a match (0 = exact location, 1000+ = broad match).
    // A match this many characters away from the expected location will add 1.0
    // to the score (0.0 is a perfect match).
    private int Match_Distance = 1000;

    // The number of bits in an int.
    private short Match_MaxBits = 32;

    private Comparator<T> comparator;
    private int bestMatchIndex = -1;
}
