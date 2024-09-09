package de.uol.swp.common.enums;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Action {
    private ActionType actionType;

    public ActionType getActionType()
    {
        return actionType;
    }

    public void setActionType(ActionType actionType)
    {
        this.actionType = actionType;
    }
}


