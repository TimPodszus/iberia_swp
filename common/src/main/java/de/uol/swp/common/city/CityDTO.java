package de.uol.swp.common.city;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.infection.IInfectionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object for City.
 */
@AllArgsConstructor
@Getter
public class CityDTO implements ICityDTO, Serializable {
    private final int id;
    private final PlagueName plagueName;
    private final CityName name;
    private final int foundationDate;
    private final boolean harbourCity;
    private boolean hospitalBuild;
    private List<IInfectionDTO> infections;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CityDTO cityDTO = (CityDTO) o;
        return id == cityDTO.id && foundationDate == cityDTO.foundationDate && harbourCity == cityDTO.harbourCity && hospitalBuild == cityDTO.hospitalBuild && plagueName == cityDTO.plagueName && Objects.equals(name,
                cityDTO.name
        ) && Objects.equals(infections, cityDTO.infections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plagueName, name, foundationDate, harbourCity, hospitalBuild, infections);
    }
}