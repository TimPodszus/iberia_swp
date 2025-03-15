package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.client.game.CardFactory;
import de.uol.swp.client.game.objects.cards.AbstractCard;
import de.uol.swp.common.cards.data.ICardDTO;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.*;

/**
 * Dialog for exchanging cards between players.
 * Extends the JavaFX Dialog class and returns a map of selected cards.
 */
public class CardExchangeDialog extends AbstractDialog<Map<String, ICardDTO>> {

    private static final String HEADER = "Karte auswählen";
    private final String username;
    private String currentOpponent;
    private final Map<String, List<ICardDTO>> playerCards;
    private final List<AbstractCard> displayedPlayerCards;
    private List<AbstractCard> displayedOpponentCards;
    private final HBox opponentHBox = new HBox(20);

    private AbstractCard currentPlayerSelectedCard;
    private AbstractCard opponentSelectedCard;

    /**
     * Constructs a new CardExchangeDialog.
     *
     * @param currentPlayer the username of the current player
     * @param playerCards   a map of player usernames to their list of cards
     */
    public CardExchangeDialog(String currentPlayer, Map<String, List<ICardDTO>> playerCards) {
        this.username = currentPlayer;
        this.playerCards = playerCards;
        this.displayedPlayerCards = createCards(username);
        super.initStyle(StageStyle.DECORATED);
        super.setHeaderText(HEADER);
        opponentHBox.getStyleClass().add(HBOX_STYLE);
        this.setContent();
        this.setButtons();
        super.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Map<String, ICardDTO> result = new HashMap<>();
                result.put(username, convertToCardDTO(username, currentPlayerSelectedCard));
                result.put(currentOpponent, convertToCardDTO(currentOpponent, opponentSelectedCard));
                return result;
            } else {
                return null;
            }
        });
    }

    /**
     * Sets the content of the dialog, including the cards to be displayed.
     */
    private void setContent() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add(VBOX_STYLE);

        HBox currentPlayerHBox = new HBox();
        currentPlayerHBox.getStyleClass().add(HBOX_STYLE);
        for (AbstractCard card : this.displayedPlayerCards) {
            card.setOnMouseClicked(mouseEvent -> this.onPlayerCardClicked(card.getCardId()));
            currentPlayerHBox.getChildren()
                             .add(card);
        }
        vBox.getChildren()
            .add(currentPlayerHBox);

        vBox.getChildren()
            .add(getOpponentTitle());

        this.setDisplayedOpponentCards();

        vBox.getChildren()
            .add(opponentHBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(vBox);
        scrollPane.getStyleClass().add(SCROLL_PANE);
        scrollPane.setFitToWidth(true);
        super.getDialogPane()
             .setContent(scrollPane);

    }

    /**
     * Sets the displayed opponent cards in the dialog.
     * Clears the current opponent cards and adds the new ones.
     * Each card is set with a mouse click event to select the card.
     */
    private void setDisplayedOpponentCards() {
        displayedOpponentCards = createCards(currentOpponent);
        opponentHBox.getChildren()
                    .clear();
        for (AbstractCard card : displayedOpponentCards) {
            card.setOnMouseClicked(mouseEvent -> this.onOpponentCardClicked(card.getCardId()));
            opponentHBox.getChildren()
                        .add(card);
        }
    }

    /**
     * Handles the event when a player card is clicked.
     * Unselects the currently selected player card, sets the new selected card, and selects it.
     *
     * @param id the ID of the clicked card
     */
    private void onPlayerCardClicked(int id) {
        if (this.currentPlayerSelectedCard != null) {
            this.currentPlayerSelectedCard.unselect();
        }
        setCurrentPlayerSelectedCard(id);
        this.currentPlayerSelectedCard.select();
    }

    /**
     * Handles the event when an opponent card is clicked.
     * Unselects the currently selected opponent card, sets the new selected card, and selects it.
     *
     * @param id the ID of the clicked card
     */
    private void onOpponentCardClicked(int id) {
        if (this.opponentSelectedCard != null) {
            this.opponentSelectedCard.unselect();
        }
        setOpponentSelectedCard(id);
        this.opponentSelectedCard.select();
    }

    /**
     * Returns a Node that represents the opponent title.
     * If there are more than two players, a ComboBox is returned to select the opponent.
     * Otherwise, a Label is returned with the opponent's name.
     *
     * @return a Node representing the opponent title
     */
    private Node getOpponentTitle() {
        if (this.playerCards.size() > 2) {
            ObservableList<String> opponents = javafx.collections.FXCollections.observableArrayList(this.playerCards.keySet()
                                                                                                                    .stream()
                                                                                                                    .filter(player -> !player.equals(
                                                                                                                            this.username))
                                                                                                                    .toList());
            ComboBox<String> comboBox = new ComboBox<>(opponents);
            comboBox.setValue(opponents.get(0));
            currentOpponent = comboBox.getValue();
            comboBox.setOnAction(actionEvent -> currentOpponent = comboBox.getValue());
            return comboBox;
        } else {
            this.playerCards.keySet()
                            .stream()
                            .filter(player -> !player.equals(this.username))
                            .findFirst()
                            .ifPresent(player -> currentOpponent = player);
            Label label = new Label(currentOpponent);
            label.getStyleClass().add(LABEL);
            return label;
        }
    }

    /**
     * Sets the buttons for the dialog.
     */
    private void setButtons() {
        super.getDialogPane()
             .getButtonTypes()
             .add(ButtonType.OK);
        super.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add(APPROVE_BUTTON);
        super.getDialogPane()
             .getButtonTypes()
             .add(ButtonType.CANCEL);
        super.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add(DENY_BUTTON);
    }

    /**
     * Creates a list of AbstractCard objects from the player cards.
     *
     * @return a list of AbstractCard objects
     */
    private List<AbstractCard> createCards(String username) {
        List<AbstractCard> cards = new ArrayList<>();
        for (ICardDTO playerCard : this.playerCards.get(username)) {
            AbstractCard card = CardFactory.createCard(playerCard);
            cards.add(card);
        }
        return cards;
    }

    /**
     * Sets the selected card based on the card ID.
     *
     * @param cardId the ID of the selected card
     */
    private void setCurrentPlayerSelectedCard(int cardId) {
        for (AbstractCard card : this.displayedPlayerCards) {
            if (card.getCardId() == cardId) {
                this.currentPlayerSelectedCard = card;
                break;
            }
        }
    }

    /**
     * Sets the selected card based on the card ID.
     *
     * @param cardId the ID of the selected card
     */
    private void setOpponentSelectedCard(int cardId) {
        for (AbstractCard card : this.displayedOpponentCards) {
            if (card.getCardId() == cardId) {
                this.opponentSelectedCard = card;
                break;
            }
        }
    }

    /**
     * Converts an AbstractCard to an ICardDTO.
     *
     * @param username the username of the player
     * @param card     the AbstractCard to convert
     * @return the corresponding ICardDTO, or null if not found
     */
    private ICardDTO convertToCardDTO(String username, AbstractCard card) {
        for (ICardDTO playerCard : this.playerCards.get(username)) {
            if (playerCard.getId() == card.getCardId()) {
                return playerCard;
            }
        }
        return null;
    }
}
