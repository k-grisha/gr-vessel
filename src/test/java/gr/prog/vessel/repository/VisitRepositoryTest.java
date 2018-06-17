package gr.prog.vessel.repository;

import gr.prog.vessel.model.VesselVisit;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("Test")
public class VisitRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;
	@Autowired
	private VisitRepository visitRepository;

	//	@Before
	public void before() {
		entityManager.persistAndFlush(new VesselVisitBuilder().build());
	}

	@Test
	public void maxTest() {
//		entityManager.persistAndFlush(new VesselVisitBuilder().setLength(10.0).setImo(1111L).build());
//		entityManager.persistAndFlush(new VesselVisitBuilder().setLength(15.0).setImo(1111L).build());
		entityManager.persistAndFlush(new VesselVisitBuilder().setLength(17.0).setImo(2222L).build());
		entityManager.persistAndFlush(new VesselVisitBuilder().setLength(3.0).setImo(1111L)
				.setTimeFinished(Timestamp.valueOf(LocalDateTime.now().plusHours(40).plusMinutes(3000))).build());
		entityManager.persistAndFlush(new VesselVisitBuilder().setLength(1000.0).setImo(1111L)
				.setTimeFinished(Timestamp.valueOf(LocalDateTime.now().plusYears(1))).build());



		ArrivalsStatistic result = visitRepository.getArrivalStatistic(2,
				Timestamp.valueOf(LocalDateTime.now().minusMonths(1)),
				Timestamp.valueOf(LocalDateTime.now().plusMonths(1)));
		System.out.println(result);
	}

	@Test
	public void base1() {
		List<VesselVisit> vesselVisits = visitRepository.findVisitsByPortIdAtTime(2, Timestamp.valueOf(LocalDateTime.now()));
		System.out.println(vesselVisits);
	}

	@Test
	public void base2() {
		List<VesselVisit> vesselVisits = visitRepository.findVisitsByPortIdAtTime(2, Timestamp.valueOf(LocalDateTime.now()));
		System.out.println(vesselVisits);
	}


	/**
	 * Test VesselVisit builder
	 */
	@Setter
	@Accessors(chain = true)
	class VesselVisitBuilder {
		private String name = UUID.randomUUID().toString();
		private Long imo = new Random().nextLong();
		private Double length = 300.1;
		private Integer portId = 2;
		private Timestamp timeStarted = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
		private Timestamp timeFinished = Timestamp.valueOf(LocalDateTime.now().plusDays(1));

		public VesselVisit build() {
			VesselVisit vesselVisit = new VesselVisit();
			vesselVisit.setName(name);
			vesselVisit.setImo(imo);
			vesselVisit.setLength(length);
			vesselVisit.setPortId(portId);
			vesselVisit.setTimeStarted(timeStarted);
			vesselVisit.setTimeFinished(timeFinished);
			return vesselVisit;
		}
	}
}