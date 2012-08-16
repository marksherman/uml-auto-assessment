package student.web.internal.tests;
 /**
 * Demo of screenscraping using TagSoup and XPATH as described at
 * http://blog.oroup.com/2006/11/05/the-joys-of-screenscraping/
 *
 * This example class downloads the content of a page from Google
 * Finance and parses it for the Google stock price. It completely
 * omits all error handling for brevity. Also a lot of objects
 * should be cached and re-used if you were really going to call
 * this multiple times.
 *
 * @author Oliver Roup <oroup@oroup.com>
 */

import java.io.InputStream;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.ccil.cowan.tagsoup.Parser;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import student.web.HtmlElement;
import student.web.HtmlHeadingElement;
import student.web.internal.MutableNamespaceContext;

public class QueryHtml
{
    // ----------------------------------------------------------
    public static void main(String[] args)
        throws Exception
    {
        QueryHtml qh = new QueryHtml();

        // Get the page and coerce it to an XML DOM. This loads the whole
        // thing into memory so massive pages should be cut down first
        // using SAX or something similar.
        Node node = qh.getHtmlUrlNode("http://localhost/mypage.html");

        final String[] queries = new String[] {
            "//html:a",
            "//html:h1",
            "//html:li",
            "//html:li//html:a",
            "//html:li/html:a",
            "//html:p//html:a",
            "//html:img",

            // All img or p tags
            "//html:img|//html:p",
            // All heading or anchor tags
            "//html:h1|//html:h2|//html:h3|//html:h4|//html:h5|//html:h6"
            + "|//html:a",
            // All tags by id
            "//*[@id='myid']",
            // All tags by class
            "//*[@class='green']"
            };

        for (String query : queries)
        {
            System.out.println("--------------------");
            System.out.println("searching for first: " + query);
            dumpTagDetails(qh.xPathQueryFirst(node, query));

            System.out.println();
            System.out.println("searching for all: " + query);
            List<HtmlElement> result = qh.xPathQueryAll(node, query);
            if (result.size() == 0)
            {
                System.out.println("No matches found.");
            }
            else
            {
                System.out.println("results:");
            }

            // Print out the result.
            for (HtmlElement tag : result)
            {
                dumpTagDetails(tag);
            }
            System.out.println();
        }

    }


    // ----------------------------------------------------------
    public static void dumpTagDetails(HtmlElement tag)
    {
        System.out.println(tag);
        if (tag == null) return;
        System.out.println("    type = " + tag.getType());
        System.out.println("    text = " + tag.getText());
        System.out.println("    attributes:");
        for (String attribute : tag.getAttributes())
        {
            System.out.println(
                "        " + attribute + " => "
                + tag.getAttributeValue(attribute));
        }
    }


