package per.duyd.interview.tre.service.predicate;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import per.duyd.interview.tre.dto.request.SearchKey;
import per.duyd.interview.tre.dto.request.SingleSearchCriteria;
import per.duyd.interview.tre.entity.TradeEvent;

@Component
public class DecimalSinglePredicateBuilder implements SinglePredicateBuilder<TradeEvent> {
  @Override
  public Predicate buildPredicate(PathBuilder<TradeEvent> entityPath,
                                  SingleSearchCriteria criteria) {
    NumberPath<BigDecimal> path = entityPath.getNumber(criteria.getKey().name(), BigDecimal.class);
    String value = criteria.getValue();
    return switch (criteria.getCop()) {
      case EQ -> path.eq(new BigDecimal(value));
      case GT -> path.gt(new BigDecimal(value));
      case GTE -> path.goe(new BigDecimal(value));
      case LT -> path.lt(new BigDecimal(value));
      case LTE -> path.loe(new BigDecimal(value));
      case IN -> path.in(Arrays.stream(value.split(",")).map(String::trim).map(BigDecimal::new)
          .toArray(BigDecimal[]::new));
      default -> throw new IllegalArgumentException(
          "Unsupported comparison operator for BigDecimal field");
    };
  }

  @Override
  public boolean isApplicableTo(SearchKey searchKey) {
    return SearchKey.getBigDecimalKeys().contains(searchKey);
  }
}
