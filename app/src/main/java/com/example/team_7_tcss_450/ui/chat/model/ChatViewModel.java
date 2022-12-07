package com.example.team_7_tcss_450.ui.chat.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.io.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatViewModel extends AndroidViewModel {

    /**
     * A Map of Lists of Chat Messages.
     * The Key represents the Chat ID
     * The value represents the List of (known) messages for that that room.
     */
    private Map<Integer, MutableLiveData<List<ChatMessage>>> mMessages;

    private MutableLiveData<List<ChatPreview>> mChatPreviewsList;
    public Map<Integer, Integer> mChatPreviewMap;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        mMessages = new HashMap<>();
        mChatPreviewsList = new MutableLiveData<>();
        mChatPreviewsList.setValue(new ArrayList<>());
        mChatPreviewMap = new HashMap<>();
    }

    public List<ChatPreview> getChatPreviewsList() {
        return mChatPreviewsList.getValue();
    }

    public void addChatPreviewsObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<ChatPreview>> observer) {
        mChatPreviewsList.observe(owner, observer);
    }

    /**
     * Register as an observer to listen to a specific chat room's list of messages.
     * @param chatId the chatid of the chat to observer
     * @param owner the fragments lifecycle owner
     * @param observer the observer
     */
    public void addMessageObserver(int chatId,
                                   @NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<ChatMessage>> observer) {
        getOrCreateMapEntry(chatId).observe(owner, observer);
    }

    /**
     * Return a reference to the List<> associated with the chat room. If the View Model does
     * not have a mapping for this chatID, it will be created.
     *
     * WARNING: While this method returns a reference to a mutable list, it should not be
     * mutated externally in client code. Use public methods available in this class as
     * needed.
     *
     * @param chatId the id of the chat room List to retrieve
     * @return a reference to the list of messages
     */
    public List<ChatMessage> getMessageListByChatId(final int chatId) {
        return getOrCreateMapEntry(chatId).getValue();
    }

    private MutableLiveData<List<ChatMessage>> getOrCreateMapEntry(final int chatId) {
        if(!mMessages.containsKey(chatId)) {
            mMessages.put(chatId, new MutableLiveData<>(new ArrayList<>()));
        }
        return mMessages.get(chatId);
    }

    /**
     * Makes a request to the web service to get the first batch of messages for a given Chat Room.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     *
     * Subsequent requests to the web service for a given chat room should be made from
     * getNextMessages()
     *
     * @param chatId the chatroom id to request messages of
     * @param jwt the users signed JWT
     */
    public void getFirstMessages(final int chatId, final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "messages/" + chatId;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handelSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

        //code here will run
    }

    /**
     * Makes a request to the web service to get the next batch of messages for a given Chat Room.
     * This request uses the earliest known ChatMessage in the associated list and passes that
     * messageId to the web service.
     * Parses the response and adds the ChatMessage object to the List associated with the
     * ChatRoom. Informs observers of the update.
     *
     * Subsequent calls to this method receive earlier and earlier messages.
     *
     * @param chatId the chatroom id to request messages of
     * @param jwt the users signed JWT
     */
    public void getNextMessages(final int chatId, final String jwt) {
        // Don't get next messages if the given chat has no messages in the first place
        if (mMessages.get(chatId).getValue().isEmpty()) {
            getOrCreateMapEntry(chatId).setValue(mMessages.get(chatId).getValue());
            return;
        }
        String url = getApplication().getResources().getString(R.string.base_url_service) +
                "messages/" +
                chatId +
                "/" +
                mMessages.get(chatId).getValue().get(0).getMessageId();

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handelSuccess,
                this::handleError) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);

        //code here will run
    }

    /**
     * When a chat message is received externally to this ViewModel, add it
     * with this method.
     * @param chatId
     * @param message
     */
    public void addMessage(final int chatId, final ChatMessage message) {
        List<ChatMessage> list = getMessageListByChatId(chatId);
        list.add(message);
        getOrCreateMapEntry(chatId).setValue(list);
        // Add latest message to corresponding ChatPreview
        List<ChatPreview> chatPreviews = mChatPreviewsList.getValue();

    }

    private void handelSuccess(final JSONObject response) {
        List<ChatMessage> list;
        if (!response.has("chatId")) {
            throw new IllegalStateException("Unexpected response in ChatViewModel: " + response);
        }
        try {
            list = getMessageListByChatId(response.getInt("chatId"));
            JSONArray messages = response.getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                ChatMessage cMessage = new ChatMessage(
                        message.getInt("messageid"),
                        message.getString("message"),
                        message.getString("email"),
                        message.getString("timestamp")
                );
                if (!list.contains(cMessage)) {
                    // don't add a duplicate
                    list.add(0, cMessage);
                } else {
                    // this shouldn't happen but could with the asynchronous
                    // nature of the application
                    Log.wtf("Chat message already received",
                            "Or duplicate id:" + cMessage.getMessageId());
                }
            }
            //inform observers of the change (setValue)
            getOrCreateMapEntry(response.getInt("chatId")).setValue(list);
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            Log.e("NETWORK ERROR", error.getMessage());
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    error.networkResponse.statusCode +
                    " " +
                    data);
        }
    }

    public void connectGetChatPreviews(final String email, final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url_service)
                + "chats/rooms"
                + "?email=" + email;

        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleGetChatPreviews,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleGetChatPreviews(final JSONObject response) {
        // quit getting chat previews if we've already populated our previews list
        // This is to prevent adding repeat data should the user inadvertently make multiple requests
        // as this success handler should only be called ONCE in the entire activity's lifetime.
        if (!mChatPreviewsList.getValue().isEmpty())
            return;
        if (response.has("rowCount")) {
            try {
                if (response.getInt("rowCount") != 0) {
                    JSONArray rows = response.getJSONArray("rows");
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject rowItem = rows.getJSONObject(i);
                        final ChatPreview chatPreview;
                        final String timestamp = rowItem.getString("timestamp");

                        // Check if the chat is empty (newly made with no messages) by seeing if timestamp is empty
                        if (timestamp.equals("null")) {
                            // if chat empty, make chat preview without latest message and date
                            chatPreview = new ChatPreview(rowItem.getInt("chatid"),
                                    rowItem.getString("name"));
                        } else {
                            // otherwise, make a full chat preview
                            chatPreview = new ChatPreview(
                                    rowItem.getString("name"),
                                    rowItem.getString("email"),
                                    rowItem.getString("message"),
                                    timestamp.substring(0, timestamp.indexOf('T')),
                                    rowItem.getInt("chatid")
                            );
                        }
                        mChatPreviewMap.put(chatPreview.getChatId(), mChatPreviewsList.getValue().size());
                        mChatPreviewsList.getValue().add(chatPreview);
                        mChatPreviewsList.setValue(mChatPreviewsList.getValue());
                    }
                } else {
                    // current user has no chats they're in
                    Log.d("CHAT", "current user is not associated with any chats");
                }
            } catch (JSONException e) {
                Log.e("CHAT", "Failed to parse rowCount in JSON object. response: " + e.getMessage());
            }
        } else {
            throw new IllegalStateException("Unexpected response in handleGetChatPreviews(). Response: " + response);
        }
    }

    public void connectAddNewChat(final String chatName, final String jwt) {
        String url = getApplication().getResources().getString(R.string.base_url_service)
                + "chats/";

        JSONObject body = new JSONObject();
        try {
            body.put("name", chatName);
        } catch (JSONException e) {
            Log.e("CHAT MODEL", "Failed to parse chat name on connectAddNewChat()");
        }

        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body, //no body for this get request
                this::handleAddNewChat,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                final Map<String, String> params = new HashMap<>();
                params.put("name", chatName);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleAddNewChat(final JSONObject response) {
        try {
            final String chatName = response.getString("chatName");
            // instantiate new chat preview
            final ChatPreview chatPreview = new ChatPreview(response.getInt("chatId"),
                    chatName);
            // Add new chat to head of chat preview list
            Objects.requireNonNull(mChatPreviewsList.getValue()).add(0, chatPreview);
            mChatPreviewsList.setValue(mChatPreviewsList.getValue()); // Signal observers that new chat made
            Log.d("CHAT MODEL", "Successfully added new chat: " + chatName);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handleAddNewChat()");
        }

    }

}
