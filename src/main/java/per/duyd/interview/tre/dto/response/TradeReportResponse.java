package per.duyd.interview.tre.dto.response;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradeReportResponse {
  private List<@Valid TradeEventDto> tradeEvents;
}
