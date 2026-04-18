package com.scm.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Contact {

    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private String address;
    private String pictures;
    private String description;
    private boolean favorite;
    private String websiteLink;
    private String githubLink;
    private String linkedInLink;


    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SocialLinks> links = new ArrayList<>();

    public void setProfilePicture(String fileURL) {
       this.pictures = fileURL;
    }

    @PrePersist
    public void prePersist() {
    this.createdAt = LocalDateTime.now();
}

}
