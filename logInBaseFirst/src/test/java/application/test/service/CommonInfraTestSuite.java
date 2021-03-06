package application.test.service;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({
	   ClientOrganisationServiceTest.class,
	   UserServiceTest.class,
	   ToDoTaskServiceTest.class,
	   MessageServiceTest.class,
	   CurrentUserDetailsServiceTest.class,
	   EmailServiceTest.class
	})
public class CommonInfraTestSuite
{

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}