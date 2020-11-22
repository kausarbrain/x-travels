package com.xtravels.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
public class Post  {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String details;

    @Enumerated(EnumType.STRING)
    private PrivacyType privacy=PrivacyType.PUBLIC;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    private Location location;

    private Date createdAt;

    private Date updatedAt;

    @NotNull
    private Boolean pinned=Boolean.FALSE;

    public Post() { }

    public Post(String details, PrivacyType privacy, Location location,User user) {
        this.details=details;
        this.privacy = privacy;
        this.user = user;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }
    @PrePersist
    void createdAt(){
        this.createdAt=new Date();
    }
    @PreUpdate
    void updatedAt(){
        this.updatedAt=new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }



}
