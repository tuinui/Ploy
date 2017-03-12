package com.nos.ploy.api.ployee.model;

/**
 * Created by Saran on 12/3/2560.
 */

public class DeleteServiceGson {
    /*
    {
    "serviceId": 2,
    "userId": 2
}
     */

    private long serviceId;
    private long userId;

    public DeleteServiceGson() {
    }

    private DeleteServiceGson(long serviceId, long userId) {
        this.serviceId = serviceId;
        this.userId = userId;
    }


    public static DeleteServiceGson with(long serviceId, long userId) {
        return new DeleteServiceGson(serviceId, userId);
    }

    public long getServiceId() {
        return serviceId;
    }

    public long getUserId() {
        return userId;
    }
}
