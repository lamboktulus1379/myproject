package com.test.myproject.web.api;

import javax.validation.Valid;

import com.test.myproject.core.category_aggregate.Category;
import com.test.myproject.core.interfaces.ICategoryService;
import com.test.myproject.core.product_aggreate.ReqQueryParam;
import com.test.myproject.web.api_models.CategoryDTO;
import com.test.myproject.web.api_models.CreateCategoryDTO;
import com.test.myproject.web.assembler.CategoryModelAssembler;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PagedResourcesAssembler<CategoryDTO> pagedResourcesAssembler;

    @Autowired
    private CategoryModelAssembler assembler;

    public CategoryController(CategoryModelAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/categories")
    public ResponseEntity<PagedModel<CategoryDTO>> all(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(name = "name", required = false) String name) {
        LOG.info("Get Categories");

        ReqQueryParam reqQueryParam = new ReqQueryParam(page, size, sort);
        Sort sortRequest = Sort.by(reqQueryParam.sort);

        Pageable pageable = PageRequest.of(reqQueryParam.page, reqQueryParam.size, sortRequest);

        Page<CategoryDTO> categoriesDTO;
        if (name != null) {
            categoriesDTO = categoryService.allByNameLike(name, pageable)
                    .map(category -> modelMapper.map(category, CategoryDTO.class));
        } else {
            categoriesDTO = categoryService.all(pageable)
                    .map(category -> modelMapper.map(category, CategoryDTO.class));
        }

        ResponseEntity response = new ResponseEntity(pagedResourcesAssembler.toModel(categoriesDTO, assembler),
                HttpStatus.OK);

        LOG.info("Response: {}", response.getBody());

        return response;
    }

    @GetMapping("/categories/{id}")
    public EntityModel<CategoryDTO> one(@PathVariable Long id) {
        Category category = categoryService.one(id);

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        return assembler.toModel(categoryDTO);
    }

    @PostMapping("/categories")
    public ResponseEntity<?> newCategory(@Valid @RequestBody CreateCategoryDTO request) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        Category categoryEntity = modelMapper.map(request, Category.class);
        Category category = categoryService.save(categoryEntity);

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        EntityModel<CategoryDTO> entityModel = assembler.toModel(categoryDTO);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/categories/{id}")
    ResponseEntity<?> replaceCategory(@RequestBody Category newCategory, @PathVariable Long id) {
        Category updatedCategory = categoryService.findAndUpdate(newCategory, id);

        CategoryDTO categoryDTO = modelMapper.map(updatedCategory, CategoryDTO.class);
        EntityModel<CategoryDTO> entityModel = assembler.toModel(categoryDTO);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/categories/{id}")
    ResponseEntity<?> deleteCity(@PathVariable Long id) {
        categoryService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
