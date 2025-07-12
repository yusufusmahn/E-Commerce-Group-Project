package org.jumia.controllers.seedController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seed.CategorySeeder;
import seed.SuperAdminSeeder;

@RestController
@RequestMapping("/api/super-admin/dev-tools")
public class SeederController {

//    @Autowired
//    private ProductSeeder productSeeder;

    @Autowired
    private CategorySeeder categorySeeder;

    @Autowired
    private SuperAdminSeeder superAdminSeeder;


//    @PostMapping("/seed-products")
//    public String seedProducts() {
//        productSeeder.seed();
//        return "Product seeding completed (or skipped if already seeded).";
//    }

    @PostMapping("/seed-categories")
    public String seedCategories() {
        categorySeeder.seed();
        return "Category seeding completed (or skipped if already seeded).";
    }

    @PostMapping("/seed-superadmin")
    public String seedSuperAdmin() {
        superAdminSeeder.seed();
        return "Super admin user seeding completed.";
    }
}
