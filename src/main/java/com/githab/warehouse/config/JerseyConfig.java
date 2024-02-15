package com.githab.warehouse.config;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages("com/githab/warehouse/resources");
        packages("com/githab/warehouse/config");

        register(MultiPartFeature.class);
        register(JerseyExceptionMapper.class);
    }
}

