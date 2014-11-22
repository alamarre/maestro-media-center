package org.maestromedia.logger;

public interface MaestroLogger {

    void debug(Object message);

    void error(Object message);

    void fatal(Object message);

    void info(Object message);

    void warn(Object message);
    
}
