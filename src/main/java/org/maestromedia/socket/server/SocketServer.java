package org.maestromedia.socket.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class SocketServer {
    
    Server socketServer;
    public SocketServer() {
        
    }
    
    public void run() throws Exception {
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
}