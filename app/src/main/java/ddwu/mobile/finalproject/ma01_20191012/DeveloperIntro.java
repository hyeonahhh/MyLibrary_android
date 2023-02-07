package ddwu.mobile.finalproject.ma01_20191012;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DeveloperIntro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer);
        ImageView imageView = findViewById(R.id.image_developer);
        imageView.setImageResource(R.mipmap.developer);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:010-4640-8115"));
                startActivity(intent);
                break;
            case R.id.image_github:
                Intent intent_view = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/hyeonahhh"));
                startActivity(intent_view);
        }
    }
}
