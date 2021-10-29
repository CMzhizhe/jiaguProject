package com.gxx.jiaguapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "MainActivity";
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val path = FileSDCardUtil.getInstance().getSandboxConfigDirsCacheDisk(
            MainActivity@ this,
            "Test"
        ) + File.separator +"app-uploadRelease.apk";
        val file = File(path);

        if (file.exists()){
            upLoad("d271cbbef23a76fcbd42e0acecbe562e", "6d0dd9894139a5bfe87683aeb3a848f3", file);
        }
    }

    fun upLoad(ukey: String, apiKey: String, apkFile: File){
        val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM);
        bodyBuilder.addFormDataPart("uKey", ukey)
        bodyBuilder.addFormDataPart("_api_key", apiKey);
        val requestFileBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), apkFile);
        val fileRequestBody = FileRequestBody(
            requestFileBody,
            object : FileRequestBody.LoadingListener {
                override fun onProgress(currentLength: Long, contentLength: Long) {
                    var percenter = (currentLength * 100 / contentLength).toInt()
                    Log.e(TAG, "进度" + percenter + "%")
                }
            })
        val partBody = MultipartBody.Part.createFormData("file", apkFile.name, fileRequestBody);
        bodyBuilder.addPart(partBody)
        val request: Request =
                Request.Builder()
                .url("http://upload.pgyer.com/apiv1/app/upload")
                .post(bodyBuilder.build())
                .build()
        val client = OkHttpClient();
        val requestCall = client.newCall(request)
        requestCall.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "上传失败")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.e(TAG, "上传成功")
            }
        })
    }

}