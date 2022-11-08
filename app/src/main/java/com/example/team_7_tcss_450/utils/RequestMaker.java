package com.example.team_7_tcss_450.utils;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class to slightly abstract away the task of making HTTP Requests.
 * The main "abstraction" you get is mainly default error handling functionality and
 * ease of adding custom arguments such as request parameters for the body and header
 * of the url which we hope leads to fewer lines of code written in the future.
 */
public class RequestMaker {

    /**
     * Sets up the class to be static.
     */
    private RequestMaker() {

    }

    /**
     * This method makes a JSON Object Request with header arguments included. This will act as
     * the base method that we will call for all other variations of this method that use different
     * arguments.
     *
     * @param connectionMethod The type of http request to make (i.e GET, POST, DELETE, etc)
     * @param url The URL to send our HTTP request to
     * @param bodyArgs The arguments we want to pass to the body of our json request
     * @param responseHandler This acts as a callback method when we get a successful response
     * @return An HTTP request with header arguments
     */
    public static Request<JSONObject> makeRequest(
            final int connectionMethod,
            final String url,
            final Map<String, ?> bodyArgs,
            final Response.Listener<JSONObject> responseHandler,
            final Response.ErrorListener errorResponseHandler,
            final Map<String, String> headerArgs) {
        JSONObject body = new JSONObject();
        if (bodyArgs != null) {
            try {
                for (String key : bodyArgs.keySet()) {
                    body.put(key, bodyArgs.get(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final Request<JSONObject> request;
        if (headerArgs != null) {
            request = new JsonObjectRequest(
                    connectionMethod,
                    url,
                    body,
                    responseHandler,
                    errorResponseHandler) {
                @Override
                public Map<String, String> getHeaders() {
                    return headerArgs;
                }
            };
        } else {
            request = new JsonObjectRequest(
                    connectionMethod,
                    url,
                    body,
                    responseHandler,
                    errorResponseHandler);
        }
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }


    /**
     * This method makes a JSON Object Request, but without any header arguments necessary
     *
     * @param connectionMethod The type of http request to make (i.e GET, POST, DELETE, etc)
     * @param url The URL to send our HTTP request to
     * @param bodyArgs The arguments we want to pass to the body of our json request
     * @param responseHandler This acts as a callback method when we get a successful response
     * @param errorResponseHandler This acts as a callback method when we get an error response
     * @return An HTTP request
     */
    public static Request<JSONObject> makeRequest(
            final int connectionMethod,
            final String url,
            final Map<String, ?> bodyArgs,
            final Response.Listener<JSONObject> responseHandler,
            final Response.ErrorListener errorResponseHandler) {
        return makeRequest(
                connectionMethod,
                url,
                bodyArgs,
                responseHandler,
                errorResponseHandler,
                null);
    }

    /**
     * Acts as the default error handling callback method.
     * @param error This is the error Volley returns if our web request gives a bad response
     * @param theResponse This is the JSON Object we mutate from the calling class to hold our error response
     */
    public static void defaultErrorHandler(final VolleyError error,
                                   MutableLiveData<JSONObject> theResponse) {
        if (Objects.isNull(error.networkResponse)) {
            try {

                theResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                theResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }
}
