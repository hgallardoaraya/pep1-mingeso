package com.mingeso.topeducation.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    Integer status;
    String message;
    String pathRedirect;
    Object data;

    public Response(Integer status, String message, String urlRedirect){
        this.status = status;
        this.message = message;
        this.pathRedirect = urlRedirect;
    }

    public Response(Integer status, String message, Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Response(Integer status, String message){
        this.status = status;
        this.message = message;
    }

}
