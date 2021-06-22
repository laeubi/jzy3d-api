/*******************************************************************************
 * (c) 2021 Läubisoft GmbH, Christoph Läubrich
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or any later version. the GNU Lesser General Public License should be
 * included with this distribution in the file LICENSE.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.jzy3d.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;

import jgl.GL;

public class JGLCanvas extends GLCanvas {

	protected GL gl = new SWTGL();

	public JGLCanvas(Composite parent, int style, GLData data) {

		super(parent, style, data);
		gl.setAutoAdaptToHiDPI(false);
		doResize(0, 0);
		addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {

				Point size = getSize();
				doResize(size.x, size.y);
			}


			@Override
			public void controlMoved(ControlEvent e) {

			}
		});
		addPaintListener(e -> {
			doPaint(e.gc);
		});
	}

	protected void doResize(int w, int h) {

		gl.glViewport(0, 0, w, h);
	}

	protected void doPaint(GC gc) {

		gc.setBackground(getBackground());
		Point size = getSize();
		gc.fillRectangle(0, 0, size.x, size.y);
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_RED));
		gc.drawLine(0, 0, size.x, size.y);
		SWTGL swtgl = (SWTGL)gl;
		if(swtgl.imageData != null) {
			Image image = new Image(gc.getDevice(), swtgl.imageData);
			gc.drawImage(image, 0, 0);
			image.dispose();
		}
		System.out.println("paint done");
	}

	public GL getGL() {

		return gl;
	}

	@Override
	public void setCurrent() {

		super.setCurrent();
	}

	private static final class SWTGL extends GL {

		private ImageData imageData;

		@Override
		public void glFlush() {

			int[] buffer = Context.ColorBuffer.Buffer;
			int width = Context.Viewport.Width;
			int height = Context.Viewport.Height;
			PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
			imageData = new ImageData(width, height, 24, palette);
			imageData.setPixels(0, 0, buffer.length, buffer, 0);
			byte[] alphaBytes = new byte[buffer.length];
			for(int i = 0; i < alphaBytes.length; i++) {
				alphaBytes[i] = (byte)((buffer[i] >> 24) & 0xFF);
			}
			imageData.setAlphas(0, 0, alphaBytes.length, alphaBytes, 0);
		}
	}
}
