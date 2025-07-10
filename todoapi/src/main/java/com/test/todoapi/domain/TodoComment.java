package com.test.todoapi.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "todo_comment")
@Entity

public class TodoComment {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
