package per.duyd.interview.tre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.core.io.ClassPathResource;

public class TestUtil {
  public static final String TEST_RESOURCES_FOLDER = "src/test/resources";

  public static String getTestResourceFilePath(String relativeFilePath) {
    return new ClassPathResource(TEST_RESOURCES_FOLDER + relativeFilePath).getPath();
  }

  public static String readTestResourceFile(String relativeFilePath) throws IOException {
    return Files.readString(Paths.get(TEST_RESOURCES_FOLDER + relativeFilePath));
  }
}
