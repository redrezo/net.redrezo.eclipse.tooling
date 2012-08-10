package net.redrezo.eclipse.tooling.imagedecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.redrezo.eclipse.tooling.imagedecorator.preferences.PreferenceConstants;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IDelayedLabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

public class ImagePreviewDecorator implements IDelayedLabelDecorator {

	private List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();
	
	private Map<IFile, Image> images = new HashMap<IFile, Image>();
	
	private Image prepareImage(Device dev, IFile f) throws CoreException {
		Image image = new Image(dev, f.getContents());
		Rectangle size = image.getBounds();
		if (size.width > 16 || size.height > 16) {
			image = resize(image);
		}
		return image;
	}
	
	private Image resize(Image source) {
		Rectangle sourceBounds = source.getBounds();
		
		boolean width = sourceBounds.width > sourceBounds.height;
		// calculate the new factor
		float factor = width?16 / (float)sourceBounds.width:16 / (float) sourceBounds.height;
		
		// calculate the dimensions
		int newW = width ? 16 : Math.round(factor * sourceBounds.width);
		int newH = width ? Math.round(factor * sourceBounds.height) : 16;
		
		Image target = new Image(source.getDevice(), newW, newH);
		GC targetGC = new GC(target);
		
		targetGC.drawImage(source, 0, 0, sourceBounds.width, sourceBounds.height, 0, 0, newW, newH);
		targetGC.dispose();
		source.dispose();
		return target;
	}
	
	@Override
	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
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
		listeners.remove(listener);
	}

	@Override
	public Image decorateImage(Image image, Object element) {
		IFile f = (IFile) element;
		if (isImageFile(f)) {
			Image result = images.get(f);
			if (result == null) {
				// seems that eclipse does not uses the prepareDecoration method, this works for me
				prepareDecoration(element, "didl");
				return image;
			}
			else {
				return result;
			}
		}
		else return image;
	}

	@Override
	public String decorateText(String text, Object element) {
		return text;
	}

	private boolean isImageFile(IFile f) {
		for (String ext : Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.FILE_EXTENSIONS).split(PreferenceConstants.FILE_EXTENSIONS_SEPARATOR)) {
			if (ext.equals(f.getFileExtension()) ) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean prepareDecoration(Object element, String originalText) {
		final IFile f = (IFile) element;
		if (isImageFile(f)) {
			if (images.containsKey(f)) {
				return true;
			}
			else {
				final Display d = Display.getDefault();
				UIJob prepareImage = new UIJob(d, "prepare Image") {
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						try {
							images.put(f, prepareImage(d, f));
							LabelProviderChangedEvent event = new LabelProviderChangedEvent(ImagePreviewDecorator.this, f);
							for (ILabelProviderListener x : listeners) {
								x.labelProviderChanged(event);
							}
							return Status.OK_STATUS;
						}
						catch (CoreException e) {
							return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "failed to load image preview", e);
						}
					}
				};
				prepareImage.setPriority(Job.DECORATE);
				prepareImage.schedule();
			}
			return false;
		}
		return true;
	}
}
