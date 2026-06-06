package com.sourabh.document.mapper;

import com.sourabh.document.dto.DocumentResponse;
import com.sourabh.document.entity.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentResponse toResponse(Document document);
}