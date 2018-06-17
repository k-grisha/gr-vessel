package gr.prog.vessel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class VesselAggregationDto {
	/**
	 * Number of port visits of vessel in the time range
	 */
	private Long portVisits;
	/**
	 * Average time in the port
	 */
	private Double avgInPortSec;
	/**
	 * Minimum time in the port
	 */
	private Long minInPortSec;
	/**
	 * Maximum tine in the port
	 */
	private Long maxInPortSec;
	/**
	 * Earliest visit (start time) in this time range
	 */
	private Date earliestVisit;
	/**
	 * Latest visit (start time) in this time range
	 */
	private Date latestVisit;
}
