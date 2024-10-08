package de.uol.swp.server.player;

import de.uol.swp.common.city.CityDTO;
import de.uol.swp.common.player.IPlayerDTO;
import de.uol.swp.common.player.PlayerDTO;
import de.uol.swp.server.cards.CardMapper;
import de.uol.swp.server.city.City;
import de.uol.swp.server.city.CityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerMapper {
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
