package com.infinum.task.city.model;

import com.infinum.task.shared.PersistenceEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "city")
public class CityORM implements PersistenceEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id")
  private Long id;

  @NotNull
  @NotEmpty(message = "Name is required")
  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @NotNull
  @NotEmpty(message = "Description is required")
  @Column(name = "description", nullable = false)
  private String description;

  @PositiveOrZero(message = "Population must not be negative")
  @NotNull(message = "Population is required")
  @Column(name = "population", nullable = false)
  private Integer population;

  @Column(name = "favourite_count", nullable = false)
  private Integer favouriteCount;

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "last_modified_at", nullable = false)
  @LastModifiedDate
  private LocalDateTime lastModifiedAt;

}
