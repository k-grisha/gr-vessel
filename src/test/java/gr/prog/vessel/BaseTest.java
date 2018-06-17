package gr.prog.vessel;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import gr.prog.vessel.repository.VisitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.sql.DataSource;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = GrVesselApplication.class)
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
//		DirtiesContextTestExecutionListener.class,
//		TransactionalTestExecutionListener.class,
//		DbUnitTestExecutionListener.class})
//@DatabaseSetup("/sampleData.xml")
//@ActiveProfiles(profiles = "test")
public class BaseTest {

	@Autowired
	VisitRepository visitRepository;

	@Test
	public void base(){
		System.out.println("123");
	}
}
