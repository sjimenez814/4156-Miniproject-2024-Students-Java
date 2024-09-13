package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for the {@link RouteController} class.
 *
 */
@SpringBootTest
@ContextConfiguration
public class RouteControllerTests {

  /** The test course instance used for testing. */
  public static RouteController testRouteController;

  @BeforeEach
  public void setupRouteControllerForTesting() {
    testRouteController = new RouteController();
  }

  @Test
  public void testIndex() {
    String expectedResult = "Welcome, in order to make an API call direct "
        + "your browser or Postman to an endpoint "
        + "\n\n This can be done using the following format: \n\n http:127.0.0"
        + ".1:8080/endpoint?arg=value";
    assertEquals(expectedResult, testRouteController.index());
  }

  @Test
  public void testRetrieveDepartment() {
    // retrieve a non existing department
    ResponseEntity<?> testResponse = testRouteController.retrieveDepartment("N/A");
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Department Not Found", testResponse.getBody());

    // retrieve an existing department
    testResponse = testRouteController.retrieveDepartment("COMS");
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("COMS 3827: \n"
            + "Instructor: Daniel Rubenstein; Location: 207 Math; Time: 10:10-11:25\n"
            + "COMS 1004: \n"
            + "Instructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55\n"
            + "COMS 3203: \n"
            + "Instructor: Ansaf Salleb-Aouissi; Location: 301 URIS; Time: 10:10-11:25\n"
            + "COMS 4156: \n"
            + "Instructor: Gail Kaiser; Location: 501 NWC; Time: 10:10-11:25\n"
            + "COMS 3157: \n"
            + "Instructor: Jae Lee; Location: 417 IAB; Time: 4:10-5:25\n"
            + "COMS 3134: \n"
            + "Instructor: Brian Borowski; Location: 301 URIS; Time: 4:10-5:25\n"
            + "COMS 3251: \n"
            + "Instructor: Tony Dear; Location: 402 CHANDLER; Time: 1:10-3:40\n"
            + "COMS 3261: \n"
            + "Instructor: Josh Alman; Location: 417 IAB; Time: 2:40-3:55\n",
            testResponse.getBody()
    );
  }

  @Test
  public void testRetrieveCourse() {
    // retrieve a non existing department
    ResponseEntity<?> testResponse = testRouteController.retrieveCourse("N/A", 9999);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, testResponse.getStatusCode());
    assertEquals("An Error has occurred", testResponse.getBody());

    // retrieve an null department
    testResponse = testRouteController.retrieveCourse(null, 4156);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Department Not Found", testResponse.getBody());

    // retrieve non-existing course
    testResponse = testRouteController.retrieveCourse("COMS", 9999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course and department
    testResponse = testRouteController.retrieveCourse("COMS", 1004);
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals(
            "\nInstructor: Adam Cannon; Location: 417 IAB; Time: 11:40-12:55",
            testResponse.getBody()
    );
  }

  @Test
  public void testIsCourseFull() {
    // retrieve a non existing course
    ResponseEntity<?> testResponse = testRouteController.isCourseFull("COMS", 9999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course and department
    testResponse = testRouteController.isCourseFull("COMS", 1004);
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals(false, testResponse.getBody());
  }

  @Test
  public void testGetMajorCtFromDept() {
    // retrieve a non existing dept
    ResponseEntity<?> testResponse = testRouteController.getMajorCtFromDept("N/A");
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, testResponse.getStatusCode());
    assertEquals("An Error has occurred", testResponse.getBody());

    // retrieve a null dept
    testResponse = testRouteController.getMajorCtFromDept(null);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Department Not Found", testResponse.getBody());

    // retrieve a real department
    testResponse = testRouteController.getMajorCtFromDept("COMS");
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("There are: 2700 majors in the department", testResponse.getBody());
  }

  @Test
  public void testIdentifyDeptChair() {
    // retrieve a non existing dept
    ResponseEntity<?> testResponse = testRouteController.identifyDeptChair("N/A");
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, testResponse.getStatusCode());
    assertEquals("An Error has occurred", testResponse.getBody());

    // retrieve a null dept
    testResponse = testRouteController.identifyDeptChair(null);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Department Not Found", testResponse.getBody());

