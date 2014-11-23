package org.maestromedia;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class SocketServer {
    
    Server socketServer;
    public SocketServer() {
        
    }
    
    public void run() throws Exception {
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        int socketServerPort = 8081;
        if (System.getenv("maestro_socket_port") != null) {
            socketServerPort = Integer.parseInt(System.getenv("maestro_socket_port"));
        }
        socketServer = new Server();
        ServerConnector connector = new ServerConnector(socketServer);
        connector.setPort(socketServerPort);
        socketServer.addConnector(connector);

        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(EventSocket.class);
            }
        };
        socketServer.setHandler(wsHandler);

        socketServer.start();
    }
    
    public void stop() throws Exception {
        socketServer.stop();
    }

    public static void main(String[] args) throws Exception {
        SocketServer server = new SocketServer();
        server.run();
        System.in.read();
        server.stop();
    }
}
