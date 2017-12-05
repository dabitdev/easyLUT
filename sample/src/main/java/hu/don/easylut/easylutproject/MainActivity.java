package hu.don.easylut.easylutproject;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import hu.don.easylut.EasyLUT;
import hu.don.easylut.filter.Filter;
import hu.don.easylut.filter.LutFilterFromResource;
import hu.don.easylut.lutimage.CoordinateToColor;
import hu.don.easylut.lutimage.LutAlignment;


public class MainActivity extends AppCompatActivity implements FilterAdapter.OnFilterSelected {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final boolean RESIZE_BITMAP = true;

    private ImageView ivImage;
    private TextView tvName;
    private ProgressBar pbBusy;
    private RecyclerView rvFilters;

    private Bitmap originalBitmap;
    private final List<FilterSelection> effectItems = new LinkedList<>();
    private FilterSelection lastFilterSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = findViewById(R.id.tv_name);
        ivImage = findViewById(R.id.iv_image);
        pbBusy = findViewById(R.id.pb_busy);
        Resources resources = getResources();
        ivImage.post(new Runnable() {
            @Override
            public void run() {
                setImage(R.drawable.landscape);
            }
        });

        rvFilters = findViewById(R.id.rv_filters);

        LutFilterFromResource.Builder squareBgr =
                EasyLUT.fromResourceId().withColorAxes(CoordinateToColor.Type.RGB_TO_ZYX).withResources(resources);
        LutFilterFromResource.Builder squareRgb =
                EasyLUT.fromResourceId().withColorAxes(CoordinateToColor.Type.RGB_TO_XYZ).withResources(resources);
        LutFilterFromResource.Builder haldRgb =
                EasyLUT.fromResourceId().withColorAxes(CoordinateToColor.Type.RGB_TO_XYZ).withResources(resources)
                       .withAlignmentMode(LutAlignment.Mode.HALD);

        addFilter("none", EasyLUT.createNonFilter());
        addFilter("identity", squareRgb.withLutBitmapId(R.drawable.identity).createFilter());
        addFilter("identity_square_2", squareRgb.withLutBitmapId(R.drawable.identity_square_2).createFilter());
        addFilter("identity_hald", haldRgb.withLutBitmapId(R.drawable.identity_hald).createFilter());
        addFilter("identity_hald_2", haldRgb.withLutBitmapId(R.drawable.identity_hald_2).createFilter());
        addFilter("square_8_00", squareRgb.withLutBitmapId(R.drawable.filter_square_8_00).createFilter());
        addFilter("square_8_01", squareRgb.withLutBitmapId(R.drawable.filter_square_8_01).createFilter());
        addFilter("square_8_02", squareRgb.withLutBitmapId(R.drawable.filter_square_8_02).createFilter());
        addFilter("square_8_03", squareRgb.withLutBitmapId(R.drawable.filter_square_8_03).createFilter());
        addFilter("square_8_04", squareRgb.withLutBitmapId(R.drawable.filter_square_8_04).createFilter());
        addFilter("square_8_05", squareRgb.withLutBitmapId(R.drawable.filter_square_8_05).createFilter());
        addFilter("square_8_06", squareRgb.withLutBitmapId(R.drawable.filter_square_8_06).createFilter());
        addFilter("square_8_07", squareRgb.withLutBitmapId(R.drawable.filter_square_8_07).createFilter());
        addFilter("square_8_08", squareRgb.withLutBitmapId(R.drawable.filter_square_8_08).createFilter());
        addFilter("square_8_09", squareRgb.withLutBitmapId(R.drawable.filter_square_8_09).createFilter());
        addFilter("square_8_09", squareRgb.withLutBitmapId(R.drawable.filter_square_8_09).createFilter());
        addFilter("square_8_vga", squareRgb.withLutBitmapId(R.drawable.filter_square_8_vga).createFilter());
        addFilter("square_8_ega", squareRgb.withLutBitmapId(R.drawable.filter_square_8_ega).createFilter());
        addFilter("square_8_nintendo", squareRgb.withLutBitmapId(R.drawable.filter_square_8_nintendo).createFilter());
        addFilter("square_8_sega", squareRgb.withLutBitmapId(R.drawable.filter_square_8_sega).createFilter());
        addFilter("wide_4_00", squareRgb.withLutBitmapId(R.drawable.filter_wide_4_00).createFilter());
        addFilter("wide_8_bgr", squareBgr.withLutBitmapId(R.drawable.filter_wide_8_bgr).createFilter());
        addFilter("hald_8_00", haldRgb.withLutBitmapId(R.drawable.filter_hald_8_00).createFilter());
        addFilter("hald_8_01", haldRgb.withLutBitmapId(R.drawable.filter_hald_8_01).createFilter());
        addFilter("hald_8_02", haldRgb.withLutBitmapId(R.drawable.filter_hald_8_02).createFilter());
        addFilter("hald_8_03", haldRgb.withLutBitmapId(R.drawable.filter_hald_8_03).createFilter());
        addFilter("hald_8_04", haldRgb.withLutBitmapId(R.drawable.filter_hald_8_04).createFilter());

