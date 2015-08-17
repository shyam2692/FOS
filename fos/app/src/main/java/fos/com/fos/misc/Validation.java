package fos.com.fos.misc;

import android.text.TextUtils;
import android.widget.EditText;

public class Validation {
  public static boolean isValid(EditText editText) {
    if (TextUtils.isEmpty(editText.getText())) {
      editText.setError("Please enter valid info!");
      return false;
    }
    return true;
  }
}
