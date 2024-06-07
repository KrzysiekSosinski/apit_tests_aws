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

import com.boeing.filing.base.BaseIntegrationTest;
import com.boeing.filing.util.SpringContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
class ExostarDataTest extends BaseIntegrationTest {
  ExostarData testclass;

  /**
   *
   */
  @BeforeEach
  void setUp() {
    testclass = SpringContext.getBean(ExostarData.class);
  }

  @Test
  void testLoad() {
    assertNotNull(testclass);
    assertNotNull(testclass.getExoData());
    assertTrue(testclass.getExoData().containsKey("113360581"));
  }
}
