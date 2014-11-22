package org.maestromedia.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationReader {
    
    HashMap<String,String> cache = new HashMap<String, String>();
    
    String rootDirectory = null;
    
    public ConfigurationReader() {
        try {
            File currentFile = new File(".").getAbsoluteFile();
            File configurationFile = new File(currentFile.getAbsolutePath()+"/configuration");
            int maxSearch = 10;
            while(!configurationFile.exists()) {
                maxSearch--;
                currentFile = currentFile.getParentFile();
                configurationFile = new File(currentFile.getAbsolutePath()+"/configuration");
                if(maxSearch<=0) {
                    break;
                }
            }
            File folder = currentFile.getAbsoluteFile();
            rootDirectory = folder.getCanonicalPath().replaceAll("\\\\", "/");
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getRootDirectory() {
        return rootDirectory;
    }
    
    public String getConfigurationString(String name) {
        if(System.getenv(name)!=null) {
            return System.getenv(name);
        }
        
        String file = rootDirectory+"/"+name+".conf";
        
        return this.readFile(file);
    }
    
    public String getSimpleConfigurationString(String name) {
        return this.getConfigurationString(name).replaceAll("\r", "").replaceAll("\n", "");
    }
    
    public static String readAll(InputStream stream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        return readAll(br);
    }
    
    public static void readAllToBuilder(InputStream stream, StringBuilder sb) {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        readAllToStringBuilder(br,sb);
    }
    
    private String readFile(String fileName) {
        if(cache.containsKey(fileName)) {
            return cache.get(fileName);
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            final String result = this.readAll(br);
            cache.put(fileName, result);
            return result;
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(ConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String readAll(BufferedReader br) {
        StringBuilder sb = new StringBuilder();
        try {
            
            String next = br.readLine();
            while(next!=null) {
                sb.append(next);
                next = br.readLine();
                if(next!=null) {
                    sb.append("\n");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString().trim();
    }
    
    public static void readAllToStringBuilder(BufferedReader br, StringBuilder sb) {
        try {   
            String next = br.readLine();
            while(next!=null) {
                sb.append(next);
                next = br.readLine();
                if(next!=null) {
                    sb.append("\n");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
