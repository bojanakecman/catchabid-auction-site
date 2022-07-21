package at.ac.ase.util;

import at.ac.ase.entities.User;
import at.ac.ase.service.user.IAuctionHouseService;
import at.ac.ase.service.user.IRegularUserService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.*;
import javax.xml.bind.DatatypeConverter;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    @Autowired
    private IRegularUserService IRegularUserService;

    @Autowired
    private IAuctionHouseService auctionHouseService;

    static String KEY = "B&E)H@McQfTjWnZr4u7x!A%D*F-JaNdR";
    static String BASE_64_KEY = DatatypeConverter.printBase64Binary(KEY.getBytes());
    static byte[] SECRET_BYTES = DatatypeConverter.parseBase64Binary(BASE_64_KEY);
    static final int EXPIRATION = 60;

    public static String generateToken(String email, String password) throws JOSEException {
        JWSSigner signer = new MACSigner(SECRET_BYTES);

        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("CatchaBid")
                .claim("password", password)
                .expirationTime(calculateExpiryDate(EXPIRATION))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    public static boolean verifyToken(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET_BYTES);
        return signedJWT.verify(verifier);
    }

    public static String getEmailFromToken(String token){
        JsonObject jsonObject = parseTokenToJSON(token);
        return jsonObject.get("sub").getAsString();
    }

    public static JsonObject parseTokenToJSON(String token){
        return JsonParser.parseString(new String(Base64.getDecoder().decode(token.split("\\.")[1].getBytes()))).getAsJsonObject();
    }

    public Authentication getAuthentication(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring("Bearer ".length());
        }
        final List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        String userEmail = getEmailFromToken(token);
        User user = IRegularUserService.getUserByEmail(userEmail);
        if (Objects.isNull(user)) {
            user = auctionHouseService.getAuctionHouseByEmail(userEmail);
        }
        return new UsernamePasswordAuthenticationToken(user, token, roles);
    }

    public static Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }


}
