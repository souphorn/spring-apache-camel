package com.codisystem.camel.configuration;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:///home/souphorn/foo").to("file:///home/souphorn/bar");
    }



}
