package org.example.mapper;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct Mapper for User Entity ↔ UserDTO
 * 
 * This is like AutoMapper in C# - it automatically generates mapping code at compile time
 */
@Mapper(
    componentModel = "spring",  // Generate Spring @Component
    uses = {BagMapper.class},   // Use BagMapper for nested mapping
    unmappedTargetPolicy = ReportingPolicy.IGNORE  // Ignore unmapped fields
)
public interface UserMapper {
    
    /**
     * Convert User entity to UserDTO
     * MapStruct automatically maps fields with same names
     */
    @Mapping(target = "bagCount", expression = "java(user.getBags() != null ? user.getBags().size() : 0)")
    UserDTO toDTO(User user);
    
    /**
     * Convert UserDTO to User entity
     * Ignore bags when creating entity (they should be managed separately)
     */
    @Mapping(target = "bags", ignore = true)
    User toEntity(UserDTO userDTO);
    
    /**
     * Convert list of Users to list of UserDTOs
     */
    List<UserDTO> toDTOList(List<User> users);
    
    /**
     * Convert list of UserDTOs to list of Users
     */
    List<User> toEntityList(List<UserDTO> userDTOs);
    
    /**
     * Update existing User entity from UserDTO
     * Useful for PATCH operations
     */
    @Mapping(target = "id", ignore = true)  // Don't update ID
    @Mapping(target = "createdAt", ignore = true)  // Don't update creation time
    @Mapping(target = "bags", ignore = true)  // Don't update bags through this mapping
    void updateUserFromDTO(UserDTO userDTO, @MappingTarget User user);
}
