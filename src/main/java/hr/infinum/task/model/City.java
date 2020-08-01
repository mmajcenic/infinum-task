package hr.infinum.task.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "city")
public class City implements ApplicationEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id")
  private Long id;

  @NotEmpty(message = "Name is required")
  @Column(name = "name", nullable = false, unique = true)
  private String name;

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

  public void incrementFavouriteCount() {
    favouriteCount++;
  }

  public void decrementFavouriteCount() {
    favouriteCount--;
  }

  @PrePersist
  public void onPrePersist() {
    if (favouriteCount == null) {
      setFavouriteCount(0);
    }
  }

}
