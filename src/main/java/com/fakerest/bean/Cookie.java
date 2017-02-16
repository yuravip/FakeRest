package com.fakerest.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;

@Builder
@Getter
public class Cookie {
    private String path;
    private String name;
    private String value;
    private int maxAge = -1;
    private boolean secure;

    @Tolerate
    public Cookie() {}
}
