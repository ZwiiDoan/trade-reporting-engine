package per.duyd.interview.tre.service.filter;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import per.duyd.interview.tre.entity.TradeEvent;

@Component
public class AnagramsPartyFilter implements SearchResultFilter {

  @Override
  public boolean test(TradeEvent tradeEvent) {
    String buyerParty = tradeEvent.getBuyerParty().toLowerCase();
    String sellerParty = tradeEvent.getSellerParty().toLowerCase();

    if (buyerParty.length() != sellerParty.length()) {
      return false;
    }

    Map<Character, Integer> charCounts = new HashMap<>();
    for (int i = 0; i < buyerParty.length(); i++) {
      char c1 = buyerParty.charAt(i);
      char c2 = sellerParty.charAt(i);
      charCounts.put(c1, charCounts.getOrDefault(c1, 0) + 1);
      charCounts.put(c2, charCounts.getOrDefault(c2, 0) - 1);
    }

    for (int count : charCounts.values()) {
      if (count != 0) {
        return false;
      }
    }

    return true;
  }
}
