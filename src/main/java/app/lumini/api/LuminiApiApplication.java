package app.lumini.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class LuminiApiApplication {

    static {
//        BlockHound
//                .builder()
//                .allowBlockingCallsInside("java.io.RandomAccessFile", "readBytes")
//                .builder()
//                .allowBlockingCallsInside("java.io.FileInputStream", "readBytes")
//                .install();
    }

    public static void main(String[] args) {
        SpringApplication.run(LuminiApiApplication.class, args);
    }

}
