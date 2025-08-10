//This is the entity. Maps to the table: url_mapping

package com.shayaan.urlshortener.model;
import lombok.*;
import jakarta.persistence.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shortCode;

    @Column(nullable = false)
    private String originalUrl;
}
