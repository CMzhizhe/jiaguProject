package com.gxx.jiagu.plugin.task

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.dsl.SigningConfig
import com.gxx.jiagu.plugin.UploadJiaGuPlugin
import com.gxx.jiagu.plugin.bean.JiaGuUploadExtension
import com.gxx.jiagu.plugin.utils.Utils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import org.jetbrains.annotations.NotNull

/**
 * 加固task
 */
public class ReinforceTask extends DefaultTask {
    private BaseVariant mVariant;
    private Project mTargetProject;

    //加固相关的信息
    public static class ReinforceModel {
        //加固包的位置(jiagu.jar) 及登录账号密码
        public File reinforceJarFile;
        public String reinforceUsername;
        public String reinforcePassword;

        //签名文件 密码等信息
        public File signStoreFile;
        public String signPassword;
        public String signKeyAlias;
        public String signKeyPassword;

        //打包apk路径及加固后apk的路径
        public File inputApkFile;
        public File outputApkFile;
    }

    public void init(BaseVariant variant, Project project) {
        this.mVariant = variant;
        this.mTargetProject = project;
        setDescription("reinforce for apk");
        setGroup(UploadJiaGuPlugin.PLUGIN_EXTENSION_NAME);
    }

    @TaskAction
    public void reinforceApk() {
        System.out.println("============== 开始加固 ==============");

        ReinforceModel request = initReinforceModel();

        String loginCmdData = "java -jar %s -login %s %s";
        String loginExec = String.format(loginCmdData, request.reinforceJarFile, request.reinforceUsername, request.reinforcePassword);
        //去登录
        Utils.exec(loginExec);
        System.out.println("登录360成功");

        System.out.println("开始签名");
        String signCmdData = "java -jar %s -importsign %s %s %s %s";
        String signExec = String.format(signCmdData, request.reinforceJarFile, request.signStoreFile, request.signPassword, request.signKeyAlias, request.signKeyPassword);

        //导入签名
        Utils.exec(signExec);
        System.out.println("签名成功");
        //自动签名
        String suffixCmd = " -autosign ";

        if (!request.outputApkFile.exists()) {
            request.outputApkFile.mkdirs();
        }
        System.out.println("开始加固");
        //获取输入文件目录，输出文件目录
        //jiagu.jar ,input apk file path, output apk dir
        String reinforceCmdData = "java -jar %s -jiagu %s %s";
        String reinforceExec = String.format(reinforceCmdData, request.reinforceJarFile, request.inputApkFile, request.outputApkFile);

        println("360执行加固命令=" + (reinforceExec + suffixCmd))

        //reinforce
        String jiaguResult = Utils.exec(reinforceExec + suffixCmd);

        System.out.println("=========exe APK JiaGu Result : " + jiaguResult);
        System.out.println("加固后文件路径=" + request.outputApkFile.getAbsolutePath());
    }

    /**
     * 初始化加固基本信息
     * @return
     */
    @NotNull
    private ReinforceModel initReinforceModel() {
        JiaGuUploadExtension extension = JiaGuUploadExtension.getConfig(mTargetProject);

        ReinforceModel request = new ReinforceModel();
        request.outputApkFile = extension.outputFile;
        request.reinforceJarFile = extension.reinforceJarFile;
        request.reinforceUsername = extension.reinforceUsername;
        request.reinforcePassword = extension.reinforcePassword;

        //获取签名配置信息
        NamedDomainObjectContainer<SigningConfig> signingConfigs = ((AppExtension) mTargetProject
                .getExtensions().findByName(UploadJiaGuPlugin.ANDROID_EXTENSION_NAME)).getSigningConfigs();

        if (signingConfigs == null) {
            throw new IllegalArgumentException("please config your sign info.");
        }

        for (SigningConfig config : signingConfigs) {
            request.signStoreFile = config.getStoreFile();
            request.signPassword = config.getStorePassword();
            request.signKeyAlias = config.getKeyAlias();
            request.signKeyPassword = config.getKeyPassword();
        }

        for (BaseVariantOutput output : mVariant.getOutputs()) {
            request.inputApkFile = output.getOutputFile();
            if (request.inputApkFile!=null){
                println("文件路径="+ request.inputApkFile.getAbsolutePath())
            }
            if (request.inputApkFile == null || !request.inputApkFile.exists()) {
                throw new GradleException("apk file is not exist!");
            }
        }

        if (request.signStoreFile == null || !request.signStoreFile.exists()) {
            throw new IllegalArgumentException("please config your sign info.");
        }
        return request;
    }
    
}