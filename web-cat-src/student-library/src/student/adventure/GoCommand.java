package student.adventure;

/**
 * Implementation of the 'go' user command for adventure games.
 *
 * @author Michael Kolling
 * @version 1.0 (December 2002)
 */
public class GoCommand
    extends Command
{
    /**
     * Constructor for objects of class GoCommand
     */
    public GoCommand()
    {
        // nothing to do
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message. Returns always 'false'.
     */
    public boolean execute(Player player)
    {
        if(hasSecondWord()) {
            String direction = getSecondWord();
            player.walk(direction);
        }
        else {
            // if there is no second word, we don't know where to go...
            System.out.println( "Go where?" );
        }
        return false;
    }
}
