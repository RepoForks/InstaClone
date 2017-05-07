package com.app.infideap.instaclone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = SignupFragment.class.getSimpleName();

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View signup1Layout;
    private View signup2Layout;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText fullnameEditText;
    private String regexEmail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signup1Layout = view.findViewById(R.id.layout_signup_1);
        signup2Layout = view.findViewById(R.id.layout_signup_2);
        signup1Layout.setVisibility(View.VISIBLE);
        signup2Layout.setVisibility(View.GONE);

        emailEditText = (EditText) view.findViewById(R.id.editText_email);
        passwordEditText = (EditText) view.findViewById(R.id.editText_password);
        fullnameEditText = (EditText) view.findViewById(R.id.editText_fullname);
        view.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String fullname = fullnameEditText.getText().toString();
                // if first layout is visible, second layout will display
                if (signup1Layout.getVisibility() == View.VISIBLE) {
                    if (email.matches(regexEmail)) {
                        signup1Layout.setVisibility(View.GONE);
                        signup2Layout.setVisibility(View.VISIBLE);
                    }else{
                        Snackbar.make(v, "Invalid e-mail.",Snackbar.LENGTH_LONG).show();
                    }
                }
                // othewise, do signup function to firebase
                else if (password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")){
                    //TODO : here put code for signup

                    FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.getException()!=null)
                                        task.getException().printStackTrace();
                                    if (task.isSuccessful()){
                                        task.getResult().getUser().sendEmailVerification();
                                        mListener.onSignUpSuccess();
                                    }else{
                                        Snackbar.make(v, "Registration failed.",Snackbar.LENGTH_LONG).show();

                                    }
                                }
                            });
                }
                else{
                    Snackbar.make(v, "Invalid password.",Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // true if second layout visible
    public boolean canGoBack() {
        Log.e(TAG, "Equal : " + (signup2Layout.getVisibility() == View.VISIBLE));

        return signup2Layout.getVisibility() == View.VISIBLE;
    }

    public void back() {
        signup2Layout.setVisibility(View.GONE);
        signup1Layout.setVisibility(View.VISIBLE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onSignUpSuccess();
    }
}
