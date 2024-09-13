package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

/**
 * This class contains unit tests for the {@link IndividualProjectApplication} class.
 */
@SpringBootTest
@ContextConfiguration
public class IndividualProjectApplicationTests {

  private IndividualProjectApplication application;
  private ByteArrayOutputStream outputStream;
  private PrintStream originalOut;

  @MockBean
  private MyFileDatabase myFileDatabase;

  /**
   * Set up tests to redirect System.out to detect print statements later.
   */
  @BeforeEach
  public void setUp() {
    application = new IndividualProjectApplication();
    outputStream = new ByteArrayOutputStream();
    originalOut = System.out;
    System.setOut(new PrintStream(outputStream));
  }

  /**
   * Set the original System.out and close the open stream.
   */
  @AfterEach
  public void closeResources() {
    System.setOut(originalOut);
    try {
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testRunWithSetupArgument() {
    String[] args = {"setup"};
    IndividualProjectApplication spyApp = spy(application);
    spyApp.run(args);
    verify(spyApp).resetDataFile();
    assertNotNull(IndividualProjectApplication.myFileDatabase);
  }

  @Test
  public void testRunWithoutSetupArgument() {
    String[] args = {"start"};
    IndividualProjectApplication spyApp = spy(application);
    spyApp.run(args);
    verify(myFileDatabase, times(0)).setMapping(any(HashMap.class));
    assertNotNull(IndividualProjectApplication.myFileDatabase);
  }

  @Test
  public void testOverrideDatabase() {
    MyFileDatabase testDatabase = mock(MyFileDatabase.class);
    IndividualProjectApplication.overrideDatabase(testDatabase);
    assertEquals(testDatabase, IndividualProjectApplication.myFileDatabase);
  }

  @Test
  public void testResetDataFile() {
    IndividualProjectApplication spyApp = spy(application);
    spyApp.resetDataFile();
    verify(spyApp).resetDataFile();
    assertNotNull(IndividualProjectApplication.myFileDatabase.getDepartmentMapping());
  }

  @Test
  public void testOnTerminationWhenSaveDataIsTrue() {
    IndividualProjectApplication.saveData = true;
    IndividualProjectApplication app = new IndividualProjectApplication();
    app.onTermination();
    assertTrue(outputStream.toString().contains("Termination"));
  }

  @Test
  public void testOnTerminationWhenSaveDataIsFalse() {
    IndividualProjectApplication.saveData = false;
    IndividualProjectApplication app = new IndividualProjectApplication();
    app.onTermination();
    assertTrue(outputStream.toString().contains("Termination"));
    verify(myFileDatabase, times(0)).saveContentsToFile();
  }
}
