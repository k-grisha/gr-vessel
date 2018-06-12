package gr.prog.vessel.rest;

import gr.prog.vessel.dto.GuestDto;
import gr.prog.vessel.dto.MonthlyAggregationDto;
import gr.prog.vessel.dto.PortAggregationDto;
import gr.prog.vessel.dto.VesselAggregationDto;
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
													 @RequestParam Timestamp e) {
		return vesselService.getVesselAggregation(portId, imo, s, e);
	}

	@GetMapping(path = "/{portId}/monthAggregation")
	public MonthlyAggregationDto getMonthAggregation(@PathVariable Integer portId,
													 @RequestParam int y,
													 @RequestParam int m) {
		return vesselService.getMonthAggregation(portId, y, m);
	}
}
