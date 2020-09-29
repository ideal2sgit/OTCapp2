package com.example.faten.testsql.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.faten.testsql.R ;
import com.example.faten.testsql.model.Article;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by fatima on 20/01/2017.
 */
public class ArticleQtAdapterRV extends RecyclerView.Adapter<ArticleQtAdapterRV.ViewHolder> {


    private final Activity activity;
    private ArrayList<Article> listArticle = new ArrayList<>();
    public static String login ;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");


    public ArticleQtAdapterRV(Activity activity  , ArrayList<Article> listArticle) {

        this.activity=activity;
        this.listArticle=listArticle;

    }






    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_cmd, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Article articleCMD = listArticle.get(position);

       /* if (articleCMD.getNbrCLick() ==0) {
            listArticle.remove(position);
            //holder.card_article_cmd.removeViewAt(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listArticle.size());
        }
        else {*/
            //holder.card_article_cmd.addView();
            holder.txt_code_article.setText(articleCMD.getCodeArticle());
            holder.txt_qt_a_commander.setText(articleCMD.getNbrCLick() + "");
       // }

    }




    @Override
    public int getItemCount() {

        Log.e("size" ,""+listArticle.size()) ;
        return listArticle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView  txt_code_article;
        public TextView txt_qt_a_commander;
        public CardView card_article_cmd ;

        public ViewHolder(View itemView) {
            super(itemView);


            card_article_cmd  = (CardView)   itemView.findViewById(R.id.card_article_cmd) ;
            txt_code_article   = (TextView)  itemView.findViewById(R.id.txt_code_article  );
            txt_qt_a_commander = (TextView) itemView.findViewById(R.id.txt_qt_a_commander);

        }


    }

}
