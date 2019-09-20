package com.codisystem.camel.configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.model.dataformat.ZipFileDataFormat;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Component
public class FtpZippedRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        Map<String, String> checksumList = new HashMap<>();
        checksumList.put("sample1.zip", "1b4494efd26684ec4a1efee51485f675");
        checksumList.put("sample2.zip", "a1c198fa6d76840d33a8a68b075b4e26");
        ZipFileDataFormat zipFile = new ZipFileDataFormat();
        zipFile.setUsingIterator(true);
        from("sftp:localhost:22/upload?username=user1&password=pass&delete=true")
                // Zip check sum
                .filter(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName").toString();
                    return fileName.equals("sample1.zip") ? true : false;//                    if

                })
                .validate(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName").toString();
                    System.out.println("Processed file name: " + fileName);
                    return true;
                })

                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        String fileName = exchange.getIn().getHeader("CamelFileName").toString();
                        System.out.println("FILENAME: " + fileName);
                    }
                })
                .split(new ZipSplitter()).streaming()
                    .convertBodyTo(String.class)
                    .to("log:com.codisystem.camel.configuration.FtpRoute")
                    .to("file:///home/souphorn/tmp/file/out")
                .end()
        ;
    }

}
