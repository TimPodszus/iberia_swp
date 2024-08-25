package de.uol.swp.server.cards;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import de.uol.swp.server.cards.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventCard implements Card {
    private final String action;
}
