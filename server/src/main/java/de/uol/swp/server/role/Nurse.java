package de.uol.swp.server.role;

public class Nurse extends Role
{
    public Nurse()
    {
        super(
                "Krankenschwester",
                "Zu deiner Rolle gehört der Präventionsmarker. Platziere deinen Präventionsmarker beim Spielaufbau in einer an deine Stadt angrenzende Region. Städte, die an deinen Präventionsmarker angrenzen, können nicht infiziert werden. Der Präventionsmarker bewegt sich mit dir. Immer wenn du deine Spielfigur bewegst (oder sie von einem Mitspieler bewegt wird) – und zwar egal aus welchem Grund – legst du deinen Präventionsmarker in eine Region, die an deine Spielfigur angrenzt."
        );
    }
}
