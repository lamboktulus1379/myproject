package com.test.myproject.web.assembler;

import com.test.myproject.web.api.RoleController;
import com.test.myproject.web.api_models.RoleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoleModelAssembler implements RepresentationModelAssembler<RoleDTO, EntityModel<RoleDTO>> {

    final ModelMapper modelMapper;

    public RoleModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<RoleDTO> toModel(RoleDTO roleDTO) {
        return EntityModel.of(roleDTO, linkTo(methodOn(RoleController.class).one(roleDTO.getId())).withSelfRel());
    }
}
