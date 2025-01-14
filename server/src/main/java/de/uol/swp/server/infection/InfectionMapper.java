package de.uol.swp.server.infection;

import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.common.infection.InfectionDTO;
import de.uol.swp.server.plague.PlagueMapper;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class InfectionMapper {
    public static IInfectionDTO toDTO(IInfection infection) {
        return new InfectionDTO(infection.getSeverity(), PlagueMapper.toDTO(infection.getPlague()));
    }

    public static List<IInfectionDTO> toDTOList(List<IInfection> infections) {
        List<IInfectionDTO> infectionsDto = new ArrayList<>();
        for (IInfection infection : infections) {
            InfectionDTO infectionDTO = new InfectionDTO(infection.getSeverity(), PlagueMapper.toDTO(infection.getPlague()));
            infectionsDto.add(infectionDTO);
        }
        return infectionsDto;
    }
}
