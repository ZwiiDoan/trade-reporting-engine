package per.duyd.interview.tre.service.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static per.duyd.interview.tre.TestUtil.getTestResourceFilePath;

import java.math.BigDecimal;
import javax.xml.xpath.XPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import per.duyd.interview.tre.config.AppConfig;
import per.duyd.interview.tre.entity.TradeEvent;
import per.duyd.interview.tre.exception.InvalidInputFileException;

class TradeEventXmlParserTest {

  private final XPath xPath = new AppConfig().xpath();
  private TradeEventParser tradeEventParser;

  @BeforeEach
  void setUp() {
    tradeEventParser = new TradeEventXmlParser(xPath);
  }

  @Test
  void parseFromFile_shouldCreateExpectedTradeEvent_givenValidXmlFile() {
    //Given:
    TradeEvent expected = TradeEvent.builder()
        .buyerParty("LEFT_BANK")
        .sellerParty("EMU_BANK")
        .premiumAmount(new BigDecimal("100.00"))
        .premiumCurrency("AUD")
        .build();

    //When:
    TradeEvent actual =
        tradeEventParser.parseFromFile(getTestResourceFilePath("/events/event0.xml"));

    //Then:
    assertEquals(expected, actual);
  }

  @Test
  void parseFromFile_shouldThrowException_givenInvalidXmlFile() {
    //When:
    assertThrows(InvalidInputFileException.class,
        () -> tradeEventParser.parseFromFile(getTestResourceFilePath(
            "/invalid_events/invalid_event.xml")));
  }
}