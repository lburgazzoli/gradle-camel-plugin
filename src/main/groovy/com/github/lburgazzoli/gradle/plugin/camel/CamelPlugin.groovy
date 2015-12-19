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

import com.github.lburgazzoli.gradle.plugin.camel.task.CamelApiComponentTask
import com.github.lburgazzoli.gradle.plugin.camel.task.CamelRunTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention

/**
 * @author lburgazzoli
 */
class CamelPlugin implements Plugin<Project> {
    static {
        // set Java AWT to headless before using Swing HTML parser
        System.setProperty("java.awt.headless", "true");
    }

    @Override
    void apply(Project project) {
        project.getPluginManager().apply(JavaPlugin.class);

        CamelPluginExtension extension = CamelPluginExtension.create(project)

        addCamelRunTask(project)
        addCamelApiFrameworkTask(project)
    }

    private void addCamelRunTask(Project project) {
        def javaConvention = project.convention.getPlugin(JavaPluginConvention.class);
        def task = project.tasks.create(CamelRunTask.NAME, CamelRunTask.class)

        task.dependsOn.add(project.getTasks().findByName('jar'))
        task.description = "Run the project"
        task.group = "camel"
        task.classpath = javaConvention.sourceSets.findByName("main").runtimeClasspath
    }

    private void addCamelApiFrameworkTask(Project project) {
        def task = project.tasks.create(CamelApiComponentTask.NAME, CamelRunTask.class)
        task.description = "Generate Component"
        task.group = "camel"
    }
}
