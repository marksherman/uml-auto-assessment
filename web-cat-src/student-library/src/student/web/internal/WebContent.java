/*==========================================================================*\
 |  $Id: WebContent.java,v 1.2 2010/02/23 17:06:36 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student.web.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//-------------------------------------------------------------------------
/**
 *  Static helper methods to retrieve web content, with built-in
 *  concurrency-protected caching of results.
 *
 *  @author Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/02/23 17:06:36 $
 */
public class WebContent
{
    public static String get(String url)
        throws MalformedURLException
    {
        URL realUrl = new URL(url);
        return get(realUrl, url);
    }


    public static String get(URL url)
    {
        return get(url, url.toString());
    }


    public static String get(URL url, String urlAsString)
    {
        if (urlAsString == null)
        {
            urlAsString = url.toString();
        }
        String result = READ_MARKER;
        boolean mustRead = false;
        synchronized (cache)
        {
            // This comparison intentionally uses == rather than equals(),
            // since we really, really mean object identity in this case
            while (result == READ_MARKER)
            {
                // Look in the cache
                result = cache.get(urlAsString);
                if (result == null)
                {
                    // If the cache has no value for this url,
                    // mark the cache to indicate some thread (this one) is
                    // going to read the value
                    cache.put(urlAsString, READ_MARKER);

                    // Now remember that we're the thread that needs to read
                    mustRead = true;
                }
                // This comparison intentionally uses == rather than equals(),
                // since we really, really mean object identity in this case
                else if (result == READ_MARKER)
                {
                    // Inside here, we know that some other thread is currently
                    // trying to read the content of this URL, but hasn't put
                    // it in the cache yet.
                    try
                    {
                        cache.wait();
                    }
                    catch (InterruptedException e)
                    {
                        // Now the reader should be done
                    }
                    // Now, let the while loop repeat to try again
                }
            }
        }

        // Check to see if this thread must read the URL's content to
        // insert into the cache
        if (mustRead)
        {
            // Now, check to see if we need to delay the read to prevent
            // throttling by the host.
            boolean canRead = false;
            while (!canRead)
            {
                String host = url.getHost();
                long sleepTime = 0L;
                synchronized (lastAccess)
                {
                    Long last = lastAccess.get(host);
                    if (last == null)
                    {
                        // No limits on this host
                        canRead = true;
                    }
                    else
                    {
                        // Impose a delay of 2 seconds, in condition above
                        sleepTime =
                            System.currentTimeMillis() - last.longValue();
                        if (sleepTime > 1000)
                        {
                            canRead = true;
                            lastAccess.put(host, System.currentTimeMillis());
                        }
                    }
                }
                if (!canRead && sleepTime > 0L)
                {
                    try
                    {
                        Thread.sleep(sleepTime);
                    }
                    catch (InterruptedException e)
                    {
                        // waking up
                    }
                }
            }
            IOException exception = null;
            try
            {
                URLConnection connection = prepConnectionFor(url);
                if (connection instanceof HttpURLConnection
                    && ((HttpURLConnection)connection).getResponseCode() == 999)
                {
//                    System.out.println("Yahoo 999 error received on " + url);
                    // Yahoo is being a pain
                    InputStream errStream =
                        ((HttpURLConnection)connection).getErrorStream();
                    if (errStream != null)
                    {
                        getYahooErrorCookies(errStream);

                        // Try again
//                        System.out.println("Attempting second load of " + url);
                        connection = prepConnectionFor(url);
                    }
                }
//                System.out.println("fetching from: " + url);
                result = getContentFrom(connection.getInputStream());
//                System.out.println("Content =\n" + result);
            }
            catch (IOException e)
            {
                exception = e;
            }

            // If there was some failure, just force it to an empty string
            synchronized (cache)
            {
                if (result == null)
                {
                    cache.remove(urlAsString);
                }
                else
                {
                    cache.put(urlAsString, result);
                }
                cache.notifyAll();
            }

            if (exception != null)
            {
                throw new RuntimeException(exception);
            }
        }

        return result;
    }


    private static URLConnection prepConnectionFor(URL url)
        throws IOException
    {
        java.net.URLConnection connection = url.openConnection();
        // Use a browser-like user agent, so that servers that
        // refuse connections from generic programs might still
        // provide a useful response
        connection.setRequestProperty("User-Agent", USER_AGENT);
        if (cookies != null && url.getHost() != null)
        {
            String host = url.getHost().toLowerCase();
            synchronized (cookies)
            {
                for (Cookie cookie : cookies)
                {
                    if (host.endsWith(cookie.host))
                    {
//                    System.out.println("adding cookie " + cookie.value);
                        connection.setRequestProperty(
                            "Cookie", cookie.value);
                        break;
                    }
                }
            }
        }
        connection.connect();
        return connection;
    }


    private static void getYahooErrorCookies(InputStream stream)
        throws IOException
    {
        String content = getContentFrom(stream);
        Pattern p = Pattern.compile("<a href=\"([^\"]*)\">let us know</a>");
        Matcher m = p.matcher(content);
        if (m.find())
        {
            String newUrl = m.group(1);
//            System.out.println("Attempting to get cookies from " + newUrl);
            getCookiesFrom(newUrl);
        }
    }


    private static String getContentFrom(InputStream stream)
    {
        Scanner in = new Scanner(stream);
        in.useDelimiter("\\z");
        StringBuffer sb = new StringBuffer(4096);
        while (in.hasNext())
        {
            sb.append(in.next());
        }
        in.close();
        return sb.toString();
    }


    private static void getCookiesFrom(String url)
        throws IOException
    {
        URLConnection connection = (new URL(url)).openConnection();
        connection.connect();
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
//                            System.out.println("loading live cookie = "
//                                + segments[0] + " for host = " + host);
                            synchronized (cookies)
                            {
                                for (int i = 0; i < cookies.size(); i++)
                                {
                                    if (host.equals(cookies.get(i).host))
                                    {
                                        cookies.remove(i);
                                        i--;
                                    }
                                }
                                cookies.add(new Cookie(host, segments[0]));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }


    private static class Cookie
    {
        public String host;
        public String value;
        public Cookie(String h, String v)
        {
            host = h;
            value = v;
        }
    }


    //~ Instance/static variables .............................................

    private static final String USER_AGENT =
        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)";
    private static final String READ_MARKER = "READ_MARKER";

    // Unlimited size to cache, but soft references mean content can
    // be garage-collected as needed
    private static final Map<String, String> cache =
        new MRUMap<String, String>(0, 180);
    private static final Map<String, Long> lastAccess =
        new HashMap<String, Long>(10);
    private static List<Cookie> cookies = new ArrayList<Cookie>();
    static
    {
        lastAccess.put("news.search.yahoo.com", System.currentTimeMillis());
//        URL cookieUrl = WebContent.class.getClassLoader()
//            .getResource("webBrowserCookies.txt");
//        if (cookieUrl != null)
//        {
//            cookies = new Vector<Cookie>();
//            Scanner in = new Scanner(WebContent.class.getClassLoader()
//                .getResourceAsStream("webBrowserCookies.txt"));
//            while (in.hasNextLine())
//            {
//                String host = in.next();
//                String cookie = in.nextLine().trim();
//                cookies.add(new Cookie(host, cookie));
////                System.out.println("loaded cookie: host=" + host
////                    + ", value=" + cookie);
//            }
//        }
    }
}
