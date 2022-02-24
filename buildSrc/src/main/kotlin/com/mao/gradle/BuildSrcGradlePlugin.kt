package com.mao.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildSrcGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("使用BuildSrc方式创建的插件")
    }

}