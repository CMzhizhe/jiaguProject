package com.gxx.jiagu.plugin.bean

import org.gradle.api.Project

public class JiaGuUploadExtension {
    public String uKey;
    public String apiKey;
    public String appName;

    public String httpRequest;//请求地址
    public boolean jiaguEnable;//加固是否可以用，默认是不可以用
    public boolean uploadFileEnable;//上传是否可以用，默认是不可以用

    //==========加固相关的信息
    //指向加固的jar包
    public File reinforceJarFile;
    //登陆用户名
    public String reinforceUsername;
    //登陆密码
    public String reinforcePassword;
    //输出apk的目录
    public File outputFile;

    public JiaGuUploadExtension(){}

    JiaGuUploadExtension(String uKey, String apiKey, String appName, File reinforceJarFile, String reinforceUsername, String reinforcePassword, File outputFile) {
        this.uKey = uKey
        this.apiKey = apiKey
        this.appName = appName
        this.reinforceJarFile = reinforceJarFile
        this.reinforceUsername = reinforceUsername
        this.reinforcePassword = reinforcePassword
        this.outputFile = outputFile
    }

    public JiaGuUploadExtension(String appName, String uKey, String apiKey){
        this.uKey = uKey;
        this.apiKey = apiKey;
        this.appName = appName;
    }


    public static JiaGuUploadExtension getConfig(Project project) {
        JiaGuUploadExtension extension = project.getExtensions().findByType(JiaGuUploadExtension.class);
        if (extension == null) {
            extension = new JiaGuUploadExtension();
        }
        return extension;
    }

    JiaGuUploadExtension(String uKey, String apiKey, String appName, String httpRequest, boolean jiaguEnable, boolean uploadFileEnable, File reinforceJarFile, String reinforceUsername, String reinforcePassword, File outputFile) {
        this.uKey = uKey
        this.apiKey = apiKey
        this.appName = appName
        this.httpRequest = httpRequest
        this.jiaguEnable = jiaguEnable
        this.uploadFileEnable = uploadFileEnable
        this.reinforceJarFile = reinforceJarFile
        this.reinforceUsername = reinforceUsername
        this.reinforcePassword = reinforcePassword
        this.outputFile = outputFile
    }
}

