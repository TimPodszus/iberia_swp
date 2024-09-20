package de.uol.swp.common.connection.response;

import de.uol.swp.common.city.CityDTO;

import java.util.List;

import de.uol.swp.common.message.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response message containing a list of available destination cities.
 */
@Getter
@AllArgsConstructor
public class AvailableDestinationsResponse extends AbstractResponseMessage {
    private final List<CityDTO> cities;
}
