package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for the {@link Course} class.
 *
 * <p>The class sets up a test {@link Course} instance before running any tests </p>
 *
 * <p>Tests included:</p>
 * <ul>
 *   <li>{@link #toStringTest()} - Verifies that the {@link Course#toString()} method returns the
 *       expected string representation of the course.</li>
 * </ul>
 *
 */

@SpringBootTest
@ContextConfiguration
public class CourseUnitTests {

  /** The test course instance used for testing. */
  public static Course testCourse;

  @BeforeEach
  public void setupCourseForTesting() {
    testCourse = new Course("Griffin Newbold", "417 IAB", "11:40-12:55", 250);
  }

  @Test
  public void toStringTest() {
    String expectedResult = "\nInstructor: Griffin Newbold; Location: 417 IAB; Time: 11:40-12:55";
    assertEquals(expectedResult, testCourse.toString());
  }

  @Test
  public void testEnrollStudent() {
    testCourse.setEnrolledStudentCount(0);
    int oldStudentCount = testCourse.getEnrolledStudentCount();
    assertTrue(testCourse.enrollStudent());
    assertEquals(1, testCourse.getEnrolledStudentCount() - oldStudentCount);
  }

  @Test
  public void testFailedEnrollStudent() {
    testCourse.setEnrolledStudentCount(testCourse.getEnrollmentCapacity());
    assertFalse(testCourse.enrollStudent());
    assertEquals(testCourse.getEnrollmentCapacity(), testCourse.getEnrolledStudentCount());
  }

  @Test
  public void testSuccessfulDropStudent() {
    int oldStudentCount = testCourse.getEnrolledStudentCount();
    assertTrue(testCourse.dropStudent());
    assertEquals(1, oldStudentCount - testCourse.getEnrolledStudentCount());
  }

  @Test
  public void testFailedDropStudent() {
    testCourse.setEnrolledStudentCount(0);
    assertFalse(testCourse.dropStudent());
    assertEquals(0, testCourse.getEnrolledStudentCount());
  }

  @Test
  public void testReassignInstructor() {
    String oldInstructor = testCourse.getInstructorName();
    String newInstructor = "Brian Borowski";
    testCourse.reassignInstructor(newInstructor);
    assertEquals(newInstructor, testCourse.getInstructorName());
    assertNotEquals(testCourse.getInstructorName(), oldInstructor);
  }

  @Test
  public void testReassignLocation() {
    String oldLocation = testCourse.getCourseLocation();
    String newLocation = "833 MUD";
    testCourse.reassignLocation(newLocation);
    assertEquals(newLocation, testCourse.getCourseLocation());
    assertNotEquals(testCourse.getCourseLocation(), oldLocation);
  }

  @Test
  public void testReassignTime() {
    String oldTime = testCourse.getCourseTimeSlot();
    String newTime = "01:10-02:25";
    testCourse.reassignTime(newTime);
    assertEquals(newTime, testCourse.getCourseTimeSlot());
    assertNotEquals(testCourse.getCourseTimeSlot(), oldTime);
  }

  @Test
  public void testSetEnrolledStudentCount() {
    int oldEnrolledStudentCount = testCourse.getEnrolledStudentCount();
    int newStudentCount = oldEnrolledStudentCount + 5;
    testCourse.setEnrolledStudentCount(newStudentCount);
    assertEquals(newStudentCount, testCourse.getEnrolledStudentCount());
    assertNotEquals(testCourse.getEnrolledStudentCount(), oldEnrolledStudentCount);
  }

  @Test
  public void testIsCourseFull() {
    // test if course is full at capacity
    int capacity = testCourse.getEnrollmentCapacity();
    testCourse.setEnrolledStudentCount(capacity);
    assertTrue(testCourse.isCourseFull());

    // test if course is full over capacity
    capacity = testCourse.getEnrollmentCapacity();
    testCourse.setEnrolledStudentCount(capacity + 1);
    assertTrue(testCourse.isCourseFull());

    // test if course is full under capacity
    capacity = testCourse.getEnrollmentCapacity();
    testCourse.setEnrolledStudentCount(capacity - 1);
    assertFalse(testCourse.isCourseFull());
  }
}
