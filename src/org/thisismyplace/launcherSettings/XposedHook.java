package org.thisismyplace.launcherSettings;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XposedHook implements IXposedHookLoadPackage {

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if(!lpparam.packageName.equals("com.android.launcher3"))
			return;
			
		findAndHookMethod("com.android.launcher3.AllAppsList", lpparam.classLoader, "add", "com.android.launcher3.AppInfo", new XC_MethodHook() {
			@Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				Object appInfo = param.args[0];
				String appInfoString = "" + appInfo;//GHETTO, Need to 
				String appTitle = appInfoString.substring(22, appInfoString.indexOf(" id"));
				if(appTitle.equals("AdAway")) {
					XposedBridge.log(param.method.getName());
					param.setResult(null);
				}
            }
			@Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				
			}
		});
	}

}
