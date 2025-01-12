package de.uol.swp.server.usermanagement;

import de.uol.swp.common.user.IUserDTO;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class UserMapperTest {

    @Test
     void testToDTO() {
        IUser user = new User("username", "password");
        IUserDTO userDTO = UserMapper.toDTO(user);

        assertEquals("username", userDTO.getUsername());
        assertEquals("{SHA512}sQnzu7wkTrgkQZF+0G1hi5AI3Qmzvv0bXgc5THBqi7mAsdd4Xll27ASbRt9fEyavWi6m0QP9B8lThf+rDKy8hg==", userDTO.getPassword());
    }

    @Test
     void testToDTOList() {
        List<IUser> users = new ArrayList<>();
        users.add(new User("username1", "password1"));
        users.add(new User("username2", "password2"));

        List<IUserDTO> userDTOs = UserMapper.toDTO(users);

        assertEquals(2, userDTOs.size());
        assertEquals("username1", userDTOs.get(0).getUsername());
        assertEquals("{SHA512}vFR3ULknl/lVs2ESzJvdXN330IYhUdA6FnraiZWqJKmtJGELNqaLwC2iQUHuUWcK6hPtZGkJmkRT8zXLI5212g==", userDTOs.get(0).getPassword());
        assertEquals("username2", userDTOs.get(1).getUsername());
        assertEquals("{SHA512}kqiR+IjnnRwui4JmPA83zG1hRmxQjsYrgTJYiv41RxKyC7dUKaogqjq3z8xYg2xzQwa0Pv02gICiJQgxv382Pw==", userDTOs.get(1).getPassword());
    }

    @Test
     void testToUser() {
        IUserDTO userDTO = new UserDTO("username", "password");
        IUser user = UserMapper.toUser(userDTO);

        assertEquals("username", user.getUsername());
        assertEquals("{SHA512}sQnzu7wkTrgkQZF+0G1hi5AI3Qmzvv0bXgc5THBqi7mAsdd4Xll27ASbRt9fEyavWi6m0QP9B8lThf+rDKy8hg==", user.getPassword());
    }

    @Test
     void testToUserList() {
        List<IUserDTO> userDTOs = new ArrayList<>();
        userDTOs.add(new UserDTO("username1", "password1"));
        userDTOs.add(new UserDTO("username2", "password2"));

        List<IUser> users = UserMapper.toUser(userDTOs);

        assertEquals(2, users.size());
        assertEquals("username1", users.get(0).getUsername());
        assertEquals("{SHA512}vFR3ULknl/lVs2ESzJvdXN330IYhUdA6FnraiZWqJKmtJGELNqaLwC2iQUHuUWcK6hPtZGkJmkRT8zXLI5212g==", users.get(0).getPassword());
        assertEquals("username2", users.get(1).getUsername());
        assertEquals("{SHA512}kqiR+IjnnRwui4JmPA83zG1hRmxQjsYrgTJYiv41RxKyC7dUKaogqjq3z8xYg2xzQwa0Pv02gICiJQgxv382Pw==", users.get(1).getPassword());
    }
}