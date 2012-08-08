package net.redrezo.eclipse.tooling.imagedecorator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public class ImagePreviewDecorator implements ILabelDecorator {

	private Map<IFile, Image> images = new HashMap<IFile, Image>();
	
	private Image getReal(Device dev, IFile f) {
		if (!images.containsKey(f)) {
			try {
				Image real = new Image(dev, f.getContents());
				Rectangle size = real.getBounds();
				if (size.width > 16 || size.height > 16) {
					real = resize(real);
				}
				images.put(f, real);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return images.get(f);
	}
	
	private Image resize(Image source) {
		Image target = new Image(source.getDevice(), 16, 16);
		GC targetGC = new GC(target);
		Rectangle sourceBounds = source.getBounds();
		targetGC.drawImage(source, 0, 0, sourceBounds.width, sourceBounds.height, 0, 0, 16, 16);
		targetGC.dispose();
		source.dispose();
		return target;
	}
	
	@Override
	public void addListener(ILabelProviderListener listener) {
		
	}

	@Override
	public void dispose() {
		for (Image i : images.values()) {
			i.dispose();
		}
		images.clear();
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public Image decorateImage(Image image, Object element) {
		IFile f = (IFile) element;
		if ("png".equals(f.getFileExtension()) ||
		    "gif".equals(f.getFileExtension()) ||
		    "jpg".equals(f.getFileExtension())) {
			return getReal(image.getDevice(), f);
		}
		else return image;
	}

	@Override
	public String decorateText(String text, Object element) {
		return text;
	}

}
