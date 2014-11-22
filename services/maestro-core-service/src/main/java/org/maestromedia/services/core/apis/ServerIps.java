package org.maestromedia.services.core.apis;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.maestromedia.request.api.ApiResponse;
import org.maestromedia.request.api.MaestroApi;
import org.maestromedia.request.management.RequestResponseManager;

public class ServerIps implements MaestroApi {
    
    private Collection<String> getAddresses() {
        Collection<String> addresses = new LinkedList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            
            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if(!networkInterface.isLoopback()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while(inetAddresses.hasMoreElements()) {
                        InetAddress address = inetAddresses.nextElement();
                        if(address instanceof Inet4Address) {
                            addresses.add(address.getHostAddress());
                        }
                    }
                }
            }
            
        } catch (SocketException ex) {
            Logger.getLogger(ServerIps.class.getName()).log(Level.SEVERE, null, ex);
        }
        return addresses;
    }

    @Override
    public String getBasePath() {
        return "/server/ips";
    }

    @Override
    public String[] getVersions() {
        return MaestroApiConstants.versions;
    }

    @Override
    public ApiResponse getResponse(RequestResponseManager requestResponseManager) {
        return new ApiResponse(getAddresses(), 200);
    }

    @Override
    public ApiResponse postResponse(RequestResponseManager requestResponseManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ApiResponse putResponse(RequestResponseManager requestResponseManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ApiResponse deleteResponse(RequestResponseManager requestResponseManager) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
