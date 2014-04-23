package org.thisismyplace.launcherSettings;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XposedHook implements IXposedHookLoadPackage {

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		final String[] hiddenApps = {"AdAway", "Clock", "Dev Tools", "Downloads", "DSP Manager",
				"Hacker's Keyboard", "magicPasswd", "Minuum Settings", "Movie Studio",
				"OmniSwitch", "Search", "Text Edit", "Voice Dialler"};
		
		if(lpparam.packageName.equals("com.android.launcher3")) {
			findAndHookMethod("com.android.launcher3.AllAppsList", lpparam.classLoader,
					"add", "com.android.launcher3.AppInfo", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					Object appInfo = param.args[0];
					String appInfoString = "" + appInfo;//GHETTO, Need to fix
					String appTitle = appInfoString.substring(22, appInfoString.indexOf(" id"));
					for(int i = 0; i < hiddenApps.length; i++) {
						if(appTitle.equals(hiddenApps[i]))
							param.setResult(null);//Stops the procedure being called
					}
            	}
			});
		} else if(lpparam.packageName.equals("com.android.launcher2") ||
				lpparam.packageName.equals("com.cyanogenmod.trebuchet")) {
			findAndHookMethod(lpparam.packageName + ".AllAppsList", lpparam.classLoader,
					"add", lpparam.packageName + ".ApplicationInfo", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					Object appInfo = param.args[0];
					String appInfoString = "" + appInfo;//GHETTO, Need to fix
					String appTitle = appInfoString.substring(22, appInfoString.indexOf(")"));
					for(int i = 0; i < hiddenApps.length; i++) {
						if(appTitle.equals(hiddenApps[i]))
							param.setResult(null);//Stops the procedure being called
					}
            	}
			});
		}
	}
}
