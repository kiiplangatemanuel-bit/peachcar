package ke.peachcar.media;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import java.util.Map;

@Configuration
@ConditionalOnProperty(name = "CLOUDINARY_CLOUD_NAME")
public class CloudinaryConfig {
 @Bean Cloudinary cloudinary(@Value("${CLOUDINARY_CLOUD_NAME:}") String cloud,@Value("${CLOUDINARY_API_KEY:}") String key,@Value("${CLOUDINARY_API_SECRET:}") String secret){
  if(cloud.isBlank()||key.isBlank()||secret.isBlank()) throw new IllegalStateException("Cloudinary credentials must be supplied together.");
  return new Cloudinary(Map.of("cloud_name",cloud,"api_key",key,"api_secret",secret,"secure",true));
 }
}
