/*
 * Copyright 2015, Luca Burgazzoli and contributors as indicated by the @author tags
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lburgazzoli.gradle.plugin.camel

import com.github.lburgazzoli.gradle.plugin.camel.model.component.ApiComponent
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

import java.util.regex.Pattern

/**
 * @author lburgazzoli
 */
class CamelPluginExtension {
    public static final String NAME = 'camel'

    private static final String  DEFAULT_EXCLUDE_PACKAGES = "javax?\\.lang.*";
    private static final Pattern RAW_ARGTYPES_PATTERN = Pattern.compile("\\s*([^<\\s,]+)\\s*(<[^>]+>)?\\s*,?");

    private final List<ApiComponent> components

    public CamelPluginExtension() {
        this.components = []
    }

    void component(Closure closure) {
        components << ConfigureUtil.configure(closure, new ApiComponent())
    }

    public List<ApiComponent> getComponents() {
        return components.asImmutable()
    }

    // *************************************************************************
    // Helpers
    // *************************************************************************

    public static CamelPluginExtension lookup(Project project) {
        return project.extensions.findByName(NAME) as CamelPluginExtension
    }

    public static CamelPluginExtension create(Project project) {
        return project.extensions.create( NAME, CamelPluginExtension, project )
    }
}
