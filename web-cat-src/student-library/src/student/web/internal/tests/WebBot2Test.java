package student.web.internal.tests;

import student.web.TurboWebBot;

public class WebBot2Test
{
    public static void main(String[] args)
    {
        TurboWebBot bot = new TurboWebBot("http://localhost/mypage.html");

        bot.echoPageTitle();
        bot.out().println();

        bot.advanceToNextElement();
        while (bot.isLookingAtElement())
        {
            bot.out().println(bot.getCurrentElement());
            bot.advanceToNextElement();
        }
    }
}
