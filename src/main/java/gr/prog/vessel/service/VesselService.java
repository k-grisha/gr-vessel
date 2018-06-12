package gr.prog.vessel.service;

import gr.prog.vessel.dto.GuestDto;
import gr.prog.vessel.dto.VisitsAggregationDto;
import gr.prog.vessel.mapper.VesselVisitMapper;
import gr.prog.vessel.model.VesselVisit;
import gr.prog.vessel.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class VesselService {

	@Autowired
	VisitRepository visitRepository;

	public List<GuestDto> getVisitors(Integer portId, Timestamp timestamp) {
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdAtTime(portId, timestamp);
		return visits.stream().map(VesselVisitMapper.INSTANCE::map).collect(Collectors.toList());
	}

	public VisitsAggregationDto getAggregation(Integer portId, Timestamp fromTime, Timestamp toTime) {
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdInPerriod(portId, fromTime, toTime);
		Long uniqueVessels = visits.stream().filter(distinctByKey(VesselVisit::getImo)).count();
		LongSummaryStatistics statistics = visits.stream()
				.mapToLong(visit -> visit.getTimeFinished().getTime() - visit.getTimeStarted().getTime())
				.summaryStatistics();
		return new VisitsAggregationDto(uniqueVessels, statistics.getAverage(), statistics.getMin(), statistics.getMax());
	}

	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}


