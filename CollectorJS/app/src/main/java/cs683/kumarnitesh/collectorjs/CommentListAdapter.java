package cs683.kumarnitesh.collectorjs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import cs683.kumarnitesh.collectorjs.Model.Comment;
import cs683.kumarnitesh.collectorjs.Model.Post;
import cs683.kumarnitesh.collectorjs.Util.Time;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    private List<Comment> mComments;
    private Context mContext;
    private static final String TAG = CommentListAdapter.class.getSimpleName();



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView commentTextView,commentUserTextView;

        public CommentViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            commentTextView =  v.findViewById(R.id.comment_Text);
            commentUserTextView = v.findViewById(R.id.comment_user_info);



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CommentListAdapter(Context context, List<Comment> comments) {
        this.mComments = comments;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_comment, parent, false);

        return new CommentViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CommentViewHolder commentholder, final int position) {

        final Comment _comment = this.mComments.get(position);
        String submissionInfo = _comment.getUserName() +" - "+ Time.getTimeAgo(_comment.getTimeStamp());
        commentholder.commentTextView.setText(_comment.getComment());
        commentholder.commentUserTextView.setText(submissionInfo);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.mComments.size();
    }



}

