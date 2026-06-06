package com.sourabh.common.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SourceDto {

    private Long documentId;
    private String fileName;
    private Integer chunkIndex;
    private Double score;
}
