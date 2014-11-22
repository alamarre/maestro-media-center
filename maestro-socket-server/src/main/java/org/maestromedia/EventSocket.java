package org.maestromedia;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class EventSocket {

    Gson gson = new Gson();

    String id;
    Session session;

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
        /*id = UUID.randomUUID().toString();
        Map<String, String> message = new HashMap<>();
        message.put("action", "setId");
        message.put("id", id);
        SessionManager.sessions.put(id, session);
        try {
            session.getRemote().sendString(gson.toJson(message));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @OnWebSocketMessage
    public void onMessage(String message){
        Map mess = gson.fromJson(message, Map.class);
        if (mess.containsKey("action")) {
            String action = mess.get("action").toString();
            if(action.equals("setId")) {
                id = mess.get("id").toString();
                String host = mess.get("host").toString();
                SessionManager.sessions.put(id, session);
                SessionManager.hostNames.put(host, id);
            }
           
            String id = mess.get("id") == null ? this.id : mess.get("id").toString();
            if (SessionManager.sessions.containsKey(id)) {
                try {
                    Session session = SessionManager.sessions.get(id);
                    switch (action) {
                        case "list":
                            session.getRemote().sendString(gson.toJson(SessionManager.hostNames));
                            break;
                        case "setHost":
                            SessionManager.hostNames.put(id, mess.get("host").toString());
                            break;
                        default:
                            session.getRemote().sendString(message);
                            break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(EventSocket.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(action.equals("list")) {
                try {
                    session.getRemote().sendString(gson.toJson(SessionManager.hostNames));
                } catch (IOException ex) {
                    Logger.getLogger(EventSocket.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
