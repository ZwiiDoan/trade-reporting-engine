package per.duyd.interview.tre.service.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import per.duyd.interview.tre.dto.request.ReportName;
import per.duyd.interview.tre.dto.request.SearchKey;
import per.duyd.interview.tre.entity.TradeEvent;

class DefaultReportPredicateBuilderTest {

  private StaticReportPredicateBuilder defaultReportPredicateBuilder;

  @BeforeEach
  void setUp() {
    defaultReportPredicateBuilder = new DefaultReportPredicateBuilder();
  }

  @Test
  void isApplicableTo_shouldReturnTrue_GivenValidReportName() {
    assertTrue(defaultReportPredicateBuilder.isApplicableTo(ReportName.DEFAULT));
  }

  @Test
  void isApplicableTo_shouldReturnFalse_GivenUnsupportedReportName() {
    assertFalse(defaultReportPredicateBuilder.isApplicableTo(ReportName.UNKNOWN));
  }

  @Test
  void getReportPredicate_shouldReturnDefaultPredicate_GivenValidReportName() {
    //Given:
    PathBuilder<TradeEvent> entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent");
    BooleanBuilder expected = new BooleanBuilder();

    expected.or(
        entityPath.getString(SearchKey.sellerParty.name()).eq("EMU_BANK")
            .and(entityPath.getString(SearchKey.premiumCurrency.name()).eq("AUD"))
    ).or(
        entityPath.getString(SearchKey.sellerParty.name()).eq("BISON_BANK")
            .and(entityPath.getString(SearchKey.premiumCurrency.name()).eq("USD"))
    );

    //When:
    Predicate actual = defaultReportPredicateBuilder.getReportPredicate();

    //Then:
    assertEquals(expected, actual);
  }
}