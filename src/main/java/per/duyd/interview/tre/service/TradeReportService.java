package per.duyd.interview.tre.service;

import java.util.List;
import per.duyd.interview.tre.dto.request.SearchCriteria;
import per.duyd.interview.tre.dto.request.LoadTradeDataRequest;
import per.duyd.interview.tre.dto.request.ReportName;
import per.duyd.interview.tre.dto.response.TradeEventDto;

public interface TradeReportService {
  void loadData(LoadTradeDataRequest loadTradeDataRequest);

  List<TradeEventDto> generate(SearchCriteria searchCriteria);

  List<TradeEventDto> generate(ReportName reportName);
}
