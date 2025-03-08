package de.uol.swp.server.region;

import com.google.inject.Inject;
import de.uol.swp.common.cards.data.CityCardDTO;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.message.response.AbstractResponseMessage;
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
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.game.states.EventState;
import de.uol.swp.server.game.states.PlaceExtraWaterTreatmentState;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.ILobbyManagement;
import de.uol.swp.server.player.data.IPlayer;
import de.uol.swp.server.player.management.IPlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.management.IRegionManagement;
import de.uol.swp.server.usermanagement.IUser;
import de.uol.swp.server.usermanagement.UserMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Optional;
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
    public void onSendAvailableRegionsRequest(AvailableRegionsRequest request) throws GameException {
        AbstractResponseMessage response;
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        if (user == null) {
            throw new GameException("User is unknown");
        }
        Set<IRegionDTO> regions = regionManagement.getAvailableRegions(user, request.getLobbyId());
        response = new AvailableRegionsResponse(request.getLobbyId(), regions);
        response.setSession(request.getSession()
                                   .orElseThrow(() -> new IllegalStateException("Session not present")));
        post(response);
    }

    @Subscribe
    public void onWaterTreatmentRegionRequest(WaterTreatmentRegionRequest request) throws GameException {
        AbstractResponseMessage response;
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        if (user == null) {
            throw new GameException("User is unknown");
        }
        List<CityCardDTO> cityCards = regionManagement.getPossibleCityCardsToDiscard(user,
                request.getRegionId(),
                request.getLobbyId()
        );
        response = new CardsToDiscardForRegionResponse(request.getLobbyId(), cityCards);
        response.setSession(request.getSession()
                                   .orElseThrow(() -> new IllegalStateException("Session not present")));
        post(response);
    }

    @Subscribe
    public void onSendWaterTreatmentRequest(WaterTreatmentRequest request) throws GameException, PlayerManagementException, GameManagementException {
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        ICard card = null;
        IGame game = regionManagement.getGame(request.getLobbyId());
        if (user == null) {
            throw new GameException("User is unknown");
        }
        if (request.getCard() != null) {
            card = playerManagement.getCard(request.getLobbyId(),
                    user.getUsername(),
                    request.getCard()
                           .getId()
            );
        }
        regionManagement.increaseWaterTreatmentsFromRegion(request.getLobbyId(),
                request.getRegionId(),
                request.getAmount(),
                card,
                UserMapper.toUser(user)
        );
        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
        sendServerMessageEvent(game.getGameId(),
                game.getCurrentPlayer().getUser().getUsername()+ " hat " + request.getAmount() + " " +
                        "Wasseraufbereitungsmarker in der Region " + game.getRegionRepository().getRegionByID(request.getRegionId())+
                        " platziert.");
    }

    @Subscribe
    public void onTreatWaterEvent(TreatWaterEvent event) {
        AbstractResponseMessage response;
        LOG.debug("[Lobby: {}] Got TreatWaterEvent", event.getLobbyId());
        IGame game = regionManagement.getGame(event.getLobbyId());
        IUser user = game.getPlayers()
                         .stream()
                         .map(IPlayer::getUser)
                         .filter(iuser -> iuser.getUsername()
                                               .equals(event.getUsername()))
                         .findFirst()
                         .orElseThrow(() -> new IllegalStateException("User not found"));
        Optional<Session> session = authenticationService.getSession(user);
        response = new TreatWaterEventResponse(false);
        response.setSession(session.orElseThrow(() -> new IllegalStateException("Session not present")));
        post(response);
    }

    @Subscribe
    public void onWaterTreatmentEventRequest(WaterTreatmentEventRequest request) throws GameException {
        AbstractResponseMessage response;
        LOG.debug("[Lobby: {}] Got TreatWaterEvenRequestt", request.getLobbyId());
        IGame game = regionManagement.getGame(request.getLobbyId());
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElseThrow(() -> new GameException("User is unknown"));

        if (game.getState() instanceof EventState && request.getAmount() == 1) {
            regionManagement.increaseWaterTreatment(request.getRegionId(), game, request.getAmount());
            game.setState(game.getPreviousState());
            game.setState(new PlaceExtraWaterTreatmentState());
            Optional<Session> session = authenticationService.getSession(UserMapper.toUser(user));
            response = new TreatWaterEventResponse(true);
            response.setSession(session.orElseThrow(() -> new IllegalStateException("Session not present")));
            post(response);
        } else if (request.isDismissed()) {
            game.setState(game.getPreviousState());
        } else {
            regionManagement.increaseWaterTreatment(request.getRegionId(), game, request.getAmount());
            game.setState(game.getPreviousState());
        }
        sendServerMessageEvent(request.getLobbyId(),
                game.getCurrentPlayer()
                    .getUser()
                    .getUsername() + " hat erfolgreich Wasseraufbereitung in einer Region " + "durchgeführt, um die Ausbruchswahrscheinlichkeit in den anliegenden Städten zu verringern"
        );

        IGameDTO gameDTO = GameMapper.toDTO(game);
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }
}
