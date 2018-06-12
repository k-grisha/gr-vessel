package gr.prog.vessel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VisitsAggregationDto {
	private Long uniqueVessels;
	private Double avgInPortSec;
	private Long minInPortSec;
	private Long maxInPortSec;
}
