package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for the {@link Department} class.
 *
 * <p>The class sets up a test {@link Department} instance before running any tests </p>
 *
 */

@SpringBootTest
@ContextConfiguration
public class DepartmentUnitTests {

  /** The test department instance used for testing. */
  private Department testDepartment;

  /**
  * Sets up a {@link Department} instance with predefined {@link Course} objects for testing.
  *
  */
  @BeforeEach
  public void setupDepartmentForTesting() {
    Course coms1004 = new Course("Adam Cannon", "417 IAB", "11:40-12:55", 400);
    coms1004.setEnrolledStudentCount(249);

    Course coms3134 = new Course("Brian Borowski", "417 IAB", "11:40-12:55", 250);
    coms3134.setEnrolledStudentCount(242);

    Map<String, Course> testCourses = new HashMap<>();
    testCourses.put("1004", coms1004);
    testCourses.put("3134", coms3134);

    testDepartment = new Department("COMS", testCourses, "Luca Carloni", 2700);
  }

  @Test
  public void toStringTest() {
    String expectedResult = "COMS 1004: \n"
            + "Instructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55\n"
            + "COMS 3134: \n"
            + "Instructor: Brian Borowski; Location: 417 IAB; Time: 11:40-12:55\n";
    assertEquals(expectedResult, testDepartment.toString());
  }

  @Test
  public void testDropPersonToMajor() {
    int oldNumMajors = testDepartment.getNumberOfMajors();
    testDepartment.dropPersonFromMajor();
    assertTrue(testDepartment.getNumberOfMajors() < oldNumMajors);
    assertEquals(-1, testDepartment.getNumberOfMajors() - oldNumMajors);

    // test dropPersonFromMajor() when number of majors is zero
    testDepartment = new Department("COMS", null, "Luca Carloni", 0);
    testDepartment.dropPersonFromMajor();
    assertEquals(0, testDepartment.getNumberOfMajors());
  }

  @Test
  public void testAddPersonToMajor() {
    int oldNumMajors = testDepartment.getNumberOfMajors();
    testDepartment.addPersonToMajor();
    assertTrue(testDepartment.getNumberOfMajors() > oldNumMajors);
    assertEquals(1, testDepartment.getNumberOfMajors() - oldNumMajors);
  }

  @Test
  public void testAddCourse() {
    Map<String, Course> oldCourseSelection = new HashMap<>(testDepartment.getCourseSelection());
    testDepartment.addCourse("3157", new Course("Jae Lee", "417 IAB", "11:40-12:55", 400));
    assertNotEquals(oldCourseSelection, testDepartment.getCourseSelection());
    assertEquals(oldCourseSelection.size() + 1, testDepartment.getCourseSelection().size());
  }

  @Test
  public void testCreateCourse() {
    Map<String, Course> oldCourseSelection = new HashMap<>(testDepartment.getCourseSelection());
    testDepartment.createCourse("3157", "Jae Lee", "417 IAB", "11:40-12:55", 400);
    assertNotEquals(oldCourseSelection, testDepartment.getCourseSelection());
    assertEquals(oldCourseSelection.size() + 1, testDepartment.getCourseSelection().size());
  }

  @Test
  public void testGetDepartmentChair() {
    assertEquals("Luca Carloni", testDepartment.getDepartmentChair());
  }

}
