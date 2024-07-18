package per.duyd.interview.tre.dto.request;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CombinedSearchCriteria extends SearchCriteria {
  @Builder.Default
  private SearchCriteriaType type = SearchCriteriaType.COMBINED;

  @Builder.Default
  private LogicalOperator lop = LogicalOperator.AND;

  private List<@Valid SearchCriteria> criteria;
}
