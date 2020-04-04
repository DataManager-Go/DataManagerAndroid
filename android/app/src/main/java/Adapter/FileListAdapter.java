package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Models.File;
import de.jojii.datamanagerandroid.R;

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_file_name, tv_file_date;
        public ImageView iv_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_file_date = itemView.findViewById(R.id.tv_file_creation_date);
            tv_file_name = itemView.findViewById(R.id.tv_file_name);
            iv_icon = itemView.findViewById(R.id.iv_file_icon);
        }
    }

    public List<File> mFiles;
    public FileListAdapter(List<File> files){
        mFiles = files;
    }

    @NonNull
    @Override
    public FileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View fileView = inflater.inflate(R.layout.file_list_item, parent, false);
        return new ViewHolder(fileView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File file = mFiles.get(position);
        holder.tv_file_name.setText(file.name);
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }
}
