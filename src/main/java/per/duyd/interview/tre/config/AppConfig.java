package per.duyd.interview.tre.config;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  @Bean
  public XPath xpath() {
    return XPathFactory.newInstance().newXPath();
  }
}
