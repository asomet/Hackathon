package fr.wildcodeschool.hackathon;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomMarkerAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomMarkerAdapter(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // on récupère les infos associées au tag
        MarkerInfos infos = (MarkerInfos) marker.getTag();

        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.item_marker, null);

        TextView markerName = view.findViewById(R.id.marker_name);
        TextView markerDescription = view.findViewById(R.id.marker_description);
        ImageView markerImage = view.findViewById(R.id.marker_image);

        markerName.setText(infos.getTitle());
        markerDescription.setText(infos.getDescription());
        markerImage.setImageDrawable(context.getResources().getDrawable(infos.getImage()));

        return view;
    }
}
