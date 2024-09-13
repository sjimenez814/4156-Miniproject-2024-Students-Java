package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for the {@link MyFileDatabase} class.
 */
@SpringBootTest
@ContextConfiguration
public class MyFileDatabaseTests {

  private MyFileDatabase myFileDatabase;
  private String filePath = "testdata.txt";

  /**
  * Deletes the test file if it exists and creates a new {@link MyFileDatabase}
  * with the filePath before each test.
  */
  @BeforeEach
  public void setUp() {
    File file = new File(filePath);
    if (file.exists()) {
      file.delete();
    }
    myFileDatabase = new MyFileDatabase(0, filePath);
  }

  @Test
  public void testConstructorWithFlagZero() {
    myFileDatabase = new MyFileDatabase(0, filePath);
    assertNull(myFileDatabase.getDepartmentMapping());
  }

  @Test
  public void testSetMapping() {
    Map<String, Department> mapping = new HashMap<>();
    mapping.put("COMS", new Department("COMS", null, "Brian Borowski", 100));
    myFileDatabase.setMapping((HashMap<String, Department>) mapping);
    assertNotNull(myFileDatabase.getDepartmentMapping());
    assertEquals(1, myFileDatabase.getDepartmentMapping().size());
    assertTrue(myFileDatabase.getDepartmentMapping().containsKey("COMS"));
  }

  @Test
  public void testDeSerializeObjectFromFile() throws IOException {
    Map<String, Department> mapping = new HashMap<>();
    mapping.put("COMS", new Department("COMS", null, "Brian Borowski", 100));
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
      out.writeObject(mapping);
    }
    MyFileDatabase db = new MyFileDatabase(0, filePath);
    Map<String, Department> deserializedMapping = db.deSerializeObjectFromFile();
    assertNotNull(deserializedMapping);
    assertEquals(1, deserializedMapping.size());
    assertTrue(deserializedMapping.containsKey("COMS"));
  }

  @Test
  public void testSaveContentsToFile() throws IOException {
    Map<String, Department> mapping = new HashMap<>();
    mapping.put("COMS", new Department("COMS", null, "Dr. Smith", 100));
    myFileDatabase.setMapping((HashMap<String, Department>) mapping);
    myFileDatabase.saveContentsToFile();
    File file = new File(filePath);
    assertTrue(file.exists());
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
      Map<String, Department> fileMapping = (HashMap<String, Department>) in.readObject();
      assertNotNull(fileMapping);
      assertEquals(1, fileMapping.size());
      assertTrue(fileMapping.containsKey("COMS"));
    } catch (ClassNotFoundException e) {
      throw new IOException();
    }
  }

  @Test
  public void testToString() {
    Map<String, Department> mapping = getStringDepartmentHashMap();
    myFileDatabase.setMapping((HashMap<String, Department>) mapping);

    String result = myFileDatabase.toString();
    assertEquals("For the COMS department: \n"
        + "COMS 1004: \n"
        + "Instructor: Adam Cannon; Location: 417 IAB; Time: 10:10-11:25\n"
        + "COMS 3134: \n"
        + "Instructor: Brian Borowski; Location: 417 IAB; Time: 2:40-3:55\n",
        result);
  }

  private static Map<String, Department> getStringDepartmentHashMap() {
    Map<String, Department> mapping = new HashMap<>();
    Course coms1004 = new Course("Adam Cannon", "417 IAB", "10:10-11:25", 400);
    coms1004.setEnrolledStudentCount(249);
    Course coms3134 = new Course("Brian Borowski", "417 IAB", "2:40-3:55", 250);
    coms3134.setEnrolledStudentCount(242);
    Map<String, Course> courses = new HashMap<>();
    courses.put("1004", coms1004);
    courses.put("3134", coms3134);
    Department dept = new Department("COMS", courses, "Jae Lee", 100);
    mapping.put("COMS", dept);
    return mapping;
  }
}
