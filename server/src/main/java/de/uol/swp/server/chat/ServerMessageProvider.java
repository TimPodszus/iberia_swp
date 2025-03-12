package de.uol.swp.server.chat;

import de.uol.swp.server.city.data.ICity;
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
}
