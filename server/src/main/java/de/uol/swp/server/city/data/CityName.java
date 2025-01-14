package de.uol.swp.server.city.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CityName {
    // Yellow Fever
    ALBACETE("Albacete", 34), ZARAGOZA("Zaragoza", 25), ALICANTE("Alicante", 32), CUENCA("Cuenca", 35), BARCELONA(
            "Barcelona",
            27
    ), ANDORRA_LA_VELLA("Andorra la Vella", 26), TARRAGONA("Tarragona", 30), VALENCIA("Valencia", 31), CARTAGENA(
            "Cartagena",
            33
    ), PALMA_DE_MALLORCA("Palma de Mallorca", 29), TERUEL("Teruel", 36), GIRONA("Girona", 28),


    // Cholera
    EVORA("Evora", 5), LISBOA("Lisboa", 3), COIMBRA("Coimbra", 2), CACERES("Caceres", 6), ALBUFEIRA(
            "Albufeira",
            4
    ), BRAGA("Braga", 8), OURENSE("Ourense", 11), SALAMANCA("Salamanca", 7), VIGO("Vigo", 9), PORTO(
            "Porto",
            1
    ), A_CORUNA("A Coruna", 12), SANTIAGO_DE_COMPOSTELA("Santiago de Compostela", 10),


    // Malaria
    ALMERIA("Almeria", 43), HUELVA("Huelva", 48), GRANADA("Granada", 42), BADAJOZ("Badajoz", 39), GIBRALTAR(
            "Gibraltar",
            45
    ), MALAGA("Malaga", 44), TOLEDO("Toledo", 37), CADIZ("Cadiz", 46), CORDOBA("Cordoba", 40), SEVILLA(
            "Sevilla",
            47
    ), JAEN("Jaen", 41), CIUDAD_REAL("Ciudad Real", 38),

    // Typhus
    SAN_SEBASTIAN_DONOSTIA("San Sebastian-Donostia", 22), SANTANDER("Santander", 15), GIJON("Gijon", 14), SORIA(
            "Soria",
            18
    ), BURGOS("Burgos", 19), VICTORIA_GASTEIZ("Victoria-Gasteiz", 20), PAMPLONA("Pamplona", 23), MADRID(
            "Madrid",
            17
    ), HUESCA("Huesca", 24), LEON("Leon", 13), BILBAO_BILBO("Bilbao-Bilbo", 21), VALLADOLID("Valladolid", 16);

    private final String displayName;

    private final Integer id;
}
