package per.duyd.interview.tre.service.filter;

import java.util.function.Predicate;
import per.duyd.interview.tre.entity.TradeEvent;

public interface SearchResultFilter extends Predicate<TradeEvent> {
}
