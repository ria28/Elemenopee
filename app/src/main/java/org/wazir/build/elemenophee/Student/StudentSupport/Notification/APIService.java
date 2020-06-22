package org.wazir.build.elemenophee.Student.StudentSupport.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
        "Content-Type:application/json",
                "Authorization:key=AAAAKh6Jq9o:APA91bHB0Cyat6IzSNDe6W0oopknGB74wyDEm4foqJTvpU62VhRmSbBeP0JCdy5GDuoDnQ_WqS1HzwoOW0HDzXrXF0qF35CSscGL3gf74gHzA80ey-yDPYNlVEXSi0ffQQRagmFBpkGA"
    }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
