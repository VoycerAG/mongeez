/*
 * (c) 2012 Voycer AG 
 * just a simple runner to get mongeez working in ant
 */
package com.voycer;

import com.mongodb.Mongo;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.mongeez.Mongeez;
import org.springframework.core.io.FileSystemResource;
/**
 *
 * @author nwagensonner
 */
public class Mongeezcli {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {       
        System.out.println("Mongeez CLI Starter Version 1.2 (c) 2013 Voycer AG");
        Mongeez mongeez = new Mongeez();         
        String hostname = "localhost"; 
        String dbname = null;
        int port = 27017;
        
        OptionParser parser = new OptionParser("l:h:d:p:"); 
        parser.accepts("l").withRequiredArg().describedAs("filename");
        parser.accepts("h").withRequiredArg().describedAs("hostname");
        parser.accepts("d").withRequiredArg().describedAs("database");
        parser.accepts("p").withOptionalArg().describedAs("port").ofType(Integer.class); 
        
        OptionSet options = parser.parse(args); 
       
        if (options.has("p")) {
            try {
                Object o = options.valueOf("p");
                if (o instanceof Integer) {
                    port = (Integer)o; 
                } else {
                    port = Integer.parseInt((String)o);     
                }
            } catch (Exception e) {
                System.err.println("port is of illegal type. Continuing.");
            }
        }
        
        if (! options.hasArgument("l") || ! options.has("l")) {
            parser.printHelpOn(System.out);
            System.exit(1); 
        } 
        
        if (! options.hasArgument("d") || ! options.has("d")) {
            parser.printHelpOn(System.out);
            System.exit(1); 
        } 
        
        if (! options.hasArgument("h") || ! options.has("h")) {
            parser.printHelpOn(System.out);
            System.exit(1); 
        } 
        
        String changesetFile = (String) options.valueOf("l"); 
        hostname = (String) options.valueOf("h"); 
        dbname = (String) options.valueOf("d");
        
        File f = new File(changesetFile); 
        if (! f.exists()) {
            System.err.println("File "+f.getAbsolutePath()+" does not exist."); 
            System.exit(2); 
        }
        
        System.out.println("Establishing Connection on "+hostname+":"+port+"/"+dbname); 
        
        try {
            mongeez.setFile(new FileSystemResource(changesetFile));
            mongeez.setMongo(new Mongo(hostname, port));
            mongeez.setDbName(dbname);
            mongeez.process();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Mongeezcli.class.getName()).log(Level.SEVERE, null, ex);
    	    System.err.println(ex.getMessage());
        }
    }
}
