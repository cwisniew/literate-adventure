package net.rptools.maptool.mtscript.sexpression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/// Utility class for reading S-Expression test files
public class SExpressionTestUtil {

  /// Read an S-Expression test file from the test resources directory
  public static String readSExpressionTestFile(String filename) throws IOException {
    filename = "/test/sexpression/" + filename;
    try (var in =  SExpressionTestUtil.class.getResourceAsStream(filename)) {
      var reader = new BufferedReader(new InputStreamReader(in));
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }

}
