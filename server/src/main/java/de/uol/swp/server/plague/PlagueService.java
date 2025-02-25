package de.uol.swp.server.plague;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.plague.PlagueResearchedMessage;
import de.uol.swp.common.plague.ResearchPlagueRequest;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


@Singleton
public class PlagueService extends AbstractService {

    private final IPlagueManagement plagueManagement;

    private static final Logger LOG = LogManager.getLogger(PlagueService.class);



    /**
     * Constructor
     *
     * @param plagueManagement The management class for researching plagues
     * @param eventBus         The server-wide EventBus
     * @since 2024-10-04
     */
    @Inject
    public PlagueService(IPlagueManagement plagueManagement, EventBus eventBus) {
        super(eventBus);
        this.plagueManagement = plagueManagement;
    }

    /**
     * Handles ResearchPlagueRequest found on the EventBus.
     * If a ResearchPlagueRequest is detected, it triggers the plague research process.
     *
     * @param researchPlagueRequest The ResearchPlagueRequest found on the EventBus
     * @see PlagueManagement#researchPlague(IGame)
     * @since 2024-10-04
     */
    @Subscribe
    public void onResearchPlagueRequest(
            ResearchPlagueRequest researchPlagueRequest
    ) throws PlagueManagementException {

        LOG.debug("");
        IGame game = plagueManagement.getGame(researchPlagueRequest.getLobbyId());
        PlagueName name = researchPlagueRequest.getName();

        plagueManagement.researchPlague(game);

        LOG.debug("Sending PlagueResearchedMessage");
        sendToAll(new PlagueResearchedMessage(name));
    }

}
