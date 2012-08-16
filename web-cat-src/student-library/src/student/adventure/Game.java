package student.adventure;

/**
 *  This class is the main class for a "World of Zuul"-style adventure
 *  game application.
 *  "World of Zuul" is a very simple, text based adventure game.  Users
 *  can walk around some scenery. That's all. It should really be extended
 *  to make it more interesting!
 *
 *  To play this game, create your own subclass of Game, and define
 *  your own {@link #createCommands()}, {@link #createRooms()},
 *  and {@link #welcomeMessage()} methods.  Then create an instance
 *  of your subclass and call the {@link #play()} method.
 *
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.
 *
 *  @author  Stephen Edwards (based on original by Michael Kolling
 *           and David J. Barnes)
 *  @version 2003.11.10
 */
public abstract class Game
{
    private Parser parser;
    private Player player;
    private boolean initialized = false;

    /**
     * Create the game and initialise its internal map.
     * This also creates a {@link Player} to represent the player,
     * and a {@link Parser} to parse player commands.
     * The internal map is determined by the {@link #createRooms()}
     * method, and the list of supported commands is determined
     * by the {@link #createCommands()} method.
     */
    public Game()
    {
        this( new Player(), new Parser() );
    }

    /**
     * Create the game and initialise its internal map.
     * The provided {@link Player} object is used to represent the
     * player, and the provided {@link Parser} object is used to
     * parse player commands.  This allows custom subclasses of
     * Player or Parser to be used if desired.
     * The internal map is determined by the {@link #createRooms()}
     * method, and the list of supported commands is determined
     * by the {@link #createCommands()} method.
     * @param player The player object to use
     * @param parser The parser to use
     */
    public Game( Player player, Parser parser )
    {
        this.player = player;
        this.parser = parser;
    // Cannot call these for initialization here, since
    // they are defined in a subclass and might therefore refer
    // to subclass fields that have not yet been initialized.
    // use lazy evaluation instead.
    // ---
        // createCommands();
        // createRooms();
    }

    /**
     * Make sure that the create methods have been called before
     * anything else.
     */
    private void ensureInitialization()
    {
    if ( !initialized )
    {
        initialized = true;
        createCommands();
        createRooms();
    }
    }

    /**
     * Access this game's player.
     * @return The player
     */
    public final Player player()
    {
    ensureInitialization();
        return player;
    }

    /**
     * Access this game's parser.
     * @return The parser
     */
    public final Parser parser()
    {
    ensureInitialization();
        return parser;
    }

    /**
     * Create all the commands this game knows about.
     * A subclass must define this method to provide its
     * own set of commands and associated words.  A new
     * command is typically added like this:
     * <pre>
     *     parser().commandWords.addCommand( "go", new GoCommand() );
     * </pre>
     */
    public abstract void createCommands();

    /**
     * Create all the rooms and link their exits together.
     * A subclass must define this method to provide its
     * own map of rooms.
     */
    public abstract void createRooms();

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play()
    {
    ensureInitialization();
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while(! finished) {
            Command command = parser.getCommand();
            if(command == null) {
                System.out.println("I don't understand what you mean ...");
            } else {
                finished = command.execute(player);
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    public void printWelcome()
    {
    ensureInitialization();
        System.out.println();
        System.out.println( welcomeMessage() );
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    /**
     * Returns the welcome message printed when the game starts.
     * A subclass must define this method to provide the content of
     * the welcome message.
     * @return The welcome message
     */
    public abstract String welcomeMessage();
}
