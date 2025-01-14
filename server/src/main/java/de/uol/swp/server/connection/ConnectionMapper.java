package de.uol.swp.server.connection;

import de.uol.swp.common.connectiom.ConnectionDTO;
import de.uol.swp.common.connectiom.IConnectionDTO;
import de.uol.swp.server.city.data.CityName;

import java.util.ArrayList;
import java.util.List;

public class ConnectionMapper {

    // Private constructor to hide the implicit public one
    private ConnectionMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

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