package gr.prog.vessel.repository;

import gr.prog.vessel.dto.PortAggregationDto;
import gr.prog.vessel.dto.VesselAggregationDto;
import gr.prog.vessel.model.VesselVisit;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface VisitRepository extends CrudRepository<VesselVisit, Long> {

	List<VesselVisit> findAll();

	/**
	 * Task A
	 */
	@Query("SELECT visit FROM VesselVisit visit " +
			"WHERE visit.portId = :portId AND :timestamp BETWEEN visit.timeStarted AND visit.timeFinished")
	List<VesselVisit> findVisitsByPortIdAtTime(@Param("portId") Integer portId, @Param("timestamp") Timestamp timestamp);

	/**
	 * Task B by java stream
	 */
	@Query("SELECT visit FROM VesselVisit visit WHERE " +
			"visit.portId = :portId AND visit.timeStarted >= :fromTime AND visit.timeFinished < :toTime")
	List<VesselVisit> findVisitsByPortIdInPeriod(@Param("portId") Integer portId,
												 @Param("fromTime") Timestamp fromTime,
												 @Param("toTime") Timestamp toTime);

	/**
	 * Task B by JPQL
	 */
	@Query("SELECT new gr.prog.vessel.dto.PortAggregationDto( COUNT(DISTINCT visit.imo), AVG(visit.durationSec), MIN(visit.durationSec), MAX(visit.durationSec)) " +
			"FROM VesselVisit visit " +
			"WHERE visit.portId = :portId AND visit.timeStarted >= :fromTime AND visit.timeFinished < :toTime")
	PortAggregationDto getPortAggregationByJpql(@Param("portId") Integer portId,
												@Param("fromTime") Timestamp fromTime,
												@Param("toTime") Timestamp toTime);
	/**
	 * Task B by SQL
	 */
	@Query(value = "SELECT COUNT(DISTINCT imo), AVG(duration_sec), MIN(duration_sec), MAX(duration_sec) " +
			"FROM vessel_visit " +
			"WHERE port_id = ?1 AND time_started >= ?2 AND time_finished < ?3", nativeQuery = true)
	List<Object[]> getPortAggregationBySql(Integer portId, Timestamp fromTime, Timestamp toTime);

	/**
	 * Task C by java stream
	 */
	@Query("SELECT visit FROM VesselVisit visit WHERE " +
			"visit.portId = :portId AND visit.imo = :imo AND " +
			"visit.timeStarted >= :fromTime AND visit.timeFinished < :toTime")
	List<VesselVisit> findVisitsByPortIdImoInPeriod(@Param("portId") Integer portId,
													@Param("imo") Long imo,
													@Param("fromTime") Timestamp fromTime,
													@Param("toTime") Timestamp toTime);

	/**
	 * Task C by JPQL
	 */
	@Query("SELECT new gr.prog.vessel.dto.VesselAggregationDto(" +
			"COUNT(visit), AVG(visit.durationSec), MIN(visit.durationSec), MAX (visit.durationSec), MIN(visit.timeStarted), MAX(visit.timeStarted)) " +
			"FROM VesselVisit visit " +
			"WHERE visit.portId = :portId AND visit.imo = :imo " +
			"AND visit.timeStarted >= :fromTime AND visit.timeFinished < :toTime")
	VesselAggregationDto getVisitAggregationByJpql(@Param("portId") Integer portId,
												   @Param("imo") Long imo,
												   @Param("fromTime") Timestamp fromTime,
												   @Param("toTime") Timestamp toTime);
	/**
	 * Task C by SQL
	 */
	@Query(value = "SELECT COUNT(*), AVG(duration_sec), MIN(duration_sec), MAX (duration_sec), MIN(time_started) as min2, MAX(time_started) max2 " +
			"FROM vessel_visit " +
			"WHERE port_id = ?1 AND imo = ?2 " +
			"AND time_started >= ?3 AND time_finished < ?4", nativeQuery = true)
	List<Object[]> getVisitAggregationBySql(Integer portId, Long imo, Timestamp fromTime, Timestamp toTime);

	/**
	 * Task D
	 */
	@Query(value = "SELECT new gr.prog.vessel.repository.ArrivalsStatistic(COUNT(visit), COUNT (DISTINCT visit.imo)) " +
			"FROM VesselVisit visit " +
			"WHERE visit.portId = :portId AND visit.timeStarted >= :fromTime AND visit.timeStarted < :toTime")
	ArrivalsStatistic getArrivalStatistic(@Param("portId") Integer portId,
										  @Param("fromTime") Timestamp fromTime,
										  @Param("toTime") Timestamp toTime);

}
