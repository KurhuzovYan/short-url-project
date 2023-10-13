package goit.com.shorturlproject.validation.impl;

import goit.com.shorturlproject.validation.StrongPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // check if string contains at least one digit, one lowercase letter, one uppercase letter, one special character and 8 characters long
    // return value.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$");
    return value.matches("^[A-Z].*\\d$");
  }

}
