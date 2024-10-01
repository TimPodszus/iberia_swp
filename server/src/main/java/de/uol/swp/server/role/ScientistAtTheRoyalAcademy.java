package de.uol.swp.server.role;

/**
 * Represents the role of a Scientist at the Royal Academy in the game.
 * This role allows the player to discard any city card regardless of color
 * when performing the "Water Treatment" action. Additionally, the player can
 * use an action to look at the next 3 player cards, rearrange them, and place
 * them back on the player card draw pile.
 */
public class ScientistAtTheRoyalAcademy extends Role {

    /**
     * Constructs a new ScientistAtTheRoyalAcademy role with predefined name and description.
     */
    public ScientistAtTheRoyalAcademy() {
        super(
                "Wissenschaftlerin der königlichen Akademie",
                "Bei der Aktion „Wasser aufbereiten“ darfst du eine beliebige Stadtkarte abwerfen, die Farbe spielt dabei keine Rolle. Nutze eine Aktion, um dir die nächsten 3 Spielerkarten anzusehen. Ordne sie nach Belieben neu und lege sie zurück auf den Spielerkarten-Nachziehstapel."
        );
    }
}