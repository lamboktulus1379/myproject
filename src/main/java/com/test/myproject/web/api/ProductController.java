package com.test.myproject.web.api;

import javax.validation.Valid;

import com.test.myproject.core.category_aggregate.Category;
import com.test.myproject.core.category_aggregate.exception.CategoryNotFoundException;
import com.test.myproject.core.interfaces.ICategoryService;
import com.test.myproject.core.interfaces.IProductService;
import com.test.myproject.core.product_aggreate.ReqQueryParam;
import com.test.myproject.core.product_aggreate.Product;
import com.test.myproject.web.api_models.CreateProductDTO;
import com.test.myproject.web.api_models.ProductDTO;
import com.test.myproject.web.api_models.UpdateProductDTO;
import com.test.myproject.web.assembler.ProductModelAssembler;

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
public class ProductController {
    private static final Logger LOG = (Logger) LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PagedResourcesAssembler<ProductDTO> pagedResourcesAssembler;

    @Autowired
    private ProductModelAssembler assembler;

    public ProductController(ProductModelAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/products")
    public ResponseEntity<PagedModel<ProductDTO>> all(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(name = "name", required = false) String name) {
        LOG.info("Get Products");

        ReqQueryParam reqQueryParam = new ReqQueryParam(page, size, sort);
        Sort sortRequest = Sort.by(reqQueryParam.sort);

        Pageable pageable = PageRequest.of(reqQueryParam.page, reqQueryParam.size, sortRequest);

        Page<ProductDTO> productsDTO;

        if (name != null) {
            productsDTO = productService.allByNameLike(name, pageable)
                    .map(product -> modelMapper.map(product, ProductDTO.class));
        } else {
            productsDTO = productService.all(pageable)
                    .map(product -> modelMapper.map(product, ProductDTO.class));
        }

        ResponseEntity response = new ResponseEntity(pagedResourcesAssembler.toModel(productsDTO, assembler),
                HttpStatus.OK);

        LOG.info("Response: {}", response.getBody());

        return response;
    }

    @GetMapping("/products/{id}")
    public EntityModel<ProductDTO> one(@PathVariable Long id) {
        Product product = productService.one(id);

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return assembler.toModel(productDTO);
    }

    @PostMapping("/products")
    public ResponseEntity<?> newProduct(@Valid @RequestBody CreateProductDTO request) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        Product productEntity = modelMapper.map(request, Product.class);

        Category category = categoryService.one(request.getCategoryId());
        productEntity.setCategory(category);
        if (category == null) {
            throw new CategoryNotFoundException(request.getCategoryId());
        }
        Product product = productService.save(productEntity);

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        EntityModel<ProductDTO> entityModel = assembler.toModel(productDTO);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/products/{id}")
    ResponseEntity<?> replaceProduct(@RequestBody UpdateProductDTO newProduct, @PathVariable Long id) {
        Product updateProductDTO = modelMapper.map(newProduct, Product.class);
        Product updatedProduct = productService.findAndUpdate(updateProductDTO, id);

        ProductDTO productDTO = modelMapper.map(updatedProduct, ProductDTO.class);
        EntityModel<ProductDTO> entityModel = assembler.toModel(productDTO);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/products/{id}")
    ResponseEntity<?> deleteCity(@PathVariable Long id) {
        productService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
