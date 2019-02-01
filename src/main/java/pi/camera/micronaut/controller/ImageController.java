package pi.camera.micronaut.controller;

import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import com.github.sarxos.webcam.Webcam;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.server.types.files.StreamedFile;

import javax.imageio.ImageIO;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller("/image")
public class ImageController {

	static {
		Webcam.setDriver(new V4l4jDriver());
	}

	@Get("/test")
	public String index() {
		return "Hello World";
	}

	@Get("/details")
	public Dimension[] details() {
		return Webcam.getDefault().getViewSizes();
	}

	@Get
	public StreamedFile download(@QueryValue(defaultValue = "640") int width,
			@QueryValue(defaultValue = "480") int height) throws IOException {
		Webcam webcam = Webcam.getDefault();
		if (webcam.isOpen()) {
			webcam.close();
		}
		webcam.setViewSize(new Dimension(width, height));
		webcam.open();
		BufferedImage image = webcam.getImage();
		if (webcam.isOpen()) {
			webcam.close();
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "PNG", os);
		InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
		return new StreamedFile(inputStream, "image.png");
	}
}