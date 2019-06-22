package cs683.kumarnitesh.collectorjs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cs683.kumarnitesh.collectorjs.Model.Post;
import cs683.kumarnitesh.collectorjs.Util.LoadProfileImage;
import cs683.kumarnitesh.collectorjs.Util.Time;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder> {

    private List<Post> mPosts;
    private Context mContext;
    private static final String TAG = PostListAdapter.class.getSimpleName();



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout postDetailLayout;
        public TextView titleView, userInfoView,summaryTextBox;
        public TextView txtUpVoteCount, txtDownVoteCount, txtCommentCount;

        public ImageButton viewUpVote, viewDownVote,viewComment;
        public CircleImageView imageView;

        public PostViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });

            postDetailLayout = v.findViewById(R.id.post_detail_layout);
            titleView =  v.findViewById(R.id.post_title);
            userInfoView =  v.findViewById(R.id.post_user_info);
            summaryTextBox = v.findViewById(R.id.post_summary);

            //counts
            txtUpVoteCount = v.findViewById(R.id.txtUpvoteCount);
            txtDownVoteCount = v.findViewById(R.id.txtDownVoteCount);
            txtCommentCount = v.findViewById(R.id.txtCommentCount);

            //image

            imageView = v.findViewById(R.id.post_user_image);

            // ImageButtons

            viewUpVote = v.findViewById(R.id.btnUpVote);
            viewDownVote = v.findViewById(R.id.btnDownVote);
            viewComment = v.findViewById(R.id.btnComments);



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostListAdapter(Context context, List<Post> posts) {
        this.mPosts = posts;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_post, parent, false);


        return new PostViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PostViewHolder postholder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Post _post = this.mPosts.get(position);
        String submissionInfo="";
        if(_post.getUserName()!=null && _post.getUserName().length() > 0)
            submissionInfo= _post.getUserName() +" - "+ Time.getTimeAgo(_post.getTimeInMilliSeconds());
        else
            submissionInfo = Time.getTimeAgo(_post.getTimeInMilliSeconds());
        postholder.titleView.setText(_post.getTitle());
        postholder.userInfoView.setText(submissionInfo);
        postholder.summaryTextBox.setText(_post.getDescription());

        //Image
        if(_post.userImg == null)
            _post.userImg ="default";
        LoadProfileImage.setImageView(Uri.parse(_post.userImg),postholder.imageView);

        // Votes count
        postholder.txtUpVoteCount.setText("("+_post.getUpVote()+")");
        postholder.txtDownVoteCount.setText("("+_post.getDownVote()+")");
        postholder.txtCommentCount.setText("("+_post.getCommentCount()+")");
        // Comments Count

        postholder.viewUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post clickedPost  = mPosts.get(position);
                clickedPost.setUpVote(clickedPost.getUpVote() + 1);
                clickedPost.updatePostVotes();
            }
        });
        postholder.viewDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post clickedPost  = mPosts.get(position);
                clickedPost.setDownVote(clickedPost.getDownVote() + 1);
                clickedPost.updatePostVotes();
            }
        });

        postholder.viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(view.getContext(),CreateComment.class);
                //rmContext.startActivity(browserIntent);
                commentIntent.putExtra("POST_ID",_post.getId());
                view.getContext().startActivity(commentIntent);
            }
        });
        postholder.postDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post clickedPost  = mPosts.get(position);

                String _link = clickedPost.getLink();
                if (!_link.startsWith("http://") && !_link.startsWith("https://"))
                    _link = "http://" + _link;

                //To use chrome default browser
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_link));
//                //rmContext.startActivity(browserIntent);
//                view.getContext().startActivity(browserIntent);
                Intent webviewIntent = new Intent(view.getContext(), WebViewActivity.class);
                webviewIntent.putExtra("URL", _link);
                view.getContext().startActivity(webviewIntent);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.mPosts.size();
    }



}

