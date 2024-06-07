package com.boeing.filing.utils;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class JwtTokenGeneratorHelper {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
  public static final String webTokenJsonFile = "src/test/resources/WebTokenExample.json";
  public static final String serviceTokenJsonFile = "src/test/resources/ServiceTokenExample.json";
  public static final String cognitoTokenJsonFile = "src/test/resources/CognitoTokenExample.json";

  public static String buildAuthorizationBearerHeader(final MockMvc mockMvc, final String username,
      final String password) throws Exception {
    return "Bearer " + prepareWebToken();
  }

  public static String load(String filename) throws IOException {
    return new String(Files.readAllBytes(Paths.get(filename)));
  }

  public static String loadWebToken() throws IOException {
    return load(webTokenJsonFile);
  }

  public static String loadServiceToken() throws IOException {
    return load(serviceTokenJsonFile);
  }

  public static String loadCognitoToken() throws IOException {
    return load(cognitoTokenJsonFile);
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> JsonStringToMap(String jsonString) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> map = new HashMap<>();
    map = mapper.readValue(jsonString, Map.class);
    return map;
  }

  public static String buildToken(Map<String, Object> claims, String subject, String secret) {
    return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public static String prepareWebToken() throws IOException {
    Map<String, Object> map = JsonStringToMap(loadWebToken());
    map.remove("exp");
    map.remove("iat");
    return buildToken(map, "test", "test");
  }

  public static String prepareServiceToken() throws IOException {
    Map<String, Object> map = JsonStringToMap(loadServiceToken());
    map.remove("exp");
    map.remove("iat");
    return buildToken(map, "test", "test");
  }

  public static String prepareCognitoToken() throws IOException {
    Map<String, Object> map = JsonStringToMap(loadCognitoToken());
    map.remove("exp");
    map.remove("iat");
    return buildToken(map, "test", "test");
  }

  public static String obtainAccessToken(final MockMvc mockMvc, final String username,
      final String password) throws Exception {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "password");
    params.add("client_id", "adminui-client");
    params.add("username", username);
    params.add("password", password);
    params.add("custom:operatorId", "test");
    params.add("cognito:username", "cognitousername");

    ResultActions result
        = mockMvc.perform(post("/oauth/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(AUTHORIZATION_HEADER,
                buildAuthorizationBasicHeader("adminui-client", "GK1gLnV1wfa3Ff5iQpvc"))
            .params(params)
            .with(httpBasic(username, password)))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"));

    String resultString = result.andReturn().getResponse().getContentAsString();

    JacksonJsonParser jsonParser = new JacksonJsonParser();
    return jsonParser.parseMap(resultString).get("access_token").toString();
  }

  private static String buildAuthorizationBasicHeader(final String username,
      final String password) {
    return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes(
        StandardCharsets.UTF_8));
  }
}
