package com.warlock.user.repository;

import com.warlock.user.model.ProfileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<ProfileDocument, String> {
}
