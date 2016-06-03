package pl.marczak.identitycrawler.hacked;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Lukasz Marczak
 * @since 03.06.16.
 */
public class InstagramUser {
    public String name;
    public String full_name;
    public String profile_pic_url;

    public InstagramUser(JsonElement js) {
        JsonObject root = js.getAsJsonObject();
        name = root.get("username").getAsString();
        profile_pic_url = root.get("profile_pic_url").getAsString();
        full_name = root.get("full_name").getAsString();
    }

    @Override
    public String toString() {
        return "name: " + name + ", full_name: " + full_name +
                ", profile_pic_url: " + profile_pic_url;
    }
}
