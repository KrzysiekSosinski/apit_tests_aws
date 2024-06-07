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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class ExostarDataTest2
{
  @Test
  public void testBadLoad()
  {
    try
    {
      ExostarData ed = new ExostarData();
      ed.setExodataPath( "abcd" );
      ed.loadExoData();
      Assertions.fail("Should be PVExostarException");
    }
    catch (PVExostarException e)
    {
      Assertions.assertEquals("Exception Loading Exostar Data:", e.getMessage());
      Assertions.assertTrue(e.getCause() instanceof FileNotFoundException);
    }
  }
  

}
