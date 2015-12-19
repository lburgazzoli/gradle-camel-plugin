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

import org.gradle.api.internal.file.collections.SimpleFileCollection
import org.gradle.api.tasks.JavaExec
import org.gradle.util.ConfigureUtil

/**
 * @author lburgazzoli
 */
class CamelRunTask extends JavaExec {
    public static final NAME = "camelRun"

    private Loader loader

    boolean trace
    String duration

    CamelRunTask() {
        this.trace = false
        this.duration = "-1"
        this.loader = null
    }

    public void spring(Closure closure) {
        ConfigureUtil.configure( closure, this.loader = new SpringLoader())
    }

    public void springJavaConfig(Closure closure) {
        ConfigureUtil.configure( closure, this.loader = new SpringJavaLoader())
    }

    public void blueprint(Closure closure) {
        ConfigureUtil.configure( closure, this.loader = new BlueprintLoader())
    }

    public void cdi(Closure closure) {
        ConfigureUtil.configure( closure, this.loader = new CdiLoader())
    }

    @Override
    public void exec() {

        def loaderClass = getMain()
        def loaderArgs = []
        def loaderCp = []

        if(! loaderClass) {
            if(this.trace) {
                loaderArgs.add('-t')
            }

            loaderArgs.add('-d')
            loaderArgs.add(duration)

            if(this.loader) {
                loaderClass = this.loader.main
                loaderCp = this.loader.classPath
                loaderArgs.addAll(this.loader.args)

            }
        }

        setMain(loaderClass)

        if(loaderArgs) {
            setArgs(getArgs() + loaderArgs)
        }
        if(loaderCp) {
            setClasspath(getClasspath() + new SimpleFileCollection(loaderCp))
        }

        super.exec();
    }

    // *************************************************************************
    //
    // *************************************************************************

    private abstract class Loader {
        private String mainClass
        private List<String> args
        private List<File> classPath

        protected Loader(String mainClass) {
            this.mainClass = mainClass
            this.args = []
            this.classPath = []
        }

        public String getMain() {
            return mainClass
        }

        public Collection<String> getArgs() {
            args.clear()
            fillArgs(args)

            return args
        }

        public Collection<File> getClassPath() {
            return classPath
        }

        public void additionalPath(File path) {
            classPath.add(path)
        }

        public void additionalPath(String path) {
            classPath.add(new File(path))
        }

        protected void addArg(Collection<String> args, String option, String value) {
            if(value) {
                args.add(option)
                args.add(value)
            }
        }

        protected void addArg(Collection<String> args, String option, Collection<String> values) {
            if(values) {
                args.add(option)
                args.addAll(values)
            }
        }

        protected void fillArgs(Collection<String> args) {
        }
    }


    private final class SpringLoader extends Loader {
        String applicationContextUri = null
        String fileApplicationContextUri = null

        public SpringLoader() {
            super('org.apache.camel.spring.Main')
        }

        @Override
        protected void fillArgs(Collection<String> args) {
            addArg(args, "-ac", applicationContextUri)
            addArg(args, "-fa", fileApplicationContextUri)
        }
    }

    private final class SpringJavaLoader extends Loader {
        private Set<String> configClasses = new LinkedHashSet<>()
        private Set<String> basePackages = new LinkedHashSet<>()

        public SpringJavaLoader() {
            super('org.apache.camel.spring.javaconfig.Main')
        }

        public void configClass(String configClass) {
            this.configClasses.add(configClass)
        }

        public void basePackage(String basePackage) {
            this.basePackages.add(basePackage)
        }

        @Override
        protected void fillArgs(Collection<String> args) {
            addArg(args, "-cc", configClasses)
            addArg(args, "-bp", basePackages)
        }
    }

    private final class BlueprintLoader extends Loader {
        String configAdminPid = null
        String configAdminFileName = null

        public BlueprintLoader() {
            super('org.apache.camel.test.blueprint.Main')
        }

        @Override
        protected void fillArgs(Collection<String> args) {
            addArg(args, "-pid", configAdminPid)
            addArg(args, "-pf", configAdminFileName)
        }
    }

    private final class CdiLoader extends Loader {
        public CdiLoader() {
            super('org.apache.camel.cdi.Main')
        }
    }
}
