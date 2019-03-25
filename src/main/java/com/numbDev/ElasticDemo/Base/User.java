package com.numbDev.ElasticDemo.Base;

//import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Users")
//@Document(indexName = "my_index", type = "user")
public class User {

    @Id
    private long id;
    private String title;
    private String content;

    public User() {

    }

    public User(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        final User obj = (User) o;
        return obj.getId().equals(this.id) && obj.getTitle().equals(this.title) && obj.getContent().equals
            (this.content);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.title);
        hash = 31 * hash + Objects.hashCode(this.content);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Id: ").append(this.id).append("\n")
            .append("Title: ").append(this.title).append("\n")
            .append("Content: ").append(this.content).append("\n");
        return builder.toString();
    }
}
