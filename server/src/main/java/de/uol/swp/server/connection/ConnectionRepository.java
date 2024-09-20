package de.uol.swp.server.connection;

import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.city.CityRepository;

import java.util.ArrayList;
import java.util.List;

import static de.uol.swp.server.city.CityRepository.getCitiesByNames;

public class ConnectionRepository
{
    // Private constructor to hide the implicit public one
    private ConnectionRepository()
    {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<Connection> getAllConnections()
    {
        ArrayList<Connection> connections = new ArrayList<>();
        List<City> allCities = CityRepository.getAllCities();

        connections.add(new Connection(1, getCitiesByNames(allCities, CityName.A_CORUNA, CityName.GIJON), true));
        connections.add(new Connection(2,
                getCitiesByNames(allCities, CityName.A_CORUNA, CityName.SANTIAGO_DE_COMPOSTELA),
                true
        ));
        connections.add(new Connection(3,
                getCitiesByNames(allCities, CityName.SANTIAGO_DE_COMPOSTELA, CityName.OURENSE),
                true
        ));
        connections.add(new Connection(4, getCitiesByNames(allCities, CityName.OURENSE, CityName.LEON), true));
        connections.add(new Connection(5, getCitiesByNames(allCities, CityName.LEON, CityName.GIJON), true));
        connections.add(new Connection(6,
                getCitiesByNames(allCities, CityName.SANTIAGO_DE_COMPOSTELA, CityName.VIGO),
                true
        ));
        connections.add(new Connection(7, getCitiesByNames(allCities, CityName.VIGO, CityName.OURENSE), true));
        connections.add(new Connection(8, getCitiesByNames(allCities, CityName.VIGO, CityName.BRAGA), true));
        connections.add(new Connection(9, getCitiesByNames(allCities, CityName.VIGO, CityName.PORTO), true));
        connections.add(new Connection(10, getCitiesByNames(allCities, CityName.PORTO, CityName.BRAGA), true));
        connections.add(new Connection(11, getCitiesByNames(allCities, CityName.BRAGA, CityName.SALAMANCA), true));
        connections.add(new Connection(12, getCitiesByNames(allCities, CityName.SALAMANCA, CityName.LEON), true));
        connections.add(new Connection(13, getCitiesByNames(allCities, CityName.PORTO, CityName.LISBOA), true));
        connections.add(new Connection(14, getCitiesByNames(allCities, CityName.LISBOA, CityName.COIMBRA), true));
        connections.add(new Connection(15, getCitiesByNames(allCities, CityName.COIMBRA, CityName.PORTO), true));
        connections.add(new Connection(16, getCitiesByNames(allCities, CityName.COIMBRA, CityName.CACERES), true));
        connections.add(new Connection(17, getCitiesByNames(allCities, CityName.CACERES, CityName.SALAMANCA), true));
        connections.add(new Connection(18, getCitiesByNames(allCities, CityName.LISBOA, CityName.EVORA), true));
        connections.add(new Connection(19, getCitiesByNames(allCities, CityName.EVORA, CityName.BADAJOZ), true));
        connections.add(new Connection(20, getCitiesByNames(allCities, CityName.BADAJOZ, CityName.CACERES), true));
        connections.add(new Connection(21, getCitiesByNames(allCities, CityName.LISBOA, CityName.ALBUFEIRA), true));
        connections.add(new Connection(22, getCitiesByNames(allCities, CityName.ALBUFEIRA, CityName.HUELVA), true));
        connections.add(new Connection(23, getCitiesByNames(allCities, CityName.HUELVA, CityName.EVORA), true));
        connections.add(new Connection(24, getCitiesByNames(allCities, CityName.HUELVA, CityName.SEVILLA), true));
        connections.add(new Connection(25, getCitiesByNames(allCities, CityName.BADAJOZ, CityName.CORDOBA), true));
        connections.add(new Connection(26, getCitiesByNames(allCities, CityName.SEVILLA, CityName.CORDOBA), true));
        connections.add(new Connection(27, getCitiesByNames(allCities, CityName.SEVILLA, CityName.CADIZ), true));
        // No Traintrack possible
        connections.add(new Connection(28, getCitiesByNames(allCities, CityName.CADIZ, CityName.GIBRALTAR), false));
        // No Traintrack possible
        connections.add(new Connection(29, getCitiesByNames(allCities, CityName.GIBRALTAR, CityName.MALAGA), false));
        connections.add(new Connection(30, getCitiesByNames(allCities, CityName.SEVILLA, CityName.MALAGA), true));
        connections.add(new Connection(31, getCitiesByNames(allCities, CityName.CORDOBA, CityName.MALAGA), true));
        connections.add(new Connection(32, getCitiesByNames(allCities, CityName.CORDOBA, CityName.JAEN), true));
        connections.add(new Connection(33, getCitiesByNames(allCities, CityName.JAEN, CityName.GRANADA), true));
        connections.add(new Connection(34, getCitiesByNames(allCities, CityName.MALAGA, CityName.GRANADA), true));
        connections.add(new Connection(35, getCitiesByNames(allCities, CityName.MALAGA, CityName.ALMERIA), true));
        connections.add(new Connection(36, getCitiesByNames(allCities, CityName.ALMERIA, CityName.GRANADA), true));
        connections.add(new Connection(37, getCitiesByNames(allCities, CityName.ALMERIA, CityName.CARTAGENA), true));
        connections.add(new Connection(38, getCitiesByNames(allCities, CityName.CARTAGENA, CityName.ALBACETE), true));
        connections.add(new Connection(39, getCitiesByNames(allCities, CityName.JAEN, CityName.ALBACETE), true));
        connections.add(new Connection(40, getCitiesByNames(allCities, CityName.CARTAGENA, CityName.ALICANTE), true));
        connections.add(new Connection(41, getCitiesByNames(allCities, CityName.ALICANTE, CityName.VALENCIA), true));
        connections.add(new Connection(42, getCitiesByNames(allCities, CityName.ALBACETE, CityName.VALENCIA), true));
        connections.add(new Connection(43, getCitiesByNames(allCities, CityName.ALBACETE, CityName.CUENCA), true));
        connections.add(new Connection(44, getCitiesByNames(allCities, CityName.CUENCA, CityName.VALENCIA), true));
        connections.add(new Connection(45, getCitiesByNames(allCities, CityName.CUENCA, CityName.TERUEL), true));
        connections.add(new Connection(46, getCitiesByNames(allCities, CityName.TERUEL, CityName.ZARAGOZA), true));
        connections.add(new Connection(47, getCitiesByNames(allCities, CityName.TERUEL, CityName.TARRAGONA), true));
        connections.add(new Connection(48, getCitiesByNames(allCities, CityName.VALENCIA, CityName.TARRAGONA), true));
        // No Traintrack possible
        connections.add(new Connection(49,
                getCitiesByNames(allCities, CityName.VALENCIA, CityName.PALMA_DE_MALLORCA),
                false
        ));
        // No Traintrack possible
        connections.add(new Connection(50,
                getCitiesByNames(allCities, CityName.PALMA_DE_MALLORCA, CityName.BARCELONA),
                false
        ));
        connections.add(new Connection(51, getCitiesByNames(allCities, CityName.TARRAGONA, CityName.BARCELONA), true));
        connections.add(new Connection(52, getCitiesByNames(allCities, CityName.ZARAGOZA, CityName.BARCELONA), true));
        connections.add(new Connection(53, getCitiesByNames(allCities, CityName.BARCELONA, CityName.GIRONA), true));
        // No Traintrack possible
        connections.add(new Connection(54,
                getCitiesByNames(allCities, CityName.GIRONA, CityName.ANDORRA_LA_VELLA),
                false
        ));
        // No Traintrack possible
        connections.add(new Connection(55,
                getCitiesByNames(allCities, CityName.ANDORRA_LA_VELLA, CityName.HUESCA),
                false
        ));
        connections.add(new Connection(56, getCitiesByNames(allCities, CityName.HUESCA, CityName.ZARAGOZA), true));
        connections.add(new Connection(57, getCitiesByNames(allCities, CityName.ZARAGOZA, CityName.SORIA), true));
        connections.add(new Connection(58, getCitiesByNames(allCities, CityName.SORIA, CityName.BURGOS), true));
        connections.add(new Connection(59,
                getCitiesByNames(allCities, CityName.BURGOS, CityName.VICTORIA_GASTEIZ),
                true
        ));
        connections.add(new Connection(60,
                getCitiesByNames(allCities, CityName.VICTORIA_GASTEIZ, CityName.ZARAGOZA),
                true
        ));
        connections.add(new Connection(61,
                getCitiesByNames(allCities, CityName.VICTORIA_GASTEIZ, CityName.PAMPLONA),
                true
        ));
        connections.add(new Connection(62, getCitiesByNames(allCities, CityName.PAMPLONA, CityName.HUESCA), true));
        connections.add(new Connection(63,
                getCitiesByNames(allCities, CityName.PAMPLONA, CityName.SAN_SEBASTIAN_DONOSTIA),
                true
        ));
        connections.add(new Connection(64,
                getCitiesByNames(allCities, CityName.SAN_SEBASTIAN_DONOSTIA, CityName.BILBAO_BILBO),
                true
        ));
        connections.add(new Connection(65,
                getCitiesByNames(allCities, CityName.BILBAO_BILBO, CityName.VICTORIA_GASTEIZ),
                true
        ));
        connections.add(new Connection(66,
                getCitiesByNames(allCities, CityName.BILBAO_BILBO, CityName.SANTANDER),
                true
        ));
        connections.add(new Connection(67, getCitiesByNames(allCities, CityName.SANTANDER, CityName.GIJON), true));
        connections.add(new Connection(68, getCitiesByNames(allCities, CityName.SANTANDER, CityName.VALLADOLID), true));
        connections.add(new Connection(69, getCitiesByNames(allCities, CityName.VALLADOLID, CityName.BURGOS), true));
        connections.add(new Connection(70, getCitiesByNames(allCities, CityName.VALLADOLID, CityName.SALAMANCA), true));
        connections.add(new Connection(71, getCitiesByNames(allCities, CityName.SALAMANCA, CityName.MADRID), true));
        connections.add(new Connection(72, getCitiesByNames(allCities, CityName.MADRID, CityName.VALLADOLID), true));
        connections.add(new Connection(73, getCitiesByNames(allCities, CityName.MADRID, CityName.ZARAGOZA), true));
        connections.add(new Connection(74, getCitiesByNames(allCities, CityName.MADRID, CityName.CUENCA), true));
        connections.add(new Connection(75, getCitiesByNames(allCities, CityName.MADRID, CityName.CIUDAD_REAL), true));
        connections.add(new Connection(76, getCitiesByNames(allCities, CityName.MADRID, CityName.TOLEDO), true));
        connections.add(new Connection(77, getCitiesByNames(allCities, CityName.TOLEDO, CityName.CACERES), true));
        connections.add(new Connection(78, getCitiesByNames(allCities, CityName.TOLEDO, CityName.CIUDAD_REAL), true));
        connections.add(new Connection(79, getCitiesByNames(allCities, CityName.CIUDAD_REAL, CityName.ALBACETE), true));
        connections.add(new Connection(80, getCitiesByNames(allCities, CityName.CIUDAD_REAL, CityName.CORDOBA), true));
        connections.add(new Connection(81, getCitiesByNames(allCities, CityName.CIUDAD_REAL, CityName.BADAJOZ), true));

        return connections;
    }
}