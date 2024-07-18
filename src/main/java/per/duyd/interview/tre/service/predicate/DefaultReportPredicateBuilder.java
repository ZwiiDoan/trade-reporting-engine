package per.duyd.interview.tre.service.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.stereotype.Component;
import per.duyd.interview.tre.dto.request.ReportName;
import per.duyd.interview.tre.dto.request.SearchKey;
import per.duyd.interview.tre.entity.TradeEvent;

@Component
public class DefaultReportPredicateBuilder implements StaticReportPredicateBuilder {
  @Override
  public boolean isApplicableTo(ReportName reportName) {
    return ReportName.DEFAULT == reportName;
  }

  @Override
  public Predicate getReportPredicate() {
    PathBuilder<TradeEvent> entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent");
    BooleanBuilder reportPredicate = new BooleanBuilder();

    reportPredicate.or(
        entityPath.getString(SearchKey.sellerParty.name()).eq("EMU_BANK")
            .and(entityPath.getString(SearchKey.premiumCurrency.name()).eq("AUD"))
    ).or(
        entityPath.getString(SearchKey.sellerParty.name()).eq("BISON_BANK")
            .and(entityPath.getString(SearchKey.premiumCurrency.name()).eq("USD"))
    );

    return reportPredicate;
  }
}
