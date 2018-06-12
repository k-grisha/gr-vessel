package gr.prog.vessel.service;

import gr.prog.vessel.dto.GuestDto;
import gr.prog.vessel.dto.MonthlyAggregationDto;
import gr.prog.vessel.dto.PortAggregationDto;
import gr.prog.vessel.dto.VesselAggregationDto;
import gr.prog.vessel.mapper.VesselVisitMapper;
import gr.prog.vessel.model.VesselVisit;
import gr.prog.vessel.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
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

	public PortAggregationDto getPortAggregation(Integer portId, Timestamp fromTime, Timestamp toTime) {
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdInPeriod(portId, fromTime, toTime);
		Long uniqueVessels = visits.stream().filter(distinctByKey(VesselVisit::getImo)).count();
		LongSummaryStatistics statistics = visits.stream()
				.mapToLong(visit -> visit.getTimeFinished().getTime() - visit.getTimeStarted().getTime())
				.summaryStatistics();
		return new PortAggregationDto(uniqueVessels, statistics.getAverage(), statistics.getMin(), statistics.getMax());
	}

	public VesselAggregationDto getVesselAggregation(Integer portId, Long imo, Timestamp fromTime, Timestamp toTime) {
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdImoInPeriod(portId, imo, fromTime, toTime);
		LongSummaryStatistics timeInPortStatistics = visits.stream()
				.mapToLong(visit -> visit.getTimeFinished().getTime() - visit.getTimeStarted().getTime())
				.summaryStatistics();
		LongSummaryStatistics visitStatistics = visits.stream().mapToLong(visit -> visit.getTimeFinished().getTime())
				.summaryStatistics();
		return new VesselAggregationDto(visits.size(),
				timeInPortStatistics.getAverage(),
				timeInPortStatistics.getMin(),
				timeInPortStatistics.getMax(),
				new Timestamp(visitStatistics.getMin()),
				new Timestamp(visitStatistics.getMax()));
	}

	public MonthlyAggregationDto getMonthAggregation(Integer portId, int year, int month) {
		Timestamp from = Timestamp.valueOf(LocalDate.of(year, month, 1).atStartOfDay());
		Timestamp to = Timestamp.valueOf(LocalDate.of(year, month + 1, 1).atStartOfDay());
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdInPeriod(portId, from, to);
		Double avgDuration = visits.stream()
				.mapToLong(visit -> visit.getTimeFinished().getTime() - visit.getTimeStarted().getTime())
				.average().orElse(0.0);
		Double sumOfLength = visits.stream().mapToDouble(VesselVisit::getLength).sum();
		List<VesselVisit> arrivals = visitRepository.findArrivalsByPortIdInPeriod(portId, from, to);
		Integer uniqueArrivals = (int) arrivals.stream().filter(distinctByKey(VesselVisit::getImo)).count();
		return new MonthlyAggregationDto(arrivals.size(), uniqueArrivals, avgDuration, sumOfLength);
	}


	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}


