package de.uol.swp.server.city;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CityName {
    // Yellow Fever
    ALBACETE("Albacete"), ZARAGOZA("Zaragoza"), ALICANTE("Alicante"), CUENCA("Cuenca"), BARCELONA("Barcelona"), ANDORRA_LA_VELLA(
            "Andorra la Vella"), TARRAGONA("Tarragona"), VALENCIA("Valencia"), CARTAGENA("Cartagena"), PALMA_DE_MALLORCA(
            "Palma de Mallorca"), TERUEL("Teruel"), GIRONA("Girona"),

    // Cholera
    EVORA("Evora"), LISBOA("Lisboa"), COIMBRA("Coimbra"), CACERES("Caceres"), ALBUFEIRA("Albufeira"), BRAGA("Braga"), OURENSE(
            "Ourense"), SALAMANCA("Salamanca"), VIGO("Vigo"), PORTO("Porto"), A_CORUNA("A Coruna"), SANTIAGO_DE_COMPOSTELA(
            "Santiago de Compostela"),

    // Malaria
    ALMERIA("Almeria"), HUELVA("Huelva"), GRANADA("Granada"), BADAJOZ("Badajoz"), GIBRALTAR("Gibraltar"), MALAGA(
            "Malaga"), TOLEDO("Toledo"), CADIZ("Cadiz"), CORDOBA("Cordoba"), SEVILLA("Sevilla"), JAEN("Jaen"), CIUDAD_REAL(
            "Ciudad Real"),

    // Typhus
    SAN_SEBASTIAN_DONOSTIA("San Sebastian-Donostia"), SANTANDER("Santander"), GIJON("Gijon"), SORIA("Soria"), BURGOS(
            "Burgos"), VICTORIA_GASTEIZ("Victoria-Gasteiz"), PAMPLONA("Pamplona"), MADRID("Madrid"), HUESCA("Huesca"), LEON(
            "Leon"), BILBAO_BILBO("Bilbao-Bilbo"), VALLADOLID("Valladolid");

    private final String displayName;

    /**
     * Returns the CityName enum constant with the specified display name.
     *
     * @param displayName the display name of the city
     * @return the CityName enum constant with the specified display name, or null if no such constant exists
     */
    public static CityName fromDisplayName(String displayName) {
        for (CityName cityName : CityName.values()) {
            if (cityName.getDisplayName()
                        .equals(displayName)) {
                return cityName;
            }
        }
        return null;
    }
}
