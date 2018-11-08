package com.iam404.restkeyvaluevault.responses;

public class Response
{

    private String status;
    private String message;

    private String data;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(final String status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(final String message)
    {
        this.message = message;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(final String data)
    {
        this.data = data;
    }
}
