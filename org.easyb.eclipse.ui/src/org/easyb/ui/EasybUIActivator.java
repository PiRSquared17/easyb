package org.easyb.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EasybUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.easyb.ui";

	// The shared instance
	private static EasybUIActivator plugin;

	//
	/**
	 * The constructor
	 */
	public EasybUIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EasybUIActivator getDefault() {
		return plugin;
	}
	
	public static void Log(String message,Exception ex){
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, 0,message,ex));
	}
	
	public static void Log(String message){
		getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID,message));
	}
	
	public static void Log(IStatus status){
		getDefault().getLog().log(status);
	}

}
