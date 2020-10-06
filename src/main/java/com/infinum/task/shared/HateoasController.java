package com.infinum.task.shared;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public interface HateoasController<D extends RepresentationModel<D> & DataEntity> {

    D getById(@Positive(message = "ID must be positive") Long id);

    default D enrichSingle(final D dataEntity) {
        dataEntity.add(linkTo(methodOn(getClass()).getById(dataEntity.getId())).withSelfRel());
        return dataEntity;
    }

}
