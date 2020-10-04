package com.infinum.task.shared;

public interface ORMConverter<E, O extends PersistenceEntity> {

    O convertFromDomain(E entity);

    E convertToDomain(O orm);

}
