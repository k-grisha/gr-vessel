package gr.prog.vessel.rest;

import gr.prog.vessel.dto.*;
import gr.prog.vessel.service.VesselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/rest/port")
public class VesselsController {

	@Autowired
	VesselService vesselService;

	@GetMapping(path = "/{portId}/guests")
	public List<GuestDto> getVisitors(@PathVariable Integer portId, @RequestParam Timestamp t) {
		return vesselService.getVisitors(portId, t);
	}


	@GetMapping(path = "/{portId}/aggregation")
	public PortAggregationDto getPortAggregation(@PathVariable Integer portId,
												 @RequestParam Timestamp s,
												 @RequestParam Timestamp e) {
		return vesselService.getPortAggregation(portId, s, e);
	}

	@GetMapping(path = "/{portId}/vessel/{imo}/aggregation")
	public VesselAggregationDto getVesselAggregation(@PathVariable Integer portId,
													 @PathVariable Long imo,
													 @RequestParam Timestamp s,
													 @RequestParam Timestamp e,
													 @RequestParam(defaultValue = "STREAM") JpaMethod jpaMethod) {
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

	@GetMapping(path = "/{portId}/monthAggregation")
	public MonthlyAggregationDto getMonthAggregation(@PathVariable Integer portId,
													 @RequestParam int y,
													 @RequestParam int m) {
		return vesselService.getMonthAggregation(portId, y, m);
	}

}
