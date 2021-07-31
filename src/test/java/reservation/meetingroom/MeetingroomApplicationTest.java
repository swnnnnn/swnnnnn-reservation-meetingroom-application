package reservation.meetingroom;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetingroomApplicationTest {

    @Test
    public void createToken() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("jti", "id");
        payloads.put("aud", "localhost");

        String strSecretKey = "PAR7XZWZX151EPDQLF27QH7V4J83ZUK9";
        String strToken = "Bearer " + Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(new Date())
                .setSubject(strSecretKey)
                .signWith(SignatureAlgorithm.HS256, strSecretKey.getBytes())
                .compact();

        System.out.println(strToken);
    }
}
