//package com.numbDev.ElasticDemo.Base;
//
//import org.springframework.data.elasticsearch.annotations.Document;
//
//import javax.persistence.Id;
//import java.util.Objects;
//import java.util.UUID;
//
//@Document(indexName = "my_index", type = "user")
//public class UserDocument implements IUser {
//
//    private int id;
//    private String title;
//    private String content;
//
//    public UserDocument() {
//
//    }
//
//    public UserDocument(int id, String title, String content) {
//        this.id = id;
//        this.title = title;
//        this.content = content;
//    }
//
//    @Override
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    @Override
//    public int getId() {
//        return id;
//    }
//
//    @Override
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    @Override
//    public String getTitle() {
//        return title;
//    }
//
//    @Override
//    public void getContent(String content) {
//        this.content = content;
//    }
//
//    @Override
//    public String setContent() {
//        return content;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof User)) {
//            return false;
//        }
//
//        final User obj = (User) o;
//        return obj.get;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 31 * hash + Objects.hashCode(this.uuid);
//        return hash;
//    }
//}
