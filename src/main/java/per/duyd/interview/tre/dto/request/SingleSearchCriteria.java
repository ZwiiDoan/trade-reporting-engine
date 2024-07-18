package per.duyd.interview.tre.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleSearchCriteria extends SearchCriteria {
  @Builder.Default
  private SearchCriteriaType type = SearchCriteriaType.SINGLE;

  @NotNull
  private SearchKey key;

  @NotNull
  private ComparisonOperator cop;

  @NotNull
  private String value;
}
