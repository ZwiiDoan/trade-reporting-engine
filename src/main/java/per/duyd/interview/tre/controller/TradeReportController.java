package per.duyd.interview.tre.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import per.duyd.interview.tre.dto.request.TradeReportRequest;
import per.duyd.interview.tre.dto.request.LoadTradeDataRequest;
import per.duyd.interview.tre.dto.request.ReportName;
import per.duyd.interview.tre.dto.response.TradeReportResponse;
import per.duyd.interview.tre.service.TradeReportService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TradeReportController {

  private final TradeReportService tradeReportService;

  @PostMapping("/v1/trade-report/generate")
  public TradeReportResponse generateTradeReport(
      @Valid @RequestBody TradeReportRequest tradeReportRequest) {
    return TradeReportResponse.builder()
        .tradeEvents(tradeReportService.generate(tradeReportRequest.getCriteria()))
        .build();
  }

  @GetMapping("/v1/trade-report/{reportName}")
  public TradeReportResponse getTradeReport(
      @Valid @PathVariable("reportName") ReportName reportName) {
    return TradeReportResponse.builder()
        .tradeEvents(tradeReportService.generate(reportName))
        .build();
  }

  @PostMapping("/v1/trade-report/load-data")
  public void loadTradeData(@Valid @RequestBody LoadTradeDataRequest loadTradeDataRequest) {
    tradeReportService.loadData(loadTradeDataRequest);
  }
}
