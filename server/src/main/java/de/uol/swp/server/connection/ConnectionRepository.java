package de.uol.swp.server.connection;

import de.uol.swp.common.city.CityName;
import de.uol.swp.server.connection.data.Connection;
import de.uol.swp.server.connection.data.IConnection;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing connections between cities.
 */
@Getter
public class ConnectionRepository {
    /**
     * List of all connections.
     */
    List<IConnection> connections;

    /**
     * Constructor that initializes the connection repository by creating all connections.
     */
    public ConnectionRepository() {
        createAllConnections();
    }

    /**
     * Creates and initializes the list of all connections with their respective attributes.
     */
    private void createAllConnections() {
        connections = new ArrayList<>();

        connections.add(new Connection(1, List.of(CityName.A_CORUNA, CityName.GIJON), true));
        connections.add(new Connection(2, List.of(CityName.A_CORUNA, CityName.SANTIAGO_DE_COMPOSTELA), true));
        connections.add(new Connection(3, List.of(CityName.SANTIAGO_DE_COMPOSTELA, CityName.OURENSE), true));
        connections.add(new Connection(4, List.of(CityName.OURENSE, CityName.LEON), true));
        connections.add(new Connection(5, List.of(CityName.LEON, CityName.GIJON), true));
        connections.add(new Connection(6, List.of(CityName.SANTIAGO_DE_COMPOSTELA, CityName.VIGO), true));
        connections.add(new Connection(7, List.of(CityName.VIGO, CityName.OURENSE), true));
        connections.add(new Connection(8, List.of(CityName.VIGO, CityName.BRAGA), true));
        connections.add(new Connection(9, List.of(CityName.VIGO, CityName.PORTO), true));
        connections.add(new Connection(10, List.of(CityName.PORTO, CityName.BRAGA), true));
        connections.add(new Connection(11, List.of(CityName.BRAGA, CityName.SALAMANCA), true));
        connections.add(new Connection(12, List.of(CityName.SALAMANCA, CityName.LEON), true));
        connections.add(new Connection(13, List.of(CityName.PORTO, CityName.LISBOA), true));
        connections.add(new Connection(14, List.of(CityName.LISBOA, CityName.COIMBRA), true));
        connections.add(new Connection(15, List.of(CityName.COIMBRA, CityName.PORTO), true));
        connections.add(new Connection(16, List.of(CityName.COIMBRA, CityName.CACERES), true));
        connections.add(new Connection(17, List.of(CityName.CACERES, CityName.SALAMANCA), true));
        connections.add(new Connection(18, List.of(CityName.LISBOA, CityName.EVORA), true));
        connections.add(new Connection(19, List.of(CityName.EVORA, CityName.BADAJOZ), true));
        connections.add(new Connection(20, List.of(CityName.BADAJOZ, CityName.CACERES), true));
        connections.add(new Connection(21, List.of(CityName.LISBOA, CityName.ALBUFEIRA), true));
        connections.add(new Connection(22, List.of(CityName.ALBUFEIRA, CityName.HUELVA), true));
        connections.add(new Connection(23, List.of(CityName.HUELVA, CityName.EVORA), true));
        connections.add(new Connection(24, List.of(CityName.HUELVA, CityName.SEVILLA), true));
        connections.add(new Connection(25, List.of(CityName.BADAJOZ, CityName.CORDOBA), true));
        connections.add(new Connection(26, List.of(CityName.SEVILLA, CityName.CORDOBA), true));
        connections.add(new Connection(27, List.of(CityName.SEVILLA, CityName.CADIZ), true));
        connections.add(new Connection(28, List.of(CityName.CADIZ, CityName.GIBRALTAR), false));
        connections.add(new Connection(29, List.of(CityName.GIBRALTAR, CityName.MALAGA), false));
        connections.add(new Connection(30, List.of(CityName.SEVILLA, CityName.MALAGA), true));
        connections.add(new Connection(31, List.of(CityName.CORDOBA, CityName.MALAGA), true));
        connections.add(new Connection(32, List.of(CityName.CORDOBA, CityName.JAEN), true));
        connections.add(new Connection(33, List.of(CityName.JAEN, CityName.GRANADA), true));
        connections.add(new Connection(34, List.of(CityName.MALAGA, CityName.GRANADA), true));
        connections.add(new Connection(35, List.of(CityName.MALAGA, CityName.ALMERIA), true));
        connections.add(new Connection(36, List.of(CityName.ALMERIA, CityName.GRANADA), true));
        connections.add(new Connection(37, List.of(CityName.ALMERIA, CityName.CARTAGENA), true));
        connections.add(new Connection(38, List.of(CityName.CARTAGENA, CityName.ALBACETE), true));
        connections.add(new Connection(39, List.of(CityName.JAEN, CityName.ALBACETE), true));
        connections.add(new Connection(40, List.of(CityName.CARTAGENA, CityName.ALICANTE), true));
        connections.add(new Connection(41, List.of(CityName.ALICANTE, CityName.VALENCIA), true));
        connections.add(new Connection(42, List.of(CityName.ALBACETE, CityName.VALENCIA), true));
        connections.add(new Connection(43, List.of(CityName.ALBACETE, CityName.CUENCA), true));
        connections.add(new Connection(44, List.of(CityName.CUENCA, CityName.VALENCIA), true));
        connections.add(new Connection(45, List.of(CityName.CUENCA, CityName.TERUEL), true));
        connections.add(new Connection(46, List.of(CityName.TERUEL, CityName.ZARAGOZA), true));
        connections.add(new Connection(47, List.of(CityName.TERUEL, CityName.TARRAGONA), true));
        connections.add(new Connection(48, List.of(CityName.VALENCIA, CityName.TARRAGONA), true));
        connections.add(new Connection(49, List.of(CityName.VALENCIA, CityName.PALMA_DE_MALLORCA), false));
        connections.add(new Connection(50, List.of(CityName.PALMA_DE_MALLORCA, CityName.BARCELONA), false));
        connections.add(new Connection(51, List.of(CityName.TARRAGONA, CityName.BARCELONA), true));
        connections.add(new Connection(52, List.of(CityName.ZARAGOZA, CityName.BARCELONA), true));
        connections.add(new Connection(53, List.of(CityName.BARCELONA, CityName.GIRONA), true));
        connections.add(new Connection(54, List.of(CityName.GIRONA, CityName.ANDORRA_LA_VELLA), false));
        connections.add(new Connection(55, List.of(CityName.ANDORRA_LA_VELLA, CityName.HUESCA), false));
        connections.add(new Connection(56, List.of(CityName.HUESCA, CityName.ZARAGOZA), true));
        connections.add(new Connection(57, List.of(CityName.ZARAGOZA, CityName.SORIA), true));
        connections.add(new Connection(58, List.of(CityName.SORIA, CityName.BURGOS), true));
        connections.add(new Connection(59, List.of(CityName.BURGOS, CityName.VICTORIA_GASTEIZ), true));
        connections.add(new Connection(60, List.of(CityName.VICTORIA_GASTEIZ, CityName.ZARAGOZA), true));
        connections.add(new Connection(61, List.of(CityName.VICTORIA_GASTEIZ, CityName.PAMPLONA), true));
        connections.add(new Connection(62, List.of(CityName.PAMPLONA, CityName.HUESCA), true));
        connections.add(new Connection(63, List.of(CityName.PAMPLONA, CityName.SAN_SEBASTIAN_DONOSTIA), true));
        connections.add(new Connection(64, List.of(CityName.SAN_SEBASTIAN_DONOSTIA, CityName.BILBAO_BILBO), true));
        connections.add(new Connection(65, List.of(CityName.BILBAO_BILBO, CityName.VICTORIA_GASTEIZ), true));
        connections.add(new Connection(66, List.of(CityName.BILBAO_BILBO, CityName.SANTANDER), true));
        connections.add(new Connection(67, List.of(CityName.SANTANDER, CityName.GIJON), true));
        connections.add(new Connection(68, List.of(CityName.SANTANDER, CityName.VALLADOLID), true));
        connections.add(new Connection(69, List.of(CityName.VALLADOLID, CityName.BURGOS), true));
        connections.add(new Connection(70, List.of(CityName.VALLADOLID, CityName.SALAMANCA), true));
        connections.add(new Connection(71, List.of(CityName.SALAMANCA, CityName.MADRID), true));
        connections.add(new Connection(72, List.of(CityName.MADRID, CityName.VALLADOLID), true));
        connections.add(new Connection(73, List.of(CityName.MADRID, CityName.ZARAGOZA), true));
        connections.add(new Connection(74, List.of(CityName.MADRID, CityName.CUENCA), true));
        connections.add(new Connection(75, List.of(CityName.MADRID, CityName.CIUDAD_REAL), true));
        connections.add(new Connection(76, List.of(CityName.MADRID, CityName.TOLEDO), true));
        connections.add(new Connection(77, List.of(CityName.TOLEDO, CityName.CACERES), true));
        connections.add(new Connection(78, List.of(CityName.TOLEDO, CityName.CIUDAD_REAL), true));
        connections.add(new Connection(79, List.of(CityName.CIUDAD_REAL, CityName.ALBACETE), true));
        connections.add(new Connection(80, List.of(CityName.CIUDAD_REAL, CityName.CORDOBA), true));
        connections.add(new Connection(81, List.of(CityName.CIUDAD_REAL, CityName.BADAJOZ), true));
    }

    /**
     * Retrieves a connection by its unique identifier.
     *
     * @param id the unique identifier of the connection
     * @return the connection with the specified id, or null if not found
     */
    public IConnection getConnectionByID(int id) {
        return connections.stream()
                          .filter(connection -> connection.getId() == id)
                          .findFirst()
                          .orElse(null);
    }

    /**
     * Retrieves a list of connections that include the specified city.
     *
     * @param cityName the name of the city
     * @return a list of connections that include the specified city
     */
    public List<IConnection> getConnectionsOfCity(CityName cityName) {
        return connections.stream()
                          .filter(connection -> connection.getCityNames()
                                                          .contains(cityName))
                          .toList();
    }

    /**
     * Retrieves the names of cities connected to the specified city.
     *
     * @param cityName the name of the city for which to find connected cities
     * @return a list of city names connected to the specified city, or null if no connections are found
     */
    public List<CityName> getCityNamesOfConnectedCitiesByCityName(CityName cityName) {
        return connections.stream()
                          .map(IConnection::getCityNames)
                          .filter(cityNames -> cityNames.contains(cityName))
                          .findFirst()
                          .orElse(null);
    }
}