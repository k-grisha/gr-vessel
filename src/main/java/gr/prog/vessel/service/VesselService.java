package gr.prog.vessel.service;

import gr.prog.vessel.dto.GuestDto;
import gr.prog.vessel.dto.MonthlyAggregationDto;
import gr.prog.vessel.dto.PortAggregationDto;
import gr.prog.vessel.dto.VesselAggregationDto;
import gr.prog.vessel.mapper.VesselVisitMapper;
import gr.prog.vessel.model.VesselVisit;
import gr.prog.vessel.repository.ArrivalsStatistic;
import gr.prog.vessel.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
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
	private VisitRepository visitRepository;

	public List<GuestDto> getVisitors(Integer portId, Timestamp timestamp) {
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdAtTime(portId, timestamp);
		return visits.stream().map(VesselVisitMapper.INSTANCE::map).collect(Collectors.toList());
	}

	public PortAggregationDto getPortAggregationByStream(Integer portId, Timestamp fromTime, Timestamp toTime) {
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdInPeriod(portId, fromTime, toTime);
		Long uniqueVessels = visits.stream().filter(distinctByKey(VesselVisit::getImo)).count();
		LongSummaryStatistics statistics = visits.stream()
				.mapToLong(VesselVisit::getDurationSec)
				.summaryStatistics();
		return new PortAggregationDto(uniqueVessels,
				statistics.getAverage(),
				statistics.getMin(),
				statistics.getMax());
	}

	public PortAggregationDto getPortAggregationByJpql(Integer portId, Timestamp fromTime, Timestamp toTime) {
		return visitRepository.getPortAggregationByJpql(portId, fromTime, toTime);
	}

	public PortAggregationDto getPortAggregationBySql(Integer portId, Timestamp fromTime, Timestamp toTime) {
		Object[] result = visitRepository.getPortAggregationBySql(portId, fromTime, toTime).get(0);
		return new PortAggregationDto(((BigInteger) result[0]).longValue(),
				((BigDecimal) result[1]).doubleValue(),
				((BigInteger) result[2]).longValue(),
				((BigInteger) result[3]).longValue());

	}

	public VesselAggregationDto getVesselAggregationByStream(Integer portId, Long imo, Timestamp fromTime, Timestamp toTime) {
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdImoInPeriod(portId, imo, fromTime, toTime);
		LongSummaryStatistics timeInPortStatistics = visits.stream()
				.mapToLong(VesselVisit::getDurationSec)
				.summaryStatistics();
		LongSummaryStatistics startTimeStatistics = visits.stream()
				.mapToLong(visit -> visit.getTimeStarted().getTime())
				.summaryStatistics();
		return new VesselAggregationDto((long) visits.size(),
				timeInPortStatistics.getAverage(),
				timeInPortStatistics.getMin(),
				timeInPortStatistics.getMax(),
				new Timestamp(startTimeStatistics.getMin()),
				new Timestamp(startTimeStatistics.getMax()));
	}

	public VesselAggregationDto getVesselAggregationByJpql(Integer portId, Long imo, Timestamp fromTime, Timestamp toTime) {
		return visitRepository.getVisitAggregationByJpql(portId, imo, fromTime, toTime);
	}

	public VesselAggregationDto getVesselAggregationBySql(Integer portId, Long imo, Timestamp fromTime, Timestamp toTime) {
		Object[] result = visitRepository.getVisitAggregationBySql(portId, imo, fromTime, toTime).get(0);
		return new VesselAggregationDto(((BigInteger) result[0]).longValue(),
				((BigDecimal) result[1]).doubleValue(),
				((BigInteger) result[2]).longValue(),
				((BigInteger) result[3]).longValue(),
				(Date) result[4],
				(Date) result[5]);
	}

	public MonthlyAggregationDto getMonthAggregation(Integer portId, int year, int month) {
		Timestamp from = Timestamp.valueOf(LocalDate.of(year, month, 1).atStartOfDay());
		Timestamp to = Timestamp.valueOf(LocalDate.of(year, month + 1, 1).atStartOfDay());
		List<VesselVisit> visits = visitRepository.findVisitsByPortIdInPeriod(portId, from, to);
		Double avgDuration = visits.stream().mapToLong(VesselVisit::getDurationSec).average().orElse(0.0);
		Double sumOfLength = visits.stream().mapToDouble(VesselVisit::getLength).sum();
		ArrivalsStatistic arrivals = visitRepository.getArrivalStatistic(portId, from, to);
		return new MonthlyAggregationDto(arrivals.total, arrivals.unique, avgDuration, sumOfLength);
	}

	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}


