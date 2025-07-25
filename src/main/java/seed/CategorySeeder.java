//package seed;
//
//import jakarta.annotation.PostConstruct;
//import org.jumia.data.models.Category;
//import org.jumia.data.respositories.CategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class CategorySeeder {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    private final List<String> defaultCategories = Arrays.asList(
////            "ELECTRONICS",
//            "FASHION",
//            "GROCERIES",
//            "HOME_APPLIANCES",
//            "BOOKS",
//            "MOBILE_DEVICES",
//            "TOYS",
//            "BEAUTY",
//            "SPORTS",
//            "OFFICE_SUPPLIES"
//    );
//
//    public void seed() {
//        if (categoryRepository.count() > 0) {
//            System.out.println("Categories already exist. Skipping seeding...");
//            return;
//        }
//
//        for (String name : defaultCategories) {
//            Category category = new Category();
//            category.setName(name);
//            categoryRepository.save(category);
//        }
//
//        System.out.println("Seeded " + defaultCategories.size() + " categories.");
//    }
//}
