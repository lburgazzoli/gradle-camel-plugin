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

import org.gradle.util.ConfigureUtil

/**
 * @author lburgazzoli
 */
class Api {
    private final List<ApiMethodDescriptor> descriptors = []

    public String apiName
    public String proxyClass

    public Api() {
        this.apiName = null
        this.proxyClass = null
        this.descriptors = []
    }

    void fromJavadoc(Closure closure) {
        descriptors.addAll(ConfigureUtil.configure(closure, new ApiJavadoc()).descriptors)
    }

    void method(Closure closure) {
        descriptors.add(ConfigureUtil.configure(closure, new ApiMethodDescriptor()))
    }

    public List<ApiMethodDescriptor> getDescriptos() {
        return Collections.unmodifiableList(descriptors)
    }
}
