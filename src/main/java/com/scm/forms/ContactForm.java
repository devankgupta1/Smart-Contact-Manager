package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.entities.Contact;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

    public ContactForm(Contact contact) {
        this.name = contact.getName();
        this.email = contact.getEmail();
        this.phoneNumber = contact.getPhoneNumber();
        this.address = contact.getAddress();
        this.description = contact.getDescription();
        this.favorite = contact.isFavorite();
        this.websiteLink = contact.getWebsiteLink();
        this.linkedInLink = contact.getLinkedInLink();
        this.picture = contact.getPictures();
        this.id = contact.getId();
    }

    // 🔹 NAME
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 to 50 characters")
    private String name;

    // 🔹 EMAIL
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // 🔹 PHONE (INDIA FRIENDLY)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Enter valid 10 digit Indian number")
    private String phoneNumber;

    // 🔹 ADDRESS
    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 200, message = "Address must be between 5 to 200 characters")
    private String address;

    // 🔹 DESCRIPTION
    @Size(max = 500, message = "Description can be max 500 characters")
    private String description;

    // 🔹 FAVORITE
    private boolean favorite;

    // 🔹 WEBSITE
    // @Pattern(
    //     regexp = "^(https?:\\/\\/)?([\\w\\-])+\\.([a-z]{2,6})([\\/\\w\\.-]*)*\\/?$",
    //     message = "Enter valid website URL"
    // )
    private String websiteLink;

    // 🔹 LINKEDIN
    // @Pattern(
    //     regexp = "^(https?:\\/\\/)?(www\\.)?linkedin\\.com\\/.*$",
    //     message = "Enter valid LinkedIn URL"
    // )
    private String linkedInLink;

    // create a annotation for image validation and use it here
    // size
    // resolution
    private MultipartFile contactImage;

    private String id;

    private String picture; // for update form to hold existing picture URL
}