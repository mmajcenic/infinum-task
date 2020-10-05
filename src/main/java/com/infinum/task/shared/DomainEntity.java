package com.infinum.task.shared;

public interface DomainEntity<E> {

    boolean sameIdentityAs(E other);

}
