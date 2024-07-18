package per.duyd.interview.tre.service.mapper;

import org.mapstruct.Mapper;
import per.duyd.interview.tre.dto.response.TradeEventDto;
import per.duyd.interview.tre.entity.TradeEvent;

@Mapper(componentModel = "spring")
public interface TradeEventMapper {
  TradeEventDto toDto(TradeEvent tradeEvent);
}
