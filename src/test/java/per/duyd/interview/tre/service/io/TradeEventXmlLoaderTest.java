package per.duyd.interview.tre.service.io;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static per.duyd.interview.tre.TestUtil.getTestResourceFilePath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import per.duyd.interview.tre.entity.TradeEvent;
import per.duyd.interview.tre.exception.InvalidInputFolderException;
import per.duyd.interview.tre.repository.TradeEventRepository;

class TradeEventXmlLoaderTest {

  private TradeEventParser tradeEventXmlParser;
  private TradeEventRepository tradeEventRepository;
  private TradeEventXmlLoader tradeEventXmlLoader;

  @BeforeEach
  void setUp() {
    tradeEventXmlParser = mock(TradeEventParser.class);
    tradeEventRepository = mock(TradeEventRepository.class);
    tradeEventXmlLoader = new TradeEventXmlLoader(tradeEventXmlParser, tradeEventRepository);
  }

  @Test
  void loadFromLocalFolder_shouldLoadTradeEvents_givenValidInputFolder() {
    //Given:
    String validFolderPath = getTestResourceFilePath("/events");
    when(tradeEventXmlParser.parseFromFile(anyString())).thenReturn(TradeEvent.builder().build());

    //When:
    tradeEventXmlLoader.loadFromLocalFolder(validFolderPath);

    //Then:
    verify(tradeEventXmlParser, times(8)).parseFromFile(anyString());
    verify(tradeEventRepository, times(1)).saveAll(anyList());
  }

  @Test
  void loadFromLocalFolder_shouldThrowException_givenValidInputFolder() {
    //Given:
    String invalidFolderPath = getTestResourceFilePath("/invalid_folder");
    when(tradeEventXmlParser.parseFromFile(anyString())).thenReturn(TradeEvent.builder().build());

    //When:
    assertThrows(InvalidInputFolderException.class,
        () -> tradeEventXmlLoader.loadFromLocalFolder(invalidFolderPath));

    //Then:
    verifyNoInteractions(tradeEventXmlParser);
    verifyNoInteractions(tradeEventRepository);
  }
}