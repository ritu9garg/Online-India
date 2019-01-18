package onlineind.com.fragment_package;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import onlineind.com.Activity_Package.Description_Activity;
import onlineind.com.Activity_Package.OnLoadMoreListener;
import onlineind.com.Adapter_Package.CustomPageAdapter;
import onlineind.com.Adapter_Package.Home_main_Adapter;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;

import static onlineind.com.Https_Urls.Urls.HOME;
import static onlineind.com.Https_Urls.Urls.LOADMORE;


public class Fragment_Home extends Fragment
{
    View view;
    Context context;
    RelativeLayout loaderitem;
    LinearLayout loader;
    private List<Home_GS> home_gsList;
    ArrayList<Home_GS> arrayList1 = new ArrayList<>();
    int oldnumber, newStartNumber, newEndNUmber;
    public View ftView;

    Home_main_Adapter home_adapter;
    RecyclerView recyclerView;
    String newsID;
    String[] imgs, titel, discription,id, date;
    String string, position, news_id, str_user_id, str_android_id;
    HashMap<String, String> hashMap;
    public static final String PREFS_NAME = "LoginPrefs";
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    private SwipeRefreshLayout swipeRefreshLayout;
    String cateId;
    String news_title, news_image, news_description, news_date, news_cate;
    int index = 10, pos;
    private Random random;

    String liked, bookmark, user_id;

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    CustomPageAdapter mCustomPagerAdapter;
    private Handler handler;
    private final int delay = 2000;
    private int page = 0;
    Runnable runnable;

