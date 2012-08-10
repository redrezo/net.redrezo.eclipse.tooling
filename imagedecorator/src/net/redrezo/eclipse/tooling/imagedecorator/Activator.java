package net.redrezo.eclipse.tooling.imagedecorator;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "net.redrezo.eclipse.tooling.imagedecorator";

	private static Activator plugin;
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		context = bundleContext;
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		context = null;
		plugin = null;
	}

	public static Activator getDefault() {
		// TODO will this project become an osgi plugin?
		if (plugin != null) {
		return plugin;
		}
		else {
			return new Activator();
		}
	}

	public IPreferenceStore getPreferenceStore() {
		return new ScopedPreferenceStore(InstanceScope.INSTANCE, PLUGIN_ID);
	}
}