        rvFilters.setLayoutManager(new LinearLayoutManager(this));
        rvFilters.setAdapter(new FilterAdapter(effectItems, this));

        lastFilterSelection = effectItems.get(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_identity:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    setImage(R.drawable.identity);
                } else {
                    setImage(R.drawable.landscape);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addFilter(String name, Filter filter) {
        effectItems.add(new FilterSelection(name.toUpperCase(Locale.ENGLISH), filter));
    }

    private void setBusy(boolean busy, boolean removeImage) {
        if (busy) {
            pbBusy.animate().alpha(1f).start();
            pbBusy.setVisibility(View.VISIBLE);
            ivImage.animate().alpha(removeImage ? 0f : 0.5f).start();
        } else {
            ivImage.animate().alpha(1f).start();
            pbBusy.animate().alpha(0f).start();
        }
    }

    private void setImage(@DrawableRes final int resource) {
        new AsyncTask<Void, Void, Bitmap>() {

            long start;

            @Override
            protected void onPreExecute() {
                setBusy(true, true);
                start = System.nanoTime();
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resource);
                if (bitmap != null && RESIZE_BITMAP) {
                    int measuredHeight = ivImage.getMeasuredHeight();
                    int measuredWidth = ivImage.getMeasuredWidth();
                    if (measuredWidth != 0 && measuredHeight != 0 && (bitmap.getHeight() >= measuredHeight || bitmap.getWidth() >= measuredWidth)) {
                        float originalRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();
                        float measuredRatio = (float) measuredWidth / (float) measuredHeight;
                        if (originalRatio > measuredRatio) {
                            measuredWidth = (int) (measuredHeight * originalRatio);
                        } else {
                            measuredHeight = (int) (measuredWidth / originalRatio);
                        }
                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        bitmap = Bitmap.createScaledBitmap(bitmap, measuredWidth, measuredHeight, true);
                    }
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                originalBitmap = bitmap;
                ivImage.setImageBitmap(bitmap);
                setBusy(false, true);
                onFilterClicked(lastFilterSelection);
                Log.d(TAG, String.format("loaded bitmap in %.2fms", (System.nanoTime() - start) / 1e6f));
            }
        }.execute();
    }

    @Override
    public void onFilterClicked(FilterSelection filterSelection) {
        lastFilterSelection = filterSelection;
        tvName.setText(filterSelection.name);
        new AsyncTask<Void, Void, Bitmap>() {

            long start;

            @Override
            protected void onPreExecute() {
                setBusy(true, false);
                start = System.nanoTime();
            }

            @Override
            protected Bitmap doInBackground(Void... voids) {
                return lastFilterSelection.filter.apply(originalBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ivImage.setImageBitmap(bitmap);
                setBusy(false, false);
                Log.d(TAG, String.format("processed bitmap in %.2fms", (System.nanoTime() - start) / 1e6f));
            }
        }.execute();
    }

}
