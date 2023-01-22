package com.urlShortner.urlShortner.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "Url")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private long id;

    @Column(name = "Long_Url")
    private String longUrl;

    @Column(name = "Short_Url")
    private String shortUrl;

    @Column(name = "Created_Date")
    private LocalDate createdDate;
}
