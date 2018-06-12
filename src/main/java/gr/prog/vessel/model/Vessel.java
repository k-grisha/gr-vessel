package gr.prog.vessel.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
public class Vessel extends AbstractModel {
	private String name;
	private Long imo;
	private Double length;
	private Integer portId;
	private Timestamp timeStarted;
	private Timestamp timeFinished;
}
