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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention

/**
 * @author lburgazzoli
 */
class CamelPlugin implements Plugin<Project> {
    public static final String PLUGIN_ID = 'com.github.lburgazzoli.camel'
    public static final String EXTENSION_NAME = 'camel'
    public static final String JAR_TASK = 'jar'

    @Override
    void apply(Project project) {
        project.getPluginManager().apply(JavaPlugin.class);

        //CamelPluginExtension extension = project.extensions.create( EXTENSION_NAME, CamelPluginExtension, project )

        addCamelRunTask(project)
    }

    private void addCamelRunTask(Project project) {
        final JavaPluginConvention javaConvention = project.convention.getPlugin(JavaPluginConvention.class);
        //final Jar jarTask = (Jar) project.getTasks().findByName(JAR_TASK);
        final CamelRunTask run = project.tasks.create(CamelRunTask.TASK_NAME, CamelRunTask.class)

        //run.dedependsOn.add(jarTask)
        run.description = "Run the project"
        run.group = "application"
        run.classpath = javaConvention.sourceSets.findByName("main").runtimeClasspath
    }
}
