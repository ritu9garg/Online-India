package onlineind.com.fragment_package;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
import java.util.List;
import java.util.Random;

import onlineind.com.Activity_Package.OnLoadMoreListener;
import onlineind.com.Adapter_Package.VideoAdapter;
import onlineind.com.GS_Package.YoutubeVideo_GS;
import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.ALLVIDEO;


public class Fragment_Videos extends Fragment {
    View view;
    Context context;
    RecyclerView recyclerView;
    RelativeLayout videoloader;
    VideoAdapter videoAdapter;
    String video_id;
    private List<YoutubeVideo_GS> youtubeVideos;
    private List<YoutubeVideo_GS> youtubeVideo_gsList;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Json = "nameKey";
    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;
    SharedPreferences sf;
    int index = 10;
    private Random random;
    boolean a = true;
    int oldnumber, newStartNumber, newEndNUmber;
    public View ftView;

    public Fragment_Videos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment__videos, container, false);

        youtubeVideos = new ArrayList<>();
        youtubeVideo_gsList = new ArrayList<>();
        random = new Random();


        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sf = getActivity().getSharedPreferences("video", 0);

        videoloader = (RelativeLayout) view.findViewById(R.id.videoloader);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        videoAdapter = new VideoAdapter(recyclerView, youtubeVideos, getActivity());
        videoAdapter.clear();
        recyclerView.setAdapter(videoAdapter);

        if(sf.getString("click","0").equals("1")){
            setdata();
        }else{
            loadVideo();
        }



        videoAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (youtubeVideo_gsList.size() != index) {
                    youtubeVideos.add(null);
                    videoAdapter.notifyItemInserted(youtubeVideos.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            youtubeVideos.remove(youtubeVideos.size() - 1);
                            videoAdapter.notifyItemRemoved(youtubeVideos.size());

                            //Generating more data
                            int end = index + 5;
                            for (int i = index; i < end; i++) {
                                YoutubeVideo_GS contact = new YoutubeVideo_GS();
                                contact.setVideoUrl(youtubeVideo_gsList.get(i).getVideoUrl());
                                youtubeVideos.add(contact);
                            }
                            index = end;
                            videoAdapter.notifyDataSetChanged();
                            videoAdapter.setLoaded();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(getContext(), "Loading data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }


    private void loadVideo() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ALLVIDEO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

                        Log.d("json", result);

                        editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();
                        editor.putString(Json, result);
                        editor.commit();

                        String response = result.toString();
                        videoloader.setVisibility(View.GONE);


                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray arr = obj.getJSONArray("result");
                            for (int i = 0; i < arr.length(); i++) {
                                obj = arr.getJSONObject(i);
                                YoutubeVideo_GS c = new YoutubeVideo_GS();
                                String video_url = obj.getString("video_url");
                                String[] words = video_url.split("embed/");
                                try {
                                    String str = words[1];
                                    c.setVideoUrl(str);
                                    youtubeVideo_gsList.add(c);
                                }catch (Exception e){}

                            }
                            for (int i = 0; i < 10; i++) {
                                YoutubeVideo_GS contact = new YoutubeVideo_GS();
                                contact.setVideoUrl(youtubeVideo_gsList.get(i).getVideoUrl());
                                youtubeVideos.add(contact);
                            }
                            videoAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }



    private void setdata() {

        editor = sharedpreferences.edit();
        String sfData = sharedpreferences.getString(Json,"");
        String response = sfData.toString();
        videoloader.setVisibility(View.GONE);


        try {
            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("result");
            for (int i = 0; i < arr.length(); i++) {
                obj = arr.getJSONObject(i);
                YoutubeVideo_GS c = new YoutubeVideo_GS();
                String video_url = obj.getString("video_url");
                String[] words = video_url.split("embed/");
                try {
                    String str = words[1];
                    c.setVideoUrl(str);
                    youtubeVideo_gsList.add(c);
                }catch (Exception e){}
                youtubeVideo_gsList.add(c);

            }
            for (int i = 0; i < 10; i++) {
                YoutubeVideo_GS contact = new YoutubeVideo_GS();
                contact.setVideoUrl(youtubeVideo_gsList.get(i).getVideoUrl());
                youtubeVideos.add(contact);
            }
            videoAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}