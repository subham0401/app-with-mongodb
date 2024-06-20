package com.shubham.repository;

import com.shubham.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{userID: ?0}")
    User findByUserID(String userID);
    @Query("{'userName':?0}")
    User findByUserName(String userName);

}
