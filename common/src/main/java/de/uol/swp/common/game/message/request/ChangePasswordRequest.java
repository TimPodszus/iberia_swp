package de.uol.swp.common.game.message.request;

import de.uol.swp.common.message.request.AbstractRequestMessage;
import de.uol.swp.common.user.IUserDTO;
import lombok.Getter;

@Getter
public class ChangePasswordRequest extends AbstractRequestMessage {
    private final IUserDTO userDTO;
    private final String newPassword;

    public ChangePasswordRequest(IUserDTO userDTO, String newPassword) {
        this.userDTO = userDTO;
        this.newPassword = newPassword;
    }
}
