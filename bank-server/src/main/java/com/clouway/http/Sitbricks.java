package com.clouway.http;

import com.google.sitebricks.SitebricksModule;

/**
 * Created by emil on 14-9-24.
 */
public class Sitbricks extends SitebricksModule {

    @Override
    protected void configureSitebricks() {
        scan(LoginCtrl.class.getPackage());
    }
}
