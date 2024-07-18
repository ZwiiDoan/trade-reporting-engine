package per.duyd.interview.tre.integration;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import per.duyd.interview.tre.dto.request.ReportName;
import per.duyd.interview.tre.dto.response.GenericErrorResponse;
import per.duyd.interview.tre.dto.response.TradeReportResponse;

@SqlGroup({
    @Sql(scripts = "/sql/seed_trade_event_data.sql"),
    @Sql(scripts = "/sql/cleanup_trade_event_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class GetTradeReportTest extends BaseIntegrationTest {

  public static final String GET_TRADE_REPORT_PATH = "/v1/trade-report";

  public static Stream<Arguments> shouldReturnErrorResponseParams() {
    return Stream.of(
        Arguments.of("Invalid Report Name", "Invalid Report Name",
            "/responses/get_report/invalid_report_name_response.json", HttpStatus.BAD_REQUEST),
        Arguments.of("Report Name has not been supported yet", ReportName.UNKNOWN.name(),
            "/responses/get_report/not_supported_report_response.json", HttpStatus.BAD_REQUEST)
    );
  }

  @Test
  void application_shouldGetTradeReportData_givenValidReportName() throws Exception {
    getAndValidateResponse(
        "/responses/generate_report/default_report_response.json",
        TradeReportResponse.class,
        HttpStatus.OK,
        GET_TRADE_REPORT_PATH + "/" + ReportName.DEFAULT);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("shouldReturnErrorResponseParams")
  void application_shouldReturnErrorResponse_givenInvalidReportName(
      String description, String reportName, String responseFile, HttpStatus expectedResponseStatus)
      throws Exception {
    getAndValidateResponse(
        responseFile,
        GenericErrorResponse.class,
        expectedResponseStatus,
        GET_TRADE_REPORT_PATH + "/" + reportName);
  }
}
