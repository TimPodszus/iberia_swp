package de.uol.swp.common.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    AGRICULTURAL_SCIENTIST(
            "#60BC46",
            "Agrarwissenschaftler",
            "Extraaktion um einen Wasseraufbereitungsmarker zu platzieren. Bei Wasseraufbereitung, darf ein " + "zusätzlicher Marker platziert werden"
    ), POLITICIAN(
            "#F1D61B",
            "Politiker",
            "Extraaktion um eine Stadtkarte an einen Spieler zu geben. Extraaktion um eine Stadtkarte auf der Hand " + "mit einer Stadtkarte aud dem Ablagestapel zu tauschen"
    ), RAILWAY_PERSON(
            "#FF0000",
            "Eisenbahner",
            "Beim Schienenbau dürfen zwei Schienen platziert werden. Beim ZUgfahren," + " darf ein Mitspieler mitgenommen werden."
    ), SAILOR(
            "#1DA6C3",
            "Seemann",
            "Schifffahren ist ohne Marker möglich. Beim Schifffahren darf ein Mitspieler mitgenommen werden."
    ), SCIENTIST_OF_THE_ROYAL_ACADEMY(
            "#FF0000",
            "Wissenschaftler der Königlichen Akademie",
            "Wasser aufbereiten mit belebiger Stadtkarte möglich. Extraaktion um die nächsten drei Spielerkarten zu " + "sortieren."
    ), NURSE(
            "#D44446",
            "Krankenschwester",
            "Ein Präventionsmarker bewegt sich auf dem Feld mit. Städte an einer Region " + "mit Präventionsmarker, können nicht infiziert werden."
    ), COUNTRY_DOCTOR("#CE74A8", "Landarzt", "Beim Seuche behandeln, darf ein weiterer Seuchenwürfel entfernt werden."),
    ;

    private final String colorCode;
    private final String name;
    private final String description;
}