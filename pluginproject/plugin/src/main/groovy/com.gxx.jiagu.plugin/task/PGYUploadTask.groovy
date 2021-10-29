package com.gxx.jiagu.plugin.task

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.gxx.jiagu.plugin.bean.JiaGuUploadExtension
import com.gxx.jiagu.plugin.utils.FileRequestBody
import com.gxx.jiagu.plugin.UploadJiaGuPlugin
import okhttp3.Call
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

public class PGYUploadTask extends DefaultTask {

    private BaseVariant mVariant;
    private Project mTargetProject;

    public static class PGYRequest {
        public String uKey;
        public String apiKey;
        public String requestUrl;
    }

    public void init(BaseVariant variant, Project project) {
        this.mVariant = variant;
        this.mTargetProject = project;
        setDescription("upload to pgy");
        setGroup(UploadJiaGuPlugin.PLUGIN_EXTENSION_NAME);
    }

    @TaskAction
    public void uploadToPGY() {
        JiaGuUploadExtension extension = JiaGuUploadExtension.getConfig(mTargetProject);
        PGYRequest request = new PGYRequest();
        request.apiKey = extension.apiKey;
        request.uKey = extension.uKey;
        request.requestUrl = extension.httpRequest;
        File apkDir = extension.outputFile;
        if (apkDir == null || !apkDir.exists()) {
            upload(request);
        } else {
            File[] files = apkDir.listFiles();
            if (files != null && files.length > 0) {
                upload(request.uKey, request.apiKey, files[files.length-1],request.requestUrl);
            }else{
                upload(request);
            }
        }
    }

    private void upload(PGYRequest request) {
        for (BaseVariantOutput output : mVariant.getOutputs()) {
            File file = output.getOutputFile();
            if (file == null || !file.exists()) {
                throw new GradleException("apk file is not exist!");
            }
            upload(request.uKey, request.apiKey, file,request.requestUrl);
        }
    }


    //https://blog.csdn.net/android_jianbo/article/details/103801481
    //https://www.jianshu.com/p/d1ef164e6b8f
    private void upload(String ukey, String apiKey, File apkFile,String requestUrl) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //add part
        bodyBuilder.addFormDataPart("uKey", ukey);
        bodyBuilder.addFormDataPart("_api_key", apiKey);
        RequestBody requestFileBody = RequestBody.create(MediaType.parse("multipart/form-data"), apkFile);
        FileRequestBody fileRequestBody = new FileRequestBody(requestFileBody, new FileRequestBody.LoadingListener() {
            @Override
            void onProgress(long currentLength, long contentLength) {
                int percenter = (int) (currentLength * 100 / contentLength)
                println("上传进度" + percenter + "%")
            }
        })
        MultipartBody.Part filePartBody = MultipartBody.Part.createFormData("file", apkFile.name, fileRequestBody);
        bodyBuilder.addPart(filePartBody)
        //request
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(bodyBuilder.build())
                .build();
        OkHttpClient client = new OkHttpClient();
        Call requestCall = client.newCall(request);
        Response response = requestCall.execute();
        String result = response.body().string();
        println("结果:" + result)
    }
}

