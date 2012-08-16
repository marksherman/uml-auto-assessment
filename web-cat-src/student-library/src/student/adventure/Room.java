package student.adventure;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents one location in the scenery of an adventure game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room.
 *
 * This class is part of the "World of Zuul" framework for writing
 * very simple text-based adventure games.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */
public class Room
{
    private String            description;
    private Map<String, Room> exits;        // stores exits of this room.

    /**
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "in a kitchen" or "in an open court
     * yard".
     * @param description The room's description
     */
    public Room(String description)
    {
        this.description = description;
        exits = new HashMap<String, Room>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit
     * @param neighbor The room connected to this one in the given direction
     */
    public void setExit(String direction, Room neighbor)
    {
        exits.put(direction, neighbor);
    }

    /**
     * Return the description of the room (the one that was defined in the
     * constructor).
     * @return The room's description
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a long description of this room, including a list of
     * available exits.  The description is phrased in this form:
     * <pre>
     *     You are in the kitchen.
     *     Exits: north west
     * </pre>
     * @return The room's description (prefixed with "You are " and
     * followed by a period), together with the room's
     * {@link #getExitString()}.
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return A textual listing of the possible exit directions from this room
     */
    private String getExitString()
    {
        StringBuffer result = new StringBuffer( 128 );
        result.append( "Exits:" );
        for(String direction : exits.keySet())
        {
            result.append( ' ' );
            result.append( direction );
        }
        return result.toString();
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The direction to travel
     * @return the neighboring room in the given direction
     */
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }
}

