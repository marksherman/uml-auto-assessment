package student.adventure;

import java.util.HashMap;
import java.util.Map;

/**
 *  This class holds a collection of all command words known to the game.
 *  It is used to recognise commands as they are typed in.
 *  It is part of the "World of Zuul" application, a very simple
 *  text-based adventure game.
 *
 *  @author  Michael Kolling and David J. Barnes
 *  @version 2.0 (December 2002)
 */
public class CommandWords
{
    private Map<String, Command> commands;

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        commands = new HashMap<String, Command>();
    }

    /**
     * Given a command word, find and return the matching command object.
     * Return null if there is no command with this name.
     * @param word The word to look up
     * @return The corresponding command, if any, or null, if the word is
     * not in this Map
     */
    public Command get(String word)
    {
        return commands.get(word);
    }

    /**
     * Add (or replace) a command word.
     * @param word     the word for this command
     * @param command  the associated command object implementing
     *                 this command
     */
    public void addCommand( String word, Command command )
    {
        commands.put( word, command );
    }

    /**
     * Print all valid commands to System.out.
     */
    public void showAll()
    {
        for (String cmd : commands.keySet() )
    {
            System.out.print( cmd + "  " );
        }
        System.out.println();
    }
}
