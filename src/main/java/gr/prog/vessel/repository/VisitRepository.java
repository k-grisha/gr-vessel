package gr.prog.vessel.repository;

import gr.prog.vessel.model.VesselVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<VesselVisit, Long> {

	@Query("SELECT visit FROM VesselVisit visit WHERE " +
			"visit.portId = :portId AND visit.timeStarted < :timestamp AND visit.timeFinished > :timestamp")
	List<VesselVisit> findGuestByPortIdAndTime(@Param("portId") Integer portId, @Param("timestamp") Timestamp timestamp);
}
