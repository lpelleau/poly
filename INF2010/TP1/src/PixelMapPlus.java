/**
 * Classe PixelMapPlus Image de type noir et blanc, tons de gris ou couleurs
 * Peut lire et ecrire des fichiers PNM Implemente les methodes de
 * ImageOperations
 * 
 * @author :
 * @date :
 */

public class PixelMapPlus extends PixelMap implements ImageOperations {
	/**
	 * Constructeur creant l'image a partir d'un fichier
	 * 
	 * @param fileName
	 *            : Nom du fichier image
	 */
	PixelMapPlus(String fileName) {
		super(fileName);
	}

	/**
	 * Constructeur copie
	 * 
	 * @param type
	 *            : type de l'image a creer (BW/Gray/Color)
	 * @param image
	 *            : source
	 */
	PixelMapPlus(PixelMap image) {
		super(image);
	}

	/**
	 * Constructeur copie (sert a changer de format)
	 * 
	 * @param type
	 *            : type de l'image a creer (BW/Gray/Color)
	 * @param image
	 *            : source
	 */
	PixelMapPlus(ImageType type, PixelMap image) {
		super(type, image);
	}

	/**
	 * Constructeur servant a allouer la memoire de l'image
	 * 
	 * @param type
	 *            : type d'image (BW/Gray/Color)
	 * @param h
	 *            : hauteur (height) de l'image
	 * @param w
	 *            : largeur (width) de l'image
	 */
	PixelMapPlus(ImageType type, int h, int w) {
		super(type, h, w);
	}

