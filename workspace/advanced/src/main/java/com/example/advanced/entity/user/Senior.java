package com.example.advanced.entity.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter @Setter @ToString
@Table(name = "TBL_SENIOR")
public class Senior {
    @Id @GeneratedValue
    private Long id;
}
