package at.ac.ase.interceptor;

import at.ac.ase.util.TokenUtil;
import at.ac.ase.util.exceptions.AuthorizationException;
import com.nimbusds.jose.JOSEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

public class TokenInterceptor implements HandlerInterceptor {

    private TokenUtil tokenUtil;


    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    private static Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

    public TokenInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = resolveToken(request);
        logger.info("Pre-handle request uri: " + request.getRequestURI() + ", request method: " + request.getMethod());
        logger.trace("Request auth type: " + request.getAuthType());
        try {
            boolean isTokenValid = TokenUtil.verifyToken(token);
            if (!isTokenValid){
                throw new AuthorizationException();
            }
            Authentication authentication = this.tokenUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ParseException | JOSEException e){
            throw new AuthorizationException();
        }
        return true;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

}
