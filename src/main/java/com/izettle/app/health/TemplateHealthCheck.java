package com.izettle.app.health;

import com.codahale.metrics.health.HealthCheck;

public class TemplateHealthCheck extends HealthCheck {

    private final String template;

    public TemplateHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(template, "test_user");
        if (!saying.contains("test_user")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }
}
