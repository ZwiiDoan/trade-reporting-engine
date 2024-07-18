package per.duyd.interview.tre.service.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import java.math.BigDecimal;
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

class DecimalSinglePredicateBuilderTest {

  private SinglePredicateBuilder<TradeEvent> decimalSinglePredicateBuilder;
  private PathBuilder<TradeEvent> entityPath;
  private static final BigDecimal TEST_VALUE = new BigDecimal(100);

  public static Stream<Arguments> buildPredicateTestParams() {
    NumberPath<BigDecimal> entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent")
        .getNumber(SearchKey.premiumCurrency.name(), BigDecimal.class);

    return Stream.of(
        Arguments.of(ComparisonOperator.EQ, entityPath.eq(TEST_VALUE)),
        Arguments.of(ComparisonOperator.GT, entityPath.gt(TEST_VALUE)),
        Arguments.of(ComparisonOperator.GTE, entityPath.goe(TEST_VALUE)),
        Arguments.of(ComparisonOperator.LT, entityPath.lt(TEST_VALUE)),
        Arguments.of(ComparisonOperator.LTE, entityPath.loe(TEST_VALUE)),
        Arguments.of(ComparisonOperator.IN, entityPath.in(TEST_VALUE))
    );
  }

  @BeforeEach
  void setUp() {
    decimalSinglePredicateBuilder = new DecimalSinglePredicateBuilder();
    entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent");
  }

  @ParameterizedTest
  @MethodSource("buildPredicateTestParams")
  void buildPredicate_shouldBuildPredicateForBigDecimalValues_GivenValidComparisonOperator(
      ComparisonOperator comparisonOperator, Predicate expected) {
    //Given:
    SingleSearchCriteria singleSearchCriteria = SingleSearchCriteria.builder()
        .key(SearchKey.premiumCurrency)
        .cop(comparisonOperator)
        .value(String.valueOf(TEST_VALUE))
        .build();

    //When:
    Predicate actual =
        decimalSinglePredicateBuilder.buildPredicate(entityPath, singleSearchCriteria);

    //Then:
    assertEquals(expected, actual);
  }

  @Test
  void buildPredicate_shouldThrowException_GivenNotSupportedComparisonOperator() {
    //Given:
    SingleSearchCriteria singleSearchCriteria = SingleSearchCriteria.builder()
        .key(SearchKey.premiumCurrency)
        .cop(ComparisonOperator.LIKE)
        .value("100")
        .build();

    //When & Then:
    assertThrows(IllegalArgumentException.class,
        () -> decimalSinglePredicateBuilder.buildPredicate(entityPath, singleSearchCriteria));
  }

  @Test
  void isApplicableTo_shouldReturnTrue_GivenBigDecimalSearchKeys() {
    assertTrue(decimalSinglePredicateBuilder.isApplicableTo(SearchKey.premiumAmount));
  }

  @Test
  void isApplicableTo_shouldReturnFalse_GivenOtherSearchKeys() {
    assertFalse(decimalSinglePredicateBuilder.isApplicableTo(SearchKey.buyerParty));
  }
}