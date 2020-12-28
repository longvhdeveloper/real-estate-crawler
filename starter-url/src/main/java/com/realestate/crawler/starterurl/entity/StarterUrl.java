package com.realestate.crawler.starterurl.entity;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "starter_url", indexes = {
        @Index(name = "idx_check_sum_url", unique = true, columnList = "check_sum_url")
})
@NoArgsConstructor
@Slf4j
public class StarterUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private long dataSourceId;

    @Getter
    private String url;

    @Column(name = "check_sum_url")
    @Getter
    private String checkSumUrl;

    @Getter
    @Lob
    private String htmlContent;

    @Getter
    @Lob
    private String checkSumHtmlContent;

    @Setter
    private int statusCode;

    @Transient
    @Getter
    private Status status;

    @Getter
    private boolean isGenerated;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    public StarterUrl(String url, long dataSourceId) {

        if (url.isEmpty()) {
            throw new IllegalArgumentException("Starter URL is empty");
        }

        if (dataSourceId < 0) {
            throw new IllegalArgumentException("Data source of starter url is less than 0");
        }

        this.url = url;
        this.checkSumUrl = Hashing.sha256().hashString(this.url, StandardCharsets.UTF_8).toString();
        this.dataSourceId = dataSourceId;
        this.status = Status.ENABLED;
        this.isGenerated = false;
    }

    public boolean isEnabled() {
        return this.status.equals(Status.ENABLED);
    }

    public void setHtmlContent(String htmlContent) {
        if (htmlContent.isEmpty()) {
            throw new IllegalArgumentException("HTML content is empty");
        }

        if (isDuplicateHtmlContent(htmlContent)) {
            log.warn("HTML content of url {} is duplicate", this.url);
            return;
        }

        this.htmlContent = htmlContent;
        this.checkSumHtmlContent = Hashing.sha256().hashString(this.htmlContent, StandardCharsets.UTF_8).toString();
    }

    public static String createCheckSumUrl(String url) {
        return Hashing.sha256().hashString(url, StandardCharsets.UTF_8).toString();
    }

    public boolean isDuplicateHtmlContent(String html) {
        String checkSumHtmlContent = Hashing.sha256().hashString(html, StandardCharsets.UTF_8).toString();
        return checkSumHtmlContent.equals(this.checkSumHtmlContent);
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
}
