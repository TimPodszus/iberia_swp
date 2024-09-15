package de.uol.swp.client.options.event;

import lombok.NoArgsConstructor;

/**
 * Event used to show the options view
 * <p>
 * In order to show the options view using this event, post an instance of it
 * onto the eventBus the SceneManager is subscribed to.
 *
 * @author Lars Hemmers
 * @see de.uol.swp.client.SceneManager
 * @since 2024-09-11
 */
@NoArgsConstructor
public class ShowOptionsViewEvent {
    // needed for eventBus
}
