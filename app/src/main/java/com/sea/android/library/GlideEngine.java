package com.sea.android.library;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.huantansheng.easyphotos.engine.ImageEngine;

public class GlideEngine implements ImageEngine {

    private static GlideEngine instance = null;

    private GlideEngine() {

    }

    public static GlideEngine getInstance() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }

    // 安卓10推荐uri，并且path的方式不再可用
    @Override
    public void loadPhoto(@NonNull Context context, @NonNull Uri uri, @NonNull ImageView imageView) {
        DrawableTransitionOptions options = withCrossFade();
        Glide.with(context).load(uri).transition(options).into(imageView);
    }

    /**
     * 加载gif动图图片到ImageView, gif动图不动
     */
    @Override
    public void loadGifAsBitmap(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {
        Glide.with(context).asBitmap().load(gifUri).into(imageView);
    }

    /**
     * 加载gif动图到ImageView, gif动图动
     */
    @Override
    public void loadGif(@NonNull Context context, @NonNull Uri gifUri, @NonNull ImageView imageView) {
        DrawableTransitionOptions options = withCrossFade();
        Glide.with(context).asGif().load(gifUri).transition(options).into(imageView);
    }

    /**
     * 获取图片加载框架中的缓存Bitmap, 不用拼图功能可以直接返回null
     */
    @Override
    public Bitmap getCacheBitmap(@NonNull Context context, @NonNull Uri uri, int width, int height) throws Exception {
        return Glide.with(context).asBitmap().load(uri).submit(width, height).get();
    }

}