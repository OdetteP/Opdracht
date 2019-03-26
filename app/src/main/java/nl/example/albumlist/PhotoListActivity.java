package nl.example.albumlist;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PhotoListActivity extends AppCompatActivity {

    ArrayList<String> titlePhotos;
    ArrayList<String> imageUrls;
    ArrayList<String> thumbnailUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        Intent intent = getIntent();
        final Integer albumId = intent.getIntExtra("albumId",0);

        titlePhotos = new ArrayList<>();
        imageUrls = new ArrayList<>();
        thumbnailUrls = new ArrayList<>();

        final ListView photoList = findViewById(R.id.photo_list_view);
        final PhotoListViewAdapter photoListViewAdapter = new PhotoListViewAdapter();
        photoList.setAdapter(photoListViewAdapter);

        photoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(PhotoListActivity.this, PhotoDetailActivity.class);
                myIntent.putExtra("titlePhoto", titlePhotos.get(position));
                myIntent.putExtra("imageUrl", imageUrls.get(position));
                PhotoListActivity.this.startActivity(myIntent);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://jsonplaceholder.typicode.com/photos?albumId=" + albumId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.optJSONObject(i);
                                String title = object.optString("title");
                                String imageUrl = object.optString("url");
                                String thumbnailUrl = object.optString("thumbnailUrl");
                                if (imageUrl != null) {
                                    titlePhotos.add(title);
                                    imageUrls.add(imageUrl);
                                    thumbnailUrls.add(thumbnailUrl);
                                }
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    photoList.setAdapter(photoListViewAdapter);
                                }
                            });
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


    public class PhotoListViewAdapter implements ListAdapter {

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public int getCount() {
            return titlePhotos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate
                        (R.layout.photo_list_details, parent, false);
            }

            TextView photoTitleTextView = convertView.findViewById(R.id.photo_title_text_view);
            ImageView thumbnailImageView = convertView.findViewById(R.id.photo_thumbnail_image_view);

            photoTitleTextView.setText(titlePhotos.get(position));
            Picasso.get().load(thumbnailUrls.get(position)).into(thumbnailImageView);

            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }
    }
}
