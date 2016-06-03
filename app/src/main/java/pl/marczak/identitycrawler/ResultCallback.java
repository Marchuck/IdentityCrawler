package pl.marczak.identitycrawler;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 28.05.16.
 */
public interface ResultCallback<T> {
    void onResult(List<T> resutls);
}
