package com.cromxt.common.crombucket.dtos;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends BaseResponse {
        private String message;
        public ErrorResponse(String message, CromxtResponseStatus status) {
                super(status);
                this.message = message;
        }
}
