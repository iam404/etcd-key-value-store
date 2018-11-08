package com.iam404.restkeyvaluevault.responses;
import org.springframework.http.HttpStatus;


public class HttpResponse extends Response {

    public HttpResponse(String message, String json, HttpStatus status)
    {
        this.setData(json);
        this.setMessage(message);
        this.setStatus(status.toString());
    }
}
