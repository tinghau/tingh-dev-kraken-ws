package dev.tingh;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.tingh.exception.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.net.URI;

public class AuthTokens {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokens.class);

    private static final String PUBLIC_KEY = "";
    private static final String PRIVATE_KEY = "";

    public static void main(String[] args) {
        AuthTokens authTokens = new AuthTokens();
        try {
            String token = authTokens.getToken();
            System.out.println("WebSocket Token: " + token);
        } catch (Exception e) {
            logger.error("Error fetching WebSocket token: {}", e.getMessage(), e);
        }
    }

    public String getToken() {
        try {
            String jsonResponse = getWebsocketsToken();
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            return jsonObject.getAsJsonObject("result").get("token").getAsString();
        } catch (NoSuchAlgorithmException | InvalidKeyException | URISyntaxException | IOException e) {
            logger.error("Failed to get websockets token", e);
            throw new AuthException(e);
        }
    }

    private String createSignature(String apiPath, String nonce, String apiPostBodyData) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            // GET 256 HASH
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((nonce + apiPostBodyData).getBytes());
            byte[] sha256Hash = md.digest();

            // GET 512 HASH
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(Base64.getDecoder().decode(AuthTokens.PRIVATE_KEY.getBytes()), "HmacSHA512"));
            mac.update((apiPath + "GetWebSocketsToken").getBytes());

            // CREATE API SIGNATURE
            return Base64.getEncoder().encodeToString(mac.doFinal(sha256Hash));
        } catch (Exception e) {
            logger.error("Error creating authentication signature", e);
            throw e;
        }
    }

    private String getWebsocketsToken() throws NoSuchAlgorithmException, InvalidKeyException, URISyntaxException, IOException {
        String privatePath = "/0/private/";
        String apiFullUrl = "https://api.kraken.com/0/private/GetWebSocketsToken" + "?";
        String nonce = String.valueOf(System.currentTimeMillis());
        String apiPostBodyData = "nonce=" + nonce + "&";
        String signature = createSignature(privatePath, nonce, apiPostBodyData);

        try {
            HttpsURLConnection httpConnection = getHttpsUrlConnection(apiFullUrl, signature, apiPostBodyData);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching the WebSockets token: {}", e.getMessage(), e);
            throw e;
        }
    }

    private static HttpsURLConnection getHttpsUrlConnection(String apiFullUrl, String signature, String apiPostBodyData) throws URISyntaxException, IOException {
        URL apiUrl = new URI(apiFullUrl).toURL();
        HttpsURLConnection httpConnection = (HttpsURLConnection) apiUrl.openConnection();
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("API-Key", AuthTokens.PUBLIC_KEY);
        httpConnection.setRequestProperty("API-Sign", signature);
        httpConnection.setDoOutput(true);

        try (DataOutputStream os = new DataOutputStream(httpConnection.getOutputStream())) {
            os.writeBytes(apiPostBodyData);
        }
        return httpConnection;
    }
}
