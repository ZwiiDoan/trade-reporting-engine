package per.duyd.interview.tre.service.predicate;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import per.duyd.interview.tre.dto.request.SearchKey;
import per.duyd.interview.tre.dto.request.SingleSearchCriteria;
import per.duyd.interview.tre.entity.TradeEvent;

public interface SinglePredicateBuilder<T> {
  Predicate buildPredicate(PathBuilder<T> entityPath, SingleSearchCriteria criteria);

  boolean isApplicableTo(SearchKey searchKey);
}
