package cz.utb.fai.dodo.sharemyloc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cz.utb.fai.dodo.sharemyloc.ListItem;
import cz.utb.fai.dodo.sharemyloc.R;

/**
 * Created by Dodo on 29.01.2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private List<ListItem> listItems;
    //private Context context;

    public MyAdapter(List<ListItem> listItems/*, Context context*/) {
        this.listItems = listItems;
        //this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);

        holder.fullName.setText(listItem.getFullName());
        // holder.checkBox.setChecked(listItem.isChecked());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView fullName;
        private Context activity;
        //public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.textView);
            //checkBox = itemView.findViewById(R.id.checkBox);
/*
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Add the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Zrušiť", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            final AlertDialog dialog = builder.create();*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  dialog.setMessage("Showing position");
                    dialog.show();*/
                    Log.d("ViewHolderClicked","Clicked");
                }
            });
        }

        public Context getActivity() {
            return activity;
        }
    }

}
