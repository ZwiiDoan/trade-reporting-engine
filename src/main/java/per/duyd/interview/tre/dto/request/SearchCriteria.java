package per.duyd.interview.tre.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CombinedSearchCriteria.class, name = "COMBINED"),
    @JsonSubTypes.Type(value = SingleSearchCriteria.class, name = "SINGLE")
})
public abstract class SearchCriteria {
  private SearchCriteriaType type;

  public enum SearchCriteriaType {
    COMBINED, SINGLE
  }
}