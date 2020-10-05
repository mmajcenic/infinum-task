package com.infinum.task.shared;

public interface DTOConverter<E extends DomainEntity<E>, D> {

    D convertFromDomain(E entity);

    E convertToDomain(D dto);

}
