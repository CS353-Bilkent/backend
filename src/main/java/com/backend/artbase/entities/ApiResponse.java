package com.backend.artbase.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {

    @JsonProperty("result")
    @Builder.Default
    private OperationResult operationResult = new OperationResult("0", "success", null);

    @JsonProperty("data")
    private T operationResultData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationResult {

        @JsonProperty("code")
        private String returnCode;

        @JsonProperty("message")
        private String returnMessage;

        @JsonProperty("errors")
        private List<String> returnErrors;
    }
}