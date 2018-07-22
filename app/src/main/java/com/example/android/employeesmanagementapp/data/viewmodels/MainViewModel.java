package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * View Model class
 * <p>
 * caches data for MainActivity
 * ---> tasksList for TasksFragment
 * ---> departmentsList for DepartmentsFragment
 * ---> employeesList for EmployeesFragment
 */
public class MainViewModel extends ViewModel {

    private LiveData<List<DepartmentEntry>> allDepartmentsList;
    private LiveData<List<TaskEntry>> tasksList;
    private LiveData<List<EmployeeEntry>> allEmployeesList;


    public MainViewModel(AppDatabase database, boolean taskIsCompleted) {

        allDepartmentsList = database.departmentsDao().loadDepartments();
        tasksList = database.tasksDao().loadTasks(taskIsCompleted);
        allEmployeesList = database.employeesDao().loadEmployees();


    }


    public LiveData<List<DepartmentEntry>> getAllDepartmentsList() {
        return allDepartmentsList;
    }

    public LiveData<List<TaskEntry>> getTasksList() {
        return tasksList;
    }

    public LiveData<List<EmployeeEntry>> getAllEmployeesList() {
        return allEmployeesList;
    }
}
