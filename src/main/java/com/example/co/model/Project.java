package com.example.co.model;

import lombok.experimental.Delegate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import org.springframework.data.repository.cdi.Eager;

@Entity
@Table(name = "project", uniqueConstraints = {@UniqueConstraint(columnNames = {"external_id", "sdlc_system_id"})})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "external_id", nullable = false)
    @NotBlank
    public String externalId;

    @Column(name = "name")
    private String name;

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sdlc_system_id" )
    @NotNull
    public SdlcSystem sdlcSystem;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Instant lastModifiedDate;
}
