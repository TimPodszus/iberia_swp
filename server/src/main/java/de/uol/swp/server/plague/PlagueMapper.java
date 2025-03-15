package de.uol.swp.server.plague;

import de.uol.swp.common.plague.dto.IPlagueDTO;
import de.uol.swp.common.plague.dto.PlagueDTO;
import de.uol.swp.server.plague.data.IPlague;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PlagueMapper {
    public static IPlagueDTO toDTO(IPlague plague) {
        return new PlagueDTO(
                plague.getName(),
                plague.getCubesRemaining(),
                plague.isResearched()
        );
    }

    public static List<IPlagueDTO> toDTOList(List<IPlague> plagues) {
        List<IPlagueDTO> plagueDTOS = new ArrayList<>();
        for (IPlague plague : plagues) {
            PlagueDTO plagueDTO = new PlagueDTO(
                    plague.getName(),
                    plague.getCubesRemaining(),
                    plague.isResearched()
            );
            plagueDTOS.add(plagueDTO);
        }
        return plagueDTOS;
    }
}
