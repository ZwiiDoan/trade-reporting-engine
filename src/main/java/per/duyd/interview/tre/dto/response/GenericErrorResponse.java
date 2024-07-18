package per.duyd.interview.tre.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericErrorResponse {
  private ResponseErrorCode errorCode;
  private String errorMessage;
}
