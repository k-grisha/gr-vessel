package gr.prog.vessel.rest;

import gr.prog.vessel.dto.GuestDto;
import gr.prog.vessel.service.VesselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/rest/vessel")
public class VesselsController {

	@Autowired
	VesselService vesselService;

	@GetMapping(path = "/{portId}")
	public List<GuestDto> getVisitors(@PathVariable Integer portId, @RequestParam Timestamp t) {
		return vesselService.getVisitors(portId, t);
	}

}
