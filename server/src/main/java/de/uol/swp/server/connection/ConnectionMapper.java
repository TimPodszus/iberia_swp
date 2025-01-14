package de.uol.swp.server.connection;

import de.uol.swp.common.connectiom.ConnectionDTO;
import de.uol.swp.common.connectiom.IConnectionDTO;
import de.uol.swp.common.city.CityName;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ConnectionMapper {
    public static IConnectionDTO toDTO(Connection connection) {
        return new ConnectionDTO(connection.getId(),
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
            ConnectionDTO connectionDTO = new ConnectionDTO(connection.getId(),
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
