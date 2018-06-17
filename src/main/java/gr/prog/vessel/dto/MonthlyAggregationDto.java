package gr.prog.vessel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MonthlyAggregationDto {
	private Long totalArrivals;
	private Long uniqueVessels;
	private Double avgDurationSec;
	private Double  sumOfLength;
}
