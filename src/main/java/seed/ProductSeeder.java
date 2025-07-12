//package seed;
//
//import jakarta.annotation.PostConstruct;
//import org.jumia.data.models.Product;
//import org.jumia.data.models.ProductStatus;
//import org.jumia.data.models.Category;
//import org.jumia.data.respositories.CategoryRepository;
//import org.jumia.data.respositories.ProductRepository;
//import org.jumia.services.CloudinaryService.CloudinaryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.*;
//
//@Component
//public class ProductSeeder {
//
//    @Autowired
//    private CloudinaryService cloudinaryService;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    private final String CATEGORY_ID = "686bda2febfedecab7aa974b";
//    private final String SELLER_ID = "6863fa6222f8edaabcd574f1";
//
//    private final Random random = new Random();
//
//    private final List<String> sampleNames = Arrays.asList(
//            "Wireless Mouse", "Mechanical Keyboard", "Noise Cancelling Headphones", "4K Monitor",
//            "Gaming Chair", "Smartwatch", "Bluetooth Speaker", "External SSD", "Fitness Tracker",
//            "Drone Camera", "Webcam", "Graphic Tablet", "Wireless Charger", "Smart Light Bulb",
//            "Home Security Camera", "Electric Kettle", "Air Purifier", "Smart Thermostat",
//            "USB Hub", "Laptop Stand"
//    );
//
//    @PostConstruct
//    public void seed() {
//        if (productRepository.count() > 0) {
//            System.out.println("Products already seeded. Skipping...");
//            return;
//        }
//        try {
//            File folder = new ClassPathResource("fake-images").getFile();
//            File[] files = folder.listFiles();
//
//            if (files == null || files.length == 0) {
//                System.out.println("No image files found.");
//                return;
//            }
//
//            Category category = categoryRepository.findById(CATEGORY_ID)
//                    .orElseThrow(() -> new RuntimeException("Category not found: " + CATEGORY_ID));
//
//            List<Product> products = new ArrayList<>();
//
//            for (File file : files) {
//                String baseName = file.getName().replace(".jpg", "").replace("_", " ");
//                String productName = baseName.length() > 3 ? baseName : getRandomName();
//
//                MultipartFile multipartFile = new MockMultipartFile(
//                        file.getName(), file.getName(), "image/jpeg", new FileInputStream(file)
//                );
//
//                String imageUrl = cloudinaryService.uploadImage(multipartFile);
//
//                Product product = new Product();
//                product.setName(productName);
//                product.setDescription("High-quality " + productName.toLowerCase());
//                product.setPrice(getRandomPrice());
//                product.setQuantityAvailable(getRandomQuantity());
//                product.setImageUrl(imageUrl);
//                product.setCategoryId(category.getId());
//                product.setCategoryName(category.getName());
//                product.setSellerId(SELLER_ID);
//                product.setStatus(ProductStatus.APPROVED); // or PENDING if you prefer
//
//                products.add(product);
//                System.out.println("Seeded " + products.size() + " products successfully!");
////                System.out.println("Saved successfully!");
//
//
//            }
//
//            productRepository.saveAll(products);
////            System.out.println("First product: " + products.get(0));
//            System.out.println("Saving " + products.size() + " products to DB...");
//
//
////            System.out.println("Seeded " + products.size() + " products successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getRandomName() {
//        return sampleNames.get(random.nextInt(sampleNames.size()));
//    }
//
//    private double getRandomPrice() {
//        return 3000 + random.nextInt(10000);
//    }
//
//    private int getRandomQuantity() {
//        return 5 + random.nextInt(20);
//    }
//}
