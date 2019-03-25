package com.numbDev.ElasticDemo.Service;

import com.numbDev.ElasticDemo.Base.Repo.DateRepository;
import com.numbDev.ElasticDemo.Base.User;
import com.numbDev.ElasticDemo.Base.UserMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final DateRepository repository;
    private final PlatformTransactionManager transactionManager;
    private final JdbcTemplate template;

    public UserService(DateRepository repository, PlatformTransactionManager transactionManager,
        JdbcTemplate template) {
        this.repository = repository;
        this.transactionManager = transactionManager;
        this.template = template;
    }



    public List<User> getAll(int begin, int end) {
//        List<User> result = new ArrayList<>();
//        repository.findAll().forEach(result::add);
//        return result;
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);
        String query = "SELECT * FROM public.articles LIMIT " + end +  " OFFSET " + begin;
        List result = template.query(query, new UserMapper());
        transactionManager.commit(status);
        return result;
    }

    public int count() {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);
        String query = "SELECT count(*) FROM public.articles";
        List<Map<String, Object>> result = template.queryForList(query);
        transactionManager.commit(status);
        Map<String, Object> t = result.get(0);
        return Math.toIntExact((Long) t.get("count"));
    }
}
