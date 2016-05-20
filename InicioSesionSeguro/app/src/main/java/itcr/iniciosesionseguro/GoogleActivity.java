package itcr.iniciosesionseguro;

import android.content.Intent;
        import android.net.Uri;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import com.android.volley.Network;
        import com.android.volley.toolbox.ImageLoader;
        import com.android.volley.toolbox.NetworkImageView;
        import java.net.URI;
public class GoogleActivity extends AppCompatActivity {
    //Image Loader object
    private ImageLoader imageLoader;
    private NetworkImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);
        Intent intent = getIntent();
//Getting values from intent

        String name = intent.getStringExtra("Name");
        String email = intent.getStringExtra("Email");
        String id = intent.getStringExtra("Id");
        String photo = intent.getStringExtra("Photo");
        TextView userName = (TextView)
                findViewById(R.id.textViewUsername);
        TextView userEmail =(TextView)
                findViewById(R.id.textViewEmail);
        profileImage = (NetworkImageView)
                findViewById(R.id.profileImage);
        imageLoader =
                CustomVolleyRequest.getInstance(this).getImageLoader();
        imageLoader.get(photo,
                ImageLoader.getImageListener(profileImage, R.mipmap.ic_launcher,
                        android.R.drawable.ic_dialog_alert));
        profileImage.setImageUrl(photo, imageLoader);
        userName.setText(name);
        userEmail.setText(email);
    }
}