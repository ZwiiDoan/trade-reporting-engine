package per.duyd.interview.tre.integration;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import per.duyd.interview.tre.dto.response.GenericErrorResponse;
import per.duyd.interview.tre.dto.response.LoadDataResponse;

@SqlGroup({
    @Sql(scripts = "/sql/cleanup_trade_event_data.sql"),
    @Sql(scripts = "/sql/cleanup_trade_event_data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class LoadTradeReportDataTest extends BaseIntegrationTest {

  public static final String LOAD_TRADE_REPORT_DATA_PATH = "/v1/trade-report/load-data";

  public static Stream<Arguments> shouldReturnErrorResponseParams() {
    return Stream.of(
        Arguments.of("Invalid folder path", "/requests/load_data/invalid_folder_path_request.json",
            "/responses/load_data/invalid_request_response.json", HttpStatus.BAD_REQUEST),
        Arguments.of("Malformed event data", "/requests/load_data/malformed_data_request.json",
            "/responses/load_data/invalid_request_response.json", HttpStatus.BAD_REQUEST),
        Arguments.of("Invalid Json request", "/requests/load_data/invalid_json_request.json",
            "/responses/load_data/invalid_json_response.json", HttpStatus.BAD_REQUEST)
    );
  }

  @Test
  void application_shouldLoadTradeReportData_givenValidRequest() throws Exception {
    //Given:
    String requestFile = "/requests/load_data/valid_request.json";
    String responseFile = "/responses/load_data/valid_request_response.json";

    //When & Then:
    postAndValidateResponse(requestFile, responseFile, LoadDataResponse.class, HttpStatus.OK,
        LOAD_TRADE_REPORT_DATA_PATH);
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
        LOAD_TRADE_REPORT_DATA_PATH);
  }
}