    // retrieve a real department
    testResponse = testRouteController.identifyDeptChair("COMS");
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Luca Carloni is the department chair.", testResponse.getBody());
  }

  @Test
  public void testFindCourseLocation() {
    // retrieve a null dept
    ResponseEntity<?> testResponse = testRouteController.findCourseLocation(null, 999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real department but invalid course
    testResponse = testRouteController.findCourseLocation("COMS", 999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course
    testResponse = testRouteController.findCourseLocation("COMS", 1004);
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("417 IAB is where the course is located.", testResponse.getBody());
  }

  @Test
  public void testFindCourseInstructor() {
    // retrieve a null dept
    ResponseEntity<?> testResponse = testRouteController.findCourseInstructor(null, 999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real department but invalid course
    testResponse = testRouteController.findCourseInstructor("COMS", 999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course
    testResponse = testRouteController.findCourseInstructor("COMS", 1004);
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Adam Cannon is the instructor for the course.", testResponse.getBody());
  }

  @Test
  public void testFindCourseTime() {
    // retrieve a null dept
    ResponseEntity<?> testResponse = testRouteController.findCourseTime(null, 999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real department but invalid course
    testResponse = testRouteController.findCourseTime("COMS", 999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course
    testResponse = testRouteController.findCourseTime("COMS", 1004);
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("The course meets at: 11:40-12:55", testResponse.getBody());
  }

  @Test
  public void testAddMajorToDept() {
    // retrieve a non-existing dept
    ResponseEntity<?> testResponse = testRouteController.addMajorToDept("N/A");
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, testResponse.getStatusCode());
    assertEquals("An Error has occurred", testResponse.getBody());

    // retrieve a null dept
    testResponse = testRouteController.addMajorToDept(null);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Department Not Found", testResponse.getBody());

    // retrieve a real department
    testResponse = testRouteController.addMajorToDept("COMS");
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Attribute was updated successfully", testResponse.getBody());
    int oldMajorCount = 2700;
    HashMap<String, Department> departmentMapping;
    departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
    Department specifiedDept = departmentMapping.get("COMS");
    assertEquals(oldMajorCount + 1, specifiedDept.getNumberOfMajors());

    // remove major to undo add
    testRouteController.removeMajorFromDept("COMS");
    assertEquals(oldMajorCount, specifiedDept.getNumberOfMajors());

  }

  @Test
  public void testRemoveMajorToDept() {
    // retrieve a non-existing dept
    ResponseEntity<?> testResponse = testRouteController.removeMajorFromDept("N/A");
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, testResponse.getStatusCode());
    assertEquals("An Error has occurred", testResponse.getBody());

    // retrieve a null dept
    testResponse = testRouteController.removeMajorFromDept(null);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Department Not Found", testResponse.getBody());

    // retrieve a real department
    testResponse = testRouteController.removeMajorFromDept("COMS");
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Attribute was updated or is at minimum", testResponse.getBody());
    int oldMajorCount = 2700;
    HashMap<String, Department> departmentMapping;
    departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
    Department specifiedDept = departmentMapping.get("COMS");
    assertEquals(oldMajorCount - 1, specifiedDept.getNumberOfMajors());

    // remove major to undo add
    testRouteController.addMajorToDept("COMS");
    assertEquals(oldMajorCount, specifiedDept.getNumberOfMajors());

  }

  @Test
  public void testDropStudent() {
    // retrieve a null dept
    ResponseEntity<?> testResponse = testRouteController.dropStudent(null, 999);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course
    HashMap<String, Department> departmentMapping;
    departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
    HashMap<String, Course> coursesMapping;
    coursesMapping = departmentMapping.get("COMS").getCourseSelection();
    Course requestedCourse = coursesMapping.get(Integer.toString(1004));
    int oldStudentCount = requestedCourse.getEnrolledStudentCount();

    // test dropping a student
    testResponse = testRouteController.dropStudent("COMS", 1004);
    assertEquals(oldStudentCount - 1, requestedCourse.getEnrolledStudentCount());
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Student has been dropped.", testResponse.getBody());

    // test failed drop student
    requestedCourse.setEnrolledStudentCount(0);
    testResponse = testRouteController.dropStudent("COMS", 1004);
    assertEquals(HttpStatus.BAD_REQUEST, testResponse.getStatusCode());
    assertEquals("Student has not been dropped.", testResponse.getBody());
    assertEquals(0, requestedCourse.getEnrolledStudentCount());

    // undo changes
    requestedCourse.setEnrolledStudentCount(oldStudentCount);
    assertEquals(oldStudentCount, requestedCourse.getEnrolledStudentCount());
  }

  @Test
  public void testSetEnrollmentCount() {
    // retrieve a null dept
    ResponseEntity<?> testResponse = testRouteController.setEnrollmentCount(null, 999, 0);
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course
    HashMap<String, Department> departmentMapping;
    departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
    HashMap<String, Course> coursesMapping;
    coursesMapping = departmentMapping.get("COMS").getCourseSelection();
    Course requestedCourse = coursesMapping.get(Integer.toString(1004));
    int oldStudentCount = requestedCourse.getEnrolledStudentCount();

    // test setEnrollmentCount()
    int newStudentCount = 0;
    testResponse = testRouteController.setEnrollmentCount("COMS", 1004, newStudentCount);
    assertNotEquals(oldStudentCount, requestedCourse.getEnrolledStudentCount());
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Attribute was updated successfully.", testResponse.getBody());
    assertEquals(newStudentCount, requestedCourse.getEnrolledStudentCount());

    // undo changes
    requestedCourse.setEnrolledStudentCount(oldStudentCount);
    assertEquals(oldStudentCount, requestedCourse.getEnrolledStudentCount());
  }

  @Test
  public void testChangeCourseTime() {
    // retrieve a null dept
    ResponseEntity<?> testResponse = testRouteController.changeCourseTime(null, 999, "N/A");
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course
    HashMap<String, Department> departmentMapping;
    departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
    HashMap<String, Course> coursesMapping;
    coursesMapping = departmentMapping.get("COMS").getCourseSelection();
    Course requestedCourse = coursesMapping.get(Integer.toString(1004));
    String oldCourseTime = requestedCourse.getCourseTimeSlot();

    // test changeCourseTime()
    String newCourseTime = "08:40-09:55";
    testResponse = testRouteController.changeCourseTime("COMS", 1004, newCourseTime);
    assertNotEquals(oldCourseTime, requestedCourse.getCourseTimeSlot());
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Attribute was updated successfully.", testResponse.getBody());
    assertEquals(newCourseTime, requestedCourse.getCourseTimeSlot());

    // undo changes
    requestedCourse.reassignTime(oldCourseTime);
    assertEquals(oldCourseTime, requestedCourse.getCourseTimeSlot());
  }

  @Test
  public void testChangeCourseTeacher() {
    // retrieve a null dept
    ResponseEntity<?> testResponse = testRouteController.changeCourseTeacher(null, 999, "N/A");
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course

    HashMap<String, Department> departmentMapping;
    departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
    HashMap<String, Course> coursesMapping;
    coursesMapping = departmentMapping.get("COMS").getCourseSelection();
    Course requestedCourse = coursesMapping.get(Integer.toString(1004));
    String oldTeacher = requestedCourse.getInstructorName();

    // test changeCourseTime()
    String newTeacher = "Janine Teagues";
    testResponse = testRouteController.changeCourseTeacher("COMS", 1004, newTeacher);
    assertNotEquals(oldTeacher, requestedCourse.getInstructorName());
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Attribute was updated successfully.", testResponse.getBody());
    assertEquals(newTeacher, requestedCourse.getInstructorName());

    // undo changes
    requestedCourse.reassignInstructor(oldTeacher);
    assertEquals(oldTeacher, requestedCourse.getInstructorName());
  }

  @Test
  public void testChangeCourseLocation() {
    // retrieve a null dept
    ResponseEntity<?> testResponse = testRouteController.changeCourseLocation(null, 999, "N/A");
    assertEquals(HttpStatus.NOT_FOUND, testResponse.getStatusCode());
    assertEquals("Course Not Found", testResponse.getBody());

    // retrieve a real course
    HashMap<String, Department> departmentMapping;
    departmentMapping = IndividualProjectApplication.myFileDatabase.getDepartmentMapping();
    HashMap<String, Course> coursesMapping;
    coursesMapping = departmentMapping.get("COMS").getCourseSelection();
    Course requestedCourse = coursesMapping.get(Integer.toString(1004));
    String oldLocation = requestedCourse.getCourseLocation();

    // test changeCourseTime()
    String newLocation = "609 HAM";
    testResponse = testRouteController.changeCourseLocation("COMS", 1004, newLocation);
    assertNotEquals(oldLocation, requestedCourse.getCourseLocation());
    assertEquals(HttpStatus.OK, testResponse.getStatusCode());
    assertEquals("Attribute was updated successfully.", testResponse.getBody());
    assertEquals(newLocation, requestedCourse.getCourseLocation());

    // undo changes
    requestedCourse.reassignLocation(oldLocation);
    assertEquals(oldLocation, requestedCourse.getCourseLocation());
  }

}
