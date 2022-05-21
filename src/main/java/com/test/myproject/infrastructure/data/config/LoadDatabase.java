package com.test.myproject.infrastructure.data.config;

import com.test.myproject.core.category_aggregate.Category;
import com.test.myproject.core.interfaces.ICategoryService;
import com.test.myproject.core.interfaces.IProductService;
import com.test.myproject.core.interfaces.IRoleService;
import com.test.myproject.core.interfaces.IUserService;
import com.test.myproject.core.product_aggreate.Product;
import com.test.myproject.core.role_aggreate.Role;
import com.test.myproject.core.user_aggreate.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(IUserService userService, IRoleService roleService, ICategoryService categoryService,
            IProductService productService) {
        return args -> {
            roleService.save(new Role(null, "ROLE_USER"));

            userService.save(new User(null, "John Doe", "john@email.com",
                    "StrongPassword123", new ArrayList<>()));

            userService.addRoleToUser("john@email.com", "ROLE_USER");

            Category category = categoryService.save(new Category(null, "Furniture", "The best furniture ever made."));
            productService.save(new Product(null, "Table", "1", 10, 20000.00, category));
            productService.save(new Product(null, "Chair", "2", 10, 25000.00, category));

            log.info("Database Load");
        };
    }
}
