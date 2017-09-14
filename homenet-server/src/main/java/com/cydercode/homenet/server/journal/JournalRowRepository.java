package com.cydercode.homenet.server.journal;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface JournalRowRepository extends MongoRepository<JournalRow, String> {

}
