package org.maestromedia.logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LogProducer {
    
    @Produces  
    public MaestroLogger produceLogger(InjectionPoint injectionPoint) {  
        return new SimpleLogger(injectionPoint.getMember().getDeclaringClass().getName());  
    } 
}
