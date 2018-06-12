package gr.prog.vessel;

import gr.prog.vessel.model.Vessel;
import gr.prog.vessel.repository.VesselRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class GrVesselApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrVesselApplication.class, args);
	}


	@Bean
	public CommandLineRunner initDB(VesselRepository vesselRepository) {
		return (args) -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.][SSS][SS][S]");
			String csvFile = "port_visits.csv";
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(csvFile);
			String line;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
				br.readLine();
				while ((line = br.readLine()) != null) {
					String[] column = line.split(",");
					Vessel vessel = new Vessel();
					vessel.setName(column[0]);
					vessel.setImo(Long.parseLong(column[1]));
					vessel.setLength(Double.parseDouble(column[2]));
					vessel.setPortId(Integer.parseInt(column[3]));
					vessel.setTimeStarted(Timestamp.valueOf(LocalDateTime.parse(column[4], formatter)));
					vessel.setTimeFinished(Timestamp.valueOf(LocalDateTime.parse(column[5], formatter)));
					vesselRepository.save(vessel);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}
}
