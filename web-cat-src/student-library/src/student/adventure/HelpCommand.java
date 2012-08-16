package student.adventure;

/**
 * Implementation of the 'help' user command for adventure games.
 *
 * @author Michael Kolling
 * @version 1.0 (December 2002)
 */
public class HelpCommand extends Command
{
    private CommandWords commandWords;

    /**
     * Constructor for objects of class HelpCommand
     * @param words The set of words to print help for
     */
    public HelpCommand(CommandWords words)
    {
        commandWords = words;
    }

    /**
     * Print out some help information. Here we print some stupid,
     * cryptic message and a list of the command words.
     * Returns always false.
     */
    public boolean execute(Player player)
    {
        System.out.println( message() );
        System.out.println();
        System.out.println( "Your command words are:" );
        commandWords.showAll();
        return false;
    }

    /**
     * Get the descriptive message that is presented before
     * the list of commands when this command is executed.
     * A subclass can override this method to customize the
     * text printed for the help command.
     * @return the help message as a string
     */
    public String message()
    {
        return
            "You are lost. You are alone. You wander around aimlessly.";
    }
}
