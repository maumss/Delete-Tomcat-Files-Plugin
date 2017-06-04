package br.com.yahoo.mau_mss.deletetomcatfilesplugin;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>Title: DeleteTempActionTest</p>
 * <p>Description:  </p>
 * <p>Date: Jun 4, 2017, 12:06:10 PM</p>
 * @author Mauricio Soares da Silva (mauri)
 */
public class DeleteTempActionTest {

    public DeleteTempActionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

  /**
   * Test of mountTomcatBase method, of class DeleteTempAction.
   */
  @Test
  public void testMountTomcatBase() {
    System.out.println("mountTomcatBase");
    DeleteTempAction instance = new DeleteTempAction();
    instance.mountTomcatBase();
    System.out.println("Found Tomcat dir base at: " + instance.getBaseDir());
    assertTrue(instance.getBaseDir() != null && !instance.getBaseDir().isEmpty());
  }

}