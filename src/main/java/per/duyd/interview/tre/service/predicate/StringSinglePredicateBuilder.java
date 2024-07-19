package per.duyd.interview.tre.service.predicate;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import per.duyd.interview.tre.dto.request.SearchKey;
import per.duyd.interview.tre.dto.request.SingleSearchCriteria;
import per.duyd.interview.tre.entity.TradeEvent;

@Component
public class StringSinglePredicateBuilder implements SinglePredicateBuilder<TradeEvent> {
  @Override
  public Predicate buildPredicate(PathBuilder<TradeEvent> entityPath,
                                  SingleSearchCriteria criteria) {
    StringPath path = entityPath.getString(criteria.getKey().name());
    String value = criteria.getValue();
    return switch (criteria.getCop()) {
      case EQ -> path.eq(value);
      case LIKE -> path.like("%" + value + "%");
      case IN -> path.in(
          Arrays.stream(criteria.getValue().split(",")).map(String::trim).toArray(String[]::new));
      default ->
          throw new IllegalArgumentException("Unsupported comparison operator for string field");
    };
  }

  @Override
  public boolean isApplicableTo(SearchKey searchKey) {
    return SearchKey.getStringKeys().contains(searchKey);
  }
}
