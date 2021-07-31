package reservation.meetingroom.controller.rest;

import reservation.meetingroom.common.api.ApiResult;
import reservation.meetingroom.domain.dto.TokenDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class TokenController {

    @Value("${application.api.url}")
    private String API_URL;

    @PostMapping("/token")
    public ApiResult token(@RequestParam("id") String id) {
        return new ApiResult<>(
            TokenDto.builder()
                .user(id)
                .token(getJWTToken(id))
                .build()
        );
    }

    private String getJWTToken(String id) {
        String strSecretKey = "PAR7XZWZX151EPDQLF27QH7V4J83ZUK9";

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("jti", id);
        payloads.put("aud", API_URL);

        return "Bearer " + Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(new Date())
                .setSubject(strSecretKey)
                .signWith(SignatureAlgorithm.HS256, strSecretKey.getBytes())
                .compact();
    }
}