package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.client.game.CardFactory;
import de.uol.swp.client.game.objects.cards.AbstractCard;
import de.uol.swp.common.cards.data.ICardDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class CardsToSortDialog extends Dialog<List<ICardDTO>> {
    private static final String TITLE = "Kartendialog";
    private static final String SORT_CARDS_HEADER = "Karten sortieren";
    private final boolean dismissible;
    private final List<ICardDTO> playerCards;
    private final ObservableList<AbstractCard> displayedPlayerCards;
    private HBox cardBox;

    public CardsToSortDialog(boolean dismissible, List<ICardDTO> playerCards) {
        this.dismissible = dismissible;
        this.playerCards = playerCards;
        this.displayedPlayerCards = FXCollections.observableArrayList(createCards());
        this.setContent();
        this.setButtons();
    }

    private void setContent() {
        super.initStyle(StageStyle.DECORATED);
        super.setTitle(TITLE);
        super.setHeaderText(SORT_CARDS_HEADER);

        VBox mainBox = new VBox();
        mainBox.setSpacing(10);

        Label instructions = new Label("Drag und drop die Karten um sie zu sortieren. Die erste Karte ist die Oberste.");
        mainBox.getChildren()
               .add(instructions);

        cardBox = new HBox();
        cardBox.setSpacing(10);

        for (AbstractCard card : this.displayedPlayerCards) {
            card.setOnDragDetected(event -> {
                Dragboard db = card.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(card.getCardId() + "");
                db.setContent(content);
                event.consume();
            });

            card.setOnDragOver(event -> {
                if (event.getGestureSource() != card && event.getDragboard()
                                                             .hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            card.setOnDragEntered(event -> {
                if (event.getGestureSource() != card && event.getDragboard()
                                                             .hasString()) {
                    card.setOpacity(0.3);
                }
            });

            card.setOnDragExited(event -> {
                if (event.getGestureSource() != card && event.getDragboard()
                                                             .hasString()) {
                    card.setOpacity(1);
                }
            });

            card.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    int draggedCardId = Integer.parseInt(db.getString());
                    AbstractCard draggedCard = displayedPlayerCards.stream()
                                                                   .filter(c -> c.getCardId() == draggedCardId)
                                                                   .findFirst()
                                                                   .orElse(null);
                    if (draggedCard != null) {
                        int thisIndex = displayedPlayerCards.indexOf(card);
                        displayedPlayerCards.remove(draggedCard);
                        displayedPlayerCards.add(thisIndex, draggedCard);
                        success = true;
                        refreshCardBox();
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            });

            card.setOnDragDone(DragEvent::consume);
            cardBox.getChildren()
                   .add(card);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(cardBox);
        super.getDialogPane()
             .setContent(scrollPane);
    }

    private void setButtons() {
        super.getDialogPane()
             .getButtonTypes()
             .add(ButtonType.OK);
        if (this.dismissible) {
            super.getDialogPane()
                 .getButtonTypes()
                 .add(ButtonType.CANCEL);
        }

        super.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return convertToCardDTOList(this.displayedPlayerCards);
            } else {
                return null;
            }
        });
    }

    private List<AbstractCard> createCards() {
        List<AbstractCard> cards = new ArrayList<>();
        for (ICardDTO playerCard : this.playerCards) {
            AbstractCard card = CardFactory.createCard(playerCard);
            cards.add(card);
        }
        return cards;
    }

    private List<ICardDTO> convertToCardDTOList(List<AbstractCard> cards) {
        List<ICardDTO> cardDTOList = new ArrayList<>();
        for (AbstractCard card : cards) {
            for (ICardDTO playerCard : this.playerCards) {
                if (playerCard.getId() == card.getCardId()) {
                    cardDTOList.add(playerCard);
                    break;
                }
            }
        }
        return cardDTOList;
    }

    private void refreshCardBox() {
        cardBox.getChildren()
               .clear();
        cardBox.getChildren()
               .addAll(displayedPlayerCards);
    }
}