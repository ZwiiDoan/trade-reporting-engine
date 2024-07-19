package per.duyd.interview.tre.controller;

public enum MdcKeys {
  CORRELATION_ID("correlationId");

  private final String value;

  MdcKeys(String value) {
    this.value = value;
  }

  public String value() {
    return this.value;
  }
}
