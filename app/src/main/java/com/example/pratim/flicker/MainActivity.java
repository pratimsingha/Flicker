package com.example.pratim.flicker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String LOG_TAG = "MainActivity";
    private List<Photo> mPhotoList = new ArrayList<Photo>();
    private RecyclerView mRecyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(new ArrayList<Photo>(), MainActivity.this);
        mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,mRecyclerView,new RecyclerItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position){
                Toast.makeText(MainActivity.this,"Normal Tap", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemLongClick(View view, int position){
                Intent intent = new Intent(MainActivity.this, ViewPhotoDetailsActivity.class);
                intent.putExtra(PHOTO_TRANSFER,flickrRecyclerViewAdapter.getPhoto(position));
                startActivity(intent);
            }
        }));
        //ProcessPhotos processPhotos = new ProcessPhotos("android,lollipop",true);
        //processPhotos.execute();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        //GetRawData theRawData = new GetRawData("https://api.flickr.com/services/feeds/photos_public.gne?tags=love&format=json&nojsoncallback=1");
        //GetFlickrJsonData jsonData = new GetFlickrJsonData("android,lollipop",true);
        //jsonData.execute();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if(flickrRecyclerViewAdapter != null){
        String query = getSavedPreferenceData(FLICKR_QUERY);
        if(query.length() > 0){
            ProcessPhotos processPhotos = new ProcessPhotos(query,true);
            processPhotos.execute();
        }
        //}
    }

    private String getSavedPreferenceData(String key){
        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedpref.getString(key,"");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.menu_search){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ProcessPhotos extends GetFlickrJsonData{
        public ProcessPhotos(String searchCriteria, Boolean matchAll) {
            super(searchCriteria, matchAll);
        }
        public void execute(){
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }
        public class ProcessData extends DownloadJsonData{
            protected void onPostExecute(String webData){
                super.onPostExecute(webData);
                //flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(getmPhotos(),MainActivity.this);
                //mRecyclerView.setAdapter(flickrRecyclerViewAdapter);
                flickrRecyclerViewAdapter.loadNewData(getPhotos());
            }

        }

    }
}
