package student.adventure;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 * This class is part of the "World of Zuul" framework for writing
 * very simple text-based adventure games.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.1 (December 2002)
 */
public class Parser
{

    private CommandWords   commands;  // holds all valid command words
    private BufferedReader reader;

    /**
     * Create a new Parser object connected to {@link System#in}.
     */
    public Parser()
    {
        commands = new CommandWords();
        reader   = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Get the next command from the input sequence.  This includes
     * printing the prompt, reading a line, grabbing the first and
     * second (if there are more than one) words from that line, using the
     * first word to look up commands in the {@link #commandWords()}, and
     * pushing the second word into the resulting command (if there was
     * a second word).  Any words on the line past the first two are
     * ignored.
     * @return the next command
     */
    public Command getCommand()
    {
        String inputLine = "";   // will hold the full input line
        String word1 = null;
        String word2 = null;

        System.out.print( promptString() );     // print prompt

        try {
            inputLine = reader.readLine();
        }
        catch(java.io.IOException exc) {
            System.out.println ("There was an error during reading: "
                                + exc.getMessage());
        }

        StringTokenizer tokenizer = new StringTokenizer(inputLine);

        if(tokenizer.hasMoreTokens())
            word1 = tokenizer.nextToken();      // get first word
        if(tokenizer.hasMoreTokens())
            word2 = tokenizer.nextToken();      // get second word

        // note: we just ignore the rest of the input line.

        Command command = commands.get(word1);
        if(command != null) {
            command.setSecondWord(word2);
        }
        return command;
    }

    /**
     * Get the prompt string printed before reading each command.
     * A subclass can override this method to change the prompt.
     * @return the prompt used for user input
     */
    public String promptString()
    {
        return "> ";
    }

    /**
     * Access the set of valid command words.
     * @return The map from Strings to Commands defining the verbs this
     * parser understands
     */
    public CommandWords commandWords()
    {
        return commands;
    }
}
