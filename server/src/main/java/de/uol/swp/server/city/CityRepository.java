package de.uol.swp.server.city;

import de.uol.swp.server.plague.PlagueName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CityRepository
{
    // Private constructor to hide the implicit public one
    private CityRepository()
    {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<City> getAllCities()
    {
        List<City> cities = new ArrayList<>();

        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.ALBACETE, 1100, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.ZARAGOZA, -24, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.ALICANTE, -324, true));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.CUENCA, 784, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.BARCELONA, -15, true));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.ANDORRA_LA_VELLA, 1278, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.TARRAGONA, -450, true));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.VALENCIA, -138, true));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.CARTAGENA, -227, true));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.PALMA_DE_MALLORCA, -123, true));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.TERUEL, 1171, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, CityName.GIRONA, -79, false));

        cities.add(new City(PlagueName.CHOLERA, CityName.EVORA, -59, false));
        cities.add(new City(PlagueName.CHOLERA, CityName.LISBOA, -1000, true));
        cities.add(new City(PlagueName.CHOLERA, CityName.COIMBRA, -45, false));
        cities.add(new City(PlagueName.CHOLERA, CityName.CACERES, -34, false));
        cities.add(new City(PlagueName.CHOLERA, CityName.ALBUFEIRA, 750, true));
        cities.add(new City(PlagueName.CHOLERA, CityName.BRAGA, -16, false));
        cities.add(new City(PlagueName.CHOLERA, CityName.OURENSE, -25, false));
        cities.add(new City(PlagueName.CHOLERA, CityName.SALAMANCA, -250, false));
        cities.add(new City(PlagueName.CHOLERA, CityName.VIGO, -150, true));
        cities.add(new City(PlagueName.CHOLERA, CityName.PORTO, -136, true));
        cities.add(new City(PlagueName.CHOLERA, CityName.A_CORUNA, 1208, true));
        cities.add(new City(PlagueName.CHOLERA, CityName.SANTIAGO_DE_COMPOSTELA, 850, false));

        cities.add(new City(PlagueName.MALARIA, CityName.ALMERIA, 995, true));
        cities.add(new City(PlagueName.MALARIA, CityName.HUELVA, -950, true));
        cities.add(new City(PlagueName.MALARIA, CityName.GRANADA, -550, false));
        cities.add(new City(PlagueName.MALARIA, CityName.BADAJOZ, 875, false));
        cities.add(new City(PlagueName.MALARIA, CityName.GIBRALTAR, 1160, true));
        cities.add(new City(PlagueName.MALARIA, CityName.MALAGA, -750, true));
        cities.add(new City(PlagueName.MALARIA, CityName.TOLEDO, -300, false));
        cities.add(new City(PlagueName.MALARIA, CityName.CADIZ, -1104, true));
        cities.add(new City(PlagueName.MALARIA, CityName.CORDOBA, -169, false));
        cities.add(new City(PlagueName.MALARIA, CityName.SEVILLA, -850, false));
        cities.add(new City(PlagueName.MALARIA, CityName.JAEN, -231, false));
        cities.add(new City(PlagueName.MALARIA, CityName.CIUDAD_REAL, 1255, false));

        cities.add(new City(PlagueName.TYPHUS, CityName.SAN_SEBASTIAN_DONOSTIA, 1180, true));
        cities.add(new City(PlagueName.TYPHUS, CityName.SANTANDER, -26, true));
        cities.add(new City(PlagueName.TYPHUS, CityName.GIJON, -500, true));
        cities.add(new City(PlagueName.TYPHUS, CityName.SORIA, -133, false));
        cities.add(new City(PlagueName.TYPHUS, CityName.BURGOS, 884, false));
        cities.add(new City(PlagueName.TYPHUS, CityName.VICTORIA_GASTEIZ, 1181, false));
        cities.add(new City(PlagueName.TYPHUS, CityName.PAMPLONA, -74, false));
        cities.add(new City(PlagueName.TYPHUS, CityName.MADRID, 871, false));
        cities.add(new City(PlagueName.TYPHUS, CityName.HUESCA, -350, false));
        cities.add(new City(PlagueName.TYPHUS, CityName.LEON, -29, false));
        cities.add(new City(PlagueName.TYPHUS, CityName.BILBAO_BILBO, 1300, false));
        cities.add(new City(PlagueName.TYPHUS, CityName.VALLADOLID, 1072, false));

        return cities;
    }

    public static List<City> getCitiesByNames(List<City> allCities, CityName... names)
    {
        List<CityName> nameList = Arrays.asList(names);
        return allCities.stream()
                        .filter(city -> nameList.contains(city.getName()))
                        .toList();
    }
}
