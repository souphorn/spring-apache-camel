package com.codisystem.camel.configuration;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class MyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:///home/souphorn/tmp/file/in")

                .unmarshal()
                .zipFile()
                .convertBodyTo(String.class)
                .to("log:com.codisystem.camel.configurationcoMyRoute")
                .to("file:///home/souphorn/tmp/file/out")
                ;
//                .
//                to("file:///home/souphorn/bar");
    }



}
