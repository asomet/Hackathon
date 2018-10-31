package fr.wildcodeschool.hackathon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CandyAdapter extends ArrayAdapter {

    public CandyAdapter (@NonNull Context context, ArrayList<CandyModel> results ) {
        super(context, 0, results);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CandyModel candys = (CandyModel) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_candy, parent, false);
        }
        TextView candyName =convertView.findViewById(R.id.name_candy);
        candyName.setText(candys.getName());

        ImageView icons =convertView.findViewById(R.id.icon_candy);
        switch (candys.getType()){

            case "Chocolate":
                icons.setImageResource(R.drawable.smarties80);
                break;
            case "Lollipops":
                icons.setImageResource(R.drawable.smarties80);
                break;
            case "Jelly Beans":
                icons.setImageResource(R.drawable.smarties80);
                break;
            case "Bubble Gum":
                icons.setImageResource(R.drawable.smarties80);
                break;
            case "Caramel":
                icons.setImageResource(R.drawable.smarties80);
                break;
            case "Tagada":
                icons.setImageResource(R.drawable.smarties80);
                break;
            case "Fudge":
                icons.setImageResource(R.drawable.smarties80);
                break;
            case "Dragibus":
                icons.setImageResource(R.drawable.smarties80);
                break;
            case "Smarties":
                icons.setImageResource(R.drawable.smarties80);
                break;

        }

        return convertView;
    }
}