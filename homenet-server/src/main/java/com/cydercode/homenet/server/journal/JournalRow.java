package com.cydercode.homenet.server.journal;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Builder
public class JournalRow {

    @Id
    private String id;

    private Date date;

    private String name;

    private String description;

}