    // ----------------------------------------------------------
    public QueryHtml()
    {
        // Create a mutable namespace context. This should really be provided
        // by the JDK, but the default implementation does not allow new
        // entries to be added.
        nc = new MutableNamespaceContext();

        // Set the prefix "html" to correspond to the xhtml namespace.
        // This can be called multiple times with different prefixes.
        nc.setNamespace("html", "http://www.w3.org/1999/xhtml");
//        nc.setNamespace("", "http://www.w3.org/1999/xhtml");
        xpath.setNamespaceContext(nc);

        try
        {
//            parser.setFeature(
//                "http://xml.org/sax/features/namespace-prefixes",true);
            xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * @param urlString The URL of the page to retrieve
     * @return A Node with a well formed XML doc coerced from the page.
     * @throws Exception if something goes wrong. No error handling at all
     * for brevity.
     */
    public Node getHtmlUrlNode(String urlString)
        throws Exception
    {
        TransformerHandler th = stf.newTransformerHandler();

        // This dom result will contain the results of the transformation
        DOMResult dr = new DOMResult();
        th.setResult(dr);

        parser.setContentHandler(th);

        URL url = new URL(urlString);
        URLConnection urlConn = url.openConnection();
        InputStream stream = urlConn.getInputStream();

        // This is where the magic happens to convert HTML to XML
        parser.parse(new InputSource(stream));
        stream.close();

        return dr.getNode();
    }


    // ----------------------------------------------------------
    private HtmlElement tagForNode(Node node)
    {
        String tagName = node.getNodeName();
        if (tagName != null
            && tagName.length() == 2
            && (tagName.charAt(0) == 'h'
                || tagName.charAt(0) == 'H')
            && tagName.charAt(1) > '0'
            && tagName.charAt(1) < '7')
        {
            return new HtmlHeadingNodeTag(node, xformer);
        }
        else
        {
            return new HtmlNodeTag(node, xformer);
        }

    }


    // ----------------------------------------------------------
    public HtmlElement xPathQueryFirst(Node node, String query)
        throws Exception
    {
        NodeList nl =
            (NodeList)xpath.evaluate(query, node, XPathConstants.NODESET);
        return (nl == null || nl.getLength() == 0)
            ? null
            : tagForNode(nl.item(0));
    }


    // ----------------------------------------------------------
    /**
     * @param node An XML DOM Tree for query
     * @param query An XPATH query to run against the DOM Tree
     * @param nc The namespaceContext that maps prefixes to XML namespace
     * @return A list of nodes that result from running the query against
     * the node.
     * @throws Exception If anything goes wrong. No error handling for brevity
     */
    public List<HtmlElement> xPathQueryAll(Node node, String query)
        throws Exception
    {
        NodeList nl =
            (NodeList)xpath.evaluate(query, node, XPathConstants.NODESET);
        ArrayList<HtmlElement> result = new ArrayList<HtmlElement>();
        for (int i = 0; i < nl.getLength(); i++)
        {
            result.add(tagForNode(nl.item(i)));
        }
        return result;
    }


    //~ private classes .......................................................

    // ----------------------------------------------------------
    private static class AttributeIterator
        implements Iterator<String>, Iterable<String>
    {
        // ----------------------------------------------------------
        public AttributeIterator(NamedNodeMap map)
        {
            inner = map;
            pos = 0;
        }


        // ----------------------------------------------------------
        public boolean hasNext()
        {
            return pos < inner.getLength();
        }


        // ----------------------------------------------------------
        public String next()
        {
            Attr attr = (Attr)inner.item(pos);
            pos++;
            return attr.getName();
        }


        // ----------------------------------------------------------
        public void remove()
        {
            throw new UnsupportedOperationException();
        }


        // ----------------------------------------------------------
        public Iterator<String> iterator()
        {
            return this;
        }


        //~ Instance/static variables .........................................
        private NamedNodeMap inner;
        private int pos;
    }


    // ----------------------------------------------------------
    private static class HtmlHeadingNodeTag
        extends HtmlNodeTag
        implements HtmlHeadingElement
    {
        // ----------------------------------------------------------
        public HtmlHeadingNodeTag(Node node, Transformer transformer)
        {
            super(node, transformer);
        }


        // ----------------------------------------------------------
        public int getHeadingLevel()
        {
            if (level == 0)
            {
                String name = getType();
                level = (int)(name.charAt(1) - '0');
            }
            return level;
        }


        //~ Instance/static variables .........................................
        private int level = 0;
    }


    // ----------------------------------------------------------
    private static class HtmlNodeTag
        implements HtmlElement
    {
        // ----------------------------------------------------------
        public HtmlNodeTag(Node node, Transformer transformer)
        {
            inner = node;
            xformer = transformer;
        }


        // ----------------------------------------------------------
        public String getType()
        {
            return inner.getNodeName();
        }


        // ----------------------------------------------------------
        public String getText()
        {
            String result = getInnerHTML();
            if (result != null)
            {
                Matcher m = INNER_TAG_TRIMMER.matcher(result);
                result = m.replaceAll("");
            }
            return result;
        }


        // ----------------------------------------------------------
        public String getInnerHTML()
        {
            if (nodeChildrenAsTextIsNull)
            {
                return null;
            }

            String result = nodeChildrenAsText == null
                ? null
                : nodeChildrenAsText.get();
            if (result == null)
            {
                result = toString();
                if (result != null)
                {
                    Matcher m = TAG_TRIMMER.matcher(result);
                    if (m.find())
                    {
                        result = m.group(1);
                        nodeChildrenAsText = new SoftReference<String>(result);
                    }
                    else
                    {
                        result = null;
                        nodeChildrenAsTextIsNull = true;
                    }
                }
            }
            return result;
        }


        // ----------------------------------------------------------
        public boolean hasAttribute(String attributeName)
        {
            return inner.getAttributes().getNamedItem(attributeName) != null;
        }


        // ----------------------------------------------------------
        public String getAttributeValue(String attributeName)
        {
            Attr attr =
                (Attr)inner.getAttributes().getNamedItem(attributeName);
            return attr == null
                ? null
                : attr.getNodeValue();
        }


        // ----------------------------------------------------------
        public Iterable<String> getAttributes()
        {
            return new AttributeIterator(inner.getAttributes());
        }


        // ----------------------------------------------------------
        public String toString()
        {
            String result = nodeAsText == null ? null : nodeAsText.get();
            if (result == null)
            {
                try
                {
                    result = dumpNode(inner);
                    nodeAsText = new SoftReference<String>(result);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return result;
        }


        // ----------------------------------------------------------
        /**
         * @param node A node to be dumped to a string
         * @param omitDeclaration A boolean whether to omit the XML declaration
         * @return A string representation of the node.
         * @throws Exception If anything goes wrong. Error handling omitted.
         */
        private String dumpNode(Node node)
            throws Exception
        {
            StringWriter sw = new StringWriter();
            Result result = new StreamResult(sw);
            Source source = new DOMSource(node);
            xformer.transform(source, result);
            return sw.toString();
        }


        //~ Instance/static variables .........................................
        private Transformer xformer;
        private Node inner;
        private SoftReference<String> nodeAsText;
        private SoftReference<String> nodeChildrenAsText;
        private boolean nodeChildrenAsTextIsNull;
    }


    //~ Instance/static variables .............................................

    private MutableNamespaceContext nc;
    private SAXTransformerFactory stf =
        (SAXTransformerFactory)TransformerFactory.newInstance();
    private Parser parser = new Parser();
    private XPathFactory xpf = XPathFactory.newInstance();
    private XPath xpath = xpf.newXPath();
    private Transformer xformer;
    private static final Pattern TAG_TRIMMER =
        Pattern.compile("^<[^>]*>(.*)</[^>]*>$");
    private static final Pattern INNER_TAG_TRIMMER =
        Pattern.compile("<[^>]*>");
}
