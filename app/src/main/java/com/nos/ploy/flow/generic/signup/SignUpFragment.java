package com.nos.ploy.flow.generic.signup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.nos.ploy.R;
import com.nos.ploy.api.account.AccountApi;
import com.nos.ploy.api.account.model.PostUploadProfileImageGson;
import com.nos.ploy.api.account.model.ProfileImageGson;
import com.nos.ploy.api.authentication.AuthenticationApi;
import com.nos.ploy.api.authentication.model.AccountGson;
import com.nos.ploy.api.authentication.model.PostSignupGson;
import com.nos.ploy.api.base.RetrofitCallUtils;
import com.nos.ploy.api.base.response.ResponseMessage;
import com.nos.ploy.base.BaseFragment;
import com.nos.ploy.utils.DatePickerUtils;
import com.nos.ploy.utils.ImagePickerUtils;
import com.nos.ploy.utils.MyFileUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import net.yazeed44.imagepicker.model.ImageEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by User on 11/11/2559.
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.button_signup_detail_create_account)
    Button mButtonCreateAccount;
    @BindView(R.id.toolbar_main)
    Toolbar mToolbar;
    @BindView(R.id.edittext_signup_detail_password)
    MaterialEditText mEditTextPassword;
    @BindView(R.id.edittext_signup_detail_re_password)
    MaterialEditText mEditTextRePassword;
    @BindView(R.id.edittext_signup_detail_birthday)
    MaterialEditText mEditTextBirthDay;
    @BindView(R.id.edittext_signup_detail_firstname)
    MaterialEditText mEditTextFirstName;
    @BindView(R.id.edittext_signup_detail_lastname)
    MaterialEditText mEditTextLastName;
    @BindView(R.id.edittext_signup_detail_email)
    MaterialEditText mEditTextEmail;
    @BindView(R.id.imageview_signup_detail_profile_image)
    ImageView mImageViewProfile;
    @BindString(R.string.This_field_is_required)
    String LThis_field_is_required;
    @BindString(R.string.Password_does_not_match_the_confirm_password)
    String LPassword_does_not_match_the_confirm_password;
    @BindDrawable(R.drawable.ic_circle_profile_120dp)
    Drawable mDrawableProfile;
    private AuthenticationApi mAuthenApi;
    private AccountApi mAccountApi;
    private Bitmap mBitmapToUpload;
//    private List<SignupFragmentContract.ViewModel> mDatas = new ArrayList<>();
//    private SignupFragmentContract.ViewModel mAddMoreVm = new SignupFragmentContract.ViewModel() {
//        @Override
//        public int getViewType() {
//            return SignupFragmentContract.ViewModel.ADD_MORE;
//        }
//
//        @Override
//        public String getImageUri() {
//            return "";
//        }
//    };
//    private Picker.PickListener mOnPickImageListener = new Picker.PickListener() {
//        @Override
//        public void onPickedSuccessfully(ArrayList<ImageEntry> images) {
//            if (null != images && !images.isEmpty()) {
//                List<String> imageUrls = new ArrayList<>();
//                for (ImageEntry imageEntry : images) {
//                    imageUrls.add(imageEntry.path);
//                }
//                addPhotoDatas(imageUrls);
//            }
//
//        }
//
//        @Override
//        public void onCancel() {
//
//        }
//    };

//    private SignupProfileImageRecyclerAdapter mProfileImageAdapter = new SignupProfileImageRecyclerAdapter() {
//        @Override
//        public void onBindViewHolder(final SignupProfileImageRecyclerAdapter.ViewHolder holder, int position) {
//            if (RecyclerUtils.isAvailableData(mDatas, position)) {
//                SignupFragmentContract.ViewModel data = mDatas.get(position);
//                if (data.getViewType() == SignupFragmentContract.ViewModel.ADD_MORE) {
//                    holder.imgProfile.setImageDrawable(mDrawableProfile);
//                    holder.imgProfile.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ImagePickerUtils.pickImage(v.getContext(), 3, mOnPickImageListener);
//                        }
//                    });
//                    holder.fabCancel.setVisibility(View.GONE);
//                    holder.fabCancel.setOnClickListener(null);
//                } else {
//                    Glide.with(holder.imgProfile.getContext()).load(data.getImageUri()).into(holder.imgProfile);
//                    holder.fabCancel.setVisibility(View.VISIBLE);
//                    holder.fabCancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int position = holder.getAdapterPosition();
//                            if (RecyclerUtils.isAvailableData(mDatas, position)) {
//                                mDatas.remove(position);
//                                notifyItemRemoved(position);
//                                notifyPhotoDataSetChanged();
//                            }
//                        }
//                    });
//                }
//            }
//        }
//
//        @Override
//        public
//        @SignupFragmentContract.ViewModel.ViewType
//        int getItemViewType(int position) {
//            if (RecyclerUtils.isAvailableData(mDatas, position)) {
//                return mDatas.get(position).getViewType();
//            }
//            return SignupFragmentContract.ViewModel.NONE;
//        }
//
//        @Override
//        public int getItemCount() {
//            return RecyclerUtils.getSize(mDatas);
//        }
//    };


    public static SignUpFragment newInstance() {
        Bundle args = new Bundle();
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    private void addPhotoDatas(List<String> imageUrls) {
//        if (mDatas.contains(mAddMoreVm)) {
//            mDatas.remove(mAddMoreVm);
//        }
//        for (final String imageUri : imageUrls) {
//            mDatas.add(new SignupFragmentContract.ViewModel() {
//                @Override
//                public int getViewType() {
//                    return NORMAL;
//                }
//
//                @Override
//                public String getImageUri() {
//                    return imageUri;
//                }
//            });
//        }
//        mProfileImageAdapter.notifyDataSetChanged();
//        notifyPhotoDataSetChanged();
//    }

//    private void notifyPhotoDataSetChanged() {
//        if (mProfileImageAdapter.getItemCount() < 3 && !mDatas.contains(mAddMoreVm)) {
//            mDatas.add(mAddMoreVm);
//            int addIndex = mDatas.indexOf(mAddMoreVm);
//            if (addIndex >= 0 && addIndex < mProfileImageAdapter.getItemCount()) {
//                mProfileImageAdapter.notifyItemInserted(mDatas.indexOf(mAddMoreVm));
//            }
//        }
//
//        if (mProfileImageAdapter.getItemCount() >= 0) {
//            mImageViewProfile.smoothScrollToPosition(mProfileImageAdapter.getItemCount() - 1);
//        }
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuthenApi = getRetrofit().create(AuthenticationApi.class);
        mAccountApi = getRetrofit().create(AccountApi.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup_detail, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        dummyData();
        initView();
        initToolbar(mToolbar);
    }

    private void dummyData() {
        mEditTextEmail.setText("nuitest20@gmail.com");
        mEditTextFirstName.setText("nuifirst");
        mEditTextLastName.setText("nuilast");
        mEditTextPassword.setText("123456");
        mEditTextRePassword.setText("123456");
        mEditTextBirthDay.setText("2016-02-02");
    }

    private void initView() {
        disableEditable(mEditTextBirthDay);
        mImageViewProfile.setOnClickListener(this);
        mEditTextBirthDay.setOnClickListener(this);
        mButtonCreateAccount.setOnClickListener(this);
    }

    private void initToolbar(Toolbar toolbar) {
        enableBackButton(toolbar);
        toolbar.setTitle(R.string.Sign_up);
    }


    private String extractAndCheckError(EditText editText) {
        String result = extractString(editText);
        if (TextUtils.isEmpty(result)) {
            editText.setError(LThis_field_is_required);
            editText.requestFocus();
        } else {
            editText.setError(null);
        }
        return result;
    }

    private void attempSubmit() {

        mEditTextBirthDay.setError(null);
        mEditTextEmail.setError(null);
        mEditTextFirstName.setError(null);
        mEditTextLastName.setError(null);
        mEditTextPassword.setError(null);
        mEditTextRePassword.setError(null);

        String birthDay = extractAndCheckError(mEditTextBirthDay);
        String email = extractAndCheckError(mEditTextEmail);
        String firstName = extractAndCheckError(mEditTextFirstName);
        String lastName = extractAndCheckError(mEditTextLastName);
        String password = extractAndCheckError(mEditTextPassword);
        String rePassword = extractAndCheckError(mEditTextRePassword);
        boolean canSubmit = (!TextUtils.isEmpty(birthDay) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(rePassword));
        if (!isEmailValid(email)) {
            mEditTextEmail.setError(getString(R.string.Invalid_email_address));
            canSubmit = false;
        }
        if (canSubmit) {
            if (TextUtils.equals(password, rePassword)) {
                PostSignupGson data = new PostSignupGson(birthDay, email, firstName, lastName, password);
                requestPostSignup(data, mEditTextPassword.getContext());
                mEditTextPassword.setError(null);
                mEditTextRePassword.setError(null);
            } else {
                mEditTextRePassword.setError(LPassword_does_not_match_the_confirm_password);
            }
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestPostSignup(PostSignupGson data, final Context context) {
        showLoading();
        RetrofitCallUtils.with(mAuthenApi.postSignup(data), new RetrofitCallUtils.RetrofitCallback<AccountGson>() {
            @Override
            public void onDataSuccess(AccountGson data) {
                dismissLoading();
                checkIfProfileImageWillUpload(data.getData().getUserId(), new Action1<Boolean>() {
                    @Override
                    public void call(Boolean success) {
                        if (success) {
                            goBackToLoginFragment();
                        }
                    }
                });
            }

            @Override
            public void onDataFailure(ResponseMessage failCause) {
                dismissLoading();
            }
        }).enqueue(context);
    }

    private void checkIfProfileImageWillUpload(Long userId, Action1<Boolean> onFinishUpload) {
        if (mBitmapToUpload != null) {
            String base64 = MyFileUtils.encodeToBase64(mBitmapToUpload);
            requestPostUploadProfileImage(userId, base64, onFinishUpload);
        } else {
            onFinishUpload.call(true);
        }
    }

    private void requestPostUploadProfileImage(Long userId, String imgBase64, final Action1<Boolean> onFinishUpload) {
        List<String> datas = new ArrayList<>();
        datas.add(imgBase64);
        showLoading();
        RetrofitCallUtils.with(mAccountApi.postUploadProfileImage(PostUploadProfileImageGson.with(userId).createNew(datas))
                , new RetrofitCallUtils.RetrofitCallback<ProfileImageGson>() {

                    @Override
                    public void onDataSuccess(ProfileImageGson data) {
                        dismissLoading();
                        onFinishUpload.call(true);
                    }

                    @Override
                    public void onDataFailure(ResponseMessage failCause) {
                        dismissLoading();
                        onFinishUpload.call(false);
                    }
                }).enqueue(getContext());
    }

    private void goBackToLoginFragment() {
        dismiss();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mButtonCreateAccount.getId()) {
            attempSubmit();
        } else if (id == mEditTextBirthDay.getId()) {
            DatePickerUtils.chooseDate(view.getContext(), new Action1<String>() {
                @Override
                public void call(String s) {
                    mEditTextBirthDay.setText(s);
                }
            });
        } else if (id == mImageViewProfile.getId()) {
            chooseImage(view.getContext());
        }
    }


    private void chooseImage(final Context context) {
        ImagePickerUtils.pickImage(context, new Action1<ImageEntry>() {
            @Override
            public void call(ImageEntry imageEntry) {
                if (null != imageEntry && !TextUtils.isEmpty(imageEntry.path)) {
                    Glide.with(context).load(imageEntry.path).asBitmap().into(new BitmapImageViewTarget(mImageViewProfile) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                            mBitmapToUpload = resource;
                            mImageViewProfile.setImageBitmap(mBitmapToUpload);
                        }
                    });
                }

            }
        });

    }
}
