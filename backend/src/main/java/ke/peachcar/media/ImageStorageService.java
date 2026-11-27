package ke.peachcar.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.ObjectProvider;
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
  byte[] bytes=file.getBytes();
  if(!isAllowedImage(type,bytes)) throw new IllegalArgumentException("Only valid JPEG, PNG and WebP images are accepted.");
  Map<?,?> result=cloudinary.uploader().upload(bytes,ObjectUtils.asMap("folder","peachcar/vehicles","resource_type","image"));
  return new StoredImage(String.valueOf(result.get("public_id")),String.valueOf(result.get("secure_url")));
 }
 public void delete(String publicId) throws IOException {
  Cloudinary cloudinary=cloudinaryProvider.getIfAvailable();
  if(cloudinary!=null) cloudinary.uploader().destroy(publicId,ObjectUtils.emptyMap());
 }
 private boolean isAllowedImage(String type,byte[] b){
  if(type==null||b.length<12)return false;
  boolean jpeg=type.equals(MediaType.IMAGE_JPEG_VALUE)&&(b[0]&255)==255&&(b[1]&255)==216&&(b[2]&255)==255;
  boolean png=type.equals(MediaType.IMAGE_PNG_VALUE)&&(b[0]&255)==137&&b[1]==80&&b[2]==78&&b[3]==71;
  boolean webp=type.equals("image/webp")&&b[0]=='R'&&b[1]=='I'&&b[2]=='F'&&b[3]=='F'&&b[8]=='W'&&b[9]=='E'&&b[10]=='B'&&b[11]=='P';
  return jpeg||png||webp;
 }
 public record StoredImage(String publicId,String secureUrl){}
}
