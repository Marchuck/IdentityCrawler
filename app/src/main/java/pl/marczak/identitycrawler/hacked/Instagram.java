package pl.marczak.identitycrawler.hacked;

import android.test.suitebuilder.annotation.SmallTest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.functions.Func1;

/**
 * @author Lukasz Marczak
 * @since 28.05.16.
 */
@SmallTest
public class Instagram {
    public static String endpoint = "http://www.instagram.com";
    public static String _enpoint = "www.instagram.com/web/search/topsearch/" +
            "?context=blended&query=" +
            "lukasz%20marczak" +
            "&rank_token=0.6350476613390368";

    /**
     * Host:www.instagram.com ----
     * User-Agent:Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:46.0) Gecko/20100101 Firefox/46.0 ---
     * Accept: "* / *".replaceAll(" ",""); ---
     * Accept-Language:pl,en-US;q=0.7,en;q=0.3 ---
     * Accept-Encoding:gzip, deflate, br ---
     * X-Requested-With:XMLHttpRequest ---
     * Referer:https://www.instagram.com/marczakl/
     */

    public interface InstagramAPI {

        @GET("/web/search/topsearch/")
        rx.Observable<JsonElement> getPeople(@Query("context") String context, @Query("query") String query,
                                             @Query("rank_token") String token);

        @GET("/web/search/topsearch/")
        rx.Observable<JsonElement> getPeople(@Query("query") String query,
                                             @Query("rank_token") String token);
    }

    private static RestAdapter buildInstagramRetrofit() {
        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept-Encoding: gzip, deflate", "br");
                        request.addHeader("X-Requested-With", "XMLHttpRequest");
                        request.addHeader("Host", "www.instagram.com");
                        request.addHeader("Referer", "https://www.instagram.com/");
                        request.addHeader("Accept", "*/*");
                        request.addHeader("Cookie", "csrftoken=f91aeb0d2c77bdf58e6966f1c89009bf; mid=V0l15QAEAAGU-UHq_WWex12AVOQL; fbm_124024574287414=base_domain=.instagram.com; sessionid=IGSC8c74869582b4b9aa0473129666403b359bc6b9c145c8635a5a6a50340314798b%3AnUoSo4J5hEcnlCKT0Qcq4WMicy7Fr6Xm%3A%7B%22_token_ver%22%3A2%2C%22_auth_user_id%22%3A3224277497%2C%22_token%22%3A%223224277497%3A1AIRzCxcnQlGFm6il5oh2eSkYkWMo4IC%3Aa51bbabed00174663236f09244a74bfb9789b73664a8eb3eb7a37177ad0fe4d0%22%2C%22asns%22%3A%7B%2289.67.167.85%22%3A6830%2C%22time%22%3A1464980180%7D%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22last_refreshed%22%3A1464980180.532673%2C%22_platform%22%3A4%7D; ds_user_id=3224277497; s_network=; ig_pr=2; ig_vw=1280; fbsr_124024574287414=hLslrENjUtXoMCrA5aJDKx758xs2y7SClsOLkZ8iw40.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJBUUFSNnR1dUNQYXlMNnZ2NzJTellkWTEweFlvbjdaRzlvXzJBdXdMVzJzeUlGOGZPbHZvTi13VFplX3liQVEySk5JdVJfRFF1UkM5WVJBV2dsUW1JTHJFTXJtZG9heFNkN293Vl9Ec2JfVTYyaThYRTd0WXBHYVpTWFltYTNMZHNpZFhYTHFVbVJ1TXRvcDFUeS15VHJldGwxTHltUjloY1lRYV9yV1QtT2RXcFQ4MG4yVEViejNvYlJpUkRFbVVWdWl4NElkeUdzS2RfTUtRZzFfVUx2UlRxY0dqazB3dXVkZS1IR0pwajdieEtrdkkzWmNtMDJiNnNaZUFkcXF0X2FUNWxwbWdIa0hrRkVnR0RXTHRKdWFUU1llOGUyVHV4amdDYmpWZXVMQlk1LWJHbDF6a1pxUnhFSTlKWVUtZS02VlFHa2oweWVLM3VlVm1mVG1nT0ZqUiIsImlzc3VlZF9hdCI6MTQ2NDk4MDUyMSwidXNlcl9pZCI6IjEwMDAwMDgwMDUwMTA0OCJ9");
                        request.addHeader("Accept-Language", "pl,en-US;q=0.7,en;q=0.3");
                        request.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:46.0) Gecko/20100101 Firefox/46.0");
                    }
                }).build();
    }

    public static rx.Observable<List<InstagramUser>> getUsers(String query) {
        return buildInstagramRetrofit().create(InstagramAPI.class).getPeople("blended", query, "0.7712428776112908").map(new Func1<JsonElement, List<InstagramUser>>() {
            @Override
            public List<InstagramUser> call(JsonElement jsonElement) {
                return parseToUsers(jsonElement);
            }
        });
    }

    private static List<InstagramUser> parseToUsers(JsonElement jsonElement) {
        JsonArray uzerz = jsonElement.getAsJsonObject().get("users").getAsJsonArray();
        List<InstagramUser> users = new ArrayList<>();
        for (JsonElement js : uzerz) {
            users.add(new InstagramUser(js));
        }
        return users;
    }
}
