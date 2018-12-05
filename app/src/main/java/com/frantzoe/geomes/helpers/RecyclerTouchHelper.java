package com.frantzoe.geomes.helpers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.frantzoe.geomes.R;
import com.frantzoe.geomes.adapters.EventAdapter;
import com.frantzoe.geomes.models.Event;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerTouchHelper extends ItemTouchHelper.SimpleCallback {

    private Activity activity;
    private View rootView;

    private Paint paint = new Paint();

    private RecyclerTouchHelperListener recyclerTouchHelperListener;

    public RecyclerTouchHelper(int dragDirs, int swipeDirs, View root, RecyclerTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.rootView = root;
        this.recyclerTouchHelperListener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        recyclerTouchHelperListener.onSwiped(viewHolder, direction);
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float width = height / 3;

            if(dX > 0){
                paint.setColor(Color.parseColor("#388E3C"));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                canvas.drawRect(background, paint);
                RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                canvas.drawBitmap(getVectorBitmap(R.drawable.ic_check_white_24dp), null, icon_dest, paint);
            } else {
                paint.setColor(Color.parseColor("#D32F2F"));
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                canvas.drawRect(background, paint);
                RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                canvas.drawBitmap(getVectorBitmap(R.drawable.ic_close_white_24dp),null, icon_dest, paint);
            }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private Bitmap getVectorBitmap(int drawableId) {

        Drawable drawable = ContextCompat.getDrawable(rootView.getContext(), drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public interface RecyclerTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction/*, int position*/);
    }
}
