package com.web.converters;

public interface Converter<Entity, DTO> {
    DTO toDTO(Entity entity);
    Entity toEntity(DTO dto);
}
