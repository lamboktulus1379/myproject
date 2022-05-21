package com.test.myproject.web.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.myproject.core.interfaces.IUserService;
import com.test.myproject.core.role_aggreate.Role;
import com.test.myproject.core.user_aggreate.ReqQueryParam;
import com.test.myproject.core.user_aggreate.User;
import com.test.myproject.web.api_models.CreateUserDTO;
import com.test.myproject.web.api_models.UpdateUserDTO;
import com.test.myproject.web.api_models.UserDTO;
import com.test.myproject.web.assembler.UserModelAssembler;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PagedResourcesAssembler<UserDTO> pagedResourcesAssembler;

    @Autowired
    private UserModelAssembler assembler;

    public UserController(UserModelAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/users")
    public ResponseEntity<PagedModel<UserDTO>> all(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id", required = false) String sort,
            @RequestParam(name = "fullname", required = false) String fullname) {
        log.info("Get Users");

        ReqQueryParam reqQueryParam = new ReqQueryParam(page, size, sort);
        Sort sortRequest = Sort.by(reqQueryParam.sort);

        Pageable pageable = PageRequest.of(reqQueryParam.page, reqQueryParam.size, sortRequest);
        Page<UserDTO> usersDTO;
        if (fullname != null) {
            usersDTO = userService.allByFullnameLike("%" + fullname + "%", pageable)
                    .map(user -> modelMapper.map(user, UserDTO.class));
        } else {
            usersDTO = userService.all(pageable).map(user -> modelMapper.map(user, UserDTO.class));
        }

        ResponseEntity<PagedModel<UserDTO>> response = new ResponseEntity(
                pagedResourcesAssembler.toModel(usersDTO, assembler),
                HttpStatus.OK);

        log.info("Response: {}", response.getBody());

        return response;
    }

    @GetMapping("/users/{id}")
    public EntityModel<UserDTO> one(@PathVariable Long id) {
        User user = userService.one(id);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return assembler.toModel(userDTO);
    }

    @GetMapping("/users/info")
    public EntityModel<UserDTO> info(Authentication authentication) {
        String email = authentication.getName();

        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return assembler.toModel(userDTO);
    }

    @PostMapping("/users")
    public ResponseEntity<?> newUser(@Valid @RequestBody CreateUserDTO request) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        User userEntity = modelMapper.map(request, User.class);
        User user = userService.save(userEntity);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        EntityModel<UserDTO> entityModel = assembler.toModel(userDTO);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserDTO request) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        User userEntity = modelMapper.map(request, User.class);
        User user = userService.save(userEntity);

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        EntityModel<UserDTO> entityModel = assembler.toModel(userDTO);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/users/{id}")
    ResponseEntity<?> replaceUser(@RequestBody UpdateUserDTO newUser, @PathVariable Long id) {
        User userEntity = modelMapper.map(newUser, User.class);
        User updatedUser = userService.findAndUpdate(userEntity, id);

        UserDTO userDTO = modelMapper.map(updatedUser, UserDTO.class);
        EntityModel<UserDTO> entityModel = assembler.toModel(userDTO);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/users/{id}")
    ResponseEntity<?> deleteCity(@PathVariable Long id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws StreamWriteException, DatabindException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

                JWTVerifier jwtVerifier = JWT.require(algorithm).build();

                DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);

                String username = decodedJWT.getSubject();
                User user = userService.getUserByEmail(username);

                String accessToken = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles",
                                user.getRoles().stream().map(Role::getName)
                                        .collect(Collectors.toList()))
                        .sign(algorithm);

                int weather = 78;
                response.setHeader("fullName", user.getFullname());
                response.setHeader("weather", String.valueOf(weather));

                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);

                response.setContentType("application/json");

                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                log.error("Error logging in: {}", exception.getMessage());

                response.setHeader("error", exception.getMessage());
                response.setStatus(403);
                // response.sendError(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());

                response.setContentType("application/json");

                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
