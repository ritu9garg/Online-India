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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import onlineind.com.Activity_Package.OnLoadMoreListener;
import onlineind.com.Adapter_Package.Home_Adapter;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.HEALTH;
import static onlineind.com.Https_Urls.Urls.LOADMORE;


public class Fragment_Health extends Fragment {
    View view;
    Context context;
    SliderLayout sliderLayout;
    RelativeLayout loaderitem;
    RelativeLayout loader;
    private List<Home_GS> home_gsList;
    ArrayList<Home_GS> arrayList1 = new ArrayList<>();
    int oldnumber, newStartNumber, newEndNUmber;
    public View ftView;

    Home_Adapter home_adapter;
    RecyclerView recyclerView;
    String newsID;
    String[] imgs, titel;
    String string, position, news_id, str_user_id, str_android_id, news_cate;
    HashMap<String, String> hashMap;
    public static final String PREFS_NAME = "LoginPrefs";
    private SwipeRefreshLayout swipeRefreshLayout;
    String cateId;
    String news_title, news_image, news_description, news_date;
    int index = 10;
    private Random random;

    String liked, bookmark, user_id;

    SharedPreferences settingsMyPre;
    SharedPreferences settings,settings_color;
    public static final String PREFS_NAME_COLOR = "ThemePrefs";
    String color_code;
    SharedPreferences.Editor editorMyPre;
    SharedPreferences.Editor editor, editor_color;
    View view1;


    public Fragment_Health() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_items, container, false);

        home_gsList = new ArrayList<>();
        random = new Random();

        //typecasting
        settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        str_user_id = settings.getString("tbl_user_id","");

        settings_color = getActivity().getSharedPreferences(PREFS_NAME_COLOR, 0);
        editor_color = settings_color.edit();
        color_code = settings_color.getString("color", "");

        view1 = (View) view.findViewById(R.id.view1);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        home_adapter = new Home_Adapter(recyclerView, home_gsList, getActivity(),"हेल्थ");
        home_adapter.clear();
        recyclerView.setAdapter(home_adapter);
        swipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_layout);

        loader = (RelativeLayout)view.findViewById(R.id.loader);

        //loadListViewData
        loadHealthNews();

        //refreshing
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // swipeRefreshLayout.setRefreshing(true);
                //       Log.d("Swipe", "Refreshing Number");
                loadHealthNews();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        home_adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                home_gsList.add(null);
                home_adapter.notifyItemInserted(home_gsList.size() - 1);
                String a = String.valueOf(recyclerView.getAdapter().getItemCount());
                oldnumber = Integer.parseInt(a);
                newStartNumber = oldnumber + 1;
                newEndNUmber = newStartNumber + 4;
                oldnumber = newEndNUmber;
                cateId="19";
                loadMoreHealthNews();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            home_gsList.remove(home_gsList.size() - 1);
                            home_adapter.notifyItemRemoved(home_gsList.size());
                        }catch (Exception e){
                        }
                        home_adapter.notifyDataSetChanged();
                        home_adapter.setLoaded();
                    }
                }, 2000);


            }
        });


        setColor(color_code);
        {

            if (color_code.equals("BLUE")) {
                setColor("BLUE");

            } else if (color_code.equals("GREEN")) {
                setColor("GREEN");

            } else if (color_code.equals("ORANGE")) {
                setColor("ORANGE");

            } else if (color_code.equals("SKYBLUE")) {
                setColor("SKYBLUE");
            }
        }

        return view;

    }




    private void loadHealthNews() {
        //  Toast.makeText(this, "loadherors", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HEALTH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

                                 Log.d("HealthNews", result);
                        String response = result.toString();
                        loader.setVisibility(View.GONE);

                        try {
                            JSONObject new_array = new JSONObject(response);
                            JSONArray arr = new_array.getJSONArray("result");

                            for (int i = 0; i < arr.length(); i++) {
                                new_array = arr.getJSONObject(i);

                                news_title = new_array.getString("news_title");
                                news_image = new_array.getString("news_image");
                                news_description = new_array.getString("news_description");
                                news_date = new_array.getString("news_date");
                                news_id = new_array.getString("news_id");
                                news_cate = new_array.getString("category_name");


                                Home_GS home_gs = new Home_GS(news_title, news_image, news_description, news_id, news_date, "");
                                home_gsList.add(home_gs);
                            }

                            home_adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //             Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        //   Toast.makeText(this, ""+stringRequest, Toast.LENGTH_SHORT).show();
    }

    //moreData


    private void loadMoreHealthNews() {
        //  Toast.makeText(this, "loadherors", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOADMORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                  //      Log.d("json", result);
                        //   Toast.makeText(context, ""+result, Toast.LENGTH_SHORT).show();
                        String response = result.toString();

                        try {
                            home_gsList.remove(home_gsList.size() - 1);
                            home_adapter.notifyItemRemoved(home_gsList.size());
                            JSONObject new_array = new JSONObject(response);
                            JSONArray arr = new_array.getJSONArray("result");
                            for (int i = 0; i < arr.length(); i++) {
                                new_array = arr.getJSONObject(i);

                                news_title = new_array.getString("news_title");
                                news_image = new_array.getString("news_image");
                                news_description = new_array.getString("news_description");
                                news_date = new_array.getString("news_date");
                                news_id = new_array.getString("news_id");
                                news_cate = new_array.getString("category_name");


                                Home_GS home_gs = new Home_GS(news_title, news_image, news_description, news_id, news_date, "");
                                home_gsList.add(home_gs);

                            }
                           /* home_adapter = new Home_Adapter(getActivity(),0,arrayList);
                            l1.setAdapter(home_adapter);*/
                            home_adapter.notifyDataSetChanged();
                            //   home_adapter.clear();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (ArrayIndexOutOfBoundsException e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

                    }

                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("startNUmber", String.valueOf(newStartNumber));
                params.put("endNmber",String.valueOf(newEndNUmber) );
                params.put("catID", cateId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        //   Toast.makeText(this, ""+stringRequest, Toast.LENGTH_SHORT).show();
    }

    private void setColor(String color_code) {
        if (color_code.equals("BLUE")) {
            view1.setBackgroundColor(getResources().getColor(R.color.Light_Light_Blue));
        } else if (color_code.equals("GREEN")) {
            view1.setBackgroundColor(getResources().getColor(R.color.LightGreen));
        } else if (color_code.equals("ORANGE")) {
            view1.setBackgroundColor(getResources().getColor(R.color.LightOrange));

        } else if (color_code.equals("SKYBLUE")) {
            view1.setBackgroundColor(getResources().getColor(R.color.LightSkyBlue));
        }

    }

}
