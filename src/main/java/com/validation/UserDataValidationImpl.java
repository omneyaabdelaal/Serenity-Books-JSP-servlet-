package com.validation;

import java.util.regex.Pattern;

import com.model.Users;

public class UserDataValidationImpl implements UserDataValidation {
	
	private Users user;

    private static final String NAME_PATTERN = "^[a-zA-Z]+$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._-]+@gmail\\.com$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
    
    public UserDataValidationImpl(Users user) {
    	this.user=user;
    }
    

    @Override
    public boolean validateName() {
        if (user.getF_name() == null || user.getL_name() == null || user.getF_name().isEmpty() || user.getL_name().isEmpty()) {
            return false;
        }
        return Pattern.matches(NAME_PATTERN,user.getF_name()  ) && Pattern.matches(NAME_PATTERN, user.getL_name());
    }

    @Override
    public boolean validatePass() {
        if (user.getPass() == null) {
            return false;
        }
        return Pattern.matches(PASSWORD_PATTERN,user.getPass() );
    }

    @Override
    public boolean validateE_mail() {
        if (user.getE_mail() == null) {
            return false;
        }
        return Pattern.matches(EMAIL_PATTERN, user.getE_mail() );
    }
    

}
