package gr.prog.vessel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class VesselAggregationDto {
	private Integer portVisits;
	private Double avgInPortSec;
	private Long minInPortSec;
	private Long maxInPortSec;
	private Timestamp earliestVisit;
	private Timestamp latestVisit;
}
