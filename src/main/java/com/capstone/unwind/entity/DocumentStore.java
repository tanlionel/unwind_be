package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "document_store")
public class DocumentStore {
    @Id
    @Column(name = "document_store_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "entity_id")
    private Integer entityId;

}