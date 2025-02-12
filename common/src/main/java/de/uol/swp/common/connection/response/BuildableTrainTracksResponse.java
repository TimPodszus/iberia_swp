package de.uol.swp.common.connection.response;

import de.uol.swp.common.connection.IConnectionDTO;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class BuildableTrainTracksResponse extends AbstractResponseMessage {
    private final List<IConnectionDTO> connections;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BuildableTrainTracksResponse that = (BuildableTrainTracksResponse) o;
        return Objects.equals(connections, that.connections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connections);
    }
}
