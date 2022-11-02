package io.shangyh.dbcreator;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private long id;

    private String name;

    private String city;

    private String address;

    private String email;

    @Column(length = 4096)
    private String article;
}
