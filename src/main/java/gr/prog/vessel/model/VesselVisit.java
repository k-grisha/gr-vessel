package gr.prog.vessel.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "vessel_visit")
public class VesselVisit extends AbstractModel {
	private String name;
	private Long imo;
	private Double length;
	private Integer portId;
	private Timestamp timeStarted;
	private Timestamp timeFinished;
}
