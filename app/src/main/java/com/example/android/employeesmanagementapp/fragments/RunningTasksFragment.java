package com.example.android.employeesmanagementapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.activities.AddTaskActivity;
import com.example.android.employeesmanagementapp.adapters.TasksAdapter;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;
import com.example.android.employeesmanagementapp.data.viewmodels.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunningTasksFragment extends Fragment implements TasksAdapter.TasksItemClickListener {

    private final String TAG = RunningTasksFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private TasksAdapter mAdapter;
    private AppDatabase mDb;
    private LinearLayout emptyView;
    private TextView emptyViewTextView;
    private Snackbar mSnackbar;

    public RunningTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mDb = AppDatabase.getInstance(getContext());

        View view = inflater.inflate(R.layout.fragments_rv, container, false);

        emptyView = view.findViewById(R.id.empty_view);
        emptyViewTextView = view.findViewById(R.id.empty_view_message_text_view);

        // Inflate the layout for this fragment
        mRecyclerView = view.findViewById(R.id.rv_fragment);

        // this setting to improves performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new TasksAdapter(this);

        LiveData<List<TaskEntry>> tasksList = ViewModelProviders.of(getActivity()).get(MainViewModel.class).getRunningTasksList();
        tasksList.observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(List<TaskEntry> taskEntries) {
                if (taskEntries != null) {
                    if (taskEntries.isEmpty())
                        showEmptyView();
                    else {
                        mAdapter.setData(taskEntries);
                        showRecyclerView();
                    }
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        setFabActivation();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSnackbar != null)
            mSnackbar.dismiss();
    }

    private void setFabActivation() {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final int depNum = mDb.departmentsDao().getNumDepartments();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (depNum == 0) {
                            getActivity().findViewById(R.id.fab).setEnabled(false);
                            mSnackbar = Snackbar.make(getView(), "please add department first", Snackbar.LENGTH_INDEFINITE);
                            mSnackbar.show();
                        } else {
                            getActivity().findViewById(R.id.fab).setEnabled(true);
                            if (mSnackbar != null)
                                mSnackbar.dismiss();
                        }

                    }
                });
            }
        });
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        emptyViewTextView.setText(R.string.task_empty_view_message);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }


    @Override
    public void onTaskClick(int taskRowID, int taskPosition) {
        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskRowID);
        startActivity(intent);
    }
}

