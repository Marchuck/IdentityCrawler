package pl.marczak.identitycrawler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Subscriber;

/**
 * @author Lukasz Marczak
 * @since 03.06.16.
 */
public class Pokemon {
   static String endpoint = "http://pokeapi.co/api/v2/";

    public interface API {
        @GET("/pokemon/{id}")
        rx.Observable<Poke> getPoke(@Path("id") int id);
    }

    public static class Poke {
        public List<Form> forms = new ArrayList<>();

    }

    public static class Form {
        public String name;
    }
    public static rx.Observable<Poke> getPoke(int id){
        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(new Gson()))
                .build()
                .create(API.class)
                .getPoke(id);

    }
}
