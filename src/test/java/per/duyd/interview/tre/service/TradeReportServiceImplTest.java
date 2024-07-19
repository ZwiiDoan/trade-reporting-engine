package per.duyd.interview.tre.service;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.querydsl.core.BooleanBuilder;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import per.duyd.interview.tre.dto.request.LoadTradeDataRequest;
import per.duyd.interview.tre.dto.request.ReportName;
import per.duyd.interview.tre.dto.request.SearchCriteria;
import per.duyd.interview.tre.dto.response.TradeEventDto;
import per.duyd.interview.tre.entity.TradeEvent;
import per.duyd.interview.tre.exception.InvalidReportNameException;
import per.duyd.interview.tre.repository.TradeEventRepository;
import per.duyd.interview.tre.service.filter.SearchResultFilter;
import per.duyd.interview.tre.service.io.TradeEventLoader;
import per.duyd.interview.tre.service.mapper.TradeEventMapper;
import per.duyd.interview.tre.service.predicate.DynamicReportPredicateBuilder;
import per.duyd.interview.tre.service.predicate.StaticReportPredicateBuilder;

class TradeReportServiceImplTest {
  private TradeEventLoader tradeEventLoader;
  private DynamicReportPredicateBuilder dynamicReportPredicateBuilder;
  private StaticReportPredicateBuilder staticReportPredicateBuilder;
  private TradeEventRepository tradeEventRepository;
  private TradeEventMapper tradeEventMapper;
  private SearchResultFilter searchResultFilter;


  private TradeReportService tradeReportService;

  @BeforeEach
  void setUp() {
    tradeEventLoader = mock(TradeEventLoader.class);
    dynamicReportPredicateBuilder = mock(DynamicReportPredicateBuilder.class);
    tradeEventRepository = mock(TradeEventRepository.class);
    tradeEventMapper = mock(TradeEventMapper.class);
    searchResultFilter = mock(SearchResultFilter.class);
    staticReportPredicateBuilder = mock(StaticReportPredicateBuilder.class);

    tradeReportService = new TradeReportServiceImpl(tradeEventLoader, dynamicReportPredicateBuilder,
        List.of(staticReportPredicateBuilder), tradeEventRepository, tradeEventMapper,
        List.of(searchResultFilter));
  }

  @Test
  void loadData_shouldLoadTradeEventsFromLocalFolder_givenValidRequest() {
    //Given:
    String eventFolderPath = "test-folder";

    //When:
    tradeReportService.loadData(eventFolderPath);

    //Then:
    verify(tradeEventLoader).loadFromLocalFolder("test-folder");
  }

  @Test
  void generate_shouldGenerateTradeReport_givenValidSearchCriteria() {
    //Given:
    SearchCriteria searchCriteria = mock(SearchCriteria.class);
    BooleanBuilder queryPredicate = mock(BooleanBuilder.class);
    when(dynamicReportPredicateBuilder.buildReportPredicate(searchCriteria)).thenReturn(
        queryPredicate);

    TradeEvent tradeEvent = mock(TradeEvent.class);
    when(tradeEventRepository.findAll(queryPredicate)).thenReturn(List.of(tradeEvent));
    when(searchResultFilter.test(tradeEvent)).thenReturn(false);

    TradeEventDto tradeEventDto = mock(TradeEventDto.class);
    when(tradeEventMapper.toDto(tradeEvent)).thenReturn(tradeEventDto);
    List<TradeEventDto> expected = List.of(tradeEventDto);

    //When:
    List<TradeEventDto> actual = tradeReportService.generate(searchCriteria);

    //Then:
    assertIterableEquals(expected, actual);
  }

  @Test
  void generate_shouldGetTradeReport_givenValidReportName() {
    //Given:
    ReportName reportName = ReportName.DEFAULT;
    when(staticReportPredicateBuilder.isApplicableTo(reportName)).thenReturn(true);

    BooleanBuilder queryPredicate = mock(BooleanBuilder.class);
    when(staticReportPredicateBuilder.getReportPredicate()).thenReturn(queryPredicate);

    TradeEvent tradeEvent = mock(TradeEvent.class);
    when(tradeEventRepository.findAll(queryPredicate)).thenReturn(List.of(tradeEvent));
    when(searchResultFilter.test(tradeEvent)).thenReturn(false);

    TradeEventDto tradeEventDto = mock(TradeEventDto.class);
    when(tradeEventMapper.toDto(tradeEvent)).thenReturn(tradeEventDto);
    List<TradeEventDto> expected = List.of(tradeEventDto);

    //When:
    List<TradeEventDto> actual = tradeReportService.generate(reportName);

    //Then:
    assertIterableEquals(expected, actual);
  }

  @Test
  void generate_shouldThrowExceptions_givenUnsupportedReportName() {
    //Given
    ReportName reportName = ReportName.UNKNOWN;
    when(staticReportPredicateBuilder.isApplicableTo(reportName)).thenReturn(false);

    //When & Then:
    assertThrows(InvalidReportNameException.class, () -> tradeReportService.generate(reportName));
  }
}