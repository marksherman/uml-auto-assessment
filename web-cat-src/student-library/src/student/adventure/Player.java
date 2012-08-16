package student.adventure;

/**
 * This class represents a player in an adventure game. Each player has
 * a current location.
 *
 * @author Michael Kolling
 * @version 1.0 (December 2002)
 */
public class Player
{
    private Room currentRoom;

    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        currentRoom = null;
    }

    /**
     * Return the current room for this player.
     * @return The room the player is currently in
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Set the current room for this player.
     * @param room The room to move to
     */
    public void setCurrentRoom(Room room)
    {
        currentRoom = room;
    }

    /**
     * Try to walk in a given direction. If there is a door
     * this will change the player's location.
     * @param direction Which way to walk
     */
    public void walk(String direction)
    {
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null)
            System.out.println("There is no exit in that direction!");
        else {
            setCurrentRoom(nextRoom);
            System.out.println(nextRoom.getLongDescription());
        }
    }
}
