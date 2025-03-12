package de.uol.swp.server.chat;

import de.uol.swp.common.city.CityName;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.player.data.IPlayer;
import lombok.NoArgsConstructor;

import java.util.Random;

/**
 * Provides random messages for chat messages sent by the server.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ServerMessageProvider {

    private static final Random random = new Random();

    /**
     * Returns a random message for a new infection in a city.
     * @param plagueName The name of the plague
     * @param cityName The name of the city
     * @return A random message for a new infection in a city
     */
    public static String infectionMessage(String plagueName, String cityName) {
        int randomNumber = random.nextInt(3);
        return switch (randomNumber) {
            case 1 -> "In " + cityName + " erkranken mehr Menschen an " + plagueName + "!";
            case 2 -> "Die Seuche " + plagueName + " breitet sich in " + cityName + " aus!";
            default -> "Menschen in " + cityName + " sind an " + plagueName + " erkrankt!";
        };
    }

    /**
     * Returns a random message for an epidemic in a city.
     * @param cityName The name of the city
     * @return A random message for an epidemic in a city
     */
    public static String epidemicMessage(String cityName) {
        int randomNumber = random.nextInt(4);
        return switch (randomNumber) {
            case 1 -> "Die Verhältnisse in " + cityName + " verschlechtern sich und eine Epidemie bricht aus!";
            case 2 -> "In " + cityName + " bricht eine Epidemie aus!";
            case 3 -> "Die Krankenzahlen in " + cityName + " steigen und die Behörden sprechen mittlerweile von einer" +
                    " Epidemie!";
            default -> "Die Menschen in " + cityName + " sind von einer Epidemie betroffen!";
        };
    }

    /**
     * Returns a random message for an outbreak in a city.
     * @param cityName The name of the city
     * @param plagueName The name of the plague
     * @return A random message for an outbreak in a city
     */
    public static String outbreakMessage(String cityName, String plagueName) {
        int randomNumber = random.nextInt(3);
        return switch (randomNumber) {
            case 1 -> "In " + cityName + " bricht " + plagueName + " aus! Mittlerweile sind auch die umliegenden " +
                    "Städte von der Krankheit betroffen.";
            case 2 -> "Die Anzahl der an " + plagueName + " erkrankten Menschen in " + cityName + " steigt rapide an!" +
                    " Die umliegenden Städte verzeichnen auch erste Fälle.";
            default -> "Die Behörden in " + cityName + " melden einen Ausbruch von " + plagueName + ". Die Krankheit " +
                    "breitet sich schnell aus und umliegende Städte sind betroffen.";
        };
    }

    /**
     * Returns a random message for a new hospital in a city.
     *
     * @param city The city where the hospital is built
     * @return A random message for a new hospital in a city
     */
    public static String hospitalBuildMessage(ICity city) {
        int randomNumber = random.nextInt(4);
        String cityName = city.getName()
                              .getDisplayName();
        return switch (randomNumber) {
            case 1 -> "In " + cityName + " wird ein neues Krankenhaus gebaut!";
            case 2 -> "Die Behörden in " + cityName + " investieren in den Bau eines neuen Krankenhauses!";
            case 3 -> "Die Bewohner in " + cityName + " freuen sich über den Bau eines neuen Krankenhauses!";
            default -> "Die Stadt " + cityName + " erhält ein neues Krankenhaus!";
        };
    }

    /**
     * Returns a random message for sailing to another city.
     *
     * @param player The player who sails to another city
     * @param city   The city where the player sails to
     * @return A random message for sailing to another city
     */
    public static String sailMessage(IPlayer player, ICity city) {
        int randomNumber = random.nextInt(4);
        String cityName = city.getName()
                              .getDisplayName();
        String playerName = player.getUser()
                                  .getUsername();
        return switch (randomNumber) {
            case 1 -> "Leinen los! " + playerName + " segelt nach " + cityName + ".";
            case 2 -> "Ahoi! " + playerName + " macht sich auf den Weg nach " + cityName + ".";
            case 3 -> "Auf in fremde Gewässer! " + playerName + " segelt nach " + cityName + ".";
            default -> "Segel setzen! " + playerName + " macht sich auf den Weg nach " + cityName + ".";
        };
    }

    /**
     * Returns a random message for traveling by carriage to another city.
     *
     * @param player The player who travels by carriage to another city
     * @param city   The city where the player travels to
     * @return A random message for traveling by carriage to another city
     */
    public static String carriageMessage(IPlayer player, ICity city) {
        int randomNumber = random.nextInt(3);
        String cityName = city.getName()
                              .getDisplayName();
        String playerName = player.getUser()
                                  .getUsername();
        return switch (randomNumber) {
            case 1 -> "Die Pferde sind gesattelt! " + playerName + " reist per Kutsche nach " + cityName + ".";
            case 2 -> "Kutsche fährt vor! " + playerName + " macht sich auf den Weg nach " + cityName + ".";
            default -> playerName + " reist per Kutsche nach " + cityName + ".";
        };
    }

    /**
     * Returns a random message for traveling by train to another city.
     *
     * @param player The player who travels by train to another city
     * @param city   The city where the player travels to
     * @return A random message for traveling by train to another city
     */
    public static String trainMessage(IPlayer player, ICity city) {
        int randomNumber = random.nextInt(4);
        String cityName = city.getName()
                              .getDisplayName();
        String playerName = player.getUser()
                                  .getUsername();
        return switch (randomNumber) {
            case 1 -> "All aboard! " + playerName + " nimmt den Zug nach " + cityName + ".";
            case 2 -> "Einsteigen bitte! " + playerName + " fährt mit dem Zug nach " + cityName + ".";
            case 3 -> "Mind the gap! " + playerName + " reist per Eilzug nach " + cityName + ".";
            default ->
                    "Der Zug fährt los! Und mit ihm macht sich " + playerName + " auf den Weg nach " + cityName + ".";
        };
    }

    /**
     * Returns a random message for a new train connection between two cities.
     *
     * @param startCity The city where the train connection starts
     * @param endCity   The city where the train connection ends
     * @return A random message for a new train connection between two cities
     */
    public static String trainConnectionMessage(CityName startCity, CityName endCity) {
        int randomNumber = random.nextInt(4);
        String startCityName = startCity.getDisplayName();
        String endCityName = endCity.getDisplayName();
        return switch (randomNumber) {
            case 1 -> "Die Bahnverbindung zwischen " + startCityName + " und " + endCityName + " wird ausgebaut!";
            case 2 ->
                    "Es wird eine neue Bahnverbindung zwischen " + startCityName + " und " + endCityName + " eröffnet!";
            case 3 ->
                    "Die Menschen in " + startCityName + " und " + endCityName + " freuen sich über die neue " + "Zugverbindung!";
            default ->
                    "Ab sofort gibt es stündlich eine Zugverbindung zwischen " + startCityName + " und " + endCityName + "!";
        };
    }
}
