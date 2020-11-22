package com.xtravels.models.dto;

import com.xtravels.models.PrivacyType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.lang.String;

public class PostDto {
    public Long id;

    @NotBlank(message = "Status should not be empty")
    @Length(min = 5, max = 250)
    public String details;

    public PrivacyType privacy=PrivacyType.PUBLIC;

    @Min(value = 1,message = "Location is not valid")
    public Long location;

    public PostDto(){}
    public PostDto(String details, PrivacyType privacy, Long location) {
        this.details = details;
        this.privacy = privacy;
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public PrivacyType getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PrivacyType privacy) {
        this.privacy = privacy;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

}
