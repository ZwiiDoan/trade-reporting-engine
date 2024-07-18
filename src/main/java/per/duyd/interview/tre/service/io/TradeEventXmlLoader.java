package per.duyd.interview.tre.service.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import per.duyd.interview.tre.entity.TradeEvent;
import per.duyd.interview.tre.exception.InvalidInputFileException;
import per.duyd.interview.tre.repository.TradeEventRepository;

@Component
@RequiredArgsConstructor
public class TradeEventXmlLoader implements TradeEventLoader {

  private final TradeEventParser tradeEventXmlParser;
  private final TradeEventRepository tradeEventRepository;

  private static final String XML_SUFFIX = "xml";

  @Override
  public void loadFromLocalFolder(String folderPath) {
    try (Stream<Path> files = Files.list(Path.of(folderPath))) {
      List<TradeEvent> tradeEvents =
          files.filter(file -> file.getFileName().toString().endsWith(XML_SUFFIX))
              .map(xmlFile -> tradeEventXmlParser.parseFromFile(xmlFile.toFile().getAbsolutePath()))
              .collect(Collectors.toList());

      tradeEventRepository.saveAll(tradeEvents);
    } catch (Exception e) {
      throw new InvalidInputFileException("Failed to load trade events from local folder", e);
    }
  }
}
