package com.test.myproject.web.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.test.myproject.core.dto.ReqQueryParam;
import com.test.myproject.core.interfaces.IOrderItemService;
import com.test.myproject.core.interfaces.IOrderService;
import com.test.myproject.core.interfaces.IProductService;
import com.test.myproject.core.order_aggregate.Order;
import com.test.myproject.core.order_aggregate.OrderItem;
import com.test.myproject.core.product_aggreate.Product;
import com.test.myproject.core.user_aggreate.User;
import com.test.myproject.web.api_models.CategoryDTO;
import com.test.myproject.web.api_models.CreateOrderDTO;
import com.test.myproject.web.api_models.OrderDTO;
import com.test.myproject.web.api_models.OrderItemDTO;
import com.test.myproject.web.api_models.ProductOrderItemDTO;
import com.test.myproject.web.api_models.UserDTO;
import com.test.myproject.web.assembler.OrderModelAssembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class OrderController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IProductService productService;

    @Autowired
    private PagedResourcesAssembler<OrderDTO> pagedResourcesAssembler;

    @Autowired
    private OrderModelAssembler assembler;

    public OrderController(OrderModelAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/orders")
    public ResponseEntity<PagedModel<OrderDTO>> all(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id", required = false) String sort) {
        log.info("Get Orders");

        ReqQueryParam reqQueryParam = new ReqQueryParam(page, size, sort);
        Sort sortRequest = Sort.by(reqQueryParam.sort);

        Pageable pageable = PageRequest.of(reqQueryParam.page, reqQueryParam.size, sortRequest);
        Page<OrderDTO> orderDTO = orderService.all(pageable).map(order -> {

            List<OrderItem> orderItemsResponse = orderItemService.allByOrder(order);
            OrderDTO orderDTOResponse = modelMapper.map(order, OrderDTO.class);
            List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
            for (OrderItem orderItem : orderItemsResponse) {
                OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
                orderItemDTOs.add(orderItemDTO);
            }
            orderDTOResponse.setOrderItems(orderItemDTOs);
            return orderDTOResponse;
        });
        ResponseEntity<PagedModel<OrderDTO>> response = new ResponseEntity(
                pagedResourcesAssembler.toModel(orderDTO, assembler),
                HttpStatus.OK);

        log.info("Response: {}", response.getBody());

        return response;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> newOrder(@Valid @RequestBody CreateOrderDTO request, Authentication authentication) {
        String email = authentication.getName();
        if (email == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        modelMapper.map(request.getOrderItems(), Order.class);
        for (OrderItem orderItem : request.getOrderItems()) {
            orderItems.add(orderItem);
        }
        User user = new User();
        user.setEmail(email);

        Order order = orderService.save(orderItems, user);

        UserDTO userDTO = modelMapper.map(order.getUser(), UserDTO.class);

        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        orderDTO.setUser(userDTO);
        List<OrderItem> orderItemsResponse = orderItemService.allByOrder(order);
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (OrderItem orderItem : orderItemsResponse) {
            OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);

            Product product = productService.one(orderItem.getProduct().getId());

            ProductOrderItemDTO productOrderItemDTO = modelMapper.map(product,
                    ProductOrderItemDTO.class);
            CategoryDTO categoryDTO = modelMapper.map(product.getCategory(), CategoryDTO.class);
            productOrderItemDTO.setCategory(categoryDTO);
            orderItemDTO.setProduct(productOrderItemDTO);
            orderItemDTOs.add(orderItemDTO);
        }
        orderDTO.setOrderItems(orderItemDTOs);

        EntityModel<OrderDTO> entityModel = assembler.toModel(orderDTO);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/orders/{id}")
    public EntityModel<OrderDTO> one(@PathVariable Long id) {
        Order order = orderService.one(id);

        List<OrderItem> orderItemsResponse = orderItemService.allByOrder(order);
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        for (OrderItem orderItem : orderItemsResponse) {
            OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
            orderItemDTOs.add(orderItemDTO);
        }
        orderDTO.setOrderItems(orderItemDTOs);

        return assembler.toModel(orderDTO);
    }
}
