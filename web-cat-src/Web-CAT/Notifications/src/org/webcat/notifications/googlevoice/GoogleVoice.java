/*==========================================================================*\
 |  $Id: GoogleVoice.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.notifications.googlevoice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.message.BasicNameValuePair;

//-------------------------------------------------------------------------
/**
 * Provides access to Google Voice services. Currently, only sending SMS
 * messages is supported.
 *
 * @author  Tony Allevato
 * @version $Id: GoogleVoice.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
 */
public class GoogleVoice
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes the Google Voice service.
     *
     * @param username the username of the Google Voice account
     * @param password the account password
     * @param source a string that identifies the application using the service
     * @param delegate the delegate notified when operations succeed or fail
     */
    public GoogleVoice(String username, String password, String source,
                       GoogleVoiceDelegate delegate)
    {
        this.username = username;
        this.password = password;
        this.source = source;
        this.delegate = delegate;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Logs in to the Google Voice service.
     */
    public void login()
    {
        HttpUriRequest request = buildRequest(
                CLIENT_LOGIN_URL, true,
                "accountType", "GOOGLE",
                "Email", username,
                "Passwd", password,
                "service", "grandcentral",
                "source", source);

        new AsyncURLConnection(request, new SimpleURLConnectionDelegate() {
            public void didFinishLoading()
            {
                if (statusCode() == HttpStatus.SC_OK)
                {
                    Pattern pattern = Pattern.compile("^auth=(.*)$",
                            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
                    Matcher matcher = pattern.matcher(responseString());

                    if (matcher.find())
                    {
                        authToken = matcher.group(1);
                    }

                    if (authToken != null)
                    {
                        getRnrSeToken();
                    }
                    else
                    {
                        delegate.loginFailed(GoogleVoice.this, new IOException(
                                "No authorization token received."));
                    }
                }
                else
                {
                    delegate.loginFailed(GoogleVoice.this, new IOException(
                            "Google Voice login failed with status code "
                            + statusCode()));
                }
            }

            public void didFailWithException(IOException e)
            {
                delegate.loginFailed(GoogleVoice.this, e);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Logs out of the Google Voice service.
     */
    public void logout()
    {
        HttpUriRequest request = buildRequest(CLIENT_LOGOUT_URL, false);
        new AsyncURLConnection(request, null);

        // Don't bother waiting for this to complete.
    }


    // ----------------------------------------------------------
    /**
     * Sends a text message to a mobile phone.
     *
     * @param toNumber the destination number for the text message
     * @param content the text message to send
     */
    public void sendSMS(String toNumber, String content)
    {
        HttpUriRequest request = buildRequest(
                SEND_SMS_URL, true,
                "auth",        authToken,
                "phoneNumber", toNumber,
                "text",        content,
                "_rnr_se",     _rnr_se);

        new AsyncURLConnection(request, new SimpleURLConnectionDelegate() {
            public void didFinishLoading()
            {
                if (statusCode() == HttpStatus.SC_OK)
                {
                    delegate.sendSMSSucceeded(GoogleVoice.this);
                }
                else
                {
                    delegate.sendSMSFailed(GoogleVoice.this,
                            new IOException("GoogleVoice.sendSMS responded "
                            + "with status code " + statusCode()));
                }
            }

            public void didFailWithException(IOException e)
            {
                delegate.sendSMSFailed(GoogleVoice.this, e);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Constructs an HTTP request with the given URL, method, and parameters.
     *
     * @param url the URL of the request
     * @param isPost true for a POST request, false for a GET request
     * @param params a list of parameter names and values
     */
    private HttpUriRequest buildRequest(String url,
                                        boolean isPost,
                                        Object... params)
    {
        if (isPost)
        {
            ArrayList<BasicNameValuePair> paramList =
                new ArrayList<BasicNameValuePair>();

            for (int i = 0; i < params.length; i += 2)
            {
                paramList.add(new BasicNameValuePair(
                        params[i].toString(),
                        params[i + 1].toString()));
            }

            HttpPost request = new HttpPost(url);
            request.addHeader("User-Agent", USER_AGENT);

            try
            {
                request.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                // Do nothing.
            }

            return request;
        }
        else
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(url);

            if (params.length > 0)
            {
                buffer.append("?");
                buffer.append(params[0]);

                String encoded = params[1].toString();
                try
                {
                    encoded = URLEncoder.encode(encoded, "UTF-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    // Do nothing;
                }

                buffer.append("=");
                buffer.append(encoded);

                for (int i = 2; i < params.length; i += 2)
                {
                    buffer.append("&");
                    buffer.append(params[i]);

                    encoded = params[i + 1].toString();
                    try
                    {
                        encoded = URLEncoder.encode(encoded, "UTF-8");
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        // Do nothing;
                    }

                    buffer.append("=");
                    buffer.append(encoded);
                }
            }

            HttpGet request = new HttpGet(buffer.toString());
            request.addHeader("User-Agent", USER_AGENT);
            return request;
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the _rnr_se token needed for further POST requests after the login
     * method is called.
     */
    private void getRnrSeToken()
    {
        HttpUriRequest request = buildRequest(
                GENERAL_PAGE_URL, false,
                "auth", authToken);

        new AsyncURLConnection(request, new SimpleURLConnectionDelegate() {
            public void didFinishLoading()
            {
                if (statusCode() == HttpStatus.SC_OK)
                {
                    String p1 = responseString().split("'_rnr_se': '", 2)[1];
                    _rnr_se = p1.split("',", 2)[0];

                    delegate.loginSucceeded(GoogleVoice.this);
                }
                else
                {
                    delegate.loginFailed(GoogleVoice.this, new IOException(
                            "Could not retrieve _rnr_se token; status code "
                            + statusCode()));
                }
            }

            public void didFailWithException(IOException e)
            {
                delegate.loginFailed(GoogleVoice.this, e);
            }
        });
    }


    //~ Static/instance variables .............................................

    public static final String GENERAL_PAGE_URL =
        "https://www.google.com/voice/";

    public static final String CLIENT_LOGIN_URL =
        "https://www.google.com/accounts/ClientLogin";

    public static final String CLIENT_LOGOUT_URL =
        "https://www.google.com/voice/account/logout";

    public static final String SEND_SMS_URL =
        "https://www.google.com/voice/sms/send/";

    final static String USER_AGENT =
        "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) "
        + "AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.A.B.C "
        + "Safari/525.13";

    private String username;
    private String password;
    private String source;
    private GoogleVoiceDelegate delegate;

    private String _rnr_se;
    private String authToken;
}
