package de.uol.swp.common.region.response;

import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class PossibleCityCardsToDiscardForRegionResponse extends AbstractResponseMessage {
    private List<CityCardDTO> cityCards;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        PossibleCityCardsToDiscardForRegionResponse that = (PossibleCityCardsToDiscardForRegionResponse) o;
        return Objects.equals(getCityCards(), that.getCityCards());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCityCards());
    }
}
