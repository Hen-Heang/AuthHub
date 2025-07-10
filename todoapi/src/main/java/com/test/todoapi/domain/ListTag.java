package com.test.todoapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "list_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ListTag {

}
