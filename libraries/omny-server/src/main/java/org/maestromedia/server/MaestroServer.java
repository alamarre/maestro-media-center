package org.maestromedia.server;

import org.eclipse.jetty.server.Server;

public class MaestroServer {
    
    public void createServer(int port) throws Exception {
        Server server = new Server(port);
        MaestroHandler handler = new MaestroHandler();
        server.setHandler(handler);
        
        server.setStopAtShutdown(true);
        
        server.start();
        System.out.println("Server started. Press enter to stop");
        System.in.read();
        server.stop();       
    }
}
