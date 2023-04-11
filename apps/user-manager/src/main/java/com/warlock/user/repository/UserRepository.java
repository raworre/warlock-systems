package com.warlock.user.repository;

import com.warlock.user.model.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, String> {
    UserDocument findByUsername(String username);
}
