package de.uol.swp.client.game.objects.dialogs;

import javafx.scene.control.Dialog;

import java.util.Objects;

public class AbstractDialog<T> extends Dialog<T> {
    protected static final String DIALOG_CSS = "/css/myDialog.css";
    protected static final String HBOX_STYLE = "dialog-hbox";
    protected static final String VBOX_STYLE = "dialog-vbox";
    protected static final String PRIMARY_BUTTON = "primary-button";
    protected static final String DENY_BUTTON = "deny-button";
    protected static final String APPROVE_BUTTON = "approve-button";
    protected static final String SCROLL_PANE = "dialog-scroll-pane";
    protected static final String LABEL = "dialog-label";

    public AbstractDialog() {
        super();
        super.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource(DIALOG_CSS))
                                                          .toExternalForm());
    }
}
