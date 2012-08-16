package org.webcat.diff;

import java.util.List;

/**
 * Class representing one diff operation.
 *
 * @param <T> the type of object represented by the diff
 */
public class Diff<T>
{
    /**
     * The data structure representing a diff is a Linked list of Diff objects:
     * {Diff(Operation.DELETE, "Hello"), Diff(Operation.INSERT, "Goodbye"),
     * Diff(Operation.EQUAL, " world.")} which means: delete "Hello", add
     * "Goodbye" and keep " world."
     */
    public static enum Operation
    {
        /** Represents a deletion operation. */
        DELETE,
        /** Represents an insertion operation. */
        INSERT,
        /** Represents a "keep unchanged" operation. */
        EQUAL
    }

    /**
     * One of: INSERT, DELETE or EQUAL.
     */
    public Operation operation;

    /**
     * The text associated with this diff operation.
     */
    public List<T> list;


    // ----------------------------------------------------------
    /**
     * Constructor. Initializes the diff with the provided values.
     *
     * @param operation
     *            One of INSERT, DELETE or EQUAL.
     * @param list
     *            The text being applied.
     */
    public Diff(Operation operation, List<T> list)
    {
        // Construct a diff with the specified operation and text.
        this.operation = operation;
        this.list = list;
    }


    // ----------------------------------------------------------
    /**
     * Display a human-readable version of this Diff.
     *
     * @return text version.
     */
    public String toString()
    {
        String prettyText = this.list.toString();
        return "Diff(" + this.operation + ", \"" + prettyText + "\")";
    }


    // ----------------------------------------------------------
    /**
     * Is this Diff equivalent to another Diff?
     *
     * @param d
     *            Another Diff to compare against.
     * @return true or false.
     */
    public boolean equals(Object d)
    {
        if (d instanceof Diff)
        {
            @SuppressWarnings("unchecked")
            Diff<T> other = (Diff<T>)d;
            return (other.operation == this.operation)
                && other.list.equals(this.list);
        }
        else
        {
            return false;
        }
    }
}
