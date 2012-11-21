package net.redrezo.eclipse.tooling.abbreviationdecorator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class AbbreviationDecorator implements ILabelDecorator {

	private List<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();
	
	@Override
	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}

	@Override
	public Image decorateImage(Image image, Object element) {
		return image;
	}

	@Override
	public String decorateText(String text, Object element) {
		System.err.println("DECORATE " + text + " / " + element.getClass());
		if (element instanceof IAdaptable) {
			if (((IAdaptable)element).getAdapter(IProject.class) != null) {
				System.err.println("project!");
				// TODO apply configured mappings here
				Preferences prefs = ConfigurationScope.INSTANCE.getNode("net.redrezo.eclipse.tooling.abbreviationdecorator.preferences").node("mappings"); 
				try {
					for (String key : prefs.keys()) {
						Preferences node = prefs.node(key);
						
						String regex = node.get("regex", null);
						String replacement = node.get("replacement", null);
						if (regex != null && replacement != null) {
							text.replaceFirst(regex, replacement);
						}
						
						
					}
				} catch (BackingStoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return text.replaceFirst("net\\.redrezo\\.eclipse\\.tooling\\.", "n.r.e.t.");
			}
			
		}
		return text;
	}

}
