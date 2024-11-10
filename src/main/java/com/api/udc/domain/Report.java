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
    private boolean no_animal;
    private String animal_description;

    public Report(String animal, String animal_description, List<String> imageUrl, List<String> location, String location_description, boolean no_image, boolean no_animal) {
        this.animal = animal;
        this.animal_description = animal_description;
        this.imageUrl = imageUrl;
        this.location = location;
        this.location_description = location_description;
        this.no_image = no_image;
        this.no_animal = no_animal;
    }

}
