package com.codisystem.camel.configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.zipfile.ZipSplitter;
import org.apache.camel.model.dataformat.ZipFileDataFormat;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component
public class FtpZippedRoute extends RouteBuilder {

    private final Map<String, String> checksumList = Map.of(
            "sample1.zip", "99E71F1F16ADB25B6C4C9615DC42107D",
            "sample2.zip", "a1c198fa6d76840d33a8a68b075b4e26");

    @Override
    public void configure() throws Exception {

        ZipFileDataFormat zipFile = new ZipFileDataFormat();
        zipFile.setUsingIterator(true);
        //filter only zip files
        from("sftp:localhost:22/upload?username=user1&password=pass&delete=true&antInclude=*.zip")
                // Zip check sum
                .filter(this::checksum)
                //xsd validation
                .split(new ZipSplitter()).streaming()
                .convertBodyTo(String.class)
                .doTry()
                .to("validator:sample.xsd")
                .process(new CorrectProcessor())
                .to("file:///home/souphorn/tmp/file/out")
                .endDoTry()
                .doCatch(ValidationException.class)
                .process(new ErrorProcessor())
                .end()
        ;
    }

    private boolean checksum(Exchange exchange){
        var x = exchange.getIn().getBody(byte[].class);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(x);
            byte[] digest = md.digest();
            var myChecksum = DatatypeConverter.printHexBinary(digest).toUpperCase();
            System.out.println("this is checksum:" + myChecksum);
            final var blue = checksumList.get("sample1.zip");
            if (blue != null && blue.equals(myChecksum)) {
                System.out.println("match");
                return true;
            }
            else{
                System.out.println("not match");
                return false;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    private class CorrectProcessor implements Processor{
        @Override
        public void process(Exchange exchange) throws Exception {
            var name = exchange.getIn().getHeader("CamelFileName");
            System.out.println("file without exception:"+name);
        }
    }

    private class ErrorProcessor implements Processor{
        @Override
        public void process(Exchange exchange) throws Exception {
            var name = exchange.getIn().getHeader("CamelFileName");
            System.out.println("file with exception:"+name);
        }
    }

}
