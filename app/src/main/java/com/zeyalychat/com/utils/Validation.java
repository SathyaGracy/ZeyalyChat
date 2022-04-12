package com.zeyalychat.com.utils;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean emptyCheck(EditText emptycheck_from) {
        Boolean cemptycheck = false;
        if (!emptycheck_from.getText().toString().trim().equalsIgnoreCase("")) {
            cemptycheck = true;
        } else {
            cemptycheck = false;
        }
        return cemptycheck;
    }


    public boolean emailValidation(EditText emailPattern_from) {
        Boolean check_email_validate = false;
        if (emailPattern_from.getText().toString().matches(emailPattern)) {
            check_email_validate = true;
        } else {
            check_email_validate = false;
        }
        return check_email_validate;
    }

    public boolean confirmPasswordValidation(EditText password1, EditText password2) {
        Boolean check_password_validate = false;
        if (password1.getText().toString().equals(password2.getText().toString())) {
            check_password_validate = true;
        } else {
            check_password_validate = false;
        }
        return check_password_validate;
    }
    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=*])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
