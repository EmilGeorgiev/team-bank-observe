package com.clouway.http;

import com.clouway.core.*;
import com.google.inject.servlet.ServletModule;

/**
 * Created by emil on 14-9-24.
 */
public class HttpModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bind(IdGenerator.class).to(SessionIdGenerator.class);
        bind(SiteMap.class).to(LabelMap.class);
    }
}
