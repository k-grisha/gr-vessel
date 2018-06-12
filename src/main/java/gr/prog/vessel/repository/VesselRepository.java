package gr.prog.vessel.repository;

import gr.prog.vessel.model.Vessel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VesselRepository extends JpaRepository<Vessel, Long> {
}
