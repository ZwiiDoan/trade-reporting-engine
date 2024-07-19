package per.duyd.interview.tre.service.predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import per.duyd.interview.tre.dto.request.CombinedSearchCriteria;
import per.duyd.interview.tre.dto.request.ComparisonOperator;
import per.duyd.interview.tre.dto.request.LogicalOperator;
import per.duyd.interview.tre.dto.request.SearchCriteria;
import per.duyd.interview.tre.dto.request.SearchKey;
import per.duyd.interview.tre.dto.request.SingleSearchCriteria;
import per.duyd.interview.tre.entity.TradeEvent;
import per.duyd.interview.tre.exception.InvalidSearchCriteriaException;

class DynamicReportPredicateBuilderImplTest {

  private DynamicReportPredicateBuilder dynamicReportPredicateBuilder;

  public static Stream<Arguments> shouldThrowExceptionTestParams() {
    return Stream.of(
        Arguments.of("Invalid 'NOT' criteria", CombinedSearchCriteria.builder()
            .lop(LogicalOperator.NOT)
            .criteria(List.of(
                SingleSearchCriteria.builder()
                    .key(SearchKey.buyerParty)
                    .cop(ComparisonOperator.EQ)
                    .value("EMU_BANK")
                    .build(),
                SingleSearchCriteria.builder()
                    .key(SearchKey.buyerParty)
                    .cop(ComparisonOperator.EQ)
                    .value("LEFT_BANK")
                    .build()
            )).build()
        ),
        Arguments.of("Not supported logical operator", CombinedSearchCriteria.builder()
            .lop(LogicalOperator.UNKNOWN)
            .criteria(List.of(
                SingleSearchCriteria.builder()
                    .key(SearchKey.buyerParty)
                    .cop(ComparisonOperator.EQ)
                    .value("EMU_BANK")
                    .build(),
                SingleSearchCriteria.builder()
                    .key(SearchKey.buyerParty)
                    .cop(ComparisonOperator.EQ)
                    .value("LEFT_BANK")
                    .build()
            )).build()),
        Arguments.of("Not supported Search Key", SingleSearchCriteria.builder()
            .key(SearchKey.Unknown)
            .cop(ComparisonOperator.EQ)
            .value("LEFT_BANK")
            .build()),
        Arguments.of("Not supported SearchCriteriaType", mock(SearchCriteria.class))
    );
  }

  @BeforeEach
  void setUp() {
    List<SinglePredicateBuilder> singlePredicateBuilders =
        List.of(new DecimalSinglePredicateBuilder(), new StringSinglePredicateBuilder());
    dynamicReportPredicateBuilder = new DynamicReportPredicateBuilderImpl(singlePredicateBuilders);
  }

  @Test
  void buildReportPredicate_shouldBuildExpectedPredicate_givenValidSearchCriteria() {
    //Given:
    SearchCriteria searchCriteria = CombinedSearchCriteria.builder()
        .lop(LogicalOperator.OR)
        .criteria(List.of(
            CombinedSearchCriteria.builder()
                .lop(LogicalOperator.AND)
                .criteria(List.of(SingleSearchCriteria.builder().key(SearchKey.buyerParty)
                        .cop(ComparisonOperator.EQ).value("LEFT_BANK").build(),
                    SingleSearchCriteria.builder().key(SearchKey.premiumAmount)
                        .cop(ComparisonOperator.LT).value("200").build()
                ))
                .build(),
            CombinedSearchCriteria.builder()
                .lop(LogicalOperator.AND)
                .criteria(List.of(SingleSearchCriteria.builder().key(SearchKey.buyerParty)
                        .cop(ComparisonOperator.EQ).value("EMU_BANK").build(),
                    SingleSearchCriteria.builder().key(SearchKey.sellerParty)
                        .cop(ComparisonOperator.EQ).value("BISON_BANK").build(),
                    CombinedSearchCriteria.builder()
                        .lop(LogicalOperator.NOT)
                        .criteria(List.of(
                            SingleSearchCriteria.builder().key(SearchKey.premiumCurrency)
                                .cop(ComparisonOperator.IN).value("AUD,HKD").build()
                        ))
                        .build()
                ))
                .build(),
            CombinedSearchCriteria.builder()
                .lop(LogicalOperator.AND)
                .criteria(List.of(SingleSearchCriteria.builder().key(SearchKey.sellerParty)
                        .cop(ComparisonOperator.LIKE).value("MU_B").build(),
                    CombinedSearchCriteria.builder()
                        .lop(LogicalOperator.NOT)
                        .criteria(List.of(
                            SingleSearchCriteria.builder().key(SearchKey.premiumAmount)
                                .cop(ComparisonOperator.GTE).value("100.0001").build()
                        ))
                        .build(),
                    CombinedSearchCriteria.builder()
                        .lop(LogicalOperator.NOT)
                        .criteria(List.of(
                            SingleSearchCriteria.builder().key(SearchKey.buyerParty)
                                .cop(ComparisonOperator.LIKE).value("EFT_B").build()
                        ))
                        .build()
                ))
                .build()
        ))
        .build();

    PathBuilder<TradeEvent> entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent");
    BooleanBuilder expected = new BooleanBuilder();
    expected.or(
        entityPath.getString("buyerParty").eq("LEFT_BANK")
            .and(entityPath.getString("premiumAmount").lt("200"))
    ).or(
        entityPath.getString("buyerParty").eq("EMU_BANK")
            .and(entityPath.getString("sellerParty").eq("BISON_BANK"))
            .and(entityPath.getString("premiumCurrency").in("AUD", "HKD").not())
    ).or(
        entityPath.getString("sellerParty").like("%MU_B%")
            .and(entityPath.getNumber("premiumAmount", BigDecimal.class)
                .goe(new BigDecimal("100.0001")).not())
            .and(entityPath.getString("buyerParty").like("%EFT_B%").not())
    );

    //When:
    BooleanBuilder actual = dynamicReportPredicateBuilder.buildReportPredicate(searchCriteria);

    //Then:
    assertNotNull(expected.getValue());
    assertNotNull(actual.getValue());
    assertEquals(expected.getValue().toString(), actual.getValue().toString());
  }

  @Test
  void buildReportPredicate_shouldBuildSingleSearchCriteria() {
    //Given:
    SearchCriteria searchCriteria = SingleSearchCriteria.builder()
        .key(SearchKey.buyerParty)
        .cop(ComparisonOperator.EQ)
        .value("LEFT_BANK")
        .build();

    PathBuilder<TradeEvent> entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent");
    BooleanBuilder expected = new BooleanBuilder();
    expected.and(
        entityPath.getString("buyerParty").eq("LEFT_BANK")
    );

    //When:
    BooleanBuilder actual = dynamicReportPredicateBuilder.buildReportPredicate(searchCriteria);

    //Then:
    assertNotNull(expected.getValue());
    assertNotNull(actual.getValue());
    assertEquals(expected.getValue().toString(), actual.getValue().toString());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("shouldThrowExceptionTestParams")
  void buildReportPredicate_shouldThrowException_whenFailedToBuildPredicate(
      String description, SearchCriteria searchCriteria
  ) {
    //When & Then
    assertThrows(InvalidSearchCriteriaException.class,
        () -> dynamicReportPredicateBuilder.buildReportPredicate(searchCriteria));
  }
}