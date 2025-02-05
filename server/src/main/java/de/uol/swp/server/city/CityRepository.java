package de.uol.swp.server.city;

import de.uol.swp.common.city.CityName;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.server.city.data.City;
import de.uol.swp.server.city.data.ICity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repository class for managing city data.
 */
@Getter
public class CityRepository {
    /**
     * List of all cities.
     */
    private List<ICity> cities;

    /**
     * Constructor that initializes the city repository by creating all cities.
     */
    public CityRepository() {
        createAllCities();
    }

    /**
     * Creates and initializes the list of all cities with their respective attributes.
     */
    private void createAllCities() {
        cities = new ArrayList<>();

        cities.add(new City(34, PlagueName.YELLOW_FEVER, CityName.ALBACETE, 1100, false));
        cities.add(new City(25, PlagueName.YELLOW_FEVER, CityName.ZARAGOZA, -24, false));
        cities.add(new City(32, PlagueName.YELLOW_FEVER, CityName.ALICANTE, -324, true));
        cities.add(new City(35, PlagueName.YELLOW_FEVER, CityName.CUENCA, 784, false));
        cities.add(new City(27, PlagueName.YELLOW_FEVER, CityName.BARCELONA, -15, true));
        cities.add(new City(26, PlagueName.YELLOW_FEVER, CityName.ANDORRA_LA_VELLA, 1278, false));
        cities.add(new City(30, PlagueName.YELLOW_FEVER, CityName.TARRAGONA, -450, true));
        cities.add(new City(31, PlagueName.YELLOW_FEVER, CityName.VALENCIA, -138, true));
        cities.add(new City(33, PlagueName.YELLOW_FEVER, CityName.CARTAGENA, -227, true));
        cities.add(new City(29, PlagueName.YELLOW_FEVER, CityName.PALMA_DE_MALLORCA, -123, true));
        cities.add(new City(36, PlagueName.YELLOW_FEVER, CityName.TERUEL, 1171, false));
        cities.add(new City(28, PlagueName.YELLOW_FEVER, CityName.GIRONA, -79, false));

        cities.add(new City(5, PlagueName.CHOLERA, CityName.EVORA, -59, false));
        cities.add(new City(3, PlagueName.CHOLERA, CityName.LISBOA, -1000, true));
        cities.add(new City(2, PlagueName.CHOLERA, CityName.COIMBRA, -45, false));
        cities.add(new City(6, PlagueName.CHOLERA, CityName.CACERES, -34, false));
        cities.add(new City(4, PlagueName.CHOLERA, CityName.ALBUFEIRA, 750, true));
        cities.add(new City(8, PlagueName.CHOLERA, CityName.BRAGA, -16, false));
        cities.add(new City(11, PlagueName.CHOLERA, CityName.OURENSE, -25, false));
        cities.add(new City(7, PlagueName.CHOLERA, CityName.SALAMANCA, -250, false));
        cities.add(new City(9, PlagueName.CHOLERA, CityName.VIGO, -150, true));
        cities.add(new City(1, PlagueName.CHOLERA, CityName.PORTO, -136, true));
        cities.add(new City(12, PlagueName.CHOLERA, CityName.A_CORUNA, 1208, true));
        cities.add(new City(10, PlagueName.CHOLERA, CityName.SANTIAGO_DE_COMPOSTELA, 850, false));

        cities.add(new City(43, PlagueName.MALARIA, CityName.ALMERIA, 995, true));
        cities.add(new City(48, PlagueName.MALARIA, CityName.HUELVA, -950, true));
        cities.add(new City(42, PlagueName.MALARIA, CityName.GRANADA, -550, false));
        cities.add(new City(39, PlagueName.MALARIA, CityName.BADAJOZ, 875, false));
        cities.add(new City(45, PlagueName.MALARIA, CityName.GIBRALTAR, 1160, true));
        cities.add(new City(44, PlagueName.MALARIA, CityName.MALAGA, -750, true));
        cities.add(new City(37, PlagueName.MALARIA, CityName.TOLEDO, -300, false));
        cities.add(new City(46, PlagueName.MALARIA, CityName.CADIZ, -1104, true));
        cities.add(new City(40, PlagueName.MALARIA, CityName.CORDOBA, -169, false));
        cities.add(new City(47, PlagueName.MALARIA, CityName.SEVILLA, -850, false));
        cities.add(new City(41, PlagueName.MALARIA, CityName.JAEN, -231, false));
        cities.add(new City(38, PlagueName.MALARIA, CityName.CIUDAD_REAL, 1255, false));

        cities.add(new City(22, PlagueName.TYPHUS, CityName.SAN_SEBASTIAN_DONOSTIA, 1180, true));
        cities.add(new City(15, PlagueName.TYPHUS, CityName.SANTANDER, -26, true));
        cities.add(new City(14, PlagueName.TYPHUS, CityName.GIJON, -500, true));
        cities.add(new City(18, PlagueName.TYPHUS, CityName.SORIA, -133, false));
        cities.add(new City(19, PlagueName.TYPHUS, CityName.BURGOS, 884, false));
        cities.add(new City(20, PlagueName.TYPHUS, CityName.VICTORIA_GASTEIZ, 1181, false));
        cities.add(new City(23, PlagueName.TYPHUS, CityName.PAMPLONA, -74, false));
        cities.add(new City(17, PlagueName.TYPHUS, CityName.MADRID, 871, false));
        cities.add(new City(24, PlagueName.TYPHUS, CityName.HUESCA, -350, false));
        cities.add(new City(13, PlagueName.TYPHUS, CityName.LEON, -29, false));
        cities.add(new City(21, PlagueName.TYPHUS, CityName.BILBAO_BILBO, 1300, false));
        cities.add(new City(16, PlagueName.TYPHUS, CityName.VALLADOLID, 1072, false));
    }

    /**
     * Retrieves a city by its name.
     *
     * @param cityName the name of the city to retrieve
     * @return the city matching the specified name
     */
    public ICity getCityByName(CityName cityName) {
        return cities.stream()
                     .filter(city -> city.getName()
                                         .equals(cityName))
                     .findFirst()
                     .orElse(null);
    }

    /**
     * Retrieves a list of cities by their names.
     *
     * @param cityNames the names of the cities to retrieve
     * @return a list of cities matching the specified names
     */
    public List<ICity> getCitiesByNames(CityName... cityNames) {
        return getCitiesByNames(Arrays.asList(cityNames));
    }

    /**
     * Retrieves a list of cities by their names.
     *
     * @param cityNames the names of the cities to retrieve
     * @return a list of cities matching the given names
     */
    public List<ICity> getCitiesByNames(List<CityName> cityNames) {
        return cities.stream()
                     .filter(city -> cityNames.contains(city.getName()))
                     .toList();
    }

    /**
     * Retrieves a city by its ID.
     *
     * @param cityId the ID of the city to retrieve
     * @return the city with the given ID, or null if no city is found
     */
    public ICity getCity(int cityId) {
        return cities.stream()
                     .filter(city -> city.getId() == cityId)
                     .findFirst()
                     .orElse(null);
    }

    public CityName getCityNameById(int cityId) {
        return cities.stream()
                     .filter(city -> city.getId() == cityId)
                     .findFirst()
                     .map(ICity::getName)
                     .orElse(null);
    }
}
