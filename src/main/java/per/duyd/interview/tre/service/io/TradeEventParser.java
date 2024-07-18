package per.duyd.interview.tre.service.io;

import per.duyd.interview.tre.entity.TradeEvent;

public interface TradeEventParser {
  TradeEvent parseFromFile(String filePath);
}
