package com.infinum.task.shared;

import java.util.List;
import java.util.stream.Collectors;

public interface EntityFactory<E, O extends PersistenceEntity, D> {

    E fromDto(D dto);

    E fromOrm(O orm);

    O toOrm(E entity);

    D toDto(E entity);

    default List<E> fromOrm(final List<O> orms) {
        return orms.stream()
                .map(this::fromOrm)
                .collect(Collectors.toUnmodifiableList());
    }

}
