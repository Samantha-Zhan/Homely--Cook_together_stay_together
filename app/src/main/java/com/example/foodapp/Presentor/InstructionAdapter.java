package com.example.foodapp.Presentor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodapp.Module.DetailRecipe;
import com.example.foodapp.Module.FavDB;
import com.example.foodapp.R;

public class InstructionAdapter extends BaseAdapter {
    private String[] instructions;
    LayoutInflater mInflater;  // take the my-listview-detail layout,
    Context context;
    // "inflate" it with content and connect it with
    //the myListView

    public InstructionAdapter(Context c, String[] instructions) {
        this.context = c;
        this.instructions = instructions;
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return instructions.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(convertView == null){
            v = mInflater.inflate(R.layout.instruction_sample,null);
        }
        TextView steps = v.findViewById(R.id.stepNum);
        TextView instruction = v.findViewById(R.id.instruction);
        steps.setText(position+1+"");
        instruction.setText(instructions[position]);
        return v;
    }



}

