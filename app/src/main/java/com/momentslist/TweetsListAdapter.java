package com.momentslist;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lalic.twmoments.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.widgets.recycleviewwrap.BaseRecyclerAdapter;
import com.widgets.recycleviewwrap.BaseRecyclerViewHolder;

import java.util.List;

/**
 * Created by liuyuwei on 2016/9/17.
 */
public class TweetsListAdapter extends BaseRecyclerAdapter<TweetsBean> {
    /**
     * @param context context
     */

    public TweetsListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutRes(int viewType) {
        return R.layout.tweets_item;
    }

    @Override
    public void convert(BaseRecyclerViewHolder holder, final TweetsBean item, int position) {

        ImageView iv = holder.getView(R.id.avatar);
        ImageLoader.getInstance().displayImage(item.getSender().getAvatar(), iv);

        holder.setViewOnClickListener(R.id.avatar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击左侧头像 Username = " + item.getSender().getUsername(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.setText(R.id.name, item.getSender().getUsername());
        holder.setViewOnClickListener(R.id.name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击名字 Username = " + item.getSender().getUsername(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.setText(R.id.content, item.getContent());
        if (item.getComments() != null) {
            holder.setComments(R.id.comments_list, item.getComments());
        } else {
            holder.setVisible(R.id.comments_list, View.GONE);
        }

        if (item.getImages() != null) {
            setImages(holder, R.id.images, item.getImages());
        } else {
            holder.setVisible(R.id.images, View.GONE);
        }


        holder.setViewOnClickListener(R.id.add_comment, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击回复 回复对象Username = " + item.getSender().getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setImages(BaseRecyclerViewHolder holder, int imagesid, final List<TweetsBean.imageurl> images) {
        if (images.size() == 0) {
            holder.setVisible(imagesid, View.GONE);
            return;
        }
        holder.setVisible(imagesid, View.VISIBLE);
        ImageView[] iv = new ImageView[9];
        iv[0] = holder.getView(R.id.image0);
        iv[1] = holder.getView(R.id.image1);
        iv[2] = holder.getView(R.id.image2);
        iv[3] = holder.getView(R.id.image3);
        iv[4] = holder.getView(R.id.image4);
        iv[5] = holder.getView(R.id.image5);
        iv[6] = holder.getView(R.id.image6);
        iv[7] = holder.getView(R.id.image7);
        iv[8] = holder.getView(R.id.image8);

        for (int i = 0; i < images.size(); i++) {
            iv[i].setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(images.get(i).getUrl(), iv[i], new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    ImageView iv=((ImageView) view);
                    if(iv!=null){
                        iv.setBackgroundColor(0xffffff);
                        iv.setImageResource(R.drawable.failed);
                    }
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ImageView iv=((ImageView) view);
                    if(iv!=null){
                        iv.setBackgroundColor(0xffffff);
                        iv.setImageBitmap(bitmap);
                    }


                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    ImageView iv=((ImageView) view);
                    if(iv!=null){
                        iv.setBackgroundColor(0xffffff);
                        iv.setImageResource(R.drawable.failed);
                    }

                }
            });
        }
        for (int j = images.size(); j < 9; j++) {
            iv[j].setVisibility(View.GONE);
        }
    }
}
