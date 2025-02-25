package de.uol.swp.server.cards;

import com.google.inject.Inject;
import de.uol.swp.common.city.CityName;
import de.uol.swp.server.cards.data.CityCard;
import de.uol.swp.server.cards.data.ICard;
import de.uol.swp.server.cards.data.InfectionCard;
import de.uol.swp.server.cards.data.eventcards.AnotherDayEventCard;
import de.uol.swp.server.cards.data.eventcards.OnTheMoveDayAndNightEventCard;
import de.uol.swp.server.cards.data.eventcards.StateMobilizationEventCard;
import de.uol.swp.server.cards.data.eventcards.TreatWaterEventCard;
import de.uol.swp.server.city.CityRepository;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CardRepository {
    CityRepository cityRepository;
    private Map<Integer, ICard> cards;

    @Inject
    public CardRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
        createAllCards();
    }

    private void createAllCards() {
        cards = new HashMap<>();

        assert cityRepository != null;
        cards.put(1, new CityCard(1, CityName.PORTO.getDisplayName(), cityRepository.getCity(1)));
        cards.put(2, new CityCard(2, CityName.COIMBRA.getDisplayName(), cityRepository.getCity(2)));
        cards.put(3, new CityCard(3, CityName.LISBOA.getDisplayName(), cityRepository.getCity(3)));
        cards.put(4, new CityCard(4, CityName.ALBUFEIRA.getDisplayName(), cityRepository.getCity(4)));
        cards.put(5, new CityCard(5, CityName.EVORA.getDisplayName(), cityRepository.getCity(5)));
        cards.put(6, new CityCard(6, CityName.CACERES.getDisplayName(), cityRepository.getCity(6)));
        cards.put(7, new CityCard(7, CityName.SALAMANCA.getDisplayName(), cityRepository.getCity(7)));
        cards.put(8, new CityCard(8, CityName.BRAGA.getDisplayName(), cityRepository.getCity(8)));
        cards.put(9, new CityCard(9, CityName.VIGO.getDisplayName(), cityRepository.getCity(9)));
        cards.put(10, new CityCard(10, CityName.SANTIAGO_DE_COMPOSTELA.getDisplayName(), cityRepository.getCity(10)));
        cards.put(11, new CityCard(11, CityName.OURENSE.getDisplayName(), cityRepository.getCity(11)));
        cards.put(12, new CityCard(12, CityName.A_CORUNA.getDisplayName(), cityRepository.getCity(12)));
        cards.put(13, new CityCard(13, CityName.LEON.getDisplayName(), cityRepository.getCity(13)));
        cards.put(14, new CityCard(14, CityName.GIJON.getDisplayName(), cityRepository.getCity(14)));
        cards.put(15, new CityCard(15, CityName.SANTANDER.getDisplayName(), cityRepository.getCity(15)));
        cards.put(16, new CityCard(16, CityName.VALLADOLID.getDisplayName(), cityRepository.getCity(16)));
        cards.put(17, new CityCard(17, CityName.MADRID.getDisplayName(), cityRepository.getCity(17)));
        cards.put(18, new CityCard(18, CityName.SORIA.getDisplayName(), cityRepository.getCity(18)));
        cards.put(19, new CityCard(19, CityName.BURGOS.getDisplayName(), cityRepository.getCity(19)));
        cards.put(20, new CityCard(20, CityName.VICTORIA_GASTEIZ.getDisplayName(), cityRepository.getCity(20)));
        cards.put(21, new CityCard(21, CityName.BILBAO_BILBO.getDisplayName(), cityRepository.getCity(21)));
        cards.put(22, new CityCard(22, CityName.SAN_SEBASTIAN_DONOSTIA.getDisplayName(), cityRepository.getCity(22)));
        cards.put(23, new CityCard(23, CityName.PAMPLONA.getDisplayName(), cityRepository.getCity(23)));
        cards.put(24, new CityCard(24, CityName.HUESCA.getDisplayName(), cityRepository.getCity(24)));
        cards.put(25, new CityCard(25, CityName.ZARAGOZA.getDisplayName(), cityRepository.getCity(25)));
        cards.put(26, new CityCard(26, CityName.ANDORRA_LA_VELLA.getDisplayName(), cityRepository.getCity(26)));
        cards.put(27, new CityCard(27, CityName.BARCELONA.getDisplayName(), cityRepository.getCity(27)));
        cards.put(28, new CityCard(28, CityName.GIRONA.getDisplayName(), cityRepository.getCity(28)));
        cards.put(29, new CityCard(29, CityName.PALMA_DE_MALLORCA.getDisplayName(), cityRepository.getCity(29)));
        cards.put(30, new CityCard(30, CityName.TARRAGONA.getDisplayName(), cityRepository.getCity(30)));
        cards.put(31, new CityCard(31, CityName.VALENCIA.getDisplayName(), cityRepository.getCity(31)));
        cards.put(32, new CityCard(32, CityName.ALICANTE.getDisplayName(), cityRepository.getCity(32)));
        cards.put(33, new CityCard(33, CityName.CARTAGENA.getDisplayName(), cityRepository.getCity(33)));
        cards.put(34, new CityCard(34, CityName.ALBACETE.getDisplayName(), cityRepository.getCity(34)));
        cards.put(35, new CityCard(35, CityName.CUENCA.getDisplayName(), cityRepository.getCity(35)));
        cards.put(36, new CityCard(36, CityName.TERUEL.getDisplayName(), cityRepository.getCity(36)));
        cards.put(37, new CityCard(37, CityName.TOLEDO.getDisplayName(), cityRepository.getCity(37)));
        cards.put(38, new CityCard(38, CityName.CIUDAD_REAL.getDisplayName(), cityRepository.getCity(38)));
        cards.put(39, new CityCard(39, CityName.BADAJOZ.getDisplayName(), cityRepository.getCity(39)));
        cards.put(40, new CityCard(40, CityName.CORDOBA.getDisplayName(), cityRepository.getCity(40)));
        cards.put(41, new CityCard(41, CityName.JAEN.getDisplayName(), cityRepository.getCity(41)));
        cards.put(42, new CityCard(42, CityName.GRANADA.getDisplayName(), cityRepository.getCity(42)));
        cards.put(43, new CityCard(43, CityName.ALMERIA.getDisplayName(), cityRepository.getCity(43)));
        cards.put(44, new CityCard(44, CityName.MALAGA.getDisplayName(), cityRepository.getCity(44)));
        cards.put(45, new CityCard(45, CityName.GIBRALTAR.getDisplayName(), cityRepository.getCity(45)));
        cards.put(46, new CityCard(46, CityName.CADIZ.getDisplayName(), cityRepository.getCity(46)));
        cards.put(47, new CityCard(47, CityName.SEVILLA.getDisplayName(), cityRepository.getCity(47)));
        cards.put(48, new CityCard(48, CityName.HUELVA.getDisplayName(), cityRepository.getCity(48)));
        cards.put(101, new InfectionCard(101, CityName.PORTO.getDisplayName(), cityRepository.getCity(1)));
        cards.put(102, new InfectionCard(102, CityName.COIMBRA.getDisplayName(), cityRepository.getCity(2)));
        cards.put(103, new InfectionCard(103, CityName.LISBOA.getDisplayName(), cityRepository.getCity(3)));
        cards.put(104, new InfectionCard(104, CityName.ALBUFEIRA.getDisplayName(), cityRepository.getCity(4)));
        cards.put(105, new InfectionCard(105, CityName.EVORA.getDisplayName(), cityRepository.getCity(5)));
        cards.put(106, new InfectionCard(106, CityName.CACERES.getDisplayName(), cityRepository.getCity(6)));
        cards.put(107, new InfectionCard(107, CityName.SALAMANCA.getDisplayName(), cityRepository.getCity(7)));
        cards.put(108, new InfectionCard(108, CityName.BRAGA.getDisplayName(), cityRepository.getCity(8)));
        cards.put(109, new InfectionCard(109, CityName.VIGO.getDisplayName(), cityRepository.getCity(9)));
        cards.put(
                110,
                new InfectionCard(110, CityName.SANTIAGO_DE_COMPOSTELA.getDisplayName(), cityRepository.getCity(10))
        );
        cards.put(111, new InfectionCard(111, CityName.OURENSE.getDisplayName(), cityRepository.getCity(11)));
        cards.put(112, new InfectionCard(112, CityName.A_CORUNA.getDisplayName(), cityRepository.getCity(12)));
        cards.put(113, new InfectionCard(113, CityName.LEON.getDisplayName(), cityRepository.getCity(13)));
        cards.put(114, new InfectionCard(114, CityName.GIJON.getDisplayName(), cityRepository.getCity(14)));
        cards.put(115, new InfectionCard(115, CityName.SANTANDER.getDisplayName(), cityRepository.getCity(15)));
        cards.put(116, new InfectionCard(116, CityName.VALLADOLID.getDisplayName(), cityRepository.getCity(16)));
        cards.put(117, new InfectionCard(117, CityName.MADRID.getDisplayName(), cityRepository.getCity(17)));
        cards.put(118, new InfectionCard(118, CityName.SORIA.getDisplayName(), cityRepository.getCity(18)));
        cards.put(119, new InfectionCard(119, CityName.BURGOS.getDisplayName(), cityRepository.getCity(19)));
        cards.put(120, new InfectionCard(120, CityName.VICTORIA_GASTEIZ.getDisplayName(), cityRepository.getCity(20)));
        cards.put(121, new InfectionCard(121, CityName.BILBAO_BILBO.getDisplayName(), cityRepository.getCity(21)));
        cards.put(
                122,
                new InfectionCard(122, CityName.SAN_SEBASTIAN_DONOSTIA.getDisplayName(), cityRepository.getCity(22))
        );
        cards.put(123, new InfectionCard(123, CityName.PAMPLONA.getDisplayName(), cityRepository.getCity(23)));
        cards.put(124, new InfectionCard(124, CityName.HUESCA.getDisplayName(), cityRepository.getCity(24)));
        cards.put(125, new InfectionCard(125, CityName.ZARAGOZA.getDisplayName(), cityRepository.getCity(25)));
        cards.put(126, new InfectionCard(126, CityName.ANDORRA_LA_VELLA.getDisplayName(), cityRepository.getCity(26)));
        cards.put(127, new InfectionCard(127, CityName.BARCELONA.getDisplayName(), cityRepository.getCity(27)));
        cards.put(128, new InfectionCard(128, CityName.GIRONA.getDisplayName(), cityRepository.getCity(28)));
        cards.put(129, new InfectionCard(129, CityName.PALMA_DE_MALLORCA.getDisplayName(), cityRepository.getCity(29)));
        cards.put(130, new InfectionCard(130, CityName.TARRAGONA.getDisplayName(), cityRepository.getCity(30)));
        cards.put(131, new InfectionCard(131, CityName.VALENCIA.getDisplayName(), cityRepository.getCity(31)));
        cards.put(132, new InfectionCard(132, CityName.ALICANTE.getDisplayName(), cityRepository.getCity(32)));
        cards.put(133, new InfectionCard(133, CityName.CARTAGENA.getDisplayName(), cityRepository.getCity(33)));
        cards.put(134, new InfectionCard(134, CityName.ALBACETE.getDisplayName(), cityRepository.getCity(34)));
        cards.put(135, new InfectionCard(135, CityName.CUENCA.getDisplayName(), cityRepository.getCity(35)));
        cards.put(136, new InfectionCard(136, CityName.TERUEL.getDisplayName(), cityRepository.getCity(36)));
        cards.put(137, new InfectionCard(137, CityName.TOLEDO.getDisplayName(), cityRepository.getCity(37)));
        cards.put(138, new InfectionCard(138, CityName.CIUDAD_REAL.getDisplayName(), cityRepository.getCity(38)));
        cards.put(139, new InfectionCard(139, CityName.BADAJOZ.getDisplayName(), cityRepository.getCity(39)));
        cards.put(140, new InfectionCard(140, CityName.CORDOBA.getDisplayName(), cityRepository.getCity(40)));
        cards.put(141, new InfectionCard(141, CityName.JAEN.getDisplayName(), cityRepository.getCity(41)));
        cards.put(142, new InfectionCard(142, CityName.GRANADA.getDisplayName(), cityRepository.getCity(42)));
        cards.put(143, new InfectionCard(143, CityName.ALMERIA.getDisplayName(), cityRepository.getCity(43)));
        cards.put(144, new InfectionCard(144, CityName.MALAGA.getDisplayName(), cityRepository.getCity(44)));
        cards.put(145, new InfectionCard(145, CityName.GIBRALTAR.getDisplayName(), cityRepository.getCity(45)));
        cards.put(146, new InfectionCard(146, CityName.CADIZ.getDisplayName(), cityRepository.getCity(46)));
        cards.put(147, new InfectionCard(147, CityName.SEVILLA.getDisplayName(), cityRepository.getCity(47)));
        cards.put(148, new InfectionCard(148, CityName.HUELVA.getDisplayName(), cityRepository.getCity(48)));
        cards.put(207, new OnTheMoveDayAndNightEventCard(207));
        cards.put(208, new AnotherDayEventCard(208));
        cards.put(209, new StateMobilizationEventCard(209));
        cards.put(210, new TreatWaterEventCard(210));
    }
}
