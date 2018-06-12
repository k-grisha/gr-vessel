package gr.prog.vessel.mapper;

import gr.prog.vessel.dto.GuestDto;
import gr.prog.vessel.model.VesselVisit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VesselVisitMapper {
	VesselVisitMapper INSTANCE = Mappers.getMapper(VesselVisitMapper.class);

	GuestDto map(VesselVisit entity);
}
