package de.uol.swp.client.game.objects.dialogs;

import de.uol.swp.client.game.CardFactory;
import de.uol.swp.client.game.objects.cards.AbstractCard;
import de.uol.swp.common.cards.CityCardDTO;
import de.uol.swp.common.game.RoleEnum;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * A dialog for selecting a card from a list of player cards.
 * Returns the selected card as result
 */
public class CardSelectionWaterTreatmentDialog extends Dialog<Pair<CityCardDTO, Integer>> {
    private static final String HEADER = "Karte auswählen";
    private final boolean dismissible;
    private final List<CityCardDTO> cityCards;
    private final List<AbstractCard> displayedPlayerCards;
    private AbstractCard selectedCard;
    private final ToggleGroup buttonGroup = new ToggleGroup();
    private final RoleEnum role;
    private Button okButton;
    private int selectedAmount = 0;

    /**
     * Constructs a CardSelectionDialog.
     *
     * @param dismissible whether the dialog can be dismissed
     * @param cityCards   the list of player cards to display
     */
    public CardSelectionWaterTreatmentDialog(boolean dismissible, List<CityCardDTO> cityCards, RoleEnum role) {
        this.dismissible = dismissible;
        this.cityCards = cityCards;
        this.role = role;
        this.displayedPlayerCards = createCards();
        super.initStyle(StageStyle.DECORATED);
        super.setHeaderText(HEADER);
        this.setContent();
        this.setButtons();
        super.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(convertToCardDTO(this.selectedCard), selectedAmount);
            } else {
                return null;
            }
        });
    }

    private void setButtons() {
        super.getDialogPane().getButtonTypes().add(ButtonType.OK);
        if (this.dismissible) {
            super.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        }

        okButton = (Button) super.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);  // Setze den Button standardmäßig auf disabled

        Label titleLabel = new Label("Wählen Sie zuerst die Anzahl");
        titleLabel.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        addButton("1", "1", buttonBox);
        addButton("2", "2", buttonBox);
        if (role.equals(RoleEnum.AGRICULTURAL_SCIENTIST)) {
            addButton("3", "3", buttonBox);
        }

        VBox mainBox = (VBox) super.getDialogPane().getContent();
        mainBox.getChildren().addAll(titleLabel, buttonBox);
    }

    /**
     * Adds a new button with an action parameter to the dialog.
     *
     * @param buttonLabel the label of the button
     * @param parameter   the parameter to pass to the button action
     */
    private void addButton(String buttonLabel, String parameter, HBox buttonBox) {
        ToggleButton button = new ToggleButton(buttonLabel);
        button.setMinWidth(60);
        button.setToggleGroup(buttonGroup);
        button.setUserData(parameter);
        button.setOnAction(event -> handleButtonAction((String) button.getUserData()));
        buttonBox.getChildren().add(button);
    }


    /**
     * Handles the action when a dynamic button is clicked.
     *
     * @param parameter the parameter passed from the button
     */
    private void handleButtonAction(String parameter) {
        selectedAmount = Integer.parseInt(parameter);
        if (parameter.equals("1")) {
            if (role.equals(RoleEnum.AGRICULTURAL_SCIENTIST)) {
                enableOkButton();
            } else {
                for (AbstractCard card : displayedPlayerCards) {
                    card.setDisable(false);
                }
            }
        } else {
            for (AbstractCard card : displayedPlayerCards) {
                card.setDisable(false);
            }
        }
    }

    /**
     * Sets the content of the dialog, including the cards to be displayed.
     */
    private void setContent() {
        HBox cardBox = new HBox(5);
        cardBox.setAlignment(Pos.CENTER);
        for (AbstractCard card : this.displayedPlayerCards) {
            card.setOnMouseClicked(mouseEvent -> onCardClicked(card.getCardId()));
            cardBox.getChildren().add(card);
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(cardBox);

        VBox mainBox = new VBox(10);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.getChildren().add(scrollPane);
        super.getDialogPane().setContent(mainBox);
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
        enableOkButton();
    }

    /**
     * Creates a list of AbstractCard objects from the player cards.
     *
     * @return a list of AbstractCard objects
     */
    private List<AbstractCard> createCards() {
        List<AbstractCard> cards = new ArrayList<>();
        for (CityCardDTO cityCard : this.cityCards) {
            AbstractCard card = CardFactory.createCard(cityCard);
            card.setDisable(true);
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
        for (AbstractCard cityCard : this.displayedPlayerCards) {
            if (cityCard.getCardId() == cardId) {
                this.selectedCard = cityCard;
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
    private CityCardDTO convertToCardDTO(AbstractCard card) {
        for (CityCardDTO cityCard : this.cityCards) {
            if (cityCard.getId() == card.getCardId()) {
                return cityCard;
            }
        }
        return null;
    }

    private void enableOkButton() {
        if (okButton != null) {
            okButton.setDisable(false);
        }
    }

}


