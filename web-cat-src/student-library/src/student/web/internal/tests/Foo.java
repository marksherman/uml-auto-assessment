package student.web.internal.tests;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import student.web.internal.WebContent;

public class Foo
{
    public static void main(String[] args)
        throws Exception
    {
        student.web.TurboWebBot bot = new student.web.TurboWebBot(
            "http://localhost/bottest.html");
//        cs1705.web.TurboWebBot bot = new cs1705.web.TurboWebBot(
//        "http://www.dailymail.co.uk/textbased/channel-1/index.html");
//            new cs1705.web.WebBot("http://news.bbc.co.uk/text_only.stm");
        bot.resetElementsOfInterest("a");
        while (!bot.isLookingAtEndOfPage())
        {
            bot.advanceToNextLink();
            if (bot.isLookingAtLink())
            {
                bot.out().println("link: url = " + bot.getLinkURI()
                    + ", text = " + bot.getCurrentElementText()
                    + ", element = " + bot.getCurrentElement());
            }
        }
    }


    public static void main1(String[] args)
        throws Exception
    {
        URL url = new URL("http://news.search.yahoo.com/news/rss?p=obama");
        URLConnection c = url.openConnection();
        HttpURLConnection connection = (HttpURLConnection)c;
        connection.setRequestProperty("User-Agent",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
//        connection.setRequestProperty("Cookie", "B=5lukv694tkmcr&b=3&s=ic");

        connection.connect();
        System.out.println("follow redirects = " + HttpURLConnection.getFollowRedirects());;
        System.out.println("response code = " + connection.getResponseCode());
        System.out.println("header fields = " + connection.getHeaderFields());
        String headerName = null;
        for (int i = 1; (headerName = connection.getHeaderFieldKey(i)) != null; i++)
        {
            System.out.println("    " + headerName + " = " + connection.getHeaderField(i));
        }

        if (connection.getResponseCode() == 999 && connection.getErrorStream() != null)
        {
            try
            {
                Scanner in = new Scanner(connection.getErrorStream());
                in.useDelimiter("\\z");
                StringBuffer sb = new StringBuffer(4096);
                while (in.hasNext())
                {
                    sb.append(in.next());
                }
                in.close();
                Pattern p = Pattern.compile("<a href=\"([^\"]*)\">let us know</a>");
                Matcher m = p.matcher(sb.toString());
                if (m.find())
                {
                    String cookieUrl = m.group(1);
                    System.out.println("error url = " + cookieUrl);
                    URLConnection errConnection =
                        (new URL(cookieUrl)).openConnection();
                    errConnection.connect();
                    Map<String, List<String>> headers =
                        connection.getHeaderFields();
                    List<String> responseCookies = headers.get("Set-Cookie");
                    if (responseCookies != null)
                    {
                        for (String cookieVal : responseCookies)
                        {
                            String[] segments = cookieVal.split("\\s*;\\s*");
                            if (segments != null && segments.length >= 1)
                            {
                                for (String segment : segments)
                                {
                                    if (segment.toLowerCase()
                                        .startsWith("domain="))
                                    {
                                        String host = segment.substring(
                                            "domain=".length());
                                        System.out.println("cookie = " + segments[0]);
                                        System.out.println("host   = " + host);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

//        if (true) return;

//        try
//        {
//
//
//            URL url =
//                new URL("http://news.search.yahoo.com/news/rss?p=nuclear+iran");
//            System.out.println("'" + url.getHost() + "'");
//
//            long before = System.currentTimeMillis();
//            String yahooContent = WebContent.get(url);
//            long after = System.currentTimeMillis();
//
//            System.out.println("read time: " + (after - before) + " ms");
//
//            before = System.currentTimeMillis();
//            WebContent.get("http://www.vt.edu/");
//            after = System.currentTimeMillis();
//
//            System.out.println("read time: " + (after - before) + " ms");
//            System.out.println(yahooContent);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }
}
