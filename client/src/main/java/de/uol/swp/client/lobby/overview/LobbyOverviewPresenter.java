package de.uol.swp.client.lobby.overview;

import com.google.inject.Inject;
import de.uol.swp.client.AbstractPresenter;
import de.uol.swp.client.lobby.LobbyService;
import de.uol.swp.client.main.event.ShowLastSceneEvent;
import de.uol.swp.common.lobby.ILobby;
import de.uol.swp.common.lobby.response.LobbyListResponse;
import de.uol.swp.common.user.UserDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LobbyOverviewPresenter extends AbstractPresenter {
    public static final String FXML = "/fxml/LobbyOverviewView.fxml";

    public static final Map<String, ILobby> lobbyList = Map.of();

    @Inject
    private LobbyService lobbyService;

    @FXML
    private TextField searchInput;

    @FXML
    private TableView<LobbyListItem> lobbyTable;

    public LobbyOverviewPresenter() {
        //necessary for java fx
    }

    /**
     * Initializes the presenter by setting up the search input listener
     * and requesting the lobby list from the lobby service.
     */
    @FXML
    private void initialize() {
        searchInput.textProperty()
                   .addListener((observable, oldValue, newValue) -> filterLobbies(newValue));
        lobbyService.requestLobbyList();
    }

    /**
     * Handles the LobbyListMessage event.
     * Updates the lobby list with the received lobbies.
     *
     * @param message the LobbyListMessage containing the list of lobbies
     */
    @Subscribe
    public void onLobbyListMessage(LobbyListResponse message) {
        setLobbyList(message.getLobbies()
                            .values()
                            .stream()
                            .toList());
    }

    /**
     * Handles the action when the back button is pressed.
     * Posts a ShowLastSceneEvent to the event bus.
     */
    public void onBackButtonPressed() {
        eventBus.post(new ShowLastSceneEvent());
    }

    /**
     * Handles the action when the search button is pressed.
     * Retrieves the text from the search input field.
     */
    public void onSearchButtonPressed() {
        String searchInputText = searchInput.getText();
        filterLobbies(searchInputText);
    }

    /**
     * Filters the lobbies based on the search input text.
     *
     * @param searchInputText the text to filter the lobbies by
     */
    private void filterLobbies(String searchInputText) {
        List<ILobby> filteredLobbies = new ArrayList<>();
        for (ILobby lobby : lobbyList.values()) {
            if (lobby.getName()
                     .contains(searchInputText) || lobby.getLobbyCode()
                                                        .contains(searchInputText)) {
                filteredLobbies.add(lobby);
            }
        }
        setLobbyList(filteredLobbies);
    }

    /**
     * Sets the list of lobbies in the lobby table.
     *
     * @param lobbyList the list of lobbies to display
     */
    private void setLobbyList(List<ILobby> lobbyList) {
        List<LobbyListItem> lobbyListItems = new ArrayList<>();
        for (ILobby lobby : lobbyList) {
            lobbyListItems.add(new LobbyListItem(
                    lobby.getName(),
                    lobby.getUsers()
                         .size(),
                    4
            ));
        }
        lobbyTable.getItems()
                  .clear();
        lobbyTable.getItems()
                  .addAll(lobbyListItems);

        lobbyTable.setRowFactory(tv -> {
            TableRow<LobbyListItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    LobbyListItem rowData = row.getItem();
                    //TODO get user from login
                    lobbyService.joinLobby(rowData.getName(), new UserDTO("ich", ""));
                }
            });
            return row;
        });
    }
}
