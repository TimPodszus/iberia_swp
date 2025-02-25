package de.uol.swp.common.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    AGRICULTURAL_SCIENTIST(
            "#1AB429",
            "Agrarwissenschaftler",
            "Extraaktion um einen Wasseraufbereitungsmarker zu platzieren. Bei Wasseraufbereitung, darf ein " + "zusätzlicher Marker platziert werden"
    ), POLITICIAN(
            "#CDD717",
            "Politiker",
            "Extraaktion um eine Stadtkarte an einen Spieler zu geben. Extraaktion um eine Stadtkarte auf der Hand " + "mit einer Stadtkarte aud dem Ablagestapel zu tauschen"
    ), RAILWAY_PERSON(
            "#705310",
            "Eisenbahner",
            "Beim Schienenbau dürfen zwei Schienen platziert werden. Beim ZUgfahren," + " darf ein Mitspieler mitgenommen werden."
    ), SAILOR(
            "#52B3EA",
            "Seemann",
            "Schifffahren ist ohne Marker möglich. Beim Schifffahren darf ein Mitspieler mitgenommen werden."
    ), SCIENTIST_OF_THE_ROYAL_ACADEMY(
            "#E9F6FC",
            "Wissenschaftler der Königlichen Akademie",
            "Wasser aufbereiten mit belebiger Stadtkarte möglich. Extraaktion um die nächsten drei Spielerkarten zu " + "sortieren."
    ), NURSE(
            "#F49092",
            "Krankenschwester",
            "Ein Präventionsmarker bewegt sich auf dem Feld mit. Städte an einer Region " + "mit Präventionsmarker, können nicht infiziert werden."
    ), COUNTRY_DOCTOR("#BD1986", "Landarzt", "Beim Seuche behandeln, darf ein weiterer Seuchenwürfel entfernt werden."),
    ;

    private final String colorCode;
    private final String name;
    private final String description;
}