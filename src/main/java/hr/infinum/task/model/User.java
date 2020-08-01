package hr.infinum.task.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "application_user")
public class User implements ApplicationEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  @Builder.Default
  @ToString.Exclude
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "user_favourite_city",
      joinColumns = @JoinColumn(name = "application_user_id"),
      inverseJoinColumns = @JoinColumn(name = "city_id"))
  private List<City> favouriteCities = new ArrayList<>();

  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "last_modified_at", nullable = false)
  @LastModifiedDate
  private LocalDateTime lastModifiedAt;

  public List<City> getFavouriteCities() {
    return Collections.unmodifiableList(favouriteCities);
  }

  public boolean hasFavouriteCity(final City city) {
    return favouriteCities.contains(city);
  }

  public void addFavouriteCity(final City city) {
    this.favouriteCities.add(city);
  }

  public void removeFavouriteCity(final City city) {
    this.favouriteCities.remove(city);
  }

}
