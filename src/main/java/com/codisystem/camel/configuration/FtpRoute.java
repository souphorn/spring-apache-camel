package com.codisystem.camel.configuration;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class FtpRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("sftp:localhost:22/upload?username=user1&password=pass&delete=true")
                .convertBodyTo(String.class)
                .to("log:om.codisystem.camel.configuration.FtpRoute")
                ;
    }
}
