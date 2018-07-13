package project.internship.tcs.collegeattendance;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ATTENDANCERESULT extends ArrayAdapter<STUDENTATTENDANCE> {
    List<STUDENTATTENDANCE> searchresults;
    Activity activity;
    public ATTENDANCERESULT(Activity activity,List<STUDENTATTENDANCE> searchresults){
        super(activity,R.layout.customcardlist,searchresults);
        this.activity=activity;
        this.searchresults=searchresults;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= activity.getLayoutInflater().inflate(R.layout.customcardlist,null);
        ImageView imageView=convertView.findViewById(R.id.image);
        TextView name=convertView.findViewById(R.id.name);
        TextView rollno=convertView.findViewById(R.id.rollno);
        TextView mobileno=convertView.findViewById(R.id.mobileno);
        LinearLayout back=convertView.findViewById(R.id.back);
        STUDENTATTENDANCE searchresult=searchresults.get(position);
        Picasso.with(getContext()).load(searchresult.getPic()).fit().centerCrop().into(imageView);
        name.setText(searchresult.getName());
        rollno.setText(searchresult.getRollno());
        mobileno.setText(searchresult.getMobileno());
        if(searchresult.isPresent())
            back.setBackgroundColor(Color.parseColor("#3EA055"));
            else
            back.setBackgroundColor(Color.parseColor("#B6B6B4"));
        return convertView;
    }
}
