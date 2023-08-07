package com.example.disign.service;

import com.example.disign.model.User;
import com.example.disign.model.Event;
import com.example.disign.util.CallBackToObservableConverter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;

public class SessionService {
    private static User user;
    private static List<Event> event=new ArrayList<>();

    public static void setUser(User user) {
        user = user;
    }
    public static void setEvent(List<Event> event) {
        SessionService.event = event;
    }

    public static User getUser() {
        return user;
    }
    public static List<Event> getEvent() {
        if(event.size()==0){
            getEventFromSource().subscribe();
        }
        return event;
    }
    public static Observable<List<Event>> getEventFromSource(){
        String option="{\"relations\":[\"media\"],\"order\":{   \"createdAt\":\"desc\"  } }";

        Call<List<Event>> call=ApiManager.apiService.listeEvent(option);
        Observable<List<Event>> observable= CallBackToObservableConverter.getObservable(call);
        return observable.map(( eventList)->{
            setEvent(eventList);
            return eventList;
        });
    }


}
