package gr.prog.vessel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MonthlyAggregationDto {
	private Integer totalArrivals;
	private Integer uniqueVessels;
	private Double avgDurationSec;
	private Double  sumOfLength;
}
