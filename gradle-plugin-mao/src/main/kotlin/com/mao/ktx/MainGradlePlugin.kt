package com.mao.ktx

import org.gradle.api.Plugin
import org.gradle.api.Project

class MainGradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("使用独立项目创建的插件")
    }

}