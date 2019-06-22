package cs683.kumarnitesh.collectorjs.Util;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import cs683.kumarnitesh.collectorjs.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoadProfileImage {
    public static void setImageView(final Uri selectedImage, final CircleImageView imageView) {
        Picasso.get()
                .load(selectedImage)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(selectedImage)
                                .placeholder(R.drawable.ic_person_grey)
                                .error(R.drawable.ic_person_grey).into(imageView);

                    }


                });
    }
    public static void setImageView(final Uri selectedImage, final ImageView imageView) {
        Picasso.get()
                .load(selectedImage)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(selectedImage)
                                .placeholder(R.drawable.ic_person_grey)
                                .error(R.drawable.ic_person_grey).into(imageView);

                    }


                });
    }
    public static void setImageView(final String selectedImage, final ImageView imageView) {
        Picasso.get()
                .load(Uri.parse(selectedImage))
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(selectedImage)
                                .placeholder(R.drawable.ic_person_grey)
                                .error(R.drawable.ic_person_grey).into(imageView);

                    }


                });
    }
}
