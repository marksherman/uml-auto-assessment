package student.adventure;

/**
 *  This class is an abstract superclass for all command classes in the game.
 *  Each user command is implemented by a specific command subclass.
 *
 *  Objects of class Command can store an optional argument word (a second
 *  word entered on the command line). If the command had only one word,
 *  then the second word is <null>.
 *
 *  @author  Michael Kolling and David J. Barnes
 *  @version 2.0 (December 2002)
 */
public abstract class Command
{
    private String secondWord;

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null. The command word should be null to
     * indicate that this was a command that is not recognised by this game.
     */
    public Command()
    {
        secondWord = null;
    }

    /**
     * Return the second word of this command. If no
     * second word was entered, the result is null.
     * @return This command's second word, if any
     */
    public String getSecondWord()
    {
        return secondWord;
    }

    /**
     * Check whether a second word was entered for this
     * command.
     * @return true if there is a non-null second word
     */
    public boolean hasSecondWord()
    {
        return secondWord != null;
    }

    /**
     * Define the second word of this command (the word
     * entered after the command word). Null indicates that
     * there was no second word.
     * @param secondWord The value to use for the second word
     */
    public void setSecondWord(String secondWord)
    {
        this.secondWord = secondWord;
    }

    /**
     * Execute this command. A flag is returned indicating whether
     * the game is over as a result of this command.
     *
     * @param player The player executing the command
     * @return True, if game should exit; false otherwise.
     */
    public abstract boolean execute(Player player);
}

