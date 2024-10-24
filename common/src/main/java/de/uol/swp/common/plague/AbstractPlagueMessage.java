package de.uol.swp.common.plague;

import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.message.AbstractServerMessage;

import java.util.Objects;

/**
 * An abstract message class for plague-related communications.
 * This class serves as a base for all messages related to plagues. It contains
 * the plague name and standard methods like equals and hashCode for comparison.
 * Any plague-related message should extend this class.
 */
public class AbstractPlagueMessage extends AbstractServerMessage {
    PlagueName name;

    public AbstractPlagueMessage() {

    }
    public AbstractPlagueMessage(PlagueName name) {
        this.name = name;
    }

    public PlagueName getName() {
        return name;
    }

    public void setName(PlagueName name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPlagueMessage that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName());
    }
}
