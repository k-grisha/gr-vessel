package gr.prog.vessel.service;

import gr.prog.vessel.dto.GuestDto;
import gr.prog.vessel.mapper.VesselVisitMapper;
import gr.prog.vessel.model.VesselVisit;
import gr.prog.vessel.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VesselService {

	@Autowired
	VisitRepository visitRepository;

	@Transactional
	public List<GuestDto> getVisitors(Integer portId, Timestamp timestamp) {
		List<VesselVisit> visitors = visitRepository.findGuestByPortIdAndTime(portId, timestamp);
		return visitors.stream().map(VesselVisitMapper.INSTANCE::map).collect(Collectors.toList());
	}
}
