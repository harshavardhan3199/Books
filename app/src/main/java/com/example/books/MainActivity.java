package com.example.books;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private TextView emptyTextView;
    private static final String LOG_TAG = MainActivity.class.getName();
    /** URL for books data from the Books dataset */
    private static final String BOOKS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=20";
    /** Adapter for the list of earthquakes */
    private BookAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find a reference to the {@link ListView} in the layout
        final ListView listView = (ListView) findViewById(R.id.list_view);
        emptyTextView=(TextView)findViewById(R.id.empty_view);
        listView.setEmptyView(emptyTextView);
        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new BookAdapter(this, new ArrayList<Books>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books currentBook=mAdapter.getItem(position);
                Uri bookUri=Uri.parse(currentBook.getPreview());
                Intent previewIntent=new Intent(Intent.ACTION_VIEW,bookUri);
                startActivity(previewIntent);
            }
        });
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected())
        {
            BookAsyncTask task = new BookAsyncTask();
            task.execute(BOOKS_REQUEST_URL);
        }
        else
        {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet);
        }

    }



    public class BookAsyncTask extends AsyncTask<String,Void,List<Books>>
    {
        @Override
        protected List<Books> doInBackground(String... urls)
        {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null)
            {
                return null;
            }
            List<Books> result = QueryUtils.fetchBookData(urls[0]);
            return result;
        }
        @Override
        protected void onPostExecute(List<Books> books)
        {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_books);
            mAdapter.clear();
            // If there is a valid list of {@link Books}, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (books != null && !books.isEmpty())
            {
                mAdapter.addAll(books);
            }
        }
    }
}
