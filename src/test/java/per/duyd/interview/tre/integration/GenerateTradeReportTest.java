package per.duyd.interview.tre.integration;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import per.duyd.interview.tre.dto.response.GenericErrorResponse;
import per.duyd.interview.tre.dto.response.TradeReportResponse;

@SqlGroup({
    @Sql(scripts = "/sql/seed_trade_event_data.sql"),
    @Sql(scripts = "/sql/cleanup_trade_event_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class GenerateTradeReportTest extends BaseIntegrationTest {

  public static final String GENERATE_TRADE_REPORT_PATH = "/v1/trade-report/generate";

  public static Stream<Arguments> shouldReturnErrorResponseParams() {
    return Stream.of(
        Arguments.of("Invalid search criteria",
            "/requests/generate_report/invalid_search_criteria_request.json",
            "/responses/generate_report/invalid_search_criteria_response.json",
            HttpStatus.BAD_REQUEST),
        Arguments.of("Invalid JSON request", "/requests/generate_report/invalid_json_request.json",
            "/responses/generate_report/invalid_json_response.json", HttpStatus.BAD_REQUEST)
    );
  }

  public static Stream<Arguments> shouldGenerateTradeReportDataParams() {
    return Stream.of(
        Arguments.of("Original search criteria for Trade Reporting Engine",
            "/requests/generate_report/default_report_request.json",
            "/responses/generate_report/default_report_response.json", HttpStatus.OK),
        Arguments.of("Trade Events from anagrams party should be filtered out",
            "/requests/generate_report/anagrams_search_criteria_request.json",
            "/responses/generate_report/anagrams_search_criteria_response.json", HttpStatus.OK),
        Arguments.of("Combined search criteria should retrieve correct data",
            "/requests/generate_report/combined_search_criteria_request.json",
            "/responses/generate_report/combined_search_criteria_response.json", HttpStatus.OK),
        Arguments.of("Complex search criteria should retrieve correct data",
            "/requests/generate_report/complex_search_criteria_request.json",
            "/responses/generate_report/complex_search_criteria_response.json", HttpStatus.OK),
        Arguments.of("Single search criteria should retrieve correct data",
            "/requests/generate_report/single_search_criteria_request.json",
            "/responses/generate_report/single_search_criteria_response.json", HttpStatus.OK)
    );
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("shouldGenerateTradeReportDataParams")
  void application_shouldGenerateTradeReportData_givenValidRequest(String description,
                                                                   String requestFile,
                                                                   String responseFile,
                                                                   HttpStatus expectedResponseStatus
  ) throws Exception {
    postAndValidateResponse(requestFile, responseFile, TradeReportResponse.class,
        expectedResponseStatus,
        GENERATE_TRADE_REPORT_PATH);
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("shouldReturnErrorResponseParams")
  void application_shouldReturnErrorResponse_givenInvalidRequest(String description,
                                                                 String requestFile,
                                                                 String responseFile,
                                                                 HttpStatus expectedResponseStatus
  ) throws Exception {
    postAndValidateResponse(requestFile, responseFile, GenericErrorResponse.class,
        expectedResponseStatus,
        GENERATE_TRADE_REPORT_PATH);
  }
}
