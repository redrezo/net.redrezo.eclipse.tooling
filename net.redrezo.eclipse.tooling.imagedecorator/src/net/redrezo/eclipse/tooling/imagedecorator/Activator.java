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

	private IPreferenceStore prefStore;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		context = bundleContext;
		plugin = this;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		context = null;
		plugin = null;
		prefStore = null;
	}

	public static Activator getDefault() {
		return plugin;
	}

	public IPreferenceStore getPreferenceStore() {
		if (prefStore == null) {
			prefStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
					PLUGIN_ID);
		}
		return prefStore;
	}
}
