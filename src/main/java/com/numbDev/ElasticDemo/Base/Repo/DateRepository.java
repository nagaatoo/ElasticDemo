package com.numbDev.ElasticDemo.Base.Repo;

import com.numbDev.ElasticDemo.Base.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DateRepository extends CrudRepository<User, Long>{

}
