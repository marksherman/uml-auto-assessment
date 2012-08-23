package org.webcat.core;

import java.util.NoSuchElementException;
import org.webcat.core.WCJSONRPC;
import org.apache.log4j.Logger;
import org.jabsorb.JSONRPCBridge;
import org.jabsorb.JSONRPCResult;
import org.json.JSONObject;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;
import er.ajax.AjaxUtils;
import er.ajax.json.JSONBridge;
import er.extensions.appserver.ERXWOContext;

/**
 * Handles javascript-java communication (client-server) between the javascript world running in a web browser and the
 * java world, running in a WebObject application. This remote-procedure-call communication is done using json protocol,
 * as implemented by the JSON-RPC library.
 * <p>
 * This component generate javascript code that will initialize a variable that will be the starting point for rpc
 * communication. The name of this variable is given in the <code>name</code> binding. There will be an object, server
 * side that will be the proxy and will handle the request. You can define the proxy on the server side (it has to be a
 * JSONRPCBridge). The proxy will be your window to the java world, from there you can access your java objects from the
 * javascript side. The name for this java variable is <code>proxyName</code>. By default, it will configure the
 * parent component as one java proxy object and name it <code>wopage</code> from the javascript side. A JSONRPCBridge
 * object is created if not given as a binding. If the binding is there but the value is null, it will create the bridge
 * then push it to the binding. That way, you can configure a single bridge for multiple proxy objects. It is of good
 * practice to provide a value in a binding (at least a binding) so that the object is not created on every ajax
 * request.
 * </p>
 * <p>
 * The <em>proxy</em> object will be the one visible for RPC from the javascript world. For example, the following
 * binding:<br>
 * <code>
 * PageProxy : AjaxProxy {<br>
 *      proxyName = "wopage";<br>
 *      name = "jsonrpc";<br>
 * }</code> <br>
 * will be used as follow : <table border="1">
 * <tr>
 * <th width="50%">JavaScript (client)</th>
 * <th width="50%">Java (server)</th>
 * </tr>
 * <tr>
 * <td><code>
 *      <em>// index of the selection on the client</em>;<br/>
 *      var idx = 3; // fixed value for the demo<br/>
 *      <em>// using rpc, ask our page to get the name for that client for that index.<br/>
 *      var nom = jsonrpc.wopage.clientNameAtIndex(idx);<br/>
 * </code></td>
 * <td><code>
 * // Java-WebObject side, we receive the index and simple return what they asked for, as for any java call.
 *      public String clientNameAtIndex(int i) { <br/>
 *          return <em>"something!"+i</em>;<br/>
 *  }<br/>
 * </code></td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * Remember that is no proxy object is given, it will use the parent component, which is the component in which this component is embeded.
 * </p>
 * <h2>Todo</h2>
 * <ul>
 * <li> Complete the JSON-RPC integration to be able to leverage all possibilities of that library (foreign references,
 * etc.).
 * </ul>
 *
 * @binding proxy Server side object (Java) that will be visible for rpc communication (Javascript).
 * If no object is bound, the parent() object is assigned by default.
 * @binding proxyName Client side name (Javascript) used to identify the proxy (Java) from the bridge object.
 * @binding name Client side name (Javascript) of the bridge object.
 * @binding JSONRPCBridge Server side object (Java) used to handle the request.  Of no value are bound, a new
 * object is created for every ajax request.  If a binding is there but null value, a new
 * object will be created and pushed to the binding so that this new object can be shared
 * for multiple proxy.
 *
 * @author Jean-Fran�ois Veillette <jfveillette@os.ca>
 * @version $Revision $, $Date $ <br>
 *          &copy; 2005-2006 OS communications informatiques, inc. http://www.os.ca
 *          Tous droits r�serv�s.
 */
public class WCJSONRPC extends WOComponent
{
    public WCJSONRPC(WOContext context)
    {
        super(context);
    }


    /**
     * Overridden because the component is stateless
     */
    public boolean isStateless() {
        return true;
    }

    /**
     * Overridden because the component does not synch with the bindings.
     */
    public boolean synchronizesVariablesWithBindings() {
        return false;
    }


