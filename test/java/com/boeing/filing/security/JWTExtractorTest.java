/*
 * The Boeing Company. Confidential & Proprietary
 * This work contains valuable confidential and proprietary information.
 * Disclosure, use or reproduction outside of The Boeing Company
 * is prohibited except as authorized in writing.  This unpublished work
 * is protected by the laws of the United States and other countries.
 * In the event of publication, the following notice shall apply:
 *
 * Copyright (c) 2024 The Boeing Company. All Rights Reserved.
 */

package com.boeing.filing.security;

import static com.boeing.filing.utils.JwtTokenGeneratorHelper.prepareCognitoToken;
import static com.boeing.filing.utils.JwtTokenGeneratorHelper.prepareServiceToken;
import static com.boeing.filing.utils.JwtTokenGeneratorHelper.prepareWebToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import com.boeing.filing.base.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

public class JWTExtractorTest extends BaseIntegrationTest
{
  @Test
  public void testCognitoToken() throws IOException {

    String auth = prepareCognitoToken();
    JWTExtractor jwt = new JWTExtractor( auth );
    jwt.processToken();
    assertEquals( auth, jwt.getAuthorization() );
    assertEquals("mockclient", jwt.getOperatorId() );
    assertEquals("jasonatest@jeppesen.com", jwt.getUsername());
  }
  
  @Test
  public void testWebToken() throws IOException {

    String auth = prepareWebToken();
    JWTExtractor jwt = new JWTExtractor( auth );
    jwt.processToken();
    assertEquals( auth, jwt.getAuthorization() );
    assertEquals("TBC", jwt.getOperatorId() );
    assertEquals("David.Jones@jeppesen.com", jwt.getUsername());
  }
  
  @Test
  public void testServiceToken() throws IOException {

    String auth = prepareServiceToken();
    JWTExtractor jwt = new JWTExtractor( auth );
    jwt.processToken();
    assertEquals( auth, jwt.getAuthorization() );
    assertEquals("FPX_mock_x509_client", jwt.getOperatorId() );
    assertNull(jwt.getUsername());
  }

  @Test
  public void testMissing()
  {
    boolean caught = false;
    try
    {
      JWTExtractor jwt = new JWTExtractor( null );
      jwt.processToken();
    }
    catch ( PVUnauthorizedException e )
    {
      caught = true;
    }
    assertTrue( caught );

    caught = false;
    try
    {
      JWTExtractor jwt = new JWTExtractor( "Authorization token does not start with 'Bearer'" );
      jwt.processToken();
    }
    catch (PVUnauthorizedException e )
    {
      caught = true;
    }
    assertTrue( caught );
  }
}