	/**
	 * Genere le negatif d'une image
	 */
	public void negate() {
		// compléter
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				imageData[i][j] = imageData[i][j].Negative();
			}
		}
	}

	/**
	 * Convertit l'image vers une image en noir et blanc
	 */
	public void convertToBWImage() {
		// compléter
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				imageData[i][j] = imageData[i][j].toBWPixel();
			}
		}
	}

	/**
	 * Convertit l'image vers un format de tons de gris
	 */
	public void convertToGrayImage() {
		// compléter
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				imageData[i][j] = imageData[i][j].toGrayPixel();
			}
		}
	}

	/**
	 * Convertit l'image vers une image en couleurs
	 */
	public void convertToColorImage() {
		// compléter
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				imageData[i][j] = imageData[i][j].toColorPixel();
			}
		}
	}

	public void convertToTransparentImage() {
		// compléter
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				imageData[i][j] = imageData[i][j].toTransparentPixel();
			}
		}
	}

	/**
	 * Fait pivoter l'image de 10 degres autour du pixel (row,col)=(0, 0) dans
	 * le sens des aiguilles d'une montre (clockWise == true) ou dans le sens
	 * inverse des aiguilles d'une montre (clockWise == false). Les pixels vides
	 * sont blancs.
	 * 
	 * @param clockWise
	 *            : Direction de la rotation
	 */
	public void rotate(int x, int y, double angleRadian) {
		// compléter
		if (180 * angleRadian / Math.PI % 360 == 0) {
			return;
		}

		AbstractPixel[][] tmp = new AbstractPixel[height][width];

		/*for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int[] res = multMat(x, y, i, j, angleRadian);
				if (res[0] < 0 || res[1] < 0 || res[0] >= height || res[1] >= width) {
					continue;
				}
				tmp[res[0]][res[1]] = imageData[i][j];
			}
		}

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (tmp[i][j] == null) {
					tmp[i][j] = new BWPixel();
				}
			}
		}*/

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int[] res = multMat(x, y, i, j, angleRadian);
				//System.out.println(i + " " + j + " > " + res[0] + " " + res[1] + " (" + height + " " + width + ")");
				if (res[0] < 0 || res[1] < 0 || res[1] >= height || res[0] >= width) {
					tmp[i][j] = new BWPixel();
				} else {
					tmp[i][j] = imageData[res[1]][res[0]];
				}
			}
		}

		clearData();
		imageData = tmp;
	}
	public void rotateDegree(int x, int y, double angleDegree) {
		rotate(x, y, angleDegree * Math.PI / 180);
	}

	private int[] multMat(int a, int b, int x, int y, double radian) {
		double[] A = new double[] { x, y, 1 };
		/*double[][] B = new double[][] {
				{ Math.cos(radian), Math.sin(radian) * -1, Math.cos(radian) * -1 * a + Math.sin(radian) * b + a },
				{ Math.sin(radian), Math.cos(radian), Math.sin(radian) * -1 * a - Math.cos(radian) * b + b },
				{ 0, 0, 1 } };*/
		double[][] B = new double[][] {
			{ Math.cos(radian), Math.sin(radian), Math.cos(radian) * -1 * a - Math.sin(radian) * b + a },
			{ 1 * Math.sin(radian), Math.cos(radian), Math.sin(radian) * a - Math.cos(radian) * b + b },
			{ 0, 0, 1 } };

		int[] C = new int[3];
		C[0] = (int) (A[0] * B[0][0] + A[1] * B[0][1] + A[2] * B[0][2]);
		C[1] = (int) (A[1] * B[1][0] + A[1] * B[1][1] + A[2] * B[1][2]);
		C[2] = (int) (A[2] * B[2][0] + A[1] * B[2][1] + A[2] * B[2][2]);

		return C;
	}

	/**
	 * Modifie la longueur et la largeur de l'image
	 * 
	 * @param w
	 *            : nouvelle largeur
	 * @param h
	 *            : nouvelle hauteur
	 */
	public void resize(int w, int h) throws IllegalArgumentException {
		if (w < 0 || h < 0)
			throw new IllegalArgumentException();

		// compléter

		if (w == 0 || h == 0) {
			height = h;
			width = w;
			imageData = new AbstractPixel[h][w];
			return;
		}

		double scaleH = (double) height / h;
		double scaleW = (double) width / w;
		AbstractPixel[][] tmp = new AbstractPixel[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				tmp[i][j] = imageData[(int) (i * scaleH)][(int) (j * scaleW)];
			}
		}
		clearData();
		height = h;
		width = w;
		imageData = tmp;
	}

	/**
	 * Insert pm dans l'image a la position row0 col0
	 */
	public void inset(PixelMap pm, int row0, int col0) {
		// compléter
		for (int i = row0, k = 0; i < height && k < pm.height; i++, k++) {
			for (int j = col0, l = 0; j < width && l < pm.width; j++, l++) {
				imageData[i][j] = pm.getPixel(k, l);
			}
		}
	}

	/**
	 * Decoupe l'image
	 */
	public void crop(int h, int w) {
		// compléter
		if (h < 0 || w < 0)
			throw new IllegalArgumentException();

		AbstractPixel[][] tmp = new AbstractPixel[h][w];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				tmp[i][j] = ((i < height && j < width) ? imageData[i][j] : new BWPixel());
			}
		}

		clearData();
		height = h;
		width = w;
		imageData = tmp;
	}

	/**
	 * Effectue une translation de l'image
	 */
	public void translate(int rowOffset, int colOffset) {
		// compléter
		AbstractPixel[][] tmp = new AbstractPixel[height][width];
		rowOffset *= -1;
		colOffset *= -1;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				try {
					tmp[i][j] = imageData[rowOffset + i][colOffset + j];
				} catch (ArrayIndexOutOfBoundsException e) {
					tmp[i][j] = new BWPixel();
				}
			}
		}

		clearData();
		imageData = tmp;
	}

	/**
	 * Effectue un zoom autour du pixel (x,y) d'un facteur zoomFactor
	 * 
	 * @param x
	 *            : colonne autour de laquelle le zoom sera effectue
	 * @param y
	 *            : rangee autour de laquelle le zoom sera effectue
	 * @param zoomFactor
	 *            : facteur du zoom a effectuer. Doit etre superieur a 1
	 */
	public void zoomIn(int x, int y, double zoomFactor) throws IllegalArgumentException {
		if (zoomFactor < 1.0)
			throw new IllegalArgumentException();

		if (zoomFactor == 1)
			return;

		// compléter
		int nH = (int) (height / zoomFactor);
		int nW = (int) (width / zoomFactor);
		AbstractPixel[][] tmp = new AbstractPixel[nH][nW];

		int sH = y - (nH / 2);
		if (sH < 0)
			sH = 0;
		else if (sH + nH >= height)
			sH = height - nH - 1;

		int sW = x - (nW / 2);
		if (sW < 0)
			sW = 0;
		else if (sW + nW >= width)
			sW = width - nW - 1;

		for (int i = 0; i < nH; i++) {
			for (int j = 0; j < nW; j++) {
				tmp[i][j] = imageData[sH + i][sW + j];
			}
		}

		imageData = tmp;
		int tH = height;
		int tW = width;
		height = nH;
		width = nW;
		resize(tW, tH);
	}
}
