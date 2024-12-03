package de.uol.swp.server.connection;

import de.uol.swp.common.connectiom.ConnectionDTO;
import de.uol.swp.common.connectiom.IConnectionDTO;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.connection.data.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for mapping Connection objects to their corresponding Data Transfer Objects (DTOs).
 */
public class ConnectionMapper {

    /**
     * Private constructor to prevent instantiation.
     */
    private ConnectionMapper() {
    }

    /**
     * Converts a Connection object to an IConnectionDTO.
     *
     * @param connection the Connection object to convert
     * @return the corresponding IConnectionDTO
     */
    public static IConnectionDTO toDTO(Connection connection) {
        return new ConnectionDTO(
                connection.getId(),
                connection.getCityNames()
                          .stream()
                          .map(CityName::getDisplayName)
                          .toList(),
                connection.isTrainTrack(),
                connection.isTrainTrackBuildable()
        );
    }

    /**
     * Converts a list of Connection objects to a list of IConnectionDTOs.
     *
     * @param connections the list of Connection objects to convert
     * @return the corresponding list of IConnectionDTOs
     */
    public static List<IConnectionDTO> toDTOList(List<Connection> connections) {
        List<IConnectionDTO> connectionDTOS = new ArrayList<>();
        for (Connection connection : connections) {
            ConnectionDTO connectionDTO = new ConnectionDTO(
                    connection.getId(),
                    connection.getCityNames()
                              .stream()
                              .map(CityName::getDisplayName)
                              .toList(),
                    connection.isTrainTrack(),
                    connection.isTrainTrackBuildable()
            );
            connectionDTOS.add(connectionDTO);
        }
        return connectionDTOS;
    }
}
