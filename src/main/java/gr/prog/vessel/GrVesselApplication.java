package gr.prog.vessel;

import gr.prog.vessel.model.VesselVisit;
import gr.prog.vessel.repository.VisitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static gr.prog.vessel.Utils.convertToUTC;

@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2
public class GrVesselApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrVesselApplication.class, args);
	}

	@Bean
	@Profile("!Test")
	public CommandLineRunner initDB(VisitRepository visitRepository) {
		return (args) -> {
			parseCSV(visitRepository, "port_visits.csv");
		};
	}

	/**
	 * Parse CSV file to DB
	 */
	public static void parseCSV(VisitRepository visitRepository, String csvFile) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS][.SS][.S]");
		InputStream in = visitRepository.getClass().getClassLoader().getResourceAsStream(csvFile);
		String line;
		int counter = 0;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] column = line.split(",");
				VesselVisit vesselVisit = new VesselVisit();
				vesselVisit.setName(column[0]);
				vesselVisit.setImo(Long.parseLong(column[1]));
				vesselVisit.setLength(Double.parseDouble(column[2]));
				vesselVisit.setPortId(Integer.parseInt(column[3]));
				//todo convert datatime to UTC Zone gr.prog.vessel.Utils.convertToUTC(java.time.LocalDateTime, java.time.ZoneId)
				vesselVisit.setTimeStarted(Timestamp.valueOf(LocalDateTime.parse(column[4], formatter)));
				vesselVisit.setTimeFinished(Timestamp.valueOf(LocalDateTime.parse(column[5], formatter)));
				visitRepository.save(vesselVisit);
				counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("==== " + counter + " visits added ====");
	}
}
