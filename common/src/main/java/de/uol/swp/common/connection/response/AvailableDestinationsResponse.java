package de.uol.swp.common.connection.response;


import java.util.List;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response message containing a list of available destination cities.
 */
@Getter
@AllArgsConstructor
public class AvailableDestinationsResponse extends AbstractResponseMessage {
    private final List<ICityDTO> cities;
}
