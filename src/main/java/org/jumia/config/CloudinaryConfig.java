package org.jumia.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dncpjrua2",
                "api_key", "741653255657251",
                "api_secret", "L_H415HwgYSpO-CyYBQU6FGhntk"
        ));
    }
}
