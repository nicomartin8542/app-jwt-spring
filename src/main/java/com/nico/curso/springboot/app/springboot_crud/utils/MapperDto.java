package com.nico.curso.springboot.app.springboot_crud.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperDto {

    @Autowired
    private ModelMapper modelMapper;

    public <D, T> D convertToDto(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public <D, T> T convertToEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public <D, T> List<D> convertToDtoList(List<T> entities, Class<D> dtoClass) {
        return entities.stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

    public <D, T> List<T> convertToEntityList(List<D> dtos, Class<T> entityClass) {
        return dtos.stream()
                .map(dto -> modelMapper.map(dto, entityClass))
                .collect(Collectors.toList());
    }

}
