package reservation.meetingroom;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEncryptableProperties
@EnableScheduling
public class MeetingroomApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetingroomApplication.class, args);
    }
}