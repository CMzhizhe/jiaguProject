package com.gxx.jiagu.plugin
import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.gxx.jiagu.plugin.bean.JiaGuUploadExtension
import com.gxx.jiagu.plugin.task.PGYUploadTask
import com.gxx.jiagu.plugin.task.ReinforceTask
import org.gradle.api.Action
import org.gradle.api.DomainObjectSet
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

//https://github.com/keepLove/android-jiagu-plugin 加固
//https://blog.csdn.net/android_jianbo/article/details/103801481 蒲公英
public class UploadJiaGuPlugin implements Plugin<Project> {

    //project的扩展名字
    public static final String PLUGIN_EXTENSION_NAME = "uploadJiaGuHelperJava";
    public static final String ANDROID_EXTENSION_NAME = "android";

    @Override
    public void apply(Project project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw new GradleException("无法在非android application插件中使用360加固")
        }
        //创建project的扩展
        JiaGuUploadExtension customExtension = project.getExtensions().create(PLUGIN_EXTENSION_NAME, JiaGuUploadExtension.class);
        //Project 配置结束后调用 .配置阶段
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project myProject) {
                DomainObjectSet<ApplicationVariant> appVariants = ((AppExtension) myProject
                        .getExtensions().findByName(ANDROID_EXTENSION_NAME)).getApplicationVariants();
                //这里，可以实现多渠道的打包
                for (ApplicationVariant variant : appVariants) {
                    String buildType = "assemble${variant.flavorName}${variant.buildType.name}"
                    //只监听 official 完成
                    if (buildType.equalsIgnoreCase("assembleofficialrelease")) {
                        String variantName = variant.getName();//officialRelease
                        //创建TASK
                        PGYUploadTask uploadTask = project.getTasks().create("uploadJavaFor" + variantName, PGYUploadTask.class);
                        uploadTask.init(variant, project);
                        //创建TASK
                        ReinforceTask reinforceTask = project.getTasks().create("reinforceFor" + variantName, ReinforceTask.class);
                        reinforceTask.init(variant,project);

                        JiaGuUploadExtension extension = JiaGuUploadExtension.getConfig(project)

                        if (extension.jiaguEnable && extension.uploadFileEnable){//加固 && 上传文件同时满足
                            println("开启加固 && 文件上传")
                            if (variant.hasProperty("assembleProvider")) {
                                Task officialReleaseTask = project.tasks.findByName(variant.assembleProvider.get().name)//assembleOfficialRelease
                                reinforceTask.dependsOn(officialReleaseTask);
                                uploadTask.dependsOn(reinforceTask);
                            } else {
                                reinforceTask.dependsOn(variant.getAssembleProvider().get());
                                uploadTask.dependsOn(reinforceTask);
                            }
                        }else if (extension.jiaguEnable && !extension.uploadFileEnable){
                            println("开启加固")
                            if (variant.hasProperty("assembleProvider")) {
                                Task officialReleaseTask = project.tasks.findByName(variant.assembleProvider.get().name)//assembleOfficialRelease
                                reinforceTask.dependsOn(officialReleaseTask);
                            } else {
                                reinforceTask.dependsOn(variant.getAssembleProvider().get());
                            }
                        }else if (!extension.jiaguEnable&& extension.uploadFileEnable){
                            println("开启文件上传")
                            if (variant.hasProperty("assembleProvider")) {
                                Task officialReleaseTask = project.tasks.findByName(variant.assembleProvider.get().name)//assembleOfficialRelease
                                uploadTask.dependsOn(officialReleaseTask);
                            } else {
                                uploadTask.dependsOn(variant.getAssembleProvider().get());
                            }
                        }
                        break;
                    }
                }
            }
        });
    }
}

