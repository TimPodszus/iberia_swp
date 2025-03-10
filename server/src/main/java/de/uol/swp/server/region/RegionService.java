package de.uol.swp.server.region;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.message.request.AvailableRegionsRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentEventRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.message.request.WaterTreatmentRequest;
import de.uol.swp.common.region.message.response.AvailableRegionsResponse;
import de.uol.swp.common.region.message.response.CardsToDiscardForRegionResponse;
import de.uol.swp.common.region.message.response.TreatWaterEventResponse;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.events.TreatWaterEvent;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.exceptions.GameException;
import de.uol.swp.server.game.exceptions.IllegalGameStateException;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.PlaceExtraWaterTreatmentState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.management.IRegionManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import de.uol.swp.server.usermanagement.exceptions.SessionNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Set;

public class RegionService extends AbstractService {
    private IRegionManagement regionManagement;
    private IPlayerManagement playerManagement;
    private ILobbyManagement lobbyManagement;
    private static final Logger LOG = LogManager.getLogger(RegionService.class);

    /**
     * Constructor
     *
     * @param bus the EvenBus used throughout the server
     * @since 2019-10-08
     */
    @Inject
    public RegionService(
            EventBus bus,
            IRegionManagement regionManagement,
            IPlayerManagement playerManagement,
            ILobbyManagement lobbyManagement
    ) {
        super(bus);
        this.regionManagement = regionManagement;
        this.playerManagement = playerManagement;
        this.lobbyManagement = lobbyManagement;
    }

    @Subscribe
    public void onSendAvailableRegionsRequest(AvailableRegionsRequest request) {
        LOG.debug("[LobbyId: {}] Got AvailableRegionsRequest", request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error(
                                             "[LobbyId: {}] Session not present in AvailableDestinationsRequest",
                                             request.getLobbyId()
                                     );
                                     return new SessionNotFoundException();
                                 });
        IUserDTO user = session.getUser();

        Set<IRegionDTO> regions;
        regions = regionManagement.getAvailableRegions(user, request.getLobbyId());

        AvailableRegionsResponse response = new AvailableRegionsResponse(request.getLobbyId(), regions);
        response.setSession(session);
        post(response);
        LOG.info("[LobbyId: {}] Sent AvailableRegionsResponse", request.getLobbyId());
    }

    @Subscribe
    public void onWaterTreatmentRegionRequest(WaterTreatmentRegionRequest request) {
        LOG.debug("[LobbyId: {}] Got WaterTreatmentRegionRequest", request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error(
                                             "[LobbyId: {}] Session not present in WaterTreatmentRegionRequest",
                                             request.getLobbyId()
                                     );
                                     return new SessionNotFoundException();
                                 });
        IUserDTO user = session.getUser();

        List<CityCardDTO> cityCards;
        cityCards = regionManagement.getPossibleCityCardsToDiscard(user, request.getRegionId(), request.getLobbyId());
        CardsToDiscardForRegionResponse response = new CardsToDiscardForRegionResponse(request.getLobbyId(), cityCards);
        response.setSession(session);
        post(response);
        LOG.info("[LobbyId: {}] Sent CardsToDiscardForRegionResponse", request.getLobbyId());
    }

    @Subscribe
    public void onSendWaterTreatmentRequest(WaterTreatmentRequest request) {
        LOG.debug("[LobbyId: {}] Got WaterTreatmentRequest", request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error(
                                             "[LobbyId: {}] Session not present in WaterTreatmentRequest",
                                             request.getLobbyId()
                                     );
                                     return new SessionNotFoundException();
                                 });
        IUserDTO user = session.getUser();


        try {
            ICard card = null;
            if (request.getCard() != null) {
                card = playerManagement.getCard(
                        request.getLobbyId(),
                        user.getUsername(),
                        request.getCard()
                               .getId()
                );
            }
            regionManagement.increaseWaterTreatmentsFromRegion(
                    request.getLobbyId(),
                    request.getRegionId(),
                    request.getAmount(),
                    card,
                    UserMapper.toUser(user)
            );
        } catch (IllegalGameStateException e) {
            LOG.error(
                    "[LobbyId: {}] Game is in an invalid state, or player is not current player",
                    request.getLobbyId()
            );
            sendStatusResponse(request, false, "Du bist nicht am Zug oder das Spiel ist in einem ungültigen Zustand");

            return;
        } catch (GameException e) {
            LOG.error("[LobbyId: {}] Card could not be discarded", request.getLobbyId());
            sendStatusResponse(request, false, "Die Karte konnte nicht abgelegt werden");

            return;
        } catch (PlayerManagementException e) {
            LOG.error("[LobbyId: {}] Error getting card", request.getLobbyId());
            sendStatusResponse(request, false, "Karte zum Ablegen konnte nicht gefunden werden");

            return;
        }

        IGame game = regionManagement.getGame(request.getLobbyId());
        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
        sendServerMessageEvent(
                game.getGameId(),
                game.getCurrentPlayer()
                    .getUser()
                    .getUsername() + " hat " + request.getAmount() + " " + "Wasseraufbereitungsmarker platziert."
        );
        LOG.info("[LobbyId: {}] Increased water treatments. Send BoardUpdate to all players", request.getLobbyId());
    }

    @Subscribe
    public void onTreatWaterEvent(TreatWaterEvent event) {
        TreatWaterEventResponse response = new TreatWaterEventResponse(event.getLobbyId(), false);
        IUser user = regionManagement.getGame(event.getLobbyId())
                                     .getPlayer(event.getUsername())
                                     .getUser();
        Session session = authenticationService.getSession(user)
                                               .orElseThrow(() -> {
                                                   LOG.error(
                                                           "[LobbyId: {}] Session not found for user",
                                                           event.getLobbyId()
                                                   );
                                                   return new SessionNotFoundException();
                                               });
        response.setSession(session);
        post(response);
        LOG.info("[LobbyId: {}] Sent TreatWaterEventResponse", event.getLobbyId());
    }

    @Subscribe
    public void onWaterTreatmentEventRequest(WaterTreatmentEventRequest request) {
        LOG.debug("[Lobby: {}] Got TreatWaterEvenRequest", request.getLobbyId());
        IGame game = regionManagement.getGame(request.getLobbyId());
        Session session = request.getSession()
                                 .orElseThrow(() -> {
                                     LOG.error("[LobbyId: {}] Session not present", request.getLobbyId());
                                     return new SessionNotFoundException();
                                 });

        if (game.getState() instanceof EventState && request.getAmount() == 1) {
            regionManagement.increaseWaterTreatment(request.getRegionId(), game, request.getAmount());
            game.setState(game.getPreviousState());
            game.setState(new PlaceExtraWaterTreatmentState());
            TreatWaterEventResponse response = new TreatWaterEventResponse(request.getLobbyId(), true);
            response.setSession(session);
            post(response);
        } else if (request.isDismissed()) {
            game.setState(game.getPreviousState());
        } else {
            regionManagement.increaseWaterTreatment(request.getRegionId(), game, request.getAmount());
            game.setState(game.getPreviousState());
        }
        sendServerMessageEvent(
                request.getLobbyId(),
                game.getCurrentPlayer()
                    .getUser()
                    .getUsername() + " hat erfolgreich Wasseraufbereitung in einer Region " + "durchgeführt, um die Ausbruchswahrscheinlichkeit in den anliegenden Städten zu verringern"
        );

        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }
}
