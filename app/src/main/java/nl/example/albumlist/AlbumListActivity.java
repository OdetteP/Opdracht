package nl.example.albumlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list);

        final ListView albumListView = findViewById(R.id.album_listview);
        final ArrayList<String> albumTitles = new ArrayList<>();
        final ArrayList<Integer> albumIds = new ArrayList<>();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.album_list_detail, R.id.album_title, albumTitles);
        albumListView.setAdapter(arrayAdapter);

        albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(AlbumListActivity.this, PhotoListActivity.class);
                myIntent.putExtra("albumId", albumIds.get(position));
                AlbumListActivity.this.startActivity(myIntent);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://jsonplaceholder.typicode.com/albums";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.optJSONObject(i);
                                String title = object.optString("title");
                                Integer albumId = object.getInt("id");
                                if (title != null) {
                                    albumTitles.add(title);
                                    albumIds.add(albumId);
                                }
                            }
                            arrayAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error," + e,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error," + error,
                        Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }
}




