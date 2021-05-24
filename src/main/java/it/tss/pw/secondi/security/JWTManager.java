/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.tss.pw.secondi.security;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import it.tss.pw.secondi.users.User;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.eclipse.microprofile.jwt.Claims;

/**
 *
 * @author 588se
 */
public class JWTManager {

    private static final String PRIVATE_KEY = "privateKey.pem";
    private static final String ISS = "it.tss.pw-secondi";

    public String generate(User usr) {

        try {
            //create JWT from user
            JSONObject jwt = generateJWT(usr);

            // JWSignature Header ,create header with sha256 hash encryption
            JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                    //inside add my private key and type, build
                    .keyID(PRIVATE_KEY)
                    .type(JOSEObjectType.JWT)
                    .build();

            //convert JWT to JWTClaimsSet
            JWTClaimsSet claimSet = JWTClaimsSet.parse(jwt);

            //create a new JWTSigned (has not been signed yet !!) with the JWTClaimSet and JWTHeader specifications
            SignedJWT signedJWT = new SignedJWT(header, claimSet);

            PrivateKey privateKey = readPrivateKey(PRIVATE_KEY);

            //Create a new privatekey with algorit RSA
            RSASSASigner signer = new RSASSASigner(privateKey);

            //now i signed the jwt
            signedJWT.sign(signer);

            return signedJWT.serialize();

        } catch (ParseException ex) {
            Logger.getLogger(JWTManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new JwtTokenException("Impossibile generare il JWT token ");
        } catch (Exception ex) {
            Logger.getLogger(JWTManager.class.getName()).log(Level.SEVERE, null, ex);
            throw new JwtTokenException("Impossibile generare il JWT token ");
        }
    }

    private PrivateKey readPrivateKey(String resourceName) throws Exception {
        //da finire di capire --------------------------------------------------------------------
        byte[] byteBuffer = new byte[16384];
        int length = Thread.currentThread().getContextClassLoader()
                .getResource(resourceName)
                .openStream()
                .read(byteBuffer);

        String key = new String(byteBuffer, 0, length).replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)----", "")
                .replaceAll("\r\n", "")
                .replaceAll("\n", "")
                .trim();

        byte[] decodedKey = Base64.getDecoder().decode(key);
        return KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
    }

    /**
     *
     * @param usr User
     * @return JsonWebToken
     *
     */
    private JSONObject generateJWT(User usr) {
        long currentTimeInSecs = (System.currentTimeMillis() / 1000);
        long expirationTime = currentTimeInSecs + 1000;
        /*
            JsonObject jwt =
                    Json.createObjectBuilder()
                            .add(Claims.iss.name(), ISS)
                            .add(Claims.iat.name(), currentTimeInSecs)
                            .add(Claims.auth_time.name(), currentTimeInSecs)
                            .add(Claims.exp.name(), expirationTime)
                            .add(Claims.upn.name(), usr)
                            .add(Claims.groups.name(), loadGroups(groups))
                            .build();
         */

        // https://auth0.com/docs/tokens/concepts/jwt-claims
        JSONObject jwt = new JSONObject();
        jwt.put(Claims.iss.name(), ISS); // iss (issuer): Issuer of the JWT
        jwt.put(Claims.iat.name(), currentTimeInSecs); //  iat (issued at time): Time at which the JWT was issued; can be used to determine age of the JWT
        jwt.put(Claims.auth_time.name(), currentTimeInSecs); // auth_time (auth_time) Time when the authentication occurred
        jwt.put(Claims.exp.name(), expirationTime); //  exp (expiration time): Time after which the JWT expires
        jwt.put(Claims.sub.name(), usr.getId().toString()); // sub (subject): Subject of the JWT (the user)
        jwt.put(Claims.upn.name(), usr.getUsr()); // name usr
        jwt.put(Claims.groups.name(), loadGroups()); // membership group -> users 
        return jwt;
    }

    /**
     * Create membership group
     *
     * @return Json -> membership group -> "users"
     */
    private JSONArray loadGroups() {
        JSONArray result = new JSONArray();
        result.add("users");
        return result;
    }
}
