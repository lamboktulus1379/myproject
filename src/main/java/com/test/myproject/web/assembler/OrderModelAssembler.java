package com.test.myproject.web.assembler;

import com.test.myproject.web.api.OrderController;
import com.test.myproject.web.api_models.OrderDTO;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<OrderDTO, EntityModel<OrderDTO>> {

    final ModelMapper modelMapper;

    public OrderModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public EntityModel<OrderDTO> toModel(OrderDTO orderDTO) {
        return EntityModel.of(orderDTO, linkTo(methodOn(OrderController.class).one(orderDTO.getId())).withSelfRel());
    }
}
