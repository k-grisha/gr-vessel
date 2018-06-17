package gr.prog.vessel.rest;

import gr.prog.vessel.dto.*;
import gr.prog.vessel.service.VesselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

//todo convert all datatime to UTC before request Zone gr.prog.vessel.Utils.convertToUTC(java.time.LocalDateTime, java.time.ZoneId)

@RestController
@RequestMapping("/rest/port")
public class VesselsController {

	@Autowired
	VesselService vesselService;

	/**
	 * A) List vessels that have been in a port at a specific time.
	 *
	 * @param portId port_id
	 * @param t      Timestamp
	 */
	@GetMapping(path = "/{portId}/guests")
	public List<GuestDto> getVisitors(@PathVariable Integer portId, @RequestParam Timestamp t) {
		return vesselService.getVisitors(portId, t);
	}

	/**
	 * B) Time-period summary / aggregation
	 *
	 * @param portId    Port-id
	 * @param s         Start time
	 * @param e         End time
	 * @param jpaMethod Type of data handle (Optional)
	 */
	@GetMapping(path = "/{portId}/aggregation")
	public PortAggregationDto getPortAggregation(@PathVariable Integer portId,
												 @RequestParam Timestamp s,
												 @RequestParam Timestamp e,
												 @RequestParam(defaultValue = "JPQL") JpaMethod jpaMethod) {
		switch (jpaMethod) {
			case STREAM:
				return vesselService.getPortAggregationByStream(portId, s, e);
			case JPQL:
				return vesselService.getPortAggregationByJpql(portId, s, e);
			case SQL:
				return vesselService.getPortAggregationBySql(portId, s, e);
		}
		throw new RuntimeException("Unknown JpaMethod method");
	}

	/**
	 * C)  Summary by vessel:
	 *
	 * @param portId    Port-id
	 * @param imo       IMO number
	 * @param s         Start time
	 * @param e         end time
	 * @param jpaMethod Type of data handle (Optional)
	 */
	@GetMapping(path = "/{portId}/vessel/{imo}/aggregation")
	public VesselAggregationDto getVesselAggregation(@PathVariable Integer portId,
													 @PathVariable Long imo,
													 @RequestParam Timestamp s,
													 @RequestParam Timestamp e,
													 @RequestParam(defaultValue = "JPQL") JpaMethod jpaMethod) {
		switch (jpaMethod) {
			case STREAM:
				return vesselService.getVesselAggregationByStream(portId, imo, s, e);
			case JPQL:
				return vesselService.getVesselAggregationByJpql(portId, imo, s, e);
			case SQL:
				return vesselService.getVesselAggregationBySql(portId, imo, s, e);
		}
		throw new RuntimeException("Unknown JpaMethod method");
	}

	/**
	 * D) Monthly summary
	 *
	 * @param portId Port-id
	 * @param y      Year
	 * @param m      Month
	 */
	@GetMapping(path = "/{portId}/monthAggregation")
	public MonthlyAggregationDto getMonthAggregation(@PathVariable Integer portId,
													 @RequestParam int y,
													 @RequestParam int m) {
		return vesselService.getMonthAggregation(portId, y, m);
	}

}
