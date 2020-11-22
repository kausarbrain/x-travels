package com.xtravels.models;

import javax.persistence.*;

@Entity
@Table(name = "role",uniqueConstraints =@UniqueConstraint(columnNames = "name"))
public class Role {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String name;
    public Role(){}
    public Role(String name){
        this.setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
