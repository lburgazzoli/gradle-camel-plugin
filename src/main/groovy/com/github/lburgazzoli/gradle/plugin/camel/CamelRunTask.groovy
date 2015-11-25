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

import org.gradle.api.tasks.JavaExec
import org.gradle.util.ConfigureUtil

/**
 * @author lburgazzoli
 */
class CamelRunTask extends JavaExec {
    public static final TASK_NAME = "camelRun"

    private SubsystemConfig config

    boolean trace
    String duration
    //boolean useCdi


    CamelRunTask() {
        //this.useCdi = false

        this.trace = false
        this.duration = "-1"
        this.config = null
    }

    public void springConfig(Closure closure) {
        ConfigureUtil.configure( closure, this.config = new SpringConfig())
    }

    public void springJavaConfig(Closure closure) {
        ConfigureUtil.configure( closure, this.config = new SpringJavaConfig())
    }

    public void blueprintConfig(Closure closure) {
        ConfigureUtil.configure( closure, this.config = new BlueprintConfig())
    }

    public void cdiConfig(Closure closure) {
        ConfigureUtil.configure( closure, this.config = new CdiConfig())
    }

    @Override
    public void exec() {

        def subs = null
        def appClass = main()
        def appArgs = []

        if(!appClass) {
            if(this.trace) {
                appArgs.add('-t')
            }

            appArgs.add('-d')
            appArgs.add(duration)

            if(this.config) {
                appClass = this.config.main()
                appArgs.addAll(this.config.args())
            }
        }

        setMain(appClass)
        setArgs(getArgs() + appArgs)

        super.exec();
    }

    // *************************************************************************
    //
    // *************************************************************************

    private abstract class SubsystemConfig {
        private String main = null;
        private List<String> args = []

        protected SubsystemConfig(String main) {
            this.main = main
        }

        public String main() {
            return main
        }

        public Collection<String> args() {
            args.clear()
            fillArgs(args)

            return args
        }

        protected addArg(Collection<String> args, String option, String value) {
            if(value) {
                args.add(option)
                args.add(value)
            }
        }

        protected addArg(Collection<String> args, String option, Collection<String> values) {
            if(values) {
                args.add(option)
                args.addAll(values)
            }
        }

        protected abstract void fillArgs(Collection<String> args)
    }

    private final class SpringConfig extends SubsystemConfig {
        String applicationContextUri = null
        String fileApplicationContextUri = null

        public SpringConfig() {
            super('org.apache.camel.spring.Main')
        }

        @Override
        protected void fillArgs(Collection<String> args) {
            addArg(args, "-ac", applicationContextUri)
            addArg(args, "-fa", fileApplicationContextUri)
        }
    }

    private final class SpringJavaConfig extends SubsystemConfig {
        private Set<String> configClasses = new LinkedHashSet<>()
        private Set<String> basePackages = new LinkedHashSet<>()

        public SpringJavaConfig() {
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

    private final class BlueprintConfig extends SubsystemConfig {
        String configAdminPid = null
        String configAdminFileName = null

        public BlueprintConfig() {
            super('org.apache.camel.test.blueprint.Main')
        }

        @Override
        protected void fillArgs(Collection<String> args) {
            addArg(args, "-pid", configAdminPid)
            addArg(args, "-pf", configAdminFileName)
        }
    }

    private final class CdiConfig extends SubsystemConfig {

        public CdiConfig() {
            super('org.apache.camel.cdi.Main')
        }

        @Override
        protected void fillArgs(Collection<String> args) {
        }
    }
}
