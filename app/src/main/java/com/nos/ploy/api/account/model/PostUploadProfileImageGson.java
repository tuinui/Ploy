package com.nos.ploy.api.account.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saran on 18/12/2559.
 */

public class PostUploadProfileImageGson {

    @SerializedName("userId")
    private Long userId;
    @SerializedName("imgBody")
    private List<ImageBody> imageBase64List = new ArrayList<>();


    private PostUploadProfileImageGson(Long userId, List<ImageBody> base64Images) {
        this.userId = userId;
        this.imageBase64List = base64Images;
    }

    private PostUploadProfileImageGson(Long userId) {
        this.userId = userId;

    }

    public static PostUploadProfileImageGson with(Long userId) {
        return new PostUploadProfileImageGson(userId);
    }

    public PostUploadProfileImageGson createNew(List<String> imageBase64List) {
        this.imageBase64List.clear();
        for (String base64 : imageBase64List) {
            this.imageBase64List.add(new ImageBody(base64));
        }
        return this;
    }

    public PostUploadProfileImageGson create(List<ImageBody> body){
        this.imageBase64List.clear();
        this.imageBase64List.addAll(body);
        return this;
    }

    public static class ImageBody {
        @SerializedName("imgId")
        private Long imgId;
        @SerializedName("imageBase64")
        private String imageBase64;

        public ImageBody(Long imgId, String imageBase64) {
            this.imgId = imgId;
            this.imageBase64 = imageBase64;
        }

        public ImageBody(String imageBase64) {
            this.imageBase64 = imageBase64;
        }
    }
}
