package de.uol.swp.common.plague;

import lombok.Getter;

public class CanResearchPlagueRequest {
    @Getter
    private final String lobbyId;

    public CanResearchPlagueRequest(String lobbyId) {
        this.lobbyId = lobbyId;
    }

}

