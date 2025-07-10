package com.test.todoapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "list_shares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
