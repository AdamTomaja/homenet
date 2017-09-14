package com.cydercode.homenet.server.journal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JournalService {

    @Autowired
    private JournalRowRepository repository;

    public void write(String name, String description) {
        repository.save(JournalRow.builder()
                .name(name)
                .description(description)
                .date(new Date())
                .build());
    }
}
