package com.test.myproject.web.assembler;

import com.test.myproject.web.api.CategoryController;
import com.test.myproject.web.api_models.CategoryDTO;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<CategoryDTO, EntityModel<CategoryDTO>> {

    final ModelMapper modelMapper;

    public CategoryModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<CategoryDTO> toModel(CategoryDTO categoryDTO) {
        return EntityModel.of(categoryDTO,
                linkTo(methodOn(CategoryController.class).one(categoryDTO.getId())).withSelfRel());
    }
}
