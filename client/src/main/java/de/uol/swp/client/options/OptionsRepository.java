package de.uol.swp.client.options;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionsRepository {
    private boolean chatEnabled;
    private double volume;

    public OptionsRepository() {
        this.chatEnabled = true;
        this.volume = 100.0;
    }
}
