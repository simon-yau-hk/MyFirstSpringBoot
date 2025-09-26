package org.example.mapper;

import org.example.dto.BagDTO;
import org.example.entity.Bag;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct Mapper for Bag Entity ↔ BagDTO
 */
@Mapper(
    componentModel = "spring",
    uses = {BagItemMapper.class},
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BagMapper {
    
    /**
     * Convert Bag entity to BagDTO
     */
    @Mapping(target = "itemCount", expression = "java(bag.getItems() != null ? bag.getItems().size() : 0)")
    @Mapping(target = "totalPrice", expression = "java(bag.getTotalPrice())")
    BagDTO toDTO(Bag bag);
    
    /**
     * Convert BagDTO to Bag entity
     */
    @Mapping(target = "user", ignore = true)  // User relationship managed separately
    @Mapping(target = "items", ignore = true)  // Items managed separately
    Bag toEntity(BagDTO bagDTO);
    
    /**
     * Convert list of Bags to list of BagDTOs
     */
    List<BagDTO> toDTOList(List<Bag> bags);
    
    /**
     * Convert list of BagDTOs to list of Bags
     */
    List<Bag> toEntityList(List<BagDTO> bagDTOs);
    
    /**
     * Update existing Bag entity from BagDTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "items", ignore = true)
    void updateBagFromDTO(BagDTO bagDTO, @MappingTarget Bag bag);
}
