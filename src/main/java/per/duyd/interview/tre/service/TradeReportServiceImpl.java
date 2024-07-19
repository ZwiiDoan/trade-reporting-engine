package per.duyd.interview.tre.service;

import com.querydsl.core.types.Predicate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeReportServiceImpl implements TradeReportService {

  private final TradeEventLoader tradeEventLoader;
  private final DynamicReportPredicateBuilder dynamicReportPredicateBuilder;
  private final List<StaticReportPredicateBuilder> staticReportPredicateBuilders;
  private final TradeEventRepository tradeEventRepository;
  private final TradeEventMapper tradeEventMapper;
  private final List<SearchResultFilter> searchResultFilters;

  @Override
  @Transactional
  public int loadData(String eventFolderPath) {
    return tradeEventLoader.loadFromLocalFolder(eventFolderPath);
  }

  @Override
  public List<TradeEventDto> generate(SearchCriteria searchCriteria) {
    Predicate queryPredicate =
        dynamicReportPredicateBuilder.buildReportPredicate(searchCriteria);

    return getTradeReport(queryPredicate);
  }

  @Override
  public List<TradeEventDto> generate(ReportName reportName) {
    Predicate queryPredicate = staticReportPredicateBuilders.stream()
        .filter(
            staticReportPredicateBuilders -> staticReportPredicateBuilders.isApplicableTo(
                reportName)
        )
        .findFirst()
        .orElseThrow(
            () -> new InvalidReportNameException("Report " + reportName + " is not supported"))
        .getReportPredicate();

    return getTradeReport(queryPredicate);
  }

  private List<TradeEventDto> getTradeReport(Predicate queryPredicate) {
    List<TradeEvent> tradeEvents = (List<TradeEvent>) tradeEventRepository.findAll(queryPredicate);

    return tradeEvents.stream()
        .filter(tradeEvent -> searchResultFilters.stream()
            .noneMatch(searchResultFilter -> searchResultFilter.test(tradeEvent))
        )
        .map(tradeEventMapper::toDto)
        .toList();
  }
}
