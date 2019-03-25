package com.numbDev.ElasticDemo.Controller;

import com.numbDev.ElasticDemo.Base.User;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Controller
public class UserController {

//    private final RestHighLevelClient client;
//
//    public UserController(RestHighLevelClient client) {
//        this.client = client;
//    }
//
//    @PostMapping("/create")
//    public String create(@RequestBody User user) throws IOException {
//        IndexResponse response = client.prepareIndex("users", "employee", String.valueOf(user.getId()))
//            .setSource(jsonBuilder()
//                .startObject()
//                .field("title", user.getTitle())
//                .field("content", user.getContent())
//                .endObject()).get();
//        System.out.println("response id:"+response.getId());
//        return response.toString();
//    }
//
//    @GetMapping("/view/{id}")
//    public Map<String, Object> view(@PathVariable final String id) {
//        GetResponse getResponse = client.prepareGet("users", "employee", id).get();
//        return getResponse.getSource();
//    }
}
