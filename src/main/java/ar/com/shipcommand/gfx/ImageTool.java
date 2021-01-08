package ar.com.shipcommand.gfx;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Image tools
 */
public class ImageTool {
    private static final int UPPER = 0;
    private static final int LEFT = 0;

    /**
     * Graphics default configuration
     */
    private static final GraphicsConfiguration config =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

    /**
     * Creates a new hardware accelerated image
     * @param width Image width in pixels
     * @param height Image height in pixels
     * @param alpha Alpha enabled
     * @return Created image
     */
    public static BufferedImage createHardwareAccelerated(final int width, final int height,
                                      final boolean alpha) {
        return config.createCompatibleImage(width, height, alpha
                ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }

    /**
     * Resize the specified buffered image
     * @param image Image to resize
     * @param newWidth New width
     * @param newHeight New height
     * @return Resized image
     */
    public static BufferedImage resize(BufferedImage image, int newWidth, int newHeight) {
        int currentWidth = image.getWidth();
        int currentHeight = image.getHeight();

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, image.getType());

        Graphics2D graphics = newImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        graphics.drawImage(image,
                UPPER, LEFT, newWidth, newHeight,
                UPPER, LEFT, currentWidth, currentHeight, null);

        graphics.dispose();

        return newImage;
    }
}
