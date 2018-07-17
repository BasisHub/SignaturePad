package net.seabears.signature;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToImageConverter {

    static class Signature {
        public byte[] data;
    }
    
    /**
     * Builds an image from the specified data of the json string
     * 
     * @param jsonString the json string
     * @param format the format (ex: Format.POINTS_LITTLE_ENDIAN, ...)
     * @param fileExtension the file extension (ex: png, bmp, jpg, ...)
     * @param filePath path to the json file
     * @return returns the created image data as a string
     */
	public String jsonDataToImage(String jsonString, Format format, String fileExtension, String filePath) {	
		return jsonDataToImage(jsonString, format, fileExtension, 1, filePath);
	}
    
    /**
     * Builds an image from the specified data of the json string
     * 
     * @param jsonString the json string
     * @param format the format (ex: Format.POINTS_LITTLE_ENDIAN, ...)
     * @param fileExtension the file extension (ex: png, bmp, jpg, ...)
     * @param strokeWidth defines the stroke width. default is 1
     * @return returns the created image data as a string
     */
	public String jsonDataToImage(String jsonString, Format format, String fileExtension, int strokeWidth) {	
		return jsonDataToImage(jsonString, format, fileExtension, strokeWidth, "./");
	}
    
    /**
     * Builds an image from the specified data of the json string
     * 
     * @param jsonString the json string
     * @param format the format (ex: Format.POINTS_LITTLE_ENDIAN, ...)
     * @param fileExtension the file extension (ex: png, bmp, jpg, ...)
     * @return returns the created image data as a string
     */
	public String jsonDataToImage(String jsonString, Format format, String fileExtension) {	
		return jsonDataToImage(jsonString, format, fileExtension, 1, "./");
	}
  
    /**
     * Builds an image from the specified data of the json string
     * 
     * @param jsonString the json string
     * @param format the format (ex: Format.POINTS_LITTLE_ENDIAN, ...)
     * @param fileExtension the file extension (ex: png, bmp, jpg, ...)
     * @param strokeWidth defines the stroke width. default is 1
     * @param filePath path to the json file
     * @return returns the created image data as a string
     */
	public String jsonDataToImage(String jsonString, Format format, String fileExtension, int strokeWidth, String fielPath) {	
		String signatureJSONString = buildSignatureJSONString(jsonString);
		
		Config.Builder builder = Config.builder().withStroke(strokeWidth);
		Config config = new Config(builder);
		
		try {
			writeImage(config, signatureJSONString, format, fileExtension, fielPath);
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		String fileData = imageToString(fileExtension, fielPath);
		return fileData;
	}
	
	private String buildSignatureJSONString(String json){
		try {
			JSONObject jsonObject = new JSONObject(json);
			String signature = jsonObject.getJSONObject("signature").getString("data");
			String signatureJSONString = "{\"data\":\"" + signature + "\"}";
			return signatureJSONString;
		} catch (JSONException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	
	private void writeImage(Config config, String signatureJSONString, Format format, String fileExtension, String fielPath) throws IOException{
		final Signature signature = new ObjectMapper().readValue(signatureJSONString, Signature.class);
		final Converter converter = new Converter(config);
		final RenderedImage image = converter.convert(signature.data, format);
		ImageIO.write(image, fileExtension, new File(fielPath + "jsonToImage." + fileExtension));
	}
	
	private String imageToString(String fileExtension, String fielPath) {
		FileReader fileReader = new FileReader();
		String fileData = fileReader.readFile(fielPath + "jsonToImage." + fileExtension);
		return fileData;
	}
	
}
