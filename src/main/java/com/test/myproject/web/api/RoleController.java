package com.test.myproject.web.api;

import javax.validation.Valid;

import com.test.myproject.core.interfaces.IRoleService;
import com.test.myproject.core.interfaces.IUserService;
import com.test.myproject.core.product_aggreate.ReqQueryParam;
import com.test.myproject.core.role_aggreate.Role;
import com.test.myproject.web.api_models.CreateRoleDTO;
import com.test.myproject.web.api_models.CreateUserRoleDTO;
import com.test.myproject.web.api_models.RoleDTO;
import com.test.myproject.web.assembler.RoleModelAssembler;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
public class RoleController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleModelAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<RoleDTO> pagedResourcesAssembler;

    public RoleController(RoleModelAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/roles")
    public ResponseEntity<PagedModel<RoleDTO>> all(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        log.info("Get Roles");

        ReqQueryParam reqQueryParam = new ReqQueryParam(page, size, sort);
        Sort sortRequest = Sort.by(reqQueryParam.sort);

        Pageable pageable = PageRequest.of(reqQueryParam.page, reqQueryParam.size, sortRequest);

        Page<RoleDTO> rolesDTO = roleService.all(pageable).map(role -> modelMapper.map(role, RoleDTO.class));

        ResponseEntity response = new ResponseEntity(pagedResourcesAssembler.toModel(rolesDTO, assembler),
                HttpStatus.OK);

        log.info("Response: {}", response.getBody());

        return response;
    }

    @PostMapping("/roles")
    public ResponseEntity<?> newRole(@Valid @RequestBody CreateRoleDTO request) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        Role roleEntity = modelMapper.map(request, Role.class);
        Role role = roleService.save(roleEntity);

        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);
        EntityModel<RoleDTO> entityModel = assembler.toModel(roleDTO);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PostMapping("/roles/add-to-user")
    public ResponseEntity<?> saveRole(@RequestBody CreateUserRoleDTO createUserRoleDTO) {
        userService.addRoleToUser(createUserRoleDTO.getEmail(), createUserRoleDTO.getRole());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles/{id}")
    public EntityModel<RoleDTO> one(@PathVariable Long id) {
        Role role = roleService.one(id);

        RoleDTO roleDTO = modelMapper.map(role, RoleDTO.class);

        return assembler.toModel(roleDTO);
    }
}
