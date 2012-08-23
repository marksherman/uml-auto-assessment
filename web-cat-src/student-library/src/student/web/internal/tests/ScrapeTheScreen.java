package student.web.internal.tests;

import student.*;
import student.web.*;
import java.util.*;
import student.web.WebBotTask;

// -------------------------------------------------------------------------
/**
 *  This robot simply prints the most popular and most downloaded projects
 *  from SourceForge.
 *  It uses a single SourceForgeScraper bot for all of its functionality.
 *
 *  @author  dboynton
 *  @version 2009.02.20
 */
public class ScrapeTheScreen
    implements WebBotTask
{
    //~ Instance/static variables .............................................

    // Change the type of this variable if you write your own custom
    // robot class.
    SourceForgeScraper bot = null;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Print the located applications, along with context labels.
     */
    public void task()
    {
        bot = new SourceForgeScraper();
		bot.out().println("Most Active");
		bot.out().println("------------");
		bot.printMostActive();
		bot.out().println("\n");
		bot.out().println("Most Popular");
		bot.out().println("------------");
		bot.printMostPopular();
    }


    // ----------------------------------------------------------
    /**
     * This method provides access to the robot that carries out this
     * task.
     * @return The SourceForgeScraper robot used by this task.
     */
    public SourceForgeScraper getRobot()
    {
        return bot;
    }


    // ----------------------------------------------------------
    public static void main(String[] args)
    {
        ScrapeTheScreen scraper = new ScrapeTheScreen();
        scraper.task();
    }
}
