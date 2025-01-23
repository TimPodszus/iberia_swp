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
 */
public class CardSelectionDialog extends Dialog<ICardDTO> {
    private static final String HEADER = "Karte auswählen";
    private final boolean dismissible;
    private final List<ICardDTO> playerCards;
    private ICardDTO selectedCard;

    /**
     * Constructs a CardSelectionDialog.
     *
     * @param dismissible whether the dialog can be dismissed
     * @param playerCards the list of player cards to display
     */
    public CardSelectionDialog(boolean dismissible, List<ICardDTO> playerCards) {
        this.dismissible = dismissible;
        this.playerCards = playerCards;
        super.initStyle(StageStyle.DECORATED);
        super.setHeaderText(HEADER);
        this.setContent();
        this.setButtons();
    }

    /**
     * Sets the content of the dialog, including the cards to be displayed.
     */
    private void setContent() {
        HBox cardBox = new HBox();
        List<AbstractCard> cards = createCards();
        for (AbstractCard card : cards) {
            card.setOnMouseClicked(mouseEvent -> this.setSelectedCard(card.getCardId()));
            cardBox.getChildren()
                   .add(card);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(cardBox);

        super.setResultConverter(dialogButton -> dialogButton == ButtonType.OK ? this.selectedCard : null);
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
        for (ICardDTO playerCard : this.playerCards) {
            if (playerCard.getId() == cardId) {
                this.selectedCard = playerCard;
                break;
            }
        }
    }
}
