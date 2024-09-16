package de.uol.swp.server.region;

import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityName;
import de.uol.swp.server.city.CityRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionRepository
{
    // Private constructor to hide the implicit public one
    private RegionRepository()
    {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<Region> getAllRegions()
    {
        List<Region> regions = new ArrayList<>();
        List<City> allCities = CityRepository.getAllCities();

        regions.add(new Region(1, getCitiesByNames(allCities,
                CityName.A_CORUNA,
                CityName.SANTIAGO_DE_COMPOSTELA,
                CityName.OURENSE,
                CityName.LEON,
                CityName.GIJON
        )));
        regions.add(new Region(2,
                getCitiesByNames(allCities, CityName.SANTIAGO_DE_COMPOSTELA, CityName.VIGO, CityName.OURENSE)
        ));
        regions.add(new Region(3,
                getCitiesByNames(allCities,
                        CityName.VIGO,
                        CityName.BRAGA,
                        CityName.SALAMANCA,
                        CityName.LEON,
                        CityName.OURENSE
                )
        ));
        regions.add(new Region(4, getCitiesByNames(allCities, CityName.VIGO, CityName.PORTO, CityName.BRAGA)));
        regions.add(new Region(5,
                getCitiesByNames(allCities,
                        CityName.BRAGA,
                        CityName.PORTO,
                        CityName.COIMBRA,
                        CityName.CACERES,
                        CityName.SALAMANCA
                )
        ));
        regions.add(new Region(6, getCitiesByNames(allCities, CityName.PORTO, CityName.LISBOA, CityName.COIMBRA)));
        regions.add(new Region(7,
                getCitiesByNames(allCities,
                        CityName.COIMBRA,
                        CityName.LISBOA,
                        CityName.EVORA,
                        CityName.BADAJOZ,
                        CityName.CACERES
                )
        ));
        regions.add(new Region(8, getCitiesByNames(allCities, CityName.LISBOA, CityName.ALBUFEIRA, CityName.EVORA)));
        regions.add(new Region(9,
                getCitiesByNames(allCities, CityName.EVORA, CityName.SEVILLA, CityName.CORDOBA, CityName.BADAJOZ)
        ));
        regions.add(new Region(10,
                getCitiesByNames(allCities, CityName.SEVILLA, CityName.CADIZ, CityName.GIBRALTAR, CityName.MALAGA)
        ));
        regions.add(new Region(11, getCitiesByNames(allCities, CityName.SEVILLA, CityName.MALAGA, CityName.CORDOBA)));
        regions.add(new Region(12,
                getCitiesByNames(allCities, CityName.BADAJOZ, CityName.CORDOBA, CityName.CIUDAD_REAL)
        ));
        regions.add(new Region(13,
                getCitiesByNames(allCities, CityName.CORDOBA, CityName.MALAGA, CityName.GRANADA, CityName.JAEN)
        ));
        regions.add(new Region(14, getCitiesByNames(allCities, CityName.MALAGA, CityName.ALMERIA, CityName.GRANADA)));
        regions.add(new Region(15,
                getCitiesByNames(allCities,
                        CityName.JAEN,
                        CityName.GRANADA,
                        CityName.ALMERIA,
                        CityName.CARTAGENA,
                        CityName.ALBACETE
                )
        ));
        regions.add(new Region(16,
                getCitiesByNames(allCities, CityName.CIUDAD_REAL, CityName.CORDOBA, CityName.JAEN, CityName.ALBACETE)
        ));
        regions.add(new Region(17,
                getCitiesByNames(allCities, CityName.ALBACETE, CityName.CARTAGENA, CityName.ALICANTE, CityName.VALENCIA)
        ));
        regions.add(new Region(18,
                getCitiesByNames(allCities, CityName.MADRID, CityName.CIUDAD_REAL, CityName.ALBACETE, CityName.CUENCA)
        ));
        regions.add(new Region(19, getCitiesByNames(allCities, CityName.CUENCA, CityName.ALBACETE, CityName.VALENCIA)));
        regions.add(new Region(20,
                getCitiesByNames(allCities, CityName.CUENCA, CityName.VALENCIA, CityName.TARRAGONA, CityName.TERUEL)
        ));
        regions.add(new Region(21,
                getCitiesByNames(allCities,
                        CityName.VALENCIA,
                        CityName.PALMA_DE_MALLORCA,
                        CityName.BARCELONA,
                        CityName.TARRAGONA
                )
        ));
        regions.add(new Region(22,
                getCitiesByNames(allCities, CityName.ZARAGOZA, CityName.TERUEL, CityName.TARRAGONA, CityName.BARCELONA)
        ));
        regions.add(new Region(23, getCitiesByNames(allCities,
                CityName.HUESCA,
                CityName.ZARAGOZA,
                CityName.BARCELONA,
                CityName.GIRONA,
                CityName.ANDORRA_LA_VELLA
        )));
        regions.add(new Region(24,
                getCitiesByNames(allCities,
                        CityName.VICTORIA_GASTEIZ,
                        CityName.ZARAGOZA,
                        CityName.HUESCA,
                        CityName.PAMPLONA
                )
        ));
        regions.add(new Region(25, getCitiesByNames(allCities,
                CityName.BILBAO_BILBO,
                CityName.VICTORIA_GASTEIZ,
                CityName.PAMPLONA,
                CityName.SAN_SEBASTIAN_DONOSTIA
        )));
        regions.add(new Region(26, getCitiesByNames(allCities,
                CityName.SANTANDER,
                CityName.VALLADOLID,
                CityName.BURGOS,
                CityName.VICTORIA_GASTEIZ,
                CityName.BILBAO_BILBO
        )));
        regions.add(new Region(27, getCitiesByNames(allCities,
                CityName.GIJON,
                CityName.LEON,
                CityName.SALAMANCA,
                CityName.VALLADOLID,
                CityName.SANTANDER
        )));
        regions.add(new Region(28,
                getCitiesByNames(allCities, CityName.SALAMANCA, CityName.MADRID, CityName.VALLADOLID)
        ));
        regions.add(new Region(29,
                getCitiesByNames(allCities,
                        CityName.VALLADOLID,
                        CityName.MADRID,
                        CityName.ZARAGOZA,
                        CityName.SORIA,
                        CityName.BURGOS
                )
        ));
        regions.add(new Region(30,
                getCitiesByNames(allCities,
                        CityName.BURGOS,
                        CityName.SORIA,
                        CityName.ZARAGOZA,
                        CityName.VICTORIA_GASTEIZ
                )
        ));
        regions.add(new Region(31,
                getCitiesByNames(allCities, CityName.MADRID, CityName.CUENCA, CityName.TERUEL, CityName.ZARAGOZA)
        ));
        regions.add(new Region(32,
                getCitiesByNames(allCities, CityName.TOLEDO, CityName.CIUDAD_REAL, CityName.MADRID)
        ));
        regions.add(new Region(33,
                getCitiesByNames(allCities, CityName.SALAMANCA, CityName.CACERES, CityName.TOLEDO, CityName.MADRID)
        ));
        regions.add(new Region(34,
                getCitiesByNames(allCities, CityName.CACERES, CityName.BADAJOZ, CityName.CIUDAD_REAL, CityName.TOLEDO)
        ));

        return regions;
    }

    private static List<City> getCitiesByNames(List<City> allCities, CityName... names)
    {
        List<CityName> nameList = Arrays.asList(names);
        return allCities.stream()
                        .filter(city -> nameList.contains(city.getName()))
                        .toList();
    }
}