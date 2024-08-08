package de.uol.swp.client;

import com.google.inject.Inject;
import de.uol.swp.client.user.ClientUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

/**
 * This class is the base for creating a new Presenter.
 *
 * This class prepares the child classes to have the UserService and EventBus set
 * in order to reduce unnecessary code repetition.
 *
 * @author Marco Grawunder
 * @since 2019-08-29
 */


public class AbstractPresenter {

    private static final Logger LOG = LogManager.getLogger(AbstractPresenter.class);

    @Inject
    protected ClientUserService userService;

    protected EventBus eventBus;

    /**
     * Sets the field eventBus
     *
     * This method sets the field eventBus to the EventBus given via parameter.
     * Afterwards it registers this class to the new EventBus.
     *
     * @implNote This method does not unregister this class from any EventBus it
     *           may already be registered to.
     * @param eventBus The EventBus this class should use.
     * @since 2019-08-29
     */
    @Inject
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        try {
            eventBus.register(this);
        }catch(Exception e){
            // register looks for @Subscribe that is not available in all classes
            LOG.warn("This class does not provide @Subscribe methods ..."+this.getClass());
        }
    }

    /**
     * Clears the field eventBus
     *
     * This method clears the field eventBus. Before clearing it unregisters this
     * class from EventBus previously used.
     *
     * @implNote This method does not check whether the field eventBus is null.
     *           The field is cleared by setting it to null.
     * @since 2019-08-29
     */
    public void clearEventBus(){
        this.eventBus.unregister(this);
        this.eventBus = null;
    }
}
