package de.uol.swp.server.usermanagement;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserMapper {

    public static IUserDTO toDTO(IUser user) {
        return new UserDTO(user.getUsername(), user.getPassword());
    }

    public static IUser toUser(IUserDTO userDTO) {
        return new User(userDTO.getUsername(), userDTO.getPassword());
    }

    public static List<IUserDTO> toDTO(List<IUser> users) {
        List<IUserDTO> userDTOs = new ArrayList<>();
        for (IUser user : users) {
            userDTOs.add(toDTO(user));
        }
        return userDTOs;
    }

    public static List<IUser> toUser(List<IUserDTO> userDTOs) {
        List<IUser> users = new ArrayList<>();
        for (IUserDTO userDTO : userDTOs) {
            users.add(toUser(userDTO));
        }
        return users;
    }

    public static Collection<IUserDTO> toDTO(Collection<IUser> users) {
        Collection<IUserDTO> userDTOs = new ArrayList<>();
        for (IUser user : users) {
            userDTOs.add(toDTO(user));
        }
        return userDTOs;
    }
}