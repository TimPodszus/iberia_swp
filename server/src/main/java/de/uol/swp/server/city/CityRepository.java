package de.uol.swp.server.city;

import de.uol.swp.server.plague.PlagueName;

import java.util.ArrayList;
import java.util.List;

public class CityRepository
{

    public static List<City> getAllCities()
    {
        List<City> cities = new ArrayList<>();

        cities.add(new City(PlagueName.YELLOW_FEVER, "Albacete", 1100, false, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Zaragoza", -24, false, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Alicante", -324, true, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Cuenca", 784, false, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Barcelona", -15, true, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Andorra la Vella", 1278, false, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Tarragona", -450, true, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Valencia", -138, true, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Cartagena", -227, true, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Palma de Mallorca", -123, true, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Teruel", 1171, false, false));
        cities.add(new City(PlagueName.YELLOW_FEVER, "Girona", -79, false, false));

        cities.add(new City(PlagueName.CHOLERA, "Evora", -59, false, false));
        cities.add(new City(PlagueName.CHOLERA, "Lisboa", -1000, true, false));
        cities.add(new City(PlagueName.CHOLERA, "Coimbra", -45, false, false));
        cities.add(new City(PlagueName.CHOLERA, "Caceres", -34, false, false));
        cities.add(new City(PlagueName.CHOLERA, "Albufeira", 750, true, false));
        cities.add(new City(PlagueName.CHOLERA, "Braga", -16, false, false));
        cities.add(new City(PlagueName.CHOLERA, "Ourense", -25, false, false));
        cities.add(new City(PlagueName.CHOLERA, "Salamanca", -250, false, false));
        cities.add(new City(PlagueName.CHOLERA, "Vigo", -150, true, false));
        cities.add(new City(PlagueName.CHOLERA, "Porto", -136, true, false));
        cities.add(new City(PlagueName.CHOLERA, "A Coruna", 1208, true, false));
        cities.add(new City(PlagueName.CHOLERA, "Santiago de Compostela", 850, false, false));

        cities.add(new City(PlagueName.MALARIA, "Almeria", 995, true, false));
        cities.add(new City(PlagueName.MALARIA, "Huelva", -950, true, false));
        cities.add(new City(PlagueName.MALARIA, "Granada", -550, false, false));
        cities.add(new City(PlagueName.MALARIA, "Badajoz", 875, false, false));
        cities.add(new City(PlagueName.MALARIA, "Gibraltar", 1160, true, false));
        cities.add(new City(PlagueName.MALARIA, "Malaga", -750, true, false));
        cities.add(new City(PlagueName.MALARIA, "Toledo", -300, false, false));
        cities.add(new City(PlagueName.MALARIA, "Cadiz", -1104, true, false));
        cities.add(new City(PlagueName.MALARIA, "Cordoba", -169, false, false));
        cities.add(new City(PlagueName.MALARIA, "Sevilla", -850, false, false));
        cities.add(new City(PlagueName.MALARIA, "Jaen", -231, false, false));
        cities.add(new City(PlagueName.MALARIA, "Ciudad Real", 1255, false, false));

        cities.add(new City(PlagueName.TYPHUS, "San Sebastian-Donostia", 1180, true, false));
        cities.add(new City(PlagueName.TYPHUS, "Santander", -26, true, false));
        cities.add(new City(PlagueName.TYPHUS, "Gijon", -500, true, false));
        cities.add(new City(PlagueName.TYPHUS, "Soria", -133, false, false));
        cities.add(new City(PlagueName.TYPHUS, "Burgos", 884, false, false));
        cities.add(new City(PlagueName.TYPHUS, "Victoria-Gasteiz", 1181, false, false));
        cities.add(new City(PlagueName.TYPHUS, "Pamplona", -74, false, false));
        cities.add(new City(PlagueName.TYPHUS, "Madrid", 871, false, false));
        cities.add(new City(PlagueName.TYPHUS, "Huesca", -350, false, false));
        cities.add(new City(PlagueName.TYPHUS, "Leon", -29, false, false));
        cities.add(new City(PlagueName.TYPHUS, "Bilbao-Bilbo", 1300, false, false));
        cities.add(new City(PlagueName.TYPHUS, "Valladolid", 1072, false, false));

        return cities;
    }
}