    /**
     * Adds the jsonrpc.js script to the head in the response if not already present and also adds a javascript proxy
     * for the supplied bridge under the name "JSONRPC_<variableName>".
     *
     * @param response
     */
    public void appendToResponse(WOResponse response, WOContext context)
    {
        @SuppressWarnings("unchecked")
        NSMutableDictionary<String, Object> userInfo =
            ERXWOContext.contextDictionary();
        String name = (String) valueForBinding("name");
        String key = "JSONRPC_" + name;
        Object oldValue = userInfo.objectForKey(key);
        Object bridge = valueForBinding("JSONRPCBridge");
        if (bridge == null) {
            bridge = NSKeyValueCoding.NullValue;
        }
        if (oldValue == null) {
            // add the javascript variable 'name' only if not already in the
            // response
            userInfo.setObjectForKey(bridge, key);

            String script = "var " + name + " = new JSONRpcClient(\""
                + AjaxUtils.ajaxComponentActionUrl(context()) + "\");";

            if (AjaxUtils.isAjaxRequest(context().request()))
            {
                AjaxUtils.appendScriptHeader(response);
                AjaxUtils.appendScript(context, script);
                AjaxUtils.appendScriptFooter(response);
            }
            else
            {
                AjaxUtils.addScriptCodeInHead(response, context, script);
            }
        }
        else {
            // ok, the javascript variable 'name' is already in the response,
            // was it referencing the same JSONRPCBridge object ?
            if (bridge != oldValue) {
                // well, it wasn't ... there is high chance of unexpected
                // problem. just warn the user (programmer), that it might cause
                // problem.
                log.warn("JSONRPCProxy detected a conflict.  You defined the javascript variable '" + name + "'  multiple times, and linked to differents proxy objects: <" + bridge + "> and <" + oldValue + ">");
            }
        }
    }

    public WOActionResults invokeAction(WORequest request, WOContext context) {
        Object result;
        if (AjaxUtils.shouldHandleRequest(request, context, null)) {
            result = handleRequest(request, context);
            AjaxUtils.updateMutableUserInfoWithAjaxInfo(context());
            if (result == null) {
                result = AjaxUtils.createResponse(request, context);
            }
        } else {
            result = super.invokeAction(request, context);
        }
        return (WOActionResults) result;
    }

    /** Ask the an JSONRPCBridge object to handle the json request.
     * @param request
     * @param context
     * @return the result of the request */
    public WOActionResults handleRequest(WORequest request, WOContext context)
    {
        WOResponse response = AjaxUtils.createResponse(request, context);

        String inputString = request.contentString();
        if (log.isDebugEnabled()) {
            log.debug("AjaxProxy.handleRequest: input = " + inputString);
        }

        // Process the request
        JSONObject input = null;
        Object output = null;
        try {
            input = new JSONObject(inputString);

            Object proxy;
            if (canGetValueForBinding("proxy")) {
                proxy = valueForBinding("proxy");
            }
            else {
                proxy = parent();
            }
            String proxyName = (String) valueForBinding("proxyName");

            JSONRPCBridge bridge = null;
            if (canGetValueForBinding("AjaxBridge")) {
                bridge = (JSONRPCBridge) valueForBinding("AjaxBridge");
            }
            else {
                bridge = JSONBridge.createBridge();
                if (canSetValueForBinding("AjaxBridge")) {
                    setValueForBinding(bridge, "AjaxBridge");
                }
            }
            bridge.registerObject(proxyName, proxy);
            output = bridge.call(new Object[] { request, context, response, proxy }, input);
        }
        catch (NoSuchElementException e) {
            log.error("No method in request");
            output = JSONRPCResult.MSG_ERR_NOMETHOD;
        }
        catch (Exception e) {
            log.error("Exception", e);
            output = JSONRPCResult.MSG_ERR_NOMETHOD;
        }

        // Write the response
        if (log.isDebugEnabled()) {
            log.debug("AjaxProxy.handleRequest: output = " + output);
        }

        response.appendContentString(output.toString());
        return response;
    }

    private static Logger log = Logger.getLogger(WCJSONRPC.class);
}
