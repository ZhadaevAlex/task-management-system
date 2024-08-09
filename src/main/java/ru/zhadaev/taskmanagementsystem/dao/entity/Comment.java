package ru.zhadaev.taskmanagementsystem.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(schema = "task_management", name = "comment")
public class Comment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private Timestamp time;

    @ManyToOne
    private User author;
    private String content;

    @ManyToOne
    private Task task;
}
