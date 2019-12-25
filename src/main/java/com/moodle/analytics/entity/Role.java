package com.moodle.analytics.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table
public class Role implements Serializable {

    private static final long serialVersionUID = -1638260980499566010L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

}
