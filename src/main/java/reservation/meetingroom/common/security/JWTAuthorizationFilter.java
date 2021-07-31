package reservation.meetingroom.common.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import reservation.meetingroom.common.enums.ErrorCode;

@Slf4j
@Builder
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String HEADER;
    private final String BEARER;
    private final String API_URL;

    public JWTAuthorizationFilter(String header, String bearer, String api_url) {
        this.HEADER = header;
        this.BEARER = bearer;
        this.API_URL = api_url;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if(request.getRequestURI().startsWith("/v1/token")){
            chain.doFilter(request, response);
            return;
        }

        String secretKey = "PAR7XZWZX151EPDQLF27QH7V4J83ZUK9";

        // 헤더 스키마 체크
        if (!checkJWTToken(request)) {
            outPrintln(response, "헤더 스키마 체크");
            return;
        }

        // 헤더 jwt 토큰의 header 영역 디코딩
        String jwtToken = request.getHeader(HEADER).replace(BEARER, "");
        byte[] headerBytes = jwtToken.split("\\.")[1].getBytes();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedBytes = decoder.decode(headerBytes);

        // 정보 가져오기
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(new String(decodedBytes), new TypeReference<Map<String, String>>(){});
        String jti = map.get("jti");

        // 헤더 jwt 토큰 파싱
        Claims claims = Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(jwtToken).getBody();

        // 클레임 파싱 오류 체크
        if (claims == null) {
            outPrintln(response, "클레임 파싱 오류 체크");
            return;
        }

        Date dtCurrent = new Date(System.currentTimeMillis()); // 현재시간

        // 토큰 생성시간, 서버간 시간차가 있을 수 있기에 5초간 텀을 둔다
        Date dtIssuedAt = new Date(Objects.requireNonNull(claims).getIssuedAt().getTime() - 5000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dtIssuedAt);
        cal.add(Calendar.MINUTE, 10);
        Date dtExpiration = cal.getTime();  // 토큰 만료시간

        // 생성일 체크
        if (dtIssuedAt.after(dtCurrent)) {
            outPrintln(response, ErrorCode.JWT_E002, "생성일 체크");
            return;
        }

        // 만료일 체크
        if (dtExpiration.before(dtCurrent)) {
            outPrintln(response, ErrorCode.JWT_E002, "만료일 체크");
            return;
        }

        // audience url 체크
        if(!API_URL.equals(claims.getAudience())) {
            outPrintln(response, "대상 경로 체크");
            return;
        }

        // 계정 체크
        if (!jti.equals(claims.getId())) {
            outPrintln(response, "계정 체크");
            return;
        }

        setSpringAuthentication(claims);
        chain.doFilter(request, response);
    }

    private void outPrintln(HttpServletResponse response, String message) {
        outPrintln(response, ErrorCode.JWT_E001, message);
    }

    private void outPrintln(HttpServletResponse response, ErrorCode errorCode, String message) {
        log.error("JWTAuthorizationFilter error: " + message);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("code", errorCode.getCode());
            jsonObject.put("message", errorCode.getMessage());
            SecurityContextHolder.clearContext();
            response.getWriter().println(jsonObject.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkJWTToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(BEARER);
    }

    private void setSpringAuthentication(Claims claims) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 고정
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getId(), null, grantedAuthorities);
        
        // 인증객체 등록
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
