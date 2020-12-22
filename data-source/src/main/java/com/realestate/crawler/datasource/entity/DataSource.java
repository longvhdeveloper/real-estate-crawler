package com.realestate.crawler.datasource.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table
@NoArgsConstructor
public class DataSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String url;

    @Setter
    private int statusCode;

    @Transient
    @Getter
    private Status status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    public DataSource(String name, String url) {

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Data source name is empty");
        }

        this.name = name;
        this.url = url;
        this.status = Status.ENABLED;
    }

    public boolean isEnabled() {
        return this.status.equals(Status.ENABLED);
    }

    @PostLoad
    void fillTransient() {
        if (statusCode > 0) {
            this.status = Status.of(statusCode);
        }
    }

    @PrePersist
    void fillPersistent() {
        if (!Objects.isNull(status)) {
            this.statusCode = status.getStatus();
        }
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", statusCode=" + statusCode +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
