/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
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
    try (var in = SExpressionTestUtil.class.getResourceAsStream(filename)) {
      var reader = new BufferedReader(new InputStreamReader(in));
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }
}
