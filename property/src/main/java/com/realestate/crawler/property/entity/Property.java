package com.realestate.crawler.property.entity;

import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Entity
@Table(indexes = {
        @Index(name = "idx_check_sum_url", unique = true, columnList = "check_sum_url")
})
@NoArgsConstructor
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private long id;

    @Getter
    @Setter
    private String name;

    @Getter
    private String price;

    @Getter
    private String area;

    @Column(length = 500)
    @Getter
    private String address;

    @Getter
    @Lob
    private String description;

    @Getter
    private String url;

    @Getter
    @Column(name = "check_sum_url")
    private String checkSumUrl;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    public Property(String name, String price, String area, String address, String description, String url) {
        if (url.isEmpty()) {
            throw new IllegalArgumentException("Property url is empty");
        }

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Property name is empty");
        }

        this.name = name;
        this.price = price;
        this.area = area;
        this.address = address;
        this.description = description;
        this.url = url;
        this.checkSumUrl = Hashing.sha256().hashString(this.url, StandardCharsets.UTF_8).toString();
    }
}
