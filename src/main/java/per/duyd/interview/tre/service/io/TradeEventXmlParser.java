package per.duyd.interview.tre.service.io;

import java.math.BigDecimal;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import per.duyd.interview.tre.entity.TradeEvent;
import per.duyd.interview.tre.exception.InvalidInputFileException;

@Component
@RequiredArgsConstructor
public class TradeEventXmlParser implements TradeEventParser {

  private final XPath xPath;

  @Override
  public TradeEvent parseFromFile(String filePath) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      Document doc = dbf.newDocumentBuilder().parse(filePath);

      String buyerParty =
          (String) xPath.evaluate("//buyerPartyReference/@href", doc, XPathConstants.STRING);
      String sellerParty =
          (String) xPath.evaluate("//sellerPartyReference/@href", doc, XPathConstants.STRING);
      BigDecimal premiumAmount = new BigDecimal(String.valueOf(
          xPath.evaluate("//paymentAmount/amount/text()", doc, XPathConstants.STRING)));
      String premiumCurrency = (String) xPath.evaluate("//paymentAmount/currency/text()", doc,
          XPathConstants.STRING);

      return TradeEvent.builder()
          .buyerParty(buyerParty)
          .sellerParty(sellerParty)
          .premiumAmount(premiumAmount)
          .premiumCurrency(premiumCurrency)
          .build();
    } catch (Exception e) {
      throw new InvalidInputFileException("Failed to parse TradeEvent from XML file", e);
    }
  }
}
