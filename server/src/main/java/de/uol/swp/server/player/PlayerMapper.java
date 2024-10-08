package de.uol.swp.server.player;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.player.PlayerDTO;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.city.CityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contains static utility methods to convert player model objects into their Data Transfer Object (DTO) forms.
 * This class is crucial for abstracting the player details that need to be sent over the network.
 * It handles the conversion of player data, including their current city and held cards, into a transferable format.
 */
public class PlayerMapper {

    /**
     * Converts a Player object into a PlayerDTO, which is a lighter, transferable version of the Player.
     * The conversion process includes converting the player's current city and card list into their respective DTO forms.
     *
     * @param player the Player object to be converted into a DTO
     * @return a PlayerDTO containing simplified, transferable data about the player
     */
    public static IPlayerDTO toDTO(Player player) {
        return new PlayerDTO(
                player.getUser()
                      .getUsername(),
                player.getRole()
                      .getName(),
                (CityDTO) CityMapper.toDTO(player.getCurrentPosition()),
                player.getCards()
                      .stream()
                      .map(CardMapper::toDTO)
                      .collect(Collectors.toList())
        );
    }

    /**
     * Converts a list of Player objects into a list of PlayerDTOs.
     * Each player in the list is individually converted using the toDTO method, and the resulting DTOs are collected into a list.
     * This method is useful for converting multiple player objects at once, typically for sending over the network.
     *
     * @param players the list of Player objects to convert
     * @return a List of IPlayerDTO representing the converted players
     */
    public static List<IPlayerDTO> toDTOList(List<Player> players) {
        List<IPlayerDTO> playerDTOS = new ArrayList<>();
        for (Player player : players) {
            PlayerDTO playerDTO = new PlayerDTO(
                    player.getUser()
                          .getUsername(),
                    player.getRole()
                          .getName(),
                    (CityDTO) CityMapper.toDTO(player.getCurrentPosition()),
                    player.getCards()
                          .stream()
                          .map(CardMapper::toDTO)
                          .collect(Collectors.toList())
            );
            playerDTOS.add(playerDTO);
        }
        return playerDTOS;
    }
}
