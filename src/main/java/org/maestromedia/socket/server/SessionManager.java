package org.maestromedia.socket.server;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jetty.websocket.api.Session;

public class SessionManager {
    public static Map<String, Session> sessions = new HashMap<String, Session>();
    public static Map<String, String> hostNames = new HashMap<String, String>();
}