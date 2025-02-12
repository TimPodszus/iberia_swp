package de.uol.swp.server.region;

import com.google.inject.Inject;
import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.game.dto.IGameDTO;
import de.uol.swp.common.game.message.event.BoardUpdateEvent;
import de.uol.swp.common.message.response.AbstractResponseMessage;
import de.uol.swp.common.region.IRegionDTO;
import de.uol.swp.common.region.request.AvailableRegionsRequest;
import de.uol.swp.common.region.request.WaterTreatmentRegionRequest;
import de.uol.swp.common.region.request.WaterTreatmentRequest;
import de.uol.swp.common.region.response.AvailableRegionsResponse;
import de.uol.swp.common.region.response.PossibleCityCardsToDiscardForRegionResponse;
import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.Session;
import de.uol.swp.server.AbstractService;
import de.uol.swp.server.cards.ICard;
import de.uol.swp.server.game.GameException;
import de.uol.swp.server.game.GameMapper;
import de.uol.swp.server.game.data.IGame;
import de.uol.swp.server.game.management.GameManagement;
import de.uol.swp.server.game.management.GameManagementException;
import de.uol.swp.server.lobby.data.ILobby;
import de.uol.swp.server.lobby.management.LobbyManagement;
import de.uol.swp.server.player.management.PlayerManagement;
import de.uol.swp.server.player.management.PlayerManagementException;
import de.uol.swp.server.region.management.RegionManagement;
import de.uol.swp.server.usermanagement.UserMapper;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Set;

public class RegionService extends AbstractService {
    private final RegionManagement regionManagement;
    private final PlayerManagement playerManagement;
    private final GameManagement gameManagement;
    private final LobbyManagement lobbyManagement;
    /**
     * Constructor
     *
     * @param bus the EvenBus used throughout the server
     * @since 2019-10-08
     */
    @Inject
    public RegionService(
            EventBus bus,
            RegionManagement regionManagement,
            PlayerManagement playerManagement,
            GameManagement gameManagement,
            LobbyManagement lobbyManagement
    ) {
        super(bus);
        this.regionManagement = regionManagement;
        this.playerManagement = playerManagement;
        this.gameManagement = gameManagement;
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
        IGame game = regionManagement.getGame(request.getLobbyId());
        Set<IRegionDTO> regions = regionManagement.getAvailableRegions(user, game);
        response = new AvailableRegionsResponse(regions);
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
        IGame game = regionManagement.getGame(request.getLobbyId());
        List<CityCardDTO> cityCards = regionManagement.getPossibleCityCardsToDiscard(
                user,
                request.getRegionId(),
                game
        );
        response = new PossibleCityCardsToDiscardForRegionResponse(cityCards);
        response.setSession(request.getSession()
                                   .orElseThrow(() -> new IllegalStateException("Session not present")));
        post(response);
    }

    @Subscribe
    public void onSendWaterTreatmentRequest(WaterTreatmentRequest request) throws GameException, PlayerManagementException, GameManagementException {
        IUserDTO user = request.getSession()
                               .map(Session::getUser)
                               .orElse(null);
        if (user == null) {
            throw new GameException("User is unknown");
        }
        IGame game = regionManagement.getGame(request.getLobbyId());
        ICard card = playerManagement.getCard(request.getLobbyId(), user.getUsername(), request.getCard().getId());
        regionManagement.increaseWaterTreatmentsFromRegion(request.getLobbyId(), request.getRegionId(),
                request.getAmount(), card, UserMapper.toUser(user), game
        );

        IGameDTO gameDTO = GameMapper.toDTO(gameManagement.getGame(request.getLobbyId()));
        ILobby lobby = lobbyManagement.getLobby(request.getLobbyId());
        sendToAllInLobby(lobby, new BoardUpdateEvent(request.getLobbyId(), gameDTO));
    }
}
