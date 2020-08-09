package com.example.books;

public class Books
{
    private String mTitle;
    private String mPublisherName;
    private String mPreview;
    public Books(String title,String publisherName,String preview)
    {
        mTitle=title;
        mPublisherName=publisherName;
        mPreview=preview;

    }
    public String getTitle()
    {
        return mTitle;
    }
    public String getPublisherName()
    {
        return mPublisherName;
    }
    public String getPreview()
    {
        return mPreview;
    }

}
