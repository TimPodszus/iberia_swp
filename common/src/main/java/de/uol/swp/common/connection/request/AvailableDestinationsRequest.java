package de.uol.swp.common.connection.request;

import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.message.request.AbstractRequestMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A request message to get available destinations for a given city.
 */
@AllArgsConstructor
@Getter
public class AvailableDestinationsRequest extends AbstractRequestMessage {
    private final ICityDTO city;
}
