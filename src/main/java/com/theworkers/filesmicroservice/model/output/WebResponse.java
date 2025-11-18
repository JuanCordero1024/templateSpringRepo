package com.theworkers.templatemicroservice.model.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        description = "The response can include a body of our models. Message & the status of the request"
)
public class WebResponse<T> {

    @Schema
    private String message;

    @Schema
    private HttpStatus codeStatus;

    @Schema
    T entity;
}
