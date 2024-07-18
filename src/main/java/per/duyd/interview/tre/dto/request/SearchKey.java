package per.duyd.interview.tre.dto.request;

import java.util.EnumSet;

public enum SearchKey {
  buyerParty, sellerParty, premiumAmount, premiumCurrency, Unknown;

  private static final EnumSet<SearchKey> stringKeys = EnumSet.of(buyerParty, sellerParty, premiumCurrency);
  private static final EnumSet<SearchKey> bigDecimalKeys = EnumSet.of(premiumAmount);

  public static EnumSet<SearchKey> getStringKeys() {
    return stringKeys.clone();
  }

  public static EnumSet<SearchKey> getBigDecimalKeys() {
    return bigDecimalKeys.clone();
  }

}
