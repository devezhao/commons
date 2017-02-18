/*
 Copyright (C) 2010 QDSS.org
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.qdss.commons.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * 图片处理
 * 
 * @author <a href="mailto:zhaofang123@gmail.com">Zhao Fangfang</a>
 * @version $Id: GraphicsUtils.java 6 2015-06-08 08:56:34Z zhaoff@wisecrm.com $
 */
public class GraphicsUtils {
	
	/**
	 * 生成指定大小的粗略图
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage resize(BufferedImage image, int width, int height) {
		boolean proportion = true;
		
		int type = image.getType();
		BufferedImage target = null;
		double sx = (double) width / image.getWidth();
		double sy = (double) height / image.getHeight();
		if (proportion) {
			if (sx > sy) {
				sx = sy;
				width = (int) (sx * image.getWidth());
			} else {
				sy = sx;
				height = (int) (sy * image.getHeight());
			}
		}
		
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = image.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else {
			target = new BufferedImage(width, height, type);
		}
		
		Graphics2D g = target.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(image, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	/**
	 * 将图片另存为指定大小
	 * 
	 * @param srcFile
	 * @param destFile
	 * @param width
	 * @param hight
	 * @throws Exception
	 */
	public static void saveImageAsJpg(String srcFile, String destFile,	int width, int hight) throws Exception {
		BufferedImage srcImage;
		String imgType = "JPEG";
		if (srcFile.toLowerCase().endsWith(".png")) {
			imgType = "PNG";
		}
		
		File saveFile = new File(destFile);
		File fromFile = new File(srcFile);
		srcImage = ImageIO.read(fromFile);
		if (width > 0 || hight > 0) {
			srcImage = resize(srcImage, width, hight);
		}
		ImageIO.write(srcImage, imgType, saveFile);
	}
}
