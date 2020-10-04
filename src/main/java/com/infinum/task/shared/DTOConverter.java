package com.infinum.task.shared;

public interface DTOConverter<E, D> {

    D convertFromDomain(E entity);

    E convertToDomain(D dto);

}
