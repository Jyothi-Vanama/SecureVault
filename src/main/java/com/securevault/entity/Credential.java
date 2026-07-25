package com.securevault.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credentials")
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credentialId;

private String title;

private String website;
private String username;
private String encryptedPassword;
private String notes;
    @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User user;

@CreationTimestamp
@Column(updatable = false)
private LocalDateTime createdAt;

@UpdateTimestamp
private LocalDateTime updatedAt;

}