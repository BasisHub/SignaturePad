package net.seabears.signature;

import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToImageConverter {

    static class Signature {
        public byte[] data;
    }
    
    /**
     * Builds an image Base64 string from the specified data of the json string
     * 
     * @param jsonString the json string
     * @param format the format (ex: Format.POINTS_LITTLE_ENDIAN, ...)
     * @param fileExtension the file extension (ex: png, bmp, jpg, ...)
     * @return returns the created image data as a Base64 string
     */
	public String jsonDataToImage(String jsonString, Format format, String fileExtension) {	
		return jsonDataToImage(jsonString, format, fileExtension, 1);
	}
  
    /**
     * Builds an image Base64 string from the specified data of the json string
     * 
     * @param jsonString the json string
     * @param format the format (ex: Format.POINTS_LITTLE_ENDIAN, ...)
     * @param fileExtension the file extension (ex: png, bmp, jpg, ...)
     * @param strokeWidth defines the stroke width. default is 1
     * @return returns the created image data as a Base64 string
     */
	public String jsonDataToImage(String jsonString, Format format, String fileExtension, int strokeWidth) {	
		String signatureJSONString = buildSignatureJSONString(jsonString);
		
		Config.Builder builder = Config.builder().withStroke(strokeWidth);
		Config config = new Config(builder);
		
		RenderedImage image = null;
		try {
			image = createImage(config, signatureJSONString, format, fileExtension);
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		String fileData = imageToBase64String(image, fileExtension);
		return fileData;
	}
	
	private String buildSignatureJSONString(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			String signature = jsonObject.getString("data");
			String signatureJSONString = "{\"data\":\"" + signature + "\"}";
			return signatureJSONString;
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	private RenderedImage createImage(Config config, String signatureJSONString, Format format, String fileExtension) throws IOException{
		final Signature signature = new ObjectMapper().readValue(signatureJSONString, Signature.class);
		final Converter converter = new Converter(config);
		final RenderedImage image = converter.convert(signature.data, format);
		return image;
	}
	
	private String imageToBase64String(RenderedImage image, String fileExtension) {
	    final ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
	        ImageIO.write(image, fileExtension, Base64.getEncoder().wrap(os));
	        return os.toString(StandardCharsets.ISO_8859_1.name());
	    } catch (final IOException ioe) {
	        throw new UncheckedIOException(ioe);
	    }
	}
	
}
