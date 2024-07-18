package per.duyd.interview.tre.service.predicate;

import com.querydsl.core.types.Predicate;
import per.duyd.interview.tre.dto.request.ReportName;

public interface StaticReportPredicateBuilder {
  boolean isApplicableTo(ReportName reportName);

  Predicate getReportPredicate();
}
