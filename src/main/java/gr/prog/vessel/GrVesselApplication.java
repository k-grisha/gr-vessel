package gr.prog.vessel;

import gr.prog.vessel.model.VesselVisit;
import gr.prog.vessel.repository.VisitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableTransactionManagement
public class GrVesselApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrVesselApplication.class, args);
	}


	@Bean
	public CommandLineRunner initDB(VisitRepository visitRepository) {
		return (args) -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]");
			String csvFile = "port_visits.csv";
			InputStream in = this.getClass().getClassLoader().getResourceAsStream(csvFile);
			String line;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
				br.readLine();
				while ((line = br.readLine()) != null) {
					String[] column = line.split(",");
					VesselVisit vesselVisit = new VesselVisit();
					vesselVisit.setName(column[0]);
					vesselVisit.setImo(Long.parseLong(column[1]));
					vesselVisit.setLength(Double.parseDouble(column[2]));
					vesselVisit.setPortId(Integer.parseInt(column[3]));
					vesselVisit.setTimeStarted(Timestamp.valueOf(LocalDateTime.parse(column[4], formatter)));
					vesselVisit.setTimeFinished(Timestamp.valueOf(LocalDateTime.parse(column[5], formatter)));
					visitRepository.save(vesselVisit);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}
}
