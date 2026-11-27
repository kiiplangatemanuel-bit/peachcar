package ke.peachcar.media;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Uses the exact Render environment-variable names. The Cloudinary bean is
 * absent when no cloud name is configured, but a partial configuration fails
 * startup instead of silently disabling image uploads.
 */
@Configuration
@ConditionalOnProperty(name = "CLOUDINARY_CLOUD_NAME")
public class CloudinaryConfig {
    @Bean
    Cloudinary cloudinary(
            @Value("${CLOUDINARY_CLOUD_NAME:}") String cloud,
            @Value("${CLOUDINARY_API_KEY:}") String key,
            @Value("${CLOUDINARY_API_SECRET:}") String secret) {
        if (cloud.isBlank() || key.isBlank() || secret.isBlank()) {
            throw new IllegalStateException(
                    "Cloudinary is partially configured. Set CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY and CLOUDINARY_API_SECRET together.");
        }
        return new Cloudinary(Map.of(
                "cloud_name", cloud,
                "api_key", key,
                "api_secret", secret,
                "secure", true));
    }
}
