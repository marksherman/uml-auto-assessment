package student.adventure;

/**
 * Implementation of the 'quit' user command for adventure games.
 *
 * @author Michael Kolling
 * @version 1.0 (December 2002)
 */
public class QuitCommand extends Command
{
    /**
     * Constructor for objects of class QuitCommand
     */
    public QuitCommand()
    {
        // nothing to do
    }

    /**
     * "Quit" was entered. Check the argument to see whether
     * we really quit the game. Return true, if we should quit, false
     * otherwise.
     */
    public boolean execute(Player player)
    {
        if(getSecondWord() == null) {
            return true;
        }
        else {
            System.out.println("I cannot quit that ...");
            return false;
        }
    }

}
