package com.example.books;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class BookAdapter extends ArrayAdapter<Books>
{

    public BookAdapter(@NonNull Context context, List<Books> booksList)
    {
        super(context,0, booksList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View listItemView=convertView;
        if(listItemView==null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.books_list_item,parent,false);
        }
        Books currentBooks=getItem(position);
        TextView titleName=(TextView)listItemView.findViewById(R.id.title_text);
        titleName.setText(currentBooks.getTitle());
        TextView publisherName=(TextView)listItemView.findViewById(R.id.publisherName_text);
        publisherName.setText(currentBooks.getPublisherName());
        return listItemView;
    }
}
