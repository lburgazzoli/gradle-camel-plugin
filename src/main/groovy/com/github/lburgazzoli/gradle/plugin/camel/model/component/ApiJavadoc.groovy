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

package com.github.lburgazzoli.gradle.plugin.camel.model.component

import java.util.regex.Pattern

/**
 * @author lburgazzoli
 */
class ApiJavadoc {
    private final List<Pattern> excludePackages = [DEFAULT_EXCLUDE_PACKAGES ]
    private final List<Pattern> excludeClasses = []
    private final List<Pattern> includeMethods = []
    private final List<Pattern> excludeMethods = []

    private boolean includeStaticMethods = false

    public void setExcludePackages(List<String> packages) {
        addPatters(excludePackages, packages)
    }

    public Collection<Pattern> getExcludePackages() {
        return Collections.unmodifiableList(excludePackages)
    }

    public void setExcludeClasses(List<String> classes) {
        addPatters(excludeClasses, classes)
    }

    public Collection<Pattern> getExcludeClasses() {
        return Collections.unmodifiableList(excludeClasses)
    }

    public void setIncludeMethods(List<String> methods) {
        addPatters(includeMethods, methods)
    }

    public Collection<Pattern> getIncludeMethods() {
        return Collections.unmodifiableList(includeMethods)
    }

    public void setExcludeMethods(List<String> methods) {
        addPatters(excludeMethods, methods)
    }

    public Collection<Pattern> getExcludeMethods() {
        return Collections.unmodifiableList(excludeMethods)
    }

    void setIincludeStaticMethods(boolean includeStaticMethods) {
        this.includeStaticMethods = includeStaticMethods
    }

    boolean getIincludeStaticMethods() {
        return this.includeStaticMethods
    }

    private void addPatters(List<Pattern> patterns, String strings) {
        strings.each {
            patterns.add(Pattern.compile(it))
        }
    }
}
