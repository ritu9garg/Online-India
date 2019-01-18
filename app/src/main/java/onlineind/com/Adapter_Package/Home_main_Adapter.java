package onlineind.com.Adapter_Package;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import onlineind.com.Activity_Package.Description_Activity;
import onlineind.com.Activity_Package.OnLoadMoreListener;
import onlineind.com.GS_Package.Home_GS;
import onlineind.com.R;
import pl.droidsonroids.gif.GifImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by admin on 8/30/2017.
 */

public class Home_main_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context ctx;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    View itemView;
    SharedPreferences sf;
    SharedPreferences.Editor et;

    String str_android_id,str_user_id;
    private List<Home_GS> oList;
    Activity activity;

    public Home_main_Adapter(RecyclerView recyclerView, List<Home_GS> oList, Activity activity ) {
        this.oList = oList;
        this.activity = activity;


        //setHasStableIds(true);
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
    @Override
    public int getItemViewType(int position) {
        return oList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.fragment_news, parent, false);
            return new VideoInfoHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof VideoInfoHolder) {
            final VideoInfoHolder videoInfoHolder = (VideoInfoHolder) holder;

            final Home_GS home_gs = oList.get(position);
         //  videoInfoHolder.title.setText(Html.fromHtml((home_gs.getNews_title())+home_gs.getCate()));
            videoInfoHolder.title.setText(Html.fromHtml((home_gs.getNews_title())));

            videoInfoHolder.content.setText(Html.fromHtml(home_gs.getnews_description()));
           videoInfoHolder.cate.setText(Html.fromHtml(home_gs.getCate()));
            Picasso.with(activity).load("http://onlineind.com/" + home_gs.getNews_image()).into(videoInfoHolder.img);

            videoInfoHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // loader when click in listview items
                    videoInfoHolder.loaderitem.setVisibility(VISIBLE);
                    android.os.Handler handler = new android.os.Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            videoInfoHolder.loaderitem.setVisibility(GONE);
                        }
                    }, 1000);

                    //_____*______________

                   Intent intent = new Intent(activity, Description_Activity.class);
                intent.putExtra("title_dec", "मुख्य ख़बरें ");
                intent.putExtra("news_title", home_gs.getNews_title());
                intent.putExtra("news_image", home_gs.getNews_image());
                intent.putExtra("news_description", home_gs.getnews_description());
                intent.putExtra("news_date", home_gs.getNews_date());
                intent.putExtra("news_id",home_gs.getNews_id());
                activity.startActivity(intent);


                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return oList == null ? 0 : oList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder {

        public TextView title, content, news_id , cate;
        ImageView img;
        GifImageView loaderitem;
        LinearLayout linearLayout;

        public VideoInfoHolder(View itemView) {
            super(itemView);

            loaderitem = (GifImageView)itemView.findViewById(R.id.loaderitem);
            loaderitem.setVisibility(GONE);
            title = (TextView) itemView.findViewById(R.id.titel1);
            content = (TextView) itemView.findViewById(R.id.content1);
            cate = (TextView)itemView.findViewById(R.id.cate) ;
            img = (ImageView) itemView.findViewById(R.id.image1);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }

    public void clear() {
        oList.clear();
    }
    @Override
    public long getItemId(int position) {
        return oList.size();
    }

}