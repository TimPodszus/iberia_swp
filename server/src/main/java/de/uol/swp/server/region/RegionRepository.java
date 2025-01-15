package de.uol.swp.server.region;

import de.uol.swp.common.city.CityName;
import de.uol.swp.server.city.CityRepository;
import de.uol.swp.server.city.data.CityName;
import de.uol.swp.server.region.data.IRegion;
import de.uol.swp.server.region.data.Region;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing regions in the game.
 */
public class RegionRepository {
    /**
     * The repository for managing city data.
     */
    private final CityRepository cityRepository;

    /**
     * List of all regions.
     */
    @Getter
    private List<IRegion> regions;

    /**
     * Constructor that initializes the region repository by creating all regions.
     *
     * @param cityRepository the repository for managing city data
     */
    public RegionRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
        createAllRegions();
    }

    /**
     * Creates and initializes the list of all regions with their respective attributes.
     */
    private void createAllRegions() {
        regions = new ArrayList<>();

        regions.add(new Region(
                1,
                cityRepository.getCitiesByNames(
                        CityName.A_CORUNA,
                        CityName.SANTIAGO_DE_COMPOSTELA,
                        CityName.OURENSE,
                        CityName.LEON,
                        CityName.GIJON
                )
        ));
        regions.add(new Region(
                2,
                cityRepository.getCitiesByNames(CityName.SANTIAGO_DE_COMPOSTELA, CityName.VIGO, CityName.OURENSE)
        ));
        regions.add(new Region(
                3,
                cityRepository.getCitiesByNames(
                        CityName.VIGO,
                        CityName.BRAGA,
                        CityName.SALAMANCA,
                        CityName.LEON,
                        CityName.OURENSE
                )
        ));
        regions.add(new Region(4, cityRepository.getCitiesByNames(CityName.VIGO, CityName.PORTO, CityName.BRAGA)));
        regions.add(new Region(
                5,
                cityRepository.getCitiesByNames(
                        CityName.BRAGA,
                        CityName.PORTO,
                        CityName.COIMBRA,
                        CityName.CACERES,
                        CityName.SALAMANCA
                )
        ));
        regions.add(new Region(6, cityRepository.getCitiesByNames(CityName.PORTO, CityName.LISBOA, CityName.COIMBRA)));
        regions.add(new Region(
                7,
                cityRepository.getCitiesByNames(
                        CityName.COIMBRA,
                        CityName.LISBOA,
                        CityName.EVORA,
                        CityName.BADAJOZ,
                        CityName.CACERES
                )
        ));
        regions.add(new Region(
                8,
                cityRepository.getCitiesByNames(CityName.LISBOA, CityName.ALBUFEIRA, CityName.EVORA, CityName.HUELVA)
        ));
        regions.add(new Region(
                9,
                cityRepository.getCitiesByNames(
                        CityName.EVORA,
                        CityName.SEVILLA,
                        CityName.CORDOBA,
                        CityName.BADAJOZ,
                        CityName.HUELVA
                )
        ));
        regions.add(new Region(
                10,
                cityRepository.getCitiesByNames(CityName.SEVILLA, CityName.CADIZ, CityName.GIBRALTAR, CityName.MALAGA)
        ));
        regions.add(new Region(
                11,
                cityRepository.getCitiesByNames(CityName.SEVILLA, CityName.MALAGA, CityName.CORDOBA)
        ));
        regions.add(new Region(
                12,
                cityRepository.getCitiesByNames(CityName.BADAJOZ, CityName.CORDOBA, CityName.CIUDAD_REAL)
        ));
        regions.add(new Region(
                13,
                cityRepository.getCitiesByNames(CityName.CORDOBA, CityName.MALAGA, CityName.GRANADA, CityName.JAEN)
        ));
        regions.add(new Region(
                14,
                cityRepository.getCitiesByNames(CityName.MALAGA, CityName.ALMERIA, CityName.GRANADA)
        ));
        regions.add(new Region(
                15,
                cityRepository.getCitiesByNames(
                        CityName.JAEN,
                        CityName.GRANADA,
                        CityName.ALMERIA,
                        CityName.CARTAGENA,
                        CityName.ALBACETE
                )
        ));
        regions.add(new Region(
                16,
                cityRepository.getCitiesByNames(
                        CityName.CIUDAD_REAL,
                        CityName.CORDOBA,
                        CityName.JAEN,
                        CityName.ALBACETE
                )
        ));
        regions.add(new Region(
                17,
                cityRepository.getCitiesByNames(
                        CityName.ALBACETE,
                        CityName.CARTAGENA,
                        CityName.ALICANTE,
                        CityName.VALENCIA
                )
        ));
        regions.add(new Region(
                18,
                cityRepository.getCitiesByNames(
                        CityName.MADRID,
                        CityName.CIUDAD_REAL,
                        CityName.ALBACETE,
                        CityName.CUENCA
                )
        ));
        regions.add(new Region(
                19,
                cityRepository.getCitiesByNames(CityName.CUENCA, CityName.ALBACETE, CityName.VALENCIA)
        ));
        regions.add(new Region(
                20,
                cityRepository.getCitiesByNames(CityName.CUENCA, CityName.VALENCIA, CityName.TARRAGONA, CityName.TERUEL)
        ));
        regions.add(new Region(
                21,
                cityRepository.getCitiesByNames(
                        CityName.VALENCIA,
                        CityName.PALMA_DE_MALLORCA,
                        CityName.BARCELONA,
                        CityName.TARRAGONA
                )
        ));
        regions.add(new Region(
                22,
                cityRepository.getCitiesByNames(
                        CityName.ZARAGOZA,
                        CityName.TERUEL,
                        CityName.TARRAGONA,
                        CityName.BARCELONA
                )
        ));
        regions.add(new Region(
                23,
                cityRepository.getCitiesByNames(
                        CityName.HUESCA,
                        CityName.ZARAGOZA,
                        CityName.BARCELONA,
                        CityName.GIRONA,
                        CityName.ANDORRA_LA_VELLA
                )
        ));
        regions.add(new Region(
                24,
                cityRepository.getCitiesByNames(
                        CityName.VICTORIA_GASTEIZ,
                        CityName.ZARAGOZA,
                        CityName.HUESCA,
                        CityName.PAMPLONA
                )
        ));
        regions.add(new Region(
                25,
                cityRepository.getCitiesByNames(
                        CityName.BILBAO_BILBO,
                        CityName.VICTORIA_GASTEIZ,
                        CityName.PAMPLONA,
                        CityName.SAN_SEBASTIAN_DONOSTIA
                )
        ));
        regions.add(new Region(
                26, cityRepository.getCitiesByNames(
                CityName.SANTANDER,
                CityName.VALLADOLID,
                CityName.BURGOS,
                CityName.VICTORIA_GASTEIZ,
                CityName.BILBAO_BILBO
        )
        ));
        regions.add(new Region(
                27,
                cityRepository.getCitiesByNames(
                        CityName.GIJON,
                        CityName.LEON,
                        CityName.SALAMANCA,
                        CityName.VALLADOLID,
                        CityName.SANTANDER
                )
        ));
        regions.add(new Region(
                28,
                cityRepository.getCitiesByNames(CityName.SALAMANCA, CityName.MADRID, CityName.VALLADOLID)
        ));
        regions.add(new Region(
                29,
                cityRepository.getCitiesByNames(
                        CityName.VALLADOLID,
                        CityName.MADRID,
                        CityName.ZARAGOZA,
                        CityName.SORIA,
                        CityName.BURGOS
                )
        ));
        regions.add(new Region(
                30,
                cityRepository.getCitiesByNames(
                        CityName.BURGOS,
                        CityName.SORIA,
                        CityName.ZARAGOZA,
                        CityName.VICTORIA_GASTEIZ
                )
        ));
        regions.add(new Region(
                31,
                cityRepository.getCitiesByNames(CityName.MADRID, CityName.CUENCA, CityName.TERUEL, CityName.ZARAGOZA)
        ));
        regions.add(new Region(
                32,
                cityRepository.getCitiesByNames(CityName.TOLEDO, CityName.CIUDAD_REAL, CityName.MADRID)
        ));
        regions.add(new Region(
                33,
                cityRepository.getCitiesByNames(CityName.SALAMANCA, CityName.CACERES, CityName.TOLEDO, CityName.MADRID)
        ));
        regions.add(new Region(
                34,
                cityRepository.getCitiesByNames(
                        CityName.CACERES,
                        CityName.BADAJOZ,
                        CityName.CIUDAD_REAL,
                        CityName.TOLEDO
                )
        ));
    }

    /**
     * Retrieves a region by its unique identifier.
     *
     * @param id the unique identifier of the region
     * @return the region with the specified id
     */
    public IRegion getRegionByID(int id) {
        return regions.stream()
                      .filter(region -> region.getId() == id)
                      .findFirst()
                      .orElseThrow();
    }

    /**
     * Retrieves a list of regions that contain the specified city.
     *
     * @param cityName the name of the city
     * @return a list of regions containing the specified city
     */
    public List<IRegion> getRegionsByCityName(CityName cityName) {
        return regions.stream()
                      .filter(region -> region.getSurroundingCities()
                                              .stream()
                                              .anyMatch(city -> city.getName()
                                                                    .equals(cityName)))
                      .toList();
    }
}