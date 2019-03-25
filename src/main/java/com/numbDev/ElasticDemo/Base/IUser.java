package com.numbDev.ElasticDemo.Base;

import java.util.UUID;

public interface IUser {
    void setId(int id);
    Integer getId();
    void setTitle(String title);
    String getTitle();
    void setContent(String content);
    String getContent();
}
