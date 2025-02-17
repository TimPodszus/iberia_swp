package de.uol.swp.common.infection;

import de.uol.swp.common.game.PlagueName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class InfectionDTO implements IInfectionDTO, Serializable {
    private int severity;
    private final PlagueName plagueName;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        InfectionDTO that = (InfectionDTO) object;
        return severity == that.severity && plagueName == that.plagueName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(severity, plagueName);
    }
}
