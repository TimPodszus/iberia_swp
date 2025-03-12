package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.client.game.CardFactory;
import de.uol.swp.client.game.objects.cards.AbstractCard;
import de.uol.swp.client.game.objects.cards.RoleCard;
import de.uol.swp.common.cards.data.ICardDTO;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * A dialog for showing a list of player cards.
 * When cardsSelectable is true, the user can select a card.
 */
public class CardDialog extends AbstractDialog<ICardDTO> {
    private static final String TITLE = "Kartendialog";
    private static final String SELECT_CARDS_HEADER = "Karte auswählen";
    private final boolean cardsSelectable;
    private final boolean dismissible;
    private final List<ICardDTO> playerCards;
    private final List<AbstractCard> displayedPlayerCards;
    private AbstractCard selectedCard;

    /**
     * Constructs a CardDialog.
     *
     * @param playerCards    the list of player cards to display
     * @param playerRoleCard the role card of the player
     */
    public CardDialog(List<ICardDTO> playerCards, RoleCard playerRoleCard) {
        this.cardsSelectable = false;
        this.dismissible = true;
        this.playerCards = playerCards;
        this.displayedPlayerCards = createCards();
        this.displayedPlayerCards.add(0, playerRoleCard);

        this.setContent();
        this.setButtons();
    }

    public CardDialog(boolean cardsSelectable, boolean dismissible, List<ICardDTO> playerCards) {
        this.cardsSelectable = cardsSelectable;
        this.dismissible = dismissible;
        this.playerCards = playerCards;
        this.displayedPlayerCards = createCards();

        this.setContent();
        this.setButtons();
    }

    /**
     * Sets the content of the dialog, including the cards to be displayed.
     */
    private void setContent() {
        super.initStyle(StageStyle.DECORATED);
        super.setTitle(TITLE);
        if (cardsSelectable) {
            super.setHeaderText(SELECT_CARDS_HEADER);
        }

        HBox cardBox = new HBox();
        cardBox.getStyleClass().add(HBOX_STYLE);
        for (AbstractCard card : this.displayedPlayerCards) {
            if (this.cardsSelectable) {
                card.setOnMouseClicked(mouseEvent -> onCardClicked(card.getCardId()));
            }
            cardBox.getChildren()
                   .add(card);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(cardBox);
        scrollPane.getStyleClass().add(SCROLL_PANE);
        scrollPane.setFitToWidth(true);
        super.getDialogPane()
             .setContent(scrollPane);
    }

    /**
     * Handles the event when a card is clicked.
     * Unselects the currently selected card, sets the new selected card,
     * and then selects the new card.
     *
     * @param id the ID of the clicked card
     */
    private void onCardClicked(int id) {
        if (this.selectedCard != null) {
            this.selectedCard.unselect();
        }
        setSelectedCard(id);
        this.selectedCard.select();
    }

    /**
     * Sets the buttons for the dialog.
     */
    private void setButtons() {
        if (this.cardsSelectable) {
            super.getDialogPane()
                 .getButtonTypes()
                 .add(ButtonType.OK);
            super.getDialogPane().lookupButton(ButtonType.OK).getStyleClass().add(APPROVE_BUTTON);
        }
        if (this.dismissible || !this.cardsSelectable) {
            super.getDialogPane()
                 .getButtonTypes()
                 .add(ButtonType.CANCEL);
            super.getDialogPane().lookupButton(ButtonType.CANCEL).getStyleClass().add(DENY_BUTTON);
        }

        if (cardsSelectable) {
            super.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return convertToCardDTO(this.selectedCard);
                } else {
                    return null;
                }
            });
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
