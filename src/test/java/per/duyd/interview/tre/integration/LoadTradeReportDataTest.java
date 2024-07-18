package per.duyd.interview.tre.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import per.duyd.interview.tre.dto.response.GenericErrorResponse;
import per.duyd.interview.tre.repository.TradeEventRepository;

class LoadTradeReportDataTest extends BaseIntegrationTest {

  @Autowired
  private TradeEventRepository tradeEventRepository;

  public static final String LOAD_TRADE_REPORT_DATA_PATH = "/v1/trade-report/load-data";

  public static Stream<Arguments> shouldReturnErrorResponseParams() {
    return Stream.of(
        Arguments.of("Invalid folder path", "/requests/load_data/invalid_folder_path_request.json",
            "/responses/load_data/invalid_request_response.json", HttpStatus.BAD_REQUEST),
        Arguments.of("Malformed event data", "/requests/load_data/malformed_data_request.json",
            "/responses/load_data/invalid_request_response.json", HttpStatus.BAD_REQUEST)
    );
  }

  @Test
  void application_shouldLoadTradeReportData_givenValidRequest() throws Exception {
    //Given:
    String requestFile = "/requests/load_data/valid_request.json";

    //When:
    postAndValidateResponseStatus(requestFile, HttpStatus.OK.value(), LOAD_TRADE_REPORT_DATA_PATH);

    //Then:
    assertEquals(8, tradeEventRepository.findAll().size());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("shouldReturnErrorResponseParams")
  void application_shouldReturnErrorResponse_givenInvalidRequest(String description,
                                                                 String requestFile,
                                                                 String responseFile,
                                                                 HttpStatus expectedResponseStatus
  ) throws Exception {
    postAndValidateResponse(requestFile, responseFile, GenericErrorResponse.class, expectedResponseStatus,
        LOAD_TRADE_REPORT_DATA_PATH);
  }
}
