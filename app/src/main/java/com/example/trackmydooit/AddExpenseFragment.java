/*
package com.example.trackmydooit;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

*/
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 *//*

public class AddExpenseFragment extends Fragment {

    private Toolbar TBExpense;
    private TextView TVTotalSpent;
    private ProgressBar PBExpense;
    private RecyclerView RVExpense;
    private FloatingActionButton FABAddTrans;
    private ProgressDialog progressDialog;

    private RadioButton RBIncome;
    private RadioButton RBExpenses;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddExpense.
     *//*

    // TODO: Rename and change types and number of parameters
    public static AddExpenseFragment newInstance(String param1, String param2) {
        AddExpenseFragment fragment = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }
}*/
