package org.example.mapper;

import org.example.dto.BagItemDTO;
import org.example.entity.BagItem;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct Mapper for BagItem Entity ↔ BagItemDTO
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BagItemMapper {
    
    /**
     * Convert BagItem entity to BagItemDTO
     */
    @Mapping(target = "totalPrice", expression = "java(bagItem.getTotalPrice())")
    BagItemDTO toDTO(BagItem bagItem);
    
    /**
     * Convert BagItemDTO to BagItem entity
     */
    @Mapping(target = "bag", ignore = true)  // Bag relationship managed separately
    BagItem toEntity(BagItemDTO bagItemDTO);
    
    /**
     * Convert list of BagItems to list of BagItemDTOs
     */
    List<BagItemDTO> toDTOList(List<BagItem> bagItems);
    
    /**
     * Convert list of BagItemDTOs to list of BagItems
     */
    List<BagItem> toEntityList(List<BagItemDTO> bagItemDTOs);
    
    /**
     * Update existing BagItem entity from BagItemDTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bag", ignore = true)
    void updateBagItemFromDTO(BagItemDTO bagItemDTO, @MappingTarget BagItem bagItem);
}
