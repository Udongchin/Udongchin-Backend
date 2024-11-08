package com.api.udc.domain;

import com.api.udc.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Getter
@Table(name="REPORT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    private List<String> imageUrl;
    private boolean no_image;
    @ElementCollection
    private List<String> location;
    private String location_description;
    private String animal;
    private String description;

    public Report(String animal, String description, List<String> imageUrl, List<String> location, String location_description, boolean no_image) {
        this.animal = animal;
        this.description = description;
        this.imageUrl = imageUrl;
        this.location = location;
        this.location_description = location_description;
        this.no_image = no_image;
    }

}
