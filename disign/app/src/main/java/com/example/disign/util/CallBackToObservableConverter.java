package com.example.disign.util;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallBackToObservableConverter {
    public static <T> Observable<T> getObservable(Call<T> call){
        return Observable.create(emitter -> {

            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    if (!emitter.isDisposed()) {
                        if (response.isSuccessful()) {
                            T data = response.body();
                            emitter.onNext(PrimitiveObjectConverter.convert(data)); // Emit the data to the observer
                            emitter.onComplete(); // Mark the observable as completed
                        } else {
                            // Handle unsuccessful response.
                            System.out.println(response.code());
                            System.out.println(response.message());
                            System.out.println(response.body());
                            emitter.onError(new Throwable("Unsuccessful response"));
                        }
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    if (!emitter.isDisposed()) {
                        // Handle network call failure.
                        emitter.onError(t);
                    }
                }
            });

            // Disposable: You can use it to dispose the network call when the Observable is disposed
            emitter.setDisposable(new Disposable() {
                @Override
                public void dispose() {
                    if (!call.isExecuted()) {
                        call.cancel();
                    }
                }

                @Override
                public boolean isDisposed() {
                    return call.isCanceled();
                }
            });
        });
    }


}
