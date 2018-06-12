package gr.prog.vessel.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GuestDto {
	private String name;
	private Long imo;
	private Double length;
	private Timestamp timeStarted;
}
