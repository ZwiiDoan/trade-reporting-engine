package per.duyd.interview.tre.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import per.duyd.interview.tre.entity.TradeEvent;

public interface TradeEventRepository
    extends JpaRepository<TradeEvent, String>, QuerydslPredicateExecutor<TradeEvent> {
}
