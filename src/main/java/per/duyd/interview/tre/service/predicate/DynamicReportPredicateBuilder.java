package per.duyd.interview.tre.service.predicate;

import com.querydsl.core.BooleanBuilder;
import per.duyd.interview.tre.dto.request.SearchCriteria;

public interface DynamicReportPredicateBuilder {
  BooleanBuilder buildReportPredicate(SearchCriteria searchCriteria);
}
