package com.jorik.gobike.Utils;

import android.content.Context;
import android.util.Base64;
import com.jorik.gobike.R;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

  private static final String TYPE_CRYPTO_ALGORITHM = "AES/CBC/PKCS5PADDING";
  private static final String UTF_8 = "UTF-8";
  private static final String ENCRYPT_KEY = "cash_encrypt_key";
  private static final String IV_KEY = "cash_encrypt_iv_";

  private SecretKeySpec mSecretKeySpec;
  private IvParameterSpec mIvParameterSpec;
  private Context mContext;

  public CryptoUtils(Context context) {
    mContext = context;
  }

  public String getData(String inputData, int mode) {
    String result = ResourceUtils.with(mContext).string(R.string.empty_string);
    try {
      byte[] cipherData = cipherData(inputData.getBytes(UTF_8), mode);
      result = new String(cipherData, UTF_8);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return result;
  }

  private void generateKey() {
    mSecretKeySpec = new SecretKeySpec(ENCRYPT_KEY.getBytes(), TYPE_CRYPTO_ALGORITHM);
    mIvParameterSpec = new IvParameterSpec(IV_KEY.getBytes());
  }

  private byte[] cipherData(byte[] data, int mode) {
    if (mSecretKeySpec != null && mIvParameterSpec != null) {
      try {
        Cipher cipher = Cipher.getInstance(TYPE_CRYPTO_ALGORITHM);
        cipher.init(mode, mSecretKeySpec, mIvParameterSpec);
        return cipher.doFinal(data);
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
        e.printStackTrace();
      }
    }
    return new byte[]{};
  }

  public String generateBasic(String login, String password) {
    String previewString = String.format("%s:%s", login, password);
    String baseAuth = ResourceUtils.with(mContext).string(R.string.empty_string);
    try {
      baseAuth = Base64.encodeToString(previewString.getBytes(UTF_8), Base64.NO_WRAP);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return String.format("%s %s", "Basic", baseAuth);
  }

}
