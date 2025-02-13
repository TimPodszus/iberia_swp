package de.uol.swp.server.cards;

import com.google.inject.Inject;
import de.uol.swp.server.AbstractService;
import org.greenrobot.eventbus.EventBus;

public class CardService extends AbstractService {

    /**
     * Constructor
     *
     * @param bus the EvenBus used throughout the server
     */
    @Inject
    public CardService(EventBus bus) {
        super(bus);
    }


}
