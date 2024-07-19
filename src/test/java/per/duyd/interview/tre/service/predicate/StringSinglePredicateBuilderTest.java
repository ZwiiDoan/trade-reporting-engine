package per.duyd.interview.tre.service.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import per.duyd.interview.tre.dto.request.ComparisonOperator;
import per.duyd.interview.tre.dto.request.SearchKey;
import per.duyd.interview.tre.dto.request.SingleSearchCriteria;
import per.duyd.interview.tre.entity.TradeEvent;

class StringSinglePredicateBuilderTest {

  private static final String TEST_VALUE = "Test";
  private SinglePredicateBuilder<TradeEvent> stringSinglePredicateBuilder;
  private PathBuilder<TradeEvent> entityPath;

  public static Stream<Arguments> buildPredicateTestParams() {
    StringPath entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent")
        .getString(SearchKey.buyerParty.name());

    return Stream.of(
        Arguments.of(ComparisonOperator.EQ, entityPath.eq(TEST_VALUE)),
        Arguments.of(ComparisonOperator.LIKE, entityPath.like("%" + TEST_VALUE + "%")),
        Arguments.of(ComparisonOperator.IN, entityPath.in(TEST_VALUE))
    );
  }

  @BeforeEach
  void setUp() {
    stringSinglePredicateBuilder = new StringSinglePredicateBuilder();
    entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent");
  }

  @ParameterizedTest
  @MethodSource("buildPredicateTestParams")
  void buildPredicate_shouldBuildPredicateForBigDecimalValues_GivenValidComparisonOperator(
      ComparisonOperator comparisonOperator, Predicate expected) {
    //Given:
    SingleSearchCriteria singleSearchCriteria = SingleSearchCriteria.builder()
        .key(SearchKey.buyerParty)
        .cop(comparisonOperator)
        .value(TEST_VALUE)
        .build();

    //When:
    Predicate actual =
        stringSinglePredicateBuilder.buildPredicate(entityPath, singleSearchCriteria);

    //Then:
    assertEquals(expected, actual);
  }

  @Test
  void buildPredicate_shouldThrowException_GivenNotSupportedComparisonOperator() {
    //Given:
    SingleSearchCriteria singleSearchCriteria = SingleSearchCriteria.builder()
        .key(SearchKey.buyerParty)
        .cop(ComparisonOperator.GTE)
        .value(TEST_VALUE)
        .build();

    //When & Then:
    assertThrows(IllegalArgumentException.class,
        () -> stringSinglePredicateBuilder.buildPredicate(entityPath, singleSearchCriteria));
  }

  @Test
  void isApplicableTo_shouldReturnTrue_GivenStringSearchKeys() {
    assertTrue(stringSinglePredicateBuilder.isApplicableTo(SearchKey.buyerParty));
  }

  @Test
  void isApplicableTo_shouldReturnFalse_GivenOtherSearchKeys() {
    assertFalse(stringSinglePredicateBuilder.isApplicableTo(SearchKey.premiumAmount));
  }
}