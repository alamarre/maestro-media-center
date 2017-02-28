package org.maestromedia;

import org.maestromedia.socket.server.SocketServer;
import ca.omny.configuration.ConfigurationReader;
import ca.omny.configuration.IEnvironmentToolsProvider;
import ca.omny.request.OmnyApi;
import ca.omny.server.OmnyHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import ca.omny.utilities.providers.FlexibleToolsProvider;
import java.util.LinkedList;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.maestromedia.api.FolderList;
import org.maestromedia.api.Health;
import org.maestromedia.api.LastPlayed;
import org.maestromedia.api.Playlists;
import org.maestromedia.api.ServerIps;
import org.maestromedia.folders.FolderMapper;
import org.maestromedia.folders.FolderMapperProvider;
import org.maestromedia.folders.IVideoProvider;
import org.maestromedia.folders.PlaylistFolderMapper;
import org.maestromedia.folders.PlaylistMapper;
import org.maestromedia.folders.TvShowProvider;
import org.maestromedia.tv.index.Indexer;
import org.maestromedia.tv.index.TitleInfoMapper;
import org.maestromedia.tv.index.TvShowMapper;
import org.maestromedia.tv.index.lucene.LuceneIndexer;

public class Main {

    private static ConfigurationReader configurationReader = ConfigurationReader.getDefaultConfigurationReader();

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("c", "configure", true, "Configure the database");
        options.addOption("e", "environment-configuration-file", true, "JSON file to configure the environment");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        if (cmd.hasOption("e")) {
            String file = cmd.getOptionValue("e");
            configurationReader.loadFromFile(file);
        }

        IEnvironmentToolsProvider provider = new FlexibleToolsProvider(configurationReader);
        
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
        String rootDirectory = ConfigurationReader.findRootDirectory("videos").getParent();
        if(staticFilesDirectory.equals(".")) {
            staticFilesDirectory = rootDirectory+"/ui/angular/public_html";
        }
   
        UiHandler uiHandler = new UiHandler(staticFilesDirectory);
        TvShowMapper tvShowMapper = new TvShowMapper(new LuceneIndexer(rootDirectory+"/index"), provider.getDefaultDocumentQuerier());
        
        LinkedList<IVideoProvider> videoProviders = new LinkedList<>();
        TvShowProvider tvShowProvider = new TvShowProvider(tvShowMapper);
        videoProviders.add(tvShowProvider);
        videoProviders.add(new PlaylistFolderMapper(new PlaylistMapper()));
        final FolderMapperProvider folderMapperProvider = new FolderMapperProvider(rootDirectory);
        videoProviders.add(folderMapperProvider);
        
              
        FolderMapper folderMapper = new FolderMapper(videoProviders);
        VideoHandler videoHandler = new VideoHandler(folderMapper, provider.getDefaultDocumentQuerier());
        
        ServletContextHandler contextHandler = new ServletContextHandler();
        VideoServlet videoServlet = new VideoServlet(folderMapper, tvShowProvider, provider.getDefaultDocumentQuerier());
        ServletHolder holder = new ServletHolder();
        holder.setServlet(videoServlet);
        contextHandler.setContextPath("/");
        contextHandler.addServlet(holder, "/videos/*");
       
        LinkedList<OmnyApi> apis = new LinkedList<>();
        apis.add(new FolderList(folderMapper));
        apis.add(new Health());
        apis.add(new Playlists(new PlaylistMapper()));
        apis.add(new ServerIps());
        apis.add(new LastPlayed());
        
        OmnyHandler handler = new OmnyHandler(provider, apis);
     
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { uiHandler, handler, contextHandler });
        edgeServer.setHandler(handlers);
        SocketServer socketServer = new SocketServer();
        
        TitleInfoMapper titleInfoMapper = new TitleInfoMapper();
        if(args.length > 0 && args[0].equals("index")) {
            Indexer indexer = new Indexer(folderMapperProvider, tvShowMapper, tvShowMapper, 5*60*1000, provider.getDefaultDocumentQuerier());
            indexer.search();
        } else {
            edgeServer.start();
            socketServer.run();
            System.out.println("press enter to stop");
            System.in.read();
            socketServer.stop();
            edgeServer.stop();
        }
    }
}
