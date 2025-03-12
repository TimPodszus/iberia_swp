package de.uol.swp.server.plague;

import com.google.inject.Inject;
import de.uol.swp.common.city.ICityDTO;
import de.uol.swp.common.game.PlagueName;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.AbstractGameResponse;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.game.message.response.StatusResponse;
import de.uol.swp.common.infection.IInfectionDTO;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.plague.message.request.AvailablePlaguesRequest;
import de.uol.swp.common.plague.message.request.ResearchPlagueRequest;
import de.uol.swp.common.plague.message.request.TreatPlagueRequest;
import de.uol.swp.common.plague.message.response.AvailablePlaguesResponse;
import de.uol.swp.common.plague.message.response.MigrationOverseasResponse;
import de.uol.swp.common.plague.message.response.TreatPlagueResponse;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.events.MigrationOverseasEvent;
import de.uol.swp.server.city.CityMapper;
import de.uol.swp.server.city.data.ICity;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.PlayerTurnState;
import de.uol.swp.server.game.states.TreatExtraPlagueState;
import de.uol.swp.server.infection.InfectionMapper;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.plague.management.IPlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagement;
import de.uol.swp.server.plague.management.PlagueManagementException;
import de.uol.swp.server.plague.management.PlagueNotFoundException;
import de.uol.swp.server.role.CountryDoctor;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class PlagueService extends AbstractService {
    private static final Logger LOG = LogManager.getLogger(PlagueService.class);

    private final IPlagueManagement plagueManagement;
    private final ILobbyManagement lobbyManagement;


    /**
     * Constructor
     *
     * @param plagueManagement The management class for researching plagues
     * @param eventBus         The server-wide EventBus
     * @since 2024-10-04
     */
    @Inject
    public PlagueService(IPlagueManagement plagueManagement, EventBus eventBus, ILobbyManagement lobbyManagement) {
        super(eventBus);
        this.plagueManagement = plagueManagement;
        this.lobbyManagement = lobbyManagement;
    }

    /**
     * Handles ResearchPlagueRequest found on the EventBus.
     * If a ResearchPlagueRequest is detected, it triggers the plague research process.
     *
     * @param request The ResearchPlagueRequest found on the EventBus
     * @see PlagueManagement#researchPlague(IGame)
     * @since 2024-10-04
     */
    @Subscribe
    public void onResearchPlagueRequest(
            ResearchPlagueRequest request
    ) {
        LOG.debug("ResearchPlagueRequest received");
        AbstractGameResponse response;
        Session session = request.getSession()
                                 .orElse(null);
        IGame game = plagueManagement.getGame(request.getLobbyId());
        try {
            plagueManagement.researchPlague(game);
            response = new StatusResponse(request.getLobbyId(), true, "Plage wurde erforscht");
        } catch (PlagueManagementException e) {
            LOG.error("Error while researching plague", e);
            response = new StatusResponse(request.getLobbyId(), false, "Fehler beim Erforschen der Seuche");
        } catch (IllegalGameStateException e) {
            LOG.error("Game is in an illegal state for researching plague");
            response = new StatusResponse(
                    request.getLobbyId(),
                    false,
                    "In dem Zustand des Spiels kann die Seuche nicht erforscht werden."
            );
        } catch (GameException e) {
            throw new RuntimeException(e);
        }


        response.setSession(session);
        post(response);

        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        LOG.debug("Sending new BoardUpdateEvent after successful plague research");
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
        sendServerMessageEvent(game.getGameId(), "Eine neue Plage wurde erforscht");
    }

    /**
     * Handles an incoming request for available plagues in the player's current city.
     * Retrieves the list of infections in the city and sends a response with available plagues.
     *
     * @param request The request containing the lobby ID and session details.
     */
    @Subscribe
    public void onAvailablePlaguesRequest(
            AvailablePlaguesRequest request
    ) {
        LOG.debug("Received AvailablePlaguesRequest for lobbyId: {}", request.getLobbyId());
        IGame game = plagueManagement.getGame(request.getLobbyId());

        List<IInfectionDTO> infectionsInCity = InfectionMapper.toDTOList(plagueManagement.getInfectionsInCity(
                game,
                request.getCityId()
        ));
        LOG.debug(
                "Found {} infections in city {}",
                infectionsInCity.size(),
                game.getCurrentPlayer()
                    .getCurrentPosition()
                    .getName()
        );

        AvailablePlaguesResponse availablePlaguesResponse = new AvailablePlaguesResponse(
                request.getLobbyId(),
                true,
                infectionsInCity,
                request.getCityId()
        );
        request.getSession()
               .ifPresent(availablePlaguesResponse::setSession);
        request.getMessageContext()
               .ifPresent(availablePlaguesResponse::setMessageContext);

        LOG.debug(
                "Sending AvailablePlaguesResponse with {} plagues and role: {}",
                infectionsInCity.size(),
                game.getCurrentPlayer()
                    .getRole()
                    .getName()
        );
        post(availablePlaguesResponse);
    }

    /**
     * Handles a request to treat a plague in a city.
     * Removes one instance of the specified plague from the city and sends a response.
     *
     * @param request The request containing the lobby ID, city ID, plague name, and doctor role.
     */
    @Subscribe
    public void onTreatPlagueRequest(TreatPlagueRequest request) {
        AbstractResponseMessage response;
        LOG.debug(
                "Received TreatPlagueRequest for lobbyId: {}, cityId: {}, plagueName: {}",
                request.getLobbyId(),
                request.getCityId(),
                request.getPlagueName()
        );
        IGame game = plagueManagement.getGame(request.getLobbyId());
        ICity city = game.getCityRepository()
                         .getCity(request.getCityId());

        LOG.debug("Removing one plague cube of type {} from city {}", request.getPlagueName(), city.getName());
        try {
            plagueManagement.treatPlague(request.getPlagueName(), city, game);
        } catch (IllegalGameStateException e) {
            LOG.error("Game is in an illegal state for treating plague");
            sendStatusResponse(request, false, "In dem Zustand des Spiels kann die Seuche nicht behandelt werden.");

            return;
        } catch (PlagueNotFoundException e) {
            LOG.error("Plague {} not found", request.getPlagueName());
            sendStatusResponse(request, false, "Die Seuche wurde nicht gefunden.");

            return;
        }
        boolean isCountryDoctor = game.getCurrentPlayer()
                                      .getRole() instanceof CountryDoctor;
        boolean isPlayerTurnState = game.getState() instanceof PlayerTurnState;
        boolean isEventState = game.getState() instanceof EventState;

        if (isCountryDoctor && isPlayerTurnState || isEventState) {
            List<ICityDTO> availableCities;
            if (isEventState) {
                game.setState(game.getPreviousState());
                availableCities = plagueManagement.getCitesWithPlagues(game);
                LOG.debug("EventState: sending available cities with plagues");
            } else {
                availableCities = CityMapper.toDTOList(plagueManagement.getCitiesNearBy(game, city));
                LOG.debug("PlayerTurnState: sending nearby cities");
            }
            if (!availableCities.isEmpty()) {
                game.setState(new TreatExtraPlagueState());

                response = new TreatPlagueResponse(request.getLobbyId(), true, availableCities, true);
                request.getSession()
                       .ifPresent(response::setSession);
                request.getMessageContext()
                       .ifPresent(response::setMessageContext);
                post(response);

                return;
            }
        } else {
            if (game.getState() instanceof TreatExtraPlagueState) {
                game.setState(game.getPreviousState());
            }
        }
        sendServerMessageEvent(request.getLobbyId(), "Die Seuche in der Stadt " + city.getName() + " wurde behandelt.");
        IGameDTO gameDTO = GameMapper.toDTO(plagueManagement.getGame(request.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
        sendServerMessageEvent(lobby.getLobbyId(),
                "In der Stadt " + game.getCurrentPlayer().getCurrentPosition().getName() + " wurde ein " + "Seuchenwürfel der " +
                        "Plage " + request.getPlagueName()
                                                                                                                         .toString() + " entfernt."
        );
    }

    /**
     * handles the MigrationOverseasEvent and sends a MigrationOverseasResponse
     * with the available cities to migrate to.
     *
     * @param event The MigrationOverseasEvent
     */
    @Subscribe
    public void onMigrationOverseasEvent(MigrationOverseasEvent event) {
        LOG.debug("[Lobbyid: {}] Received MigrationOverseasEvent", event.getLobbyId());
        IGame game = plagueManagement.getGame(event.getLobbyId());
        IUser user = game.getPlayer(event.getUsername())
                         .getUser();
        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(
                                                           "[LobbyId: {}] Session not found for user",
                                                           event.getLobbyId()
                                                   );
                                                   return new SessionNotFoundException();
                                               });
        List<ICityDTO> availableCities = plagueManagement.getCitesWithPlagues(game);
        MigrationOverseasResponse response = new MigrationOverseasResponse(event.getLobbyId(), true, availableCities);
        response.setSession(session);
        post(response);
        sendServerMessageEvent(
                event.getLobbyId(),
                user.getUsername() + " hat die Ereigniskarte 'Migration nach Übersee' gespielt und darf bis zu zwei Seuchenwürfel entfernen."
        );
    }
}
