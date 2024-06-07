package com.boeing.filing.functionalTests.steps.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {

  public static String fetchJsonFileContent(String path) {
    String content = null;
    try {
      content = Files.readString(Path.of(path));
    } catch (IOException e) {
      e.getMessage();
    }
    return content;
  }
}