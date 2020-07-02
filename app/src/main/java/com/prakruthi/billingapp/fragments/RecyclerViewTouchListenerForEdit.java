package com.prakruthi.billingapp.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Admin on 1/31/2017.
 */

public class RecyclerViewTouchListenerForEdit implements RecyclerView.OnItemTouchListener {

    private ClickListener clicklistener;
    private GestureDetector gestureDetector;

    public RecyclerViewTouchListenerForEdit(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {
        this.clicklistener = clicklistener;
        gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                super.onDoubleTap(e);

                /*View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && clicklistener!=null) {
                    clicklistener.onClick(child, recycleView.getChildAdapterPosition(child));
                }

                return true;*/

                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                /*View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && clicklistener!=null) {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                }*/

                View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && clicklistener!=null) {
                    clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                }

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);

            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child=rv.findChildViewUnder(e.getX(),e.getY());
        if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
            clicklistener.onClick(child,rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
