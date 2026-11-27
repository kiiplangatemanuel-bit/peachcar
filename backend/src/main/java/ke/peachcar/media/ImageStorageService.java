package ke.peachcar.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageStorageService {
 private final ObjectProvider<Cloudinary> cloudinaryProvider;
 public ImageStorageService(ObjectProvider<Cloudinary> cloudinaryProvider){this.cloudinaryProvider=cloudinaryProvider;}
 public StoredImage upload(MultipartFile file) throws IOException {
  Cloudinary cloudinary=cloudinaryProvider.getIfAvailable();
  if(cloudinary==null) throw new IllegalStateException("Image storage is not configured. Set Cloudinary credentials.");
  if(file==null||file.isEmpty()||file.getSize()>10L*1024*1024) throw new IllegalArgumentException("Each image must be non-empty and no larger than 10 MB.");
  String type=file.getContentType();
  if(type==null||!(type.equals(MediaType.IMAGE_JPEG_VALUE)||type.equals(MediaType.IMAGE_PNG_VALUE)||type.equals("image/webp"))) throw new IllegalArgumentException("Only JPEG, PNG and WebP images are accepted.");
  Map<?,?> result=cloudinary.uploader().upload(file.getBytes(),ObjectUtils.asMap("folder","peachcar/vehicles","resource_type","image"));
  return new StoredImage(String.valueOf(result.get("public_id")),String.valueOf(result.get("secure_url")));
 }
 public record StoredImage(String publicId,String secureUrl){}
}
