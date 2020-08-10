package com.example.co.model;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Entity
@Table(name = "sdlc_system")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Data
public class SdlcSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @URL
    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private Instant lastModifiedDate;
}
