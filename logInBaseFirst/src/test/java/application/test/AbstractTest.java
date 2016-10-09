package application.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import application.Application;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public abstract class AbstractTest {

}
