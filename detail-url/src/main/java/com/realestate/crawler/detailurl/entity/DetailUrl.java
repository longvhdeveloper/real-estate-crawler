package com.realestate.crawler.detailurl.entity;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Table(name = "url", indexes = {
        @Index(name = "idx_check_sum_url", unique = true, columnList = "check_sum_url"),
})
@Entity
@NoArgsConstructor
public class DetailUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Getter
    private String url;

    @Column(name = "check_sum_url")
    @Getter
    private String checkSumUrl;

    @Lob
    @Getter
    private String htmlContent;

    @Lob
    @Getter
    private String checkSumHtmlContent;

    @Getter
    private long dataSourceId;

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

    public DetailUrl(String url, String htmlContent, long dataSourceId) {

        if (url.isEmpty()) {
            throw new IllegalArgumentException("Starter URL is empty");
        }

        if (htmlContent.isEmpty()) {
            throw new IllegalArgumentException("HTML content is empty");
        }

        if (dataSourceId <= 0) {
            throw new IllegalArgumentException("Data source of starter url is null");
        }

        this.url = url;
        this.checkSumUrl = Hashing.sha256().hashString(this.url, StandardCharsets.UTF_8).toString();
        this.htmlContent = htmlContent;
        this.checkSumHtmlContent = Hashing.sha256().hashString(this.htmlContent, StandardCharsets.UTF_8).toString();
        this.dataSourceId = dataSourceId;
        this.status = Status.ENABLED;
    }

    public boolean isEnabled() {
        return Status.ENABLED.equals(this.status);
    }

    public void setHtmlContent(String htmlContent) {
        if (htmlContent.isEmpty()) {
            throw new IllegalArgumentException("HTML content is empty");
        }

        if (!isHtmlContentDuplicate(htmlContent)) {
            this.htmlContent = htmlContent;
            this.checkSumHtmlContent = Hashing.sha256().hashString(this.htmlContent, StandardCharsets.UTF_8).toString();
        }
    }

    public boolean isHtmlContentDuplicate(String htmlContent) {
        String checkSumContent = Hashing.sha256().hashString(htmlContent, StandardCharsets.UTF_8).toString();

        return checkSumContent.equals(this.checkSumHtmlContent);
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
