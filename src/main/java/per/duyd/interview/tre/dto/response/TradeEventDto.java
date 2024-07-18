package per.duyd.interview.tre.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TradeEventDto {
  @NotBlank
  private String buyerParty;

  @NotBlank
  private String sellerParty;

  @Positive
  private BigDecimal premiumAmount;

  @NotBlank
  private String premiumCurrency;
}
