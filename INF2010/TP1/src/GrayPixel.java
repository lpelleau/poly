/**
 * Classe de pixel en tons de gris
 * 
 * @author :
 * @date :
 */

public class GrayPixel extends AbstractPixel {
	int pixel; // donnee du pixel

	/**
	 * Constructeur par defaut (pixel blanc)
	 */
	GrayPixel() {
		this.pixel = 255;
	}

	/**
	 * Constructeur par parametre
	 * 
	 * @param pixel
	 *            : valeur du pixel
	 */
	GrayPixel(int pixel) {
		// compléter
		this.pixel = pixel;
	}

	/**
	 * Renvoie la valeur du pixel
	 */
	public int getPixel() {
		return pixel;
	}

	/**
	 * Assigne une valeur au pixel
	 * 
	 * @param pixel:
	 *            valeur a assigner
	 */
	public void setPixel(int pixel) {
		this.pixel = pixel;
	}

	/**
	 * Renvoie un pixel copie de type noir et blanc
	 */
	public BWPixel toBWPixel() {
		// compléter
		return new BWPixel(pixel < 128 ? false : true);
	}

	/**
	 * Renvoie un pixel copie de type tons de gris
	 */
	public GrayPixel toGrayPixel() {
		// compléter
		return new GrayPixel(pixel);
	}

	/**
	 * Renvoie un pixel copie de type couleurs
	 */
	public ColorPixel toColorPixel() {
		// compléter
		return new ColorPixel(new int[] { pixel, pixel, pixel });
	}

	public TransparentPixel toTransparentPixel() {
		// compléter
		return new TransparentPixel(new int[] { pixel, pixel, pixel, 255 });
	}

	/**
	 * Renvoie le negatif du pixel (255-pixel)
	 */
	public AbstractPixel Negative() {
		// compléter
		return new GrayPixel(255 - pixel);
	}

	public void setAlpha(int alpha) {
		// ne fait rien
	}

	/**
	 * Convertit le pixel en String (sert a ecrire dans un fichier (avec un
	 * espace supplémentaire en fin)s
	 */
	public String toString() {
		return ((Integer) (pixel)).toString() + " ";
	}
}
