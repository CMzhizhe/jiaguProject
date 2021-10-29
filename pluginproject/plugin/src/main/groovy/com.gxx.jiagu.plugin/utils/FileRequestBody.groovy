package com.gxx.jiagu.plugin.utils

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Okio
import okio.Sink

/**
 * 文件上传监听
 */
public class FileRequestBody extends RequestBody {
    private RequestBody mRequestBody;
    private LoadingListener mLoadingListener;
    private long mContentLength;

    public FileRequestBody(RequestBody requestBody, LoadingListener loadingListener) {
        mRequestBody = requestBody;
        mLoadingListener = loadingListener;
    }

    //文件的总长度
    @Override
    public long contentLength() {
        try {
            if (mContentLength == 0)
                mContentLength = mRequestBody.contentLength();
            return mContentLength;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        ByteSink byteSink = new ByteSink(sink);
        BufferedSink mBufferedSink = Okio.buffer(byteSink);
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }


    class ByteSink extends ForwardingSink {
        //已经上传的长度
        private long mByteLength = 0L;

        ByteSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            mByteLength += byteCount;
            mLoadingListener.onProgress(mByteLength, contentLength());
        }
    }

    public interface LoadingListener {
        void onProgress(long currentLength, long contentLength);
    }
}