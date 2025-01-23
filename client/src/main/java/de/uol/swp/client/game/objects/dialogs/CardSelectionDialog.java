package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.client.game.CardFactory;
import de.uol.swp.client.game.objects.cards.AbstractCard;
import de.uol.swp.common.cards.ICardDTO;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * A dialog for selecting a card from a list of player cards.
 * Returns the selected card as result
 */
public class CardSelectionDialog extends Dialog<ICardDTO> {
    private static final String HEADER = "Karte auswählen";
    private final boolean dismissible;
    private final List<ICardDTO> playerCards;
    private final List<AbstractCard> displayedPlayerCards;
    private AbstractCard selectedCard;

    /**
     * Constructs a CardSelectionDialog.
     *
     * @param dismissible whether the dialog can be dismissed
     * @param playerCards the list of player cards to display
     */
    public CardSelectionDialog(boolean dismissible, List<ICardDTO> playerCards) {
        this.dismissible = dismissible;
        this.playerCards = playerCards;
        this.displayedPlayerCards = createCards();
        super.initStyle(StageStyle.DECORATED);
        super.setHeaderText(HEADER);
        this.setContent();
        this.setButtons();
        super.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return convertToCardDTO(this.selectedCard);
            } else {
                return null;
            }
        });
    }

    /**
     * Sets the content of the dialog, including the cards to be displayed.
     */
    private void setContent() {
        HBox cardBox = new HBox();
        for (AbstractCard card : this.displayedPlayerCards) {
            card.setOnMouseClicked(mouseEvent -> onCardClicked(card.getCardId()));
            cardBox.getChildren()
                   .add(card);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(cardBox);
    }

    /**
     * Handles the event when a card is clicked.
     * Unselects the currently selected card, sets the new selected card,
     * and then selects the new card.
     *
     * @param id the ID of the clicked card
     */
    private void onCardClicked(int id) {
        this.selectedCard.unselect();
        setSelectedCard(id);
        this.selectedCard.select();
    }

    /**
     * Sets the buttons for the dialog.
     */
    private void setButtons() {
        super.getDialogPane()
             .getButtonTypes()
             .add(ButtonType.OK);
        if (this.dismissible) {
            super.getDialogPane()
                 .getButtonTypes()
                 .add(ButtonType.CANCEL);
        }
    }

    /**
     * Creates a list of AbstractCard objects from the player cards.
     *
     * @return a list of AbstractCard objects
     */
    private List<AbstractCard> createCards() {
        List<AbstractCard> cards = new ArrayList<>();
        for (ICardDTO playerCard : this.playerCards) {
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
    private void setSelectedCard(int cardId) {
        for (AbstractCard playerCard : this.displayedPlayerCards) {
            if (playerCard.getCardId() == cardId) {
                this.selectedCard = playerCard;
                break;
            }
        }
    }

    /**
     * Converts an AbstractCard to an ICardDTO.
     *
     * @param card the AbstractCard to convert
     * @return the corresponding ICardDTO, or null if not found
     */
    private ICardDTO convertToCardDTO(AbstractCard card) {
        for (ICardDTO playerCard : this.playerCards) {
            if (playerCard.getId() == card.getCardId()) {
                return playerCard;
            }
        }
        return null;
    }
}