    public Fragment_Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragment__home, container, false);

        home_gsList = new ArrayList<>();
        random = new Random();

        settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        str_user_id = settings.getString("tbl_user_id", "मुख्य ख़बरें");


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        home_adapter = new Home_main_Adapter(recyclerView, home_gsList, getActivity());
        home_adapter.clear();
        recyclerView.setAdapter(home_adapter);


        handler = new Handler();
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);




        swipeRefreshLayout = (SwipeRefreshLayout)view. findViewById(R.id.swipe_refresh_layout);

        LayoutInflater li = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);

        loaderitem = (RelativeLayout) view.findViewById(R.id.loaderitem);
        loader = (LinearLayout) view.findViewById(R.id.loader);

        loadHomeNews();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadHomeNews();
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
                cateId="NA";
                loadMoreHomeNews();
                new android.os.Handler().postDelayed(new Runnable() {
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int mPosition = 0;
            int mOffset = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int position = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                View v = recyclerView.getChildAt(0);
                int offset = (v == null) ? 0 : v.getTop();
                if (mPosition < position || (mPosition == position && mOffset < offset))
                {
                    // Now I have to check if the user has scrolled down or up.
                    viewPager.setVisibility(View.GONE);
                }else
                {
                    viewPager.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        return view;
    }

    private List<Home_GS> dataSource() {
        List<Home_GS> data = new ArrayList<>();
        data.add(new Home_GS(titel[0], "http://www.onlineind.com/"+imgs[0],discription[0],id[0],date[0],""));
        data.add(new Home_GS(titel[1], "http://www.onlineind.com/"+imgs[1],discription[1],id[1],date[1],""));
        data.add(new Home_GS(titel[2], "http://www.onlineind.com/"+imgs[2],discription[2],id[2],date[2],""));
        data.add(new Home_GS(titel[3], "http://www.onlineind.com/"+imgs[3],discription[3],id[3],date[3],""));

        return data;
    }
    @Override
   public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }
/*

    @SuppressLint("ResourceAsColor")
    public void slider() {

        hashMap = new HashMap<>();
        hashMap.put("1."+titel[0], "http://www.onlineind.com/" + imgs[0]);
        hashMap.put("2."+titel[1], "http://www.onlineind.com/" + imgs[1]);
        hashMap.put("3."+titel[2], "http://www.onlineind.com/" + imgs[2]);
        hashMap.put("4."+titel[3], "http://www.onlineind.com/" + imgs[3]);
     */
/*   hashMap.put(titel[4], "http://www.onlineind.com/" + imgs[5]);
        hashMap.put(titel[5], "http://www.onlineind.com/" + imgs[5]);*//*

        for (String name : hashMap.keySet()) {

            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .description(name)
                    .image(hashMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
        sliderLayout.addOnPageChangeListener(this);


    }
    @Override
    public void onSliderClick(BaseSliderView slider) {

       //Toast.makeText(getActivity(), titel[pos]+""+pos, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), Description_Activity.class);
        intent.putExtra("title_dec", "मुख्य ख़बरें ");
      //  intent.putExtra("bm", bookmark);
        if (pos==3){
            pos=0;
        }else if (pos==0){
            pos=3;
        }else if (pos==1){
            pos=2;
        }else if (pos==2){
            pos=1;
        }
        intent.putExtra("news_title", titel[pos]);
        intent.putExtra("news_image", imgs[pos]);
        intent.putExtra("news_id",id[pos]);
        intent.putExtra("news_description",discription[pos]);
        intent.putExtra("str_user_id",str_user_id);
      //  intent.putExtra("liked", liked);
        startActivity(intent);
      //  getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      */
/*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.onlineind.com/"));
        startActivity(browserIntent);*//*

    }
*/


    private void loadHomeNews() {
        //  Toast.makeText(this, "loadherors", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {

                     //   Log.d("json", result);
                        String response = result.toString();
                        loader.setVisibility(View.GONE);
                        try {
                            JSONObject new_array = new JSONObject(response);
                            JSONArray arr = new_array.getJSONArray("result");
                            imgs = new String[4];
                            titel = new String[4];
                            discription = new String[4];
                            date = new String[4];
                            id = new String[4];


                            for (int i = 0; i < arr.length(); i++) {
                                new_array = arr.getJSONObject(i);

                                 news_title = new_array.getString("news_title");
                                 news_image = new_array.getString("news_image");
                                 news_description = new_array.getString("news_description");
                                 news_date = new_array.getString("news_date");
                                news_id = new_array.getString("news_id");
                                news_cate = new_array.getString("category_name");

                                if (i < imgs.length) {
                                    imgs[i] = new_array.getString("news_image");

                                }
                                if (i < titel.length) {
                                    titel[i] = new_array.getString("news_title");

                                }if (i < discription.length) {
                                    discription[i] = new_array.getString("news_description");

                                }if (i < date.length) {
                                    date[i] = new_array.getString("news_date");

                                }if (i < id.length) {
                                    id[i] = new_array.getString("news_id");

                                }
                                Home_GS home_gs = new Home_GS(news_title, news_image, news_description, news_id, news_date, news_cate );
                                home_gsList.add(home_gs);

                            }
                            List<Home_GS> getData = dataSource();
                            mCustomPagerAdapter = new CustomPageAdapter(getActivity(), getData);
                            viewPager.setAdapter(mCustomPagerAdapter);

                            runnable = new Runnable() {
                                public void run() {
                                    if (mCustomPagerAdapter.getCount() == page) {
                                        page = 0;
                                    } else {
                                        page++;
                                    }
                                    viewPager.setCurrentItem(page, true);
                                    handler.postDelayed(this, delay);
                                }
                            };

                            //  slider();
                           /* home_adapter = new Home_Adapter(getActivity(),0,arrayList);
                            l1.setAdapter(home_adapter);*/

                            home_adapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
            //            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        //   Toast.makeText(this, ""+stringRequest, Toast.LENGTH_SHORT).show();
    }

/*

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      //  Log.d("position",position+"");
        pos=position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }*/
    ///////
    private void loadMoreHomeNews() {
        //  Toast.makeText(this, "loadherors", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOADMORE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                  //         Log.d("json", result);
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


                                Home_GS home_gs = new Home_GS(news_title, news_image, news_description, news_id, news_date, news_cate);
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
}