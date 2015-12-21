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

/**
 * @author lburgazzoli
 */
class ApiJavadoc {
    private List<ApiMethodDescriptor> descriptors

    public ApiJavadoc() {
        descriptors = []
    }

    void filter(Closure closure) {
        descriptors.removeAll(closure)
    }

    void map(Closure closure) {
        descriptors.forEach(closure)
    }

    protected List<ApiMethodDescriptor> getDescriptors() {
        return this.descriptors
    }
}
