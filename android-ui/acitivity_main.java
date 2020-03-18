```java
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConstraintLayout root_container = new ConstraintLayout(this);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(R.id.linearLayout);

        TextView tv1 = new TextView(this);
        tv1.setText("Large Text1");
        tv1.setTextAppearance(this,R.style.TextAppearance_AppCompat_Large);
        linearLayout.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText("Large Text2");
        tv2.setTextAppearance(this,R.style.TextAppearance_AppCompat_Large);
        linearLayout.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText("Large Text3");
        tv3.setTextAppearance(this,R.style.TextAppearance_AppCompat_Large);
        linearLayout.addView(tv3);

        root_container.addView(linearLayout);

        Button ok = new Button(this);
        ok.setId(R.id.button1);
        ok.setText(android.R.string.ok);

        Button cancel = new Button(this);
        cancel.setId(R.id.button2);
        cancel.setText(android.R.string.cancel);

        root_container.addView(ok);
        root_container.addView(cancel);

        int topMarginDP = Math.round(12 * getResources().getDisplayMetrics().density);
        int leftMarginDP = Math.round(12 * getResources().getDisplayMetrics().density);
        int rightMarginDP = Math.round(12 * getResources().getDisplayMetrics().density);

        ConstraintSet applyConstraintSet = new ConstraintSet();
        applyConstraintSet.clone(root_container);
        applyConstraintSet.connect(R.id.linearLayout, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        applyConstraintSet.connect(R.id.button1, ConstraintSet.TOP,R.id.linearLayout,ConstraintSet.BOTTOM,topMarginDP);
        applyConstraintSet.connect(R.id.button1, ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,leftMarginDP);
        applyConstraintSet.connect(R.id.button2, ConstraintSet.TOP,R.id.linearLayout,ConstraintSet.BOTTOM,topMarginDP);
        applyConstraintSet.connect(R.id.button2, ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,rightMarginDP);
        applyConstraintSet.applyTo(root_container);

        setContentView(root_container);
    }
}
```