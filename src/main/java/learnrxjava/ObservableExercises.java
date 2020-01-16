package learnrxjava;

import learnrxjava.types.*;
import rx.Observable;

public class ObservableExercises {

    /**
     * Return an Observable that emits a single value "Hello World!"
     * 
     * @return "Hello World!"
     */
    public Observable<String> exerciseHello() {
        return Observable.just("Hello World!");
    }

    /**
     * Transform the incoming Observable from "Hello" to "Hello [Name]" where [Name] is your name.
     *
     * @param "Hello Name!"
     */
    public Observable<String> exerciseMap(Observable<String> hello) {
        return Observable.just("Hello World!")
                .map(s -> {
                   return s.replace("World!", "Dan");
                });
    }

    /**
     * Given a stream of numbers, choose the even ones and return a stream like:
     * <p>
     * 2-Even
     * 4-Even
     * 6-Even
     */
    public Observable<String> exerciseFilterMap(Observable<Integer> nums) {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .filter(i -> {
                    return i % 2 == 0;
                }).map(i -> "" + i + "-Even");
    }

    /**
     * Flatten out all video in the stream of Movies into a stream of videoIDs
     * 
     * @param movies
     * @return Observable of Integers of Movies.videos.id
     */
    public Observable<Integer> exerciseConcatMap(Observable<Movies> movies) {
        return movies.concatMap(m -> {
            return m.videos.map(movie -> {
               return movie.id;
            });
        });
    }

    /**
     * Flatten out all video in the stream of Movies into a stream of videoIDs
     * 
     * Use flatMap this time instead of concatMap. In Observable streams
     * it is almost always flatMap that is wanted, not concatMap as flatMap
     * uses merge instead of concat and allows multiple concurrent streams
     * whereas concat only does one at a time.
     * 
     * We'll see more about this later when we add concurrency.
     * 
     * @param movies
     * @return Observable of Integers of Movies.videos.id
     */
    public Observable<Integer> exerciseFlatMap(Observable<Movies> movies) {
        return movies.flatMap(movieList -> {
            return movieList.videos.map(movie -> movie.id);
        });
    }

    /**
     * Retrieve the largest number.
     * 
     * Use reduce to select the maximum value in a list of numbers.
     */
    public Observable<Integer> exerciseReduce(Observable<Integer> nums) {
        return nums.reduce((i1, i2) -> {
            if (i1 > i2) {
                return i1;
            }
            return i2;
        });
    }

    /**
     * Retrieve the id, title, and <b>smallest</b> box art url for every video.
     * 
     * Now let's try combining reduce() with our other functions to build more complex queries.
     * 
     * This is a variation of the problem we solved earlier, where we retrieved the url of the boxart with a
     * width of 150px. This time we'll use reduce() instead of filter() to retrieve the _smallest_ box art in
     * the boxarts list.
     * 
     * See Exercise 19 of ComposableListExercises
     */
    public Observable<JSON> exerciseMovie(Observable<Movies> movies) {
        return movies.flatMap(movieList -> {
            return movieList.videos.flatMap(movie -> {
                return movie.boxarts.reduce((b1, b2) -> {
                    if (b1.width * b1.height < b2.width * b2.height) {
                        return b1;
                    }
                    return b2;
                }).map(b -> {
                   return json("id", movie.id, "title", movie.title, "boxart", b.url);
                });
            });
        });
    }

    /**
     * Combine 2 streams into pairs using zip.
     * 
     * a -> "one", "two", "red", "blue"
     * b -> "fish", "fish", "fish", "fish"
     * output -> "one fish", "two fish", "red fish", "blue fish"
     */
    public Observable<String> exerciseZip(Observable<String> a, Observable<String> b) {
        return Observable.zip(a, b, (a1, b1) -> {
            return a1 + " " + b1;
        });
    }

    /**
     * Don't modify any values in the stream but do handle the error
     * and replace it with "default-value".
     */
    public Observable<String> handleError(Observable<String> data) {
        return data.map(d -> {
            return d;
        }).onErrorReturn(t -> "default-value");
    }

    /**
     * The data stream fails intermittently so return the stream
     * with retry capability.
     */
    public Observable<String> retry(Observable<String> data) {
        return data.map(d -> {
            return d;
        }).retry(5);
    }

    // This function can be used to build JSON objects within an expression
    private static JSON json(Object... keyOrValue) {
        JSON json = new JSON();

        for (int counter = 0; counter < keyOrValue.length; counter += 2) {
            json.put((String) keyOrValue[counter], keyOrValue[counter + 1]);
        }

        return json;
    }
}
