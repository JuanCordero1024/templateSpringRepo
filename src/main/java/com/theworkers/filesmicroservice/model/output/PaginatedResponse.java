package com.theworkers.filesmicroservice.model.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        description = "The response can include a body of our models. Message & the status of the request Paginated"
)
public class PaginatedResponse <T>{
    @Schema
    private List<T> data;
    @Schema
    private int currentPage;
    @Schema
    private int pageSize;
    @Schema
    private long totalElements;
    @Schema
    private int totalPages;
    @Schema
    private boolean isLast;
}
