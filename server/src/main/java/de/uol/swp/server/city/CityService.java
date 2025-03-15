package de.uol.swp.server.city;

import com.google.inject.Inject;
import de.uol.swp.common.city.message.request.BuildHospitalRequest;
import de.uol.swp.common.city.message.request.HospitalFoundationEventRequest;
import de.uol.swp.common.city.message.response.HospitalFoundationEventResponse;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.events.HospitalFoundationEvent;
import de.uol.swp.server.city.management.ICityManagement;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.management.IGameManagement;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import de.uol.swp.server.usermanagement.management.ServerUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CityService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(CityService.class);
    ICityManagement cityManagement;
    IGameManagement gameManagement;
    protected ILobbyManagement lobbyManagement;
    ServerUserService userManagement;

    /**
     * Constructor
     *
     * @param bus             the EventBus used throughout the server
     * @param cityManagement  the city management instance for city operations
     * @param gameManagement  the game management instance for game operations
     * @param lobbyManagement the lobby management instance for lobby operations
     */
    @Inject
    public CityService(
            EventBus bus,
            ICityManagement cityManagement,
            IGameManagement gameManagement,
            ILobbyManagement lobbyManagement,
            ServerUserService userManagement
    ) {
        super(bus);
        this.cityManagement = cityManagement;
        this.gameManagement = gameManagement;
        this.lobbyManagement = lobbyManagement;
        this.userManagement = userManagement;
    }

    /**
     * Handles the BuildHospitalRequest event.
     *
     * @param request the BuildHospitalRequest containing the details for building a hospital
     */
    @Subscribe
    public void onBuildHospitalRequest(BuildHospitalRequest request) {
        LOG.debug("Got BuildHospitalRequest for lobby {}", request.getLobbyId());

        try {
            cityManagement.buildHospital(
                    request.getLobbyId(),
                    request.getSession()
                           .orElseThrow()
                           .getUser()
                           .getUsername(),
                    request.getCityId()
            );

            IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()));
            ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
            sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
        } catch (GameException e) {
            LOG.error("Error building hospital: {} ", e.getMessage());
            sendStatusResponse(request, false, "Das Krankenhaus konnte nicht gebaut werden.");
        }
    }

    /**
     * Handles the HospitalFoundationEvent event.
     *
     * @param event the HospitalFoundationEvent containing the detail who wants to build a hospital
     */
    @Subscribe
    public void onHospitalFoundationEvent(HospitalFoundationEvent event) {
        LOG.debug("[Lobby: {}] Got HospitalFoundationEvent.", event.getLobbyId());
        IGame game = gameManagement.getGame(event.getLobbyId());
        PlagueName plagueName = game.getPlayer(event.getUsername())
                                    .getCurrentPosition()
                                    .getPlagueName();
        IUser user = userManagement.getUser(event.getUsername());
        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(
                                                           "[Lobby: {}] Session not found for user",
                                                           event.getLobbyId()
                                                   );
                                                   return new SessionNotFoundException();
                                               });

        HospitalFoundationEventResponse response = new HospitalFoundationEventResponse(
                event.getLobbyId(),
                game.getCityRepository()
                    .getCityIdsForPlague(plagueName)
        );
        response.setSession(session);
        sendResponseWithDelay(response);
    }

    /**
     * Handles the HospitalFoundationEventRequest event.
     *
     * @param event the HospitalFoundationEventRequest containing the details for building a hospital
     */
    @Subscribe
    public void onHospitalFoundationRequest(HospitalFoundationEventRequest event) {
        LOG.debug("[Lobby: {}] Got HospitalFoundationEventRequest.", event.getLobbyId());
        IGame game = gameManagement.getGame(event.getLobbyId());
        cityManagement.buildHospitalWithEventCard(
                event.getLobbyId(),
                event.getCityId(),
                event.getSession()
                     .orElseThrow()
                     .getUser()
                     .getUsername()
        );
        game.setState(game.getPreviousState());
        IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(event.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(event.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(event.getLobbyId(), gameDTO));
        LOG.debug("[Lobby: {}] HospitalFoundationEventRequest handled successfully.", event.getLobbyId());
    }
}