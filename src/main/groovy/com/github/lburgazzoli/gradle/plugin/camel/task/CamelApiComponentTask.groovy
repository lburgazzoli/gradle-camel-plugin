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
package com.github.lburgazzoli.gradle.plugin.camel.task
import com.github.lburgazzoli.gradle.plugin.camel.CamelPluginExtension
import org.apache.commons.lang3.StringUtils
import org.ccil.cowan.tagsoup.Parser
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.tasks.TaskAction

import java.util.jar.JarFile
/**
 * @author lburgazzoli
 */
class CamelApiComponentTask extends DefaultTask {
    public static final String NAME = "generateComponent"

    private CamelPluginExtension pluginExtension;

    @TaskAction
    def run() {
        extension.components.each { component ->

            println("$component.componentName")

            component.apis.each { api ->
                def node = getJavadocContent(component.configuration, api.type)
                if(node) {
                    dumpNodes(node, 0)
                }
            }


            /*
            component.apis.each { api ->
                println("  $api.name")
                api.descriptors.each { descriptor ->
                    println("    $descriptor.name")
                    descriptor.arguments.each { arg ->
                        println("      $arg.name ($arg.type)")
                    }
                }
            }
            */
        }
    }

    def getJavadocContent(Configuration configuration, String className) {
        String path = className.replace('.', '/') + ".html"

        for(ResolvedArtifact artifact : configuration.resolvedConfiguration.resolvedArtifacts) {
            if(artifact.classifier?.equalsIgnoreCase("javadoc")) {
                def jarFile = new JarFile(artifact.file)
                def entry = jarFile.getJarEntry(path)

                if(entry) {
                    return new XmlSlurper(new Parser()).parse(jarFile.getInputStream(entry))
                }
            }
        }

        return null
    }

    def dumpNodes(def root, int depth) {
        println "${StringUtils.leftPad('.', depth)}  ${root.name()} ${root.text()}"

        root.children().each {
            dumpNodes(it, depth + 2)
        }

    }

    def CamelPluginExtension getExtension() {
        // Don't keep looking it up...
        if (this.pluginExtension == null) {
            this.pluginExtension = CamelPluginExtension.lookup(project)
        }

        return this.pluginExtension;
    }
}
