package com.test.myproject.web.assembler;

import com.test.myproject.web.api.ProductController;
import com.test.myproject.web.api_models.ProductDTO;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<ProductDTO, EntityModel<ProductDTO>> {

    final ModelMapper modelMapper;

    public ProductModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<ProductDTO> toModel(ProductDTO userDTO) {
        return EntityModel.of(userDTO, linkTo(methodOn(ProductController.class).one(userDTO.getId())).withSelfRel());
    }
}
