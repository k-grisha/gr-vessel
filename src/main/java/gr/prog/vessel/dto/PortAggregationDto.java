package gr.prog.vessel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PortAggregationDto {
	/**
	 * Number of unique vessels (IMO number) during this time range
	 */
	private Long uniqueVessels;
	/**
	 * Average time in the port in sec.
	 */
	private Double avgInPortSec;
	/**
	 * Minimum time in the port in sec.
	 */
	private Long minInPortSec;
	/**
	 * Maximum time in the port in sec.
	 */
	private Long maxInPortSec;
}
