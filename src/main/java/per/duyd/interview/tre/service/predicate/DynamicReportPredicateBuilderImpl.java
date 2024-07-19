package per.duyd.interview.tre.service.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import per.duyd.interview.tre.dto.request.CombinedSearchCriteria;
import per.duyd.interview.tre.dto.request.SearchCriteria;
import per.duyd.interview.tre.dto.request.SingleSearchCriteria;
import per.duyd.interview.tre.entity.TradeEvent;
import per.duyd.interview.tre.exception.InvalidSearchCriteriaException;

@Component
@RequiredArgsConstructor
public class DynamicReportPredicateBuilderImpl implements DynamicReportPredicateBuilder {

  private final List<SinglePredicateBuilder> singlePredicateBuilders;

  @Override
  public BooleanBuilder buildReportPredicate(SearchCriteria criteria) {
    try {
      PathBuilder<TradeEvent> entityPath = new PathBuilder<>(TradeEvent.class, "tradeEvent");
      return buildPredicate(entityPath, criteria);
    } catch (Exception ex) {
      throw new InvalidSearchCriteriaException("Failed to build search predicate", ex);
    }
  }

  private BooleanBuilder buildPredicate(PathBuilder<TradeEvent> entityPath,
                                        SearchCriteria criteria) {
    BooleanBuilder rootPredicate = new BooleanBuilder();

    if (criteria instanceof CombinedSearchCriteria combinedSearchCriteria) {
      BooleanBuilder combinedPredicate =
          buildCombinedPredicate(entityPath, combinedSearchCriteria);
      rootPredicate.and(combinedPredicate);
    } else if (criteria instanceof SingleSearchCriteria singleSearchCriteria) {
      Predicate singlePredicate = buildSinglePredicate(entityPath, singleSearchCriteria);
      rootPredicate.and(singlePredicate);
    } else {
      throw new IllegalArgumentException(
          "Unsupported search criteria type: " + criteria.getClass());
    }

    return rootPredicate;
  }

  private BooleanBuilder buildCombinedPredicate(PathBuilder<TradeEvent> entityPath,
                                                CombinedSearchCriteria combinedSearchCriteria) {
    BooleanBuilder combinedPredicate = new BooleanBuilder();
    List<BooleanBuilder> subPredicates = combinedSearchCriteria.getCriteria().stream()
        .map(subCriteria -> buildPredicate(entityPath, subCriteria))
        .toList();

    switch (combinedSearchCriteria.getLop()) {
      case AND -> subPredicates.forEach(combinedPredicate::and);
      case OR -> subPredicates.forEach(combinedPredicate::or);
      case NOT -> {
        if (subPredicates.size() > 1) {
          throw new IllegalArgumentException("Invalid 'NOT' criteria");
        } else {
          combinedPredicate.andNot(subPredicates.get(0));
        }
      }
      default -> throw new IllegalArgumentException(
          "Unsupported logical operator: " + combinedSearchCriteria.getLop());
    }

    return combinedPredicate;
  }

  private Predicate buildSinglePredicate(PathBuilder<TradeEvent> entityPath,
                                         SingleSearchCriteria criteria) {
    return singlePredicateBuilders.stream()
        .filter(singlePredicateBuilder -> singlePredicateBuilder.isApplicableTo(criteria.getKey()))
        .findFirst()
        .map(singlePredicateBuilder -> singlePredicateBuilder.buildPredicate(entityPath, criteria))
        .orElseThrow(
            () -> new IllegalArgumentException("Unsupported search key: " + criteria.getKey()));
  }
}
