package org.maestromedia.all;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.maestromedia.SocketServer;
import org.maestromedia.configuration.ConfigurationReader;
import org.maestromedia.server.MaestroHandler;
import org.maestromedia.services.cores.mappers.FolderMapper;

public class MaestroAllInOneServer {

    public static void main(String[] args) throws Exception {
        
        Weld weld = new Weld();
        WeldContainer container = weld.initialize();
        int edgeRouterPort = 8080;
        if(System.getenv("maestro_edge_port")!=null) {
            edgeRouterPort = Integer.parseInt(System.getenv("maestro_edge_port"));
        }
        Server edgeServer = new Server(edgeRouterPort);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        String staticFilesDirectory = ".";
        if(System.getenv("maestro_static_location")!=null) {
            staticFilesDirectory = System.getenv("maestro_static_location");
        }
        
        ConfigurationReader configurationReader = container.instance().select(ConfigurationReader.class).get();
        if(staticFilesDirectory.equals(".")) {
            staticFilesDirectory = configurationReader.getRootDirectory()+"/ui/angular/public_html";
        }

        UiHandler uiHandler = new UiHandler(staticFilesDirectory);
        
        MaestroHandler handler = new MaestroHandler();
        
        VideoHandler videoHandler = container.instance().select(VideoHandler.class).get();
        
        ServletContextHandler contextHandler = new ServletContextHandler();
        VideoServlet videoServlet = new VideoServlet();
        ServletHolder holder = new ServletHolder();
        holder.setServlet(videoServlet);
        videoServlet.setFolderMapper(container.instance().select(FolderMapper.class).get());
        contextHandler.setContextPath("/");
        contextHandler.addServlet(holder, "/videos/*");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { uiHandler, handler, contextHandler  });
        edgeServer.setHandler(handlers);
        
        SocketServer socketServer = new SocketServer();
        edgeServer.start();
        socketServer.run();
        System.out.println("press enter to stop");
        System.in.read();
        socketServer.stop();
        edgeServer.stop();
    }
}
