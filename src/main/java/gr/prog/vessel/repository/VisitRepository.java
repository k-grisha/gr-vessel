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
	List<VesselVisit> findVisitsByPortIdAtTime(@Param("portId") Integer portId, @Param("timestamp") Timestamp timestamp);

	@Query("SELECT visit FROM VesselVisit visit WHERE " +
			"visit.portId = :portId AND visit.timeStarted > :fromTime AND visit.timeFinished < :toTime")
	List<VesselVisit> findVisitsByPortIdInPerriod(@Param("portId") Integer portId,
												  @Param("fromTime") Timestamp fromTime,
												  @Param("toTime") Timestamp toTime);

	@Query("SELECT visit FROM VesselVisit visit WHERE " +
			"visit.portId = :portId AND visit.imo = :imo AND " +
			"visit.timeStarted > :fromTime AND visit.timeFinished < :toTime")
	List<VesselVisit> findVisitsByPortIdImoInPeriod(@Param("portId") Integer portId,
													@Param("imo") Long imo,
													@Param("fromTime") Timestamp fromTime,
													@Param("toTime") Timestamp toTime);
}
