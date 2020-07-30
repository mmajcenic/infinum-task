package hr.infinum.task.service;

import hr.infinum.task.model.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityCreator<E extends ApplicationEntity> {

  default E create(E entity) {
    if (entity.getId() != null) {
      throw new IllegalArgumentException("ID must not be provided when creating new entities");
    }
    return getRepository().save(entity);
  }

  JpaRepository<E, Long> getRepository();

}
