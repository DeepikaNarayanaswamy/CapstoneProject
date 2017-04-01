package com.deepika.newsnow.util;

import com.deepika.newsnow.pojo.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.deepika.newsnow.util.NewsNowConstants.NEWS_CATEGORY_GENERAL;
import static com.deepika.newsnow.util.NewsNowConstants.NEWS_CATEGORY_SPORTS;
import static com.deepika.newsnow.util.NewsNowConstants.NEWS_CATEGORY_TECHNOLOGY;
import static com.deepika.newsnow.util.NewsNowConstants.NEWS_CATEGORY_WILDLIFE;
import static com.deepika.newsnow.util.NewsNowConstants.NEWS_TYPE_HEADLINES;
import static com.deepika.newsnow.util.NewsNowConstants.NEWS_TYPE_TRENDING;

/**
 * Created by user on 3/26/2017.
 */

public class NewsNowUtil {

    public static  List<News> parseJSONString(String newsJSON){


        List<News> newsList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(newsJSON);
            String sortBy = jsonObject.getString(NewsNowConstants.NEWS_SORTBY_PARAMETER);
            String source = jsonObject.getString(NewsNowConstants.NEWS_SOURCE_PARAMETER);
            String newsType,newsCategory;
            switch (sortBy){
                case NewsNowConstants.NEWS_SORTBY_TOP :  newsType = NEWS_TYPE_HEADLINES; break;
                case NewsNowConstants.NEWS_SORTBY_POPULAR :  newsType = NEWS_TYPE_TRENDING; break;
                default:newsType = NEWS_TYPE_HEADLINES;
            }
            switch (source) {
                case NewsNowConstants.THE_HINDU:
                    newsCategory = NEWS_CATEGORY_GENERAL;
                    break;
                case NewsNowConstants.ESPN:
                    newsCategory = NEWS_CATEGORY_SPORTS;
                    break;

                case NewsNowConstants.TECH_CRUNCH:
                    newsCategory = NEWS_CATEGORY_TECHNOLOGY;
                    break;
                case NewsNowConstants.NATIONAL_GEOGRAPHIC:
                    newsCategory = NEWS_CATEGORY_WILDLIFE;
                    break;
                default:
                    newsCategory = NEWS_CATEGORY_GENERAL;
                    break;
            }

             JSONArray newsArray = (JSONArray)jsonObject.get(NewsNowConstants.ARTICLES);
            for (int i=0;i<newsArray.length();i++){
                News news = new News();
                JSONObject newsObject = newsArray.getJSONObject(i);
                news.setNewsTitle((String)newsObject.get(NewsNowConstants.TITLE));
                news.setNewsDescription((String)newsObject.get(NewsNowConstants.DESCRIPTION));
                news.setNewsURL((String)newsObject.get(NewsNowConstants.NEWSURL));
                news.setNewsImageURL((String)newsObject.get(NewsNowConstants.NEWSIMAGEURL));
                news.setBookmarked(false);
                news.setNewsType(newsType);
                news.setDeleted(false);
                news.setNewsCategory(newsCategory);
                news.setNewsID("NID_"+i+System.currentTimeMillis()+"_"+sortBy);
                newsList.add(news);
            }

        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return newsList;
    }
}
