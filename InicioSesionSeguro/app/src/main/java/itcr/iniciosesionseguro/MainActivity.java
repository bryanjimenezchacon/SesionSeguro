package itcr.iniciosesionseguro;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import io.fabric.sdk.android.Fabric;



public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "xlFlwft8uKGRG66sNtQaK8CVo";
    private static final String TWITTER_SECRET = "juZD59AfSGIMfNpzU9HAqqUw6wpwOyeHUEJCh7LEAPKi3eoCwZ";
    //Tags to send the username and image url to next activity using intent
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE_URL = "image_url";
    //Twitter Login Button
    TwitterLoginButton twitterLoginButton;
    //google api client
    private GoogleApiClient mGoogleApiClient;
    //Signin constant to check the activity result
    private int RC_SIGN_IN = 100;
    public DBHandler db = new DBHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Initializing TwitterAuthConfig, these two line will alsoadded automatically while configuration we did
        TwitterAuthConfig authConfig = new
                TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        // Configure sign-in to request the user's ID, emailaddress, and basic
// profile. ID and basic profile are included inDEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this
/* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
// ...
                }
            }
        });
//Initializing twitter login button
        twitterLoginButton = (TwitterLoginButton)
                findViewById(R.id.twitterLogin);
//Adding callback to the button
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {//If login succeeds passing the Calling the loginmethod and passing Result object
                login(result);
            }
            @Override
            public void failure(TwitterException exception) {
//If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        Button iniciarSesion = (Button) findViewById(R.id.iniciar);
        Button registrarse = (Button)
                findViewById(R.id.registrarse);
        iniciarSesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                EditText correo = (EditText)
                        findViewById(R.id.correo);
                EditText contraseña = (EditText)
                        findViewById(R.id.contraseña);
                validarUsuario(correo.getText().toString(),
                        contraseña.getText().toString());
            }
        });
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        RegistroActivity.class);
                startActivity(intent);
            }
        });
    }
    private void signIn() {
        Intent signInIntent =
                Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int
            resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//Adding the login result back to the button
// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result =
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            twitterLoginButton.onActivityResult(requestCode,
                    resultCode, data);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Intent intent = new Intent(MainActivity.this,
                    GoogleActivity.class);
//Adding the values to intent
            intent.putExtra("Name", personName);
            intent.putExtra("Email", personEmail);
            intent.putExtra("Id", personId);
            intent.putExtra("Photo", personPhoto.toString());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error de conexion",
                    Toast.LENGTH_LONG).show();
        }
    }
    //The login function accepting the result object
    public void login(Result<TwitterSession> result) {
//Creating a twitter session with result's data
        TwitterSession session = result.data;
//Getting the username from session
        final String username = session.getUserName();
//This code will fetch the profile image URL
//Getting the account service of the user logged in
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new
                        Callback<User>() {
                            @Override
                            public void failure(TwitterException e) {
//If any error occurs handle it here
                            }
                            @Override
                            public void success(Result<User> userResult) {
//If it succeeds creating a User objectfrom userResult.data
                                User user = userResult.data;
//Getting the profile image url
                                String profileImage =
                                        user.profileImageUrl.replace("_normal", "");
//Creating an Intent
                                Intent intent = new
                                        Intent(MainActivity.this, ProfileActivity.class);
//Adding the values to intent
                                intent.putExtra(KEY_USERNAME, username);
                                intent.putExtra(KEY_PROFILE_IMAGE_URL,
                                        profileImage);
//Starting intent
                                startActivity(intent);
                            }
                        });
    }


    public void validarUsuario(String correo, String contraseña) {
        Usuario usuario = db.getUsuario(correo);
        String md5Pass = md5(contraseña);
        if (usuario.getCorreo().equals(correo) &&
                usuario.getContraseña().equals(md5Pass)) {
            Intent intent = new Intent(MainActivity.this,
                    IngresoActivity.class);
            intent.putExtra("nombre", usuario.getNombre());
            intent.putExtra("correo", usuario.getCorreo());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Lo valores no coinciden para un usuario registrado"
                    , Toast.LENGTH_LONG);
        }
    }
    public static String md5(String string) {
        try {
            MessageDigest digest =
                    java.security.MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF &
                        messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    public void onConnectionFailed(ConnectionResult
                                           connectionResult) {
    }
}