package com.numbDev.ElasticDemo.Base.DAO;

import com.numbDev.ElasticDemo.Base.User;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.ingest.PutPipelineRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//@Repository
@Service
public class UserDAO  {

    @Value("${elasticsearch.index.name}")
    private String indexName;

    @Value("${elasticsearch.user.type}")
    private String userTypeName;

    private final RestHighLevelClient client;

    private static int count = 1;
    private final String index = "my_index"; //только lowercase
    private String file = "";

    private static final int buffSize = 1170966550;
    private static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
       // builder.addHeader("Authorization", "Bearer " + TOKEN);
        builder.setHttpAsyncResponseConsumerFactory(
            new HttpAsyncResponseConsumerFactory
                .HeapBufferedResponseConsumerFactory(buffSize));
        COMMON_OPTIONS = builder.build();
    }

    @PostConstruct
    private void readTestPDFFile() {
        String filePath = "D:\\";
        String originalFileName = "kolbasa_large.pdf";

        byte[] input_file = new byte[0];
        try {
            input_file = Files.readAllBytes(Paths.get(filePath+originalFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] encodedBytes = Base64.getEncoder().encode(input_file);
        file = new String(encodedBytes);
    }

    public UserDAO(RestHighLevelClient client) {
        this.client = client;
    }

    public void beginSettin() {
        //СОздание пайплайна с движком для аттачей
                String source =
                    "{"
                        + "\"processors\" : ["
                        +   "{"
                        +     "\"attachment\" : {"
                        +       "\"field\" : \"data\"" //,
                     //   +       "\"indexed_chars\" : \"-1\""
                     //   +        "\"properties\" : [ \"content\", \"title\"]"
                        +     "}"
                        +   "}"
                        + "]"
                        +"}";
                PutPipelineRequest request = new PutPipelineRequest(
                    "attachment",
                    new BytesArray(source.getBytes(StandardCharsets.UTF_8)),
                    XContentType.JSON
                );
                try {
                    client.ingest().putPipeline(request, COMMON_OPTIONS);
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }

    public void newPiplene() {
//        String source =  "\"settings\": { "
//             + "\"analysis\": {"
//             +   "\"analyzer\": { "
//             +       "\"my_analyzer\": {"
//             +           "\"tokenizer\": \"my_tokenizer\" "
//             +       "}"
//             +  "},"
//             +   "\"tokenizer\": { "
//             +       "\"my_tokenizer\": { "
//             +           "\"type\": \"standard\", "
//             +              "\"max_token_length\": 3"
//             +      " }"
//             +  "}"
//            +"}"
//        +"}";
        beginSettin();
        String source =  "settings: { "
            + "analysis: {"
            +   "analyzer: { "
            +       "my_analyzer: {"
            +           "tokenizer: my_tokenizer "
            +       "}"
            +  "},"
            +   "tokenizer: { "
            +       "my_tokenizer: { "
            +           "type: standard, "
            +              "max_token_length: 3"
            +      " }"
            +  "}"
            +"}"
            +"}";
//        Settings settings = Settings.builder()
        //            .put("analysis.analyzer.my_analyzer.tokenizer", "my_tokenizer")
        //            .put("tokenizer.my_tokenizer.type", "standard")
        //            .put("tokenizer.my_tokenizer.max_token_length", "3")
        //            .build();

        Settings settings = Settings.builder()
            .put("analysis.analyzer.customNgram.type", "custom")
            .put("analysis.analyzer.customNgram.tokenizer", "icu_tokenizer")
            .putList("analysis.analyzer.customNgram.filter", "customNgram")

            .put("analysis.filter.customNgram.type", "edgeNGram")
            .put("analysis.filter.customNgram.min_gram", "3")
            .put("analysis.filter.customNgram.max_gram", "18")
            .put("analysis.filter.customNgram.side", "front")
            .put("index.number_of_shards", "6")
           // .put("settings.index.number_of_replicas", "1")

            .build();
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.settings(settings);
        try {
            client.indices().create(request, RequestOptions.DEFAULT);
            System.out.println("created index");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //        UpdateSettingsRequest request = new UpdateSettingsRequest("posts10");
//        request.ma
//        request.fromXContent();
    }

    public User addUser(User user) {
        IndexRequest indexRequest = new IndexRequest(index, "doc99", Integer.toString(count))
            .setPipeline("attachment")
            .source(
                "id", user.getId(),
                "title", user.getTitle(),
                "content", user.getContent(),
                "data", file);

        try {
            System.out.println("try");
            IndexResponse response = client.index(indexRequest, COMMON_OPTIONS);
            System.out.println(count + ": done!");
            count++;
        } catch (ElasticsearchException | IOException e) {
            System.out.println("problem");
            e.printStackTrace();
        }
        return user;
    }

    public void search(String title) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        WildcardQueryBuilder mmQuery = QueryBuilders.wildcardQuery("content", "*" + title + "*");
        QueryBuilder query = QueryBuilders.queryStringQuery(title);
//        MatchQueryBuilder mmQuery = QueryBuilders.matchQuery("attachment.content", title)
//            .fuzziness(Fuzziness.AUTO); // works!!!!!!!!
//        MatchQueryBuilder mmQuery2 = QueryBuilders.matchQuery("title", title)
//            .fuzziness(Fuzziness.AUTO);
//        MatchQueryBuilder mmQuery3 = QueryBuilders.matchQuery("content", title)
//            .fuzziness(Fuzziness.AUTO);
//        BoolQueryBuilder qb = QueryBuilders.boolQuery().must(mmQuery).must(mmQuery2).must(mmQuery3);

     //   mmQuery.analyzer("russian");
//        mmQuery.type(MultiMatchQueryBuilder.DEFAULT_TYPE);
        searchSourceBuilder.query(query).fetchSource("title", "attachment");
        searchRequest.source(searchSourceBuilder);


//        AnalyzeRequest request = new AnalyzeRequest();
//        request.text(title).analyzer("russian");
////        AnalyzeRequest request = analyzeRequestBuilder.setField("content").setField("title").setText(title).request();
//        try {
//            AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);
//            for (AnalyzeResponse.AnalyzeToken token : response) {
//                System.out.println(token.getTerm() + " ======= " + token.getType());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {

            SearchResponse searchResponse = client.search(searchRequest, COMMON_OPTIONS);
//            Response searchResponse = client.getLowLevelClient().performRequest("GET", index + "/_search",
//                new HashMap<>(),
//                new NStringEntity(searchRequest.toString(), ContentType.APPLICATION_JSON),
//                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(1024 * 1024 * 1024));
//            System.out.println("done search!");
//            String result = IOUtils.toString(searchResponse.getEntity().getContent(), StandardCharsets.UTF_8);
//            System.out.println(result);
            for (SearchHit item : searchResponse.getHits().getHits()) {
                System.out.println(searchResponse.getSuccessfulShards());
                System.out.println(item.getSourceAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dropCount() {
        count = 0;
    }
//    public List<User> findUsers(String prof) {
//        SearchQuery query = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery("profession",
//            prof)).build();
//        return esTemplate.queryForList(query, User.class);
//    }

//    public void addIndex() throws IOException {
//        IndexResponse response = client.prepareIndex("twitter", "_doc", "1")
//            .setSource(jsonBuilder()
//                .startObject()
//                .field("user", "kimchy")
//                .field("postDate", new Date())
//                .field("message", "trying out Elasticsearch")
//                .endObject()
//            )
//            .get();
//    }
//
//    public void search() {
//        SearchResponse response = client.prepareSearch("index1", "index2")
//            .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//            .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
//            .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
//            .setFrom(0).setSize(60).setExplain(true)
//            .get();
//    }





//    private IUser transformUser(IUser user) {
//        IUser transUser = null;
//        if (user instanceof User) {
//            transUser = new UserDocument();
//        }
//
//        if (user instanceof UserDocument) {
//            transUser = new User();
//        }
//
//        assert transUser != null;
//        transUser.setAddress(user.getAddress());
//        transUser.setBodyType(user.getBodyType());
//        transUser.setChilds(user.isChilds());
//        transUser.setName(user.getName());
//        transUser.setProfession(user.getProfession());
//        transUser.setUuid(user.getUuid());
//        return transUser;
//    }
//
//    private User documentToUser(UserDocument document) {
//        User user = new User();
//        user.setAddress(document.getAddress());
//        user.setBodyType(document.getBodyType());
//        user.setChilds(document.isChilds());
//        user.setName(document.getName());
//        user.setProfession(document.getProfession());
//        user.setUuid(document.getUuid());
//        return user;
//    }
}
