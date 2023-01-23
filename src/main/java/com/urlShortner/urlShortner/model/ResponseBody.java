package com.urlShortner.urlShortner.model;

import lombok.Data;

@Data
public class ResponseBody {
    private String status;
    private String error;
    private Url url;
}
