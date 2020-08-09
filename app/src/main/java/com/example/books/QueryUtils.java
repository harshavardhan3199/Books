package com.example.books;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils
{
    private static String LOG_TAG=QueryUtils.class.getSimpleName();

    //Creating a private constructor so that no one can use this QueryUtils and create instances
    private QueryUtils()
    {

    }
    public static List<Books> fetchBookData(String requestUrl)
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url=createUrl(requestUrl);
        String jsonResponce=null;
        try
        {
            jsonResponce=makeHttpRequest(url);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Problem making Http Request "+e);
        }
        List<Books> book =extractFeatureFromJson(jsonResponce);
        return book;
    }
    private static URL createUrl(String requestUrl)
    {
        URL url=null;
        try
        {
            if(requestUrl!=null)
            {
                url=new URL(requestUrl);
            }
        }
        catch (MalformedURLException e)
        {
            Log.e(LOG_TAG,"Problem making the URL "+e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url)throws IOException
    {
        String jsonResponce="";
        if(url==null)
        {
            return jsonResponce;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try
        {
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200)
            {
                inputStream=urlConnection.getInputStream();
                jsonResponce=readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG,"Error responce code: "+urlConnection.getResponseCode());
            }
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,"Problem retrieving books data"+e);
        }
        finally
        {
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
        }
        return jsonResponce;
    }
    private static String readFromStream(InputStream inputStream)throws IOException
    {
        StringBuilder output=new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while (line!=null)
            {
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }
    public static List<Books> extractFeatureFromJson(String bookJson)
    {
        if(TextUtils.isEmpty(bookJson))
            return null;
        List<Books> books=new ArrayList<Books>();
        try
        {
            JSONObject baseJsonResponce=new JSONObject(bookJson);
            JSONArray bookArray=baseJsonResponce.getJSONArray("items");
            for(int i=0;i<bookArray.length();i++)
            {
                JSONObject currentBook=bookArray.getJSONObject(i);
                JSONObject  volumeInfo=currentBook.getJSONObject("volumeInfo");
                String title=volumeInfo.getString("title");
                String publisherName=volumeInfo.getString("publisher");
                String  previewLink=volumeInfo.getString("previewLink");
                Books book =new Books(title,publisherName,previewLink);
                books.add(book);

            }
        }
        catch(JSONException e)
        {
            Log.e("QueryUtils","Problem parsing the book data");
        }
        return books;
    }
}
