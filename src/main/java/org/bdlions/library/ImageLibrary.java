package org.bdlions.library;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author nazmul hasan
 */
public class ImageLibrary {
    private final Logger logger = LoggerFactory.getLogger(ImageLibrary.class);
    private int IMG_WIDTH = 100;
    private int IMG_HEIGHT = 100;
    
    public static void main(String [] args){

//	try
//        {
//            BufferedImage originalImage = ImageIO.read(new File("c:\\image\\mkyong.jpg"));
//            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
//
//            BufferedImage resizeImageJpg = drawImage(originalImage, type);
//            ImageIO.write(resizeImageJpg, "jpg", new File("c:\\image\\mkyong_jpg.jpg"));
//
//            BufferedImage resizeImagePng = drawImage(originalImage, type);
//            ImageIO.write(resizeImagePng, "png", new File("c:\\image\\mkyong_png.jpg"));
//
//            BufferedImage resizeImageHintJpg = resizeImageWithHint(originalImage, type);
//            ImageIO.write(resizeImageHintJpg, "jpg", new File("c:\\image\\mkyong_hint_jpg.jpg"));
//
//            BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
//            ImageIO.write(resizeImageHintPng, "png", new File("c:\\image\\mkyong_hint_png.jpg"));
//
//	}
//        catch(IOException e){
//		System.out.println(e.getMessage());
//	}

    }
    
    public void resizeImage(String sourcePath, String destinationPath, int width, int height)
    {
        this.IMG_WIDTH = width;
        this.IMG_HEIGHT = height;
        try
        {
            BufferedImage originalImage = ImageIO.read(new File(sourcePath));
            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            BufferedImage resizeImageJpg = drawImage(originalImage, type);
            ImageIO.write(resizeImageJpg, "jpg", new File(destinationPath));
        }
        catch(Exception ex)
        {
            logger.error(ex.toString());
        }
    }
        
    private BufferedImage drawImage(BufferedImage originalImage, int type){
	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	g.dispose();

	return resizedImage;
    }

    private BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){

	BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	g.dispose();
	g.setComposite(AlphaComposite.Src);

	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.setRenderingHint(RenderingHints.KEY_RENDERING,
	RenderingHints.VALUE_RENDER_QUALITY);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	RenderingHints.VALUE_ANTIALIAS_ON);

	return resizedImage;
    }
}
