package gr.prog.vessel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@Getter
public class VesselAggregationDto {
	private Long portVisits;
	private Double avgInPortSec;
	private Long minInPortSec;
	private Long maxInPortSec;
	private Date earliestVisit;
	private Date latestVisit;
}
