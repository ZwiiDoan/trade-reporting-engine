package per.duyd.interview.tre.service.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import per.duyd.interview.tre.entity.TradeEvent;

class AnagramsPartyFilterTest {

  private SearchResultFilter anagramsPartyFilter;

  public static Stream<Arguments> testAnagramsPartyParams() {
    return Stream.of(
        Arguments.of("2 parties are anagrams", TradeEvent.builder()
            .buyerParty("buyer_Party1").sellerParty("p_artyBuyer1").build(), true),
        Arguments.of("2 parties have different length", TradeEvent.builder()
            .buyerParty("buyerParty1").sellerParty("buyerParty12").build(), false),
        Arguments.of("2 parties are not anagrams", TradeEvent.builder()
            .buyerParty("buyerParty1").sellerParty("buyerParty2").build(), false)
    );
  }

  @BeforeEach
  void setUp() {
    anagramsPartyFilter = new AnagramsPartyFilter();
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("testAnagramsPartyParams")
  void test_shouldDetectAnagramsParty_givenDifferentInputs(String description,
                                                           TradeEvent tradeEvent,
                                                           boolean expected) {
    assertEquals(expected, anagramsPartyFilter.test(tradeEvent));
  }
}