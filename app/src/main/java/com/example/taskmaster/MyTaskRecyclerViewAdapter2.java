package com.example.taskmaster;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmaster.TaskFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */


public class MyTaskRecyclerViewAdapter2 extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter2.ViewHolder> {


    static final String TAG = "voytov";
    private final List<Task> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context mContext;


    public MyTaskRecyclerViewAdapter2(List<Task> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mBodyView.setText(mValues.get(position).getBody());
        holder.mStateView.setText(mValues.get(position).getState());

        holder.mView.setOnClickListener(new View.OnClickListener() {

            // one part is different from the other adapter
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                Log.i(TAG, "Clicked");

                String str1 = new StringBuilder(mValues.get(position).title) + "\n" + (mValues.get(position).body) + "\n" + (mValues.get(position).state);

                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, str1, duration);
                toast.show();

            }
        });
    }
/////////
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mBodyView;
        public final TextView mStateView;

        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.title);
            mBodyView = (TextView) view.findViewById(R.id.body);
            mStateView = (TextView) view.findViewById(R.id.state);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBodyView.getText() + "'";
        }
    }
}
