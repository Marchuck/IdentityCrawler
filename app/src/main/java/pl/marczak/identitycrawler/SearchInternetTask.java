package pl.marczak.identitycrawler;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Lukasz Marczak
 * @since 28.05.16.
 */
public class SearchInternetTask {

    private ResultCallback<String> resultCallback;
    private String query;

    public void setResultCallback(ResultCallback<String> resultCallback) {
        this.resultCallback = resultCallback;
    }

    public SearchInternetTask(String query) {
        this.query = query;
    }

    public void execute() {

    }

    private static Retrofit buildRxRetrofit(String endpoint) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
