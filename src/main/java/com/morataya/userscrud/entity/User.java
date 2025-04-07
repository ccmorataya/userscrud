package com.morataya.userscrud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_users")
public class User {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long userId;
  private String firstName;
  private String lastName;
  @Column(unique = true, nullable = false)
  private String email;
  @Column(nullable = false)
  private Boolean status;
  @Column(nullable = false)
  private String password;
}
