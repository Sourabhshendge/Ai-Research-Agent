package com.sourabh.document.mapper;

import com.sourabh.document.dto.DocumentResponse;
import com.sourabh.document.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    @Mapping(target = "status", source = "status")
    DocumentResponse toResponse(Document document);
}