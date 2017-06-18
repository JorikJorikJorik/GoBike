package com.jorik.gobike.Utils;

import static com.jorik.gobike.Model.Enum.ErrorNetworkType.UNKNOWN_ERROR;

import android.content.Context;
import com.jorik.gobike.Model.Enum.ErrorNetworkType;
import com.jorik.gobike.Model.POJO.ErrorModel;
import com.jorik.gobike.R;

public class ErrorUtils {

  private static final int UNKNOWN_ERROR_CODE = -1;
  private static final int JSON_ERROR_CODE = 100;
  private static final int HOSTNAME_ERROR_CODE = 101;
  private static final int TIMEOUT_ERROR_CODE = 102;
  private static final int CONNECT_ERROR_CODE = 103;
  private static final int NETWORK_ERROR_CODE = 104;

  private Context mContext;

  public ErrorUtils(Context context) {
    mContext = context;
  }

  public ErrorModel parseError(Throwable throwable) {
    ErrorModel model = new ErrorModel();
    ErrorNetworkType type = ErrorNetworkType.fromValue(throwable.getMessage(), true);

    model.setErrorNetworkType(type);
    model.setCode(parseCodeError(type));
    model.setMessage(formatMessageError(type, throwable));

    return model;
  }

  private String formatMessageError(ErrorNetworkType type, Throwable throwable) {
    StringBuilder message = new StringBuilder();
    message.append(chooseMessageByType(type, true));
    if (type.equals(UNKNOWN_ERROR)) {
      message.append(throwable.getMessage());
    }
    return message.toString();
  }

  public String chooseMessageByType(ErrorNetworkType type, boolean isFullMessage) {
    int errorId;
    switch (type) {
      case BAD_REQUEST:
        errorId = isFullMessage ? R.string.error_response_bad_request : R.string.error_short_response_bad_request;
        break;
      case UNAUTHORIZED:
        errorId = isFullMessage ? R.string.error_response_unauthorized : R.string.error_short_response_unauthorized;
        break;
      case FORBIDDEN:
        errorId = isFullMessage ? R.string.error_response_forbidden : R.string.error_short_response_forbidden;
        break;
      case NOT_FOUND:
        errorId = isFullMessage ? R.string.error_response_not_found : R.string.error_short_response_not_found;
        break;
      case NOT_ACCEPTABLE:
        errorId = isFullMessage ? R.string.error_response_not_acceptable : R.string.error_short_response_not_acceptable;
        break;
      case GONE:
        errorId = isFullMessage ? R.string.error_response_gone : R.string.error_short_response_gone;
        break;
      case ENHANCE_YOUR_CALM:
        errorId = isFullMessage ? R.string.error_response_enhance_your_calm : R.string.error_short_response_enhance_your_calm;
        break;
      case TOO_MANY_REQUESTS:
        errorId = isFullMessage ? R.string.error_response_too_many_requests : R.string.error_short_response_too_many_requests;
        break;
      case INTERNAL_SERVER_ERROR:
        errorId = isFullMessage ? R.string.error_response_internal_server_error : R.string.error_short_response_internal_server_error;
        break;
      case BAD_GATEWAY:
        errorId = isFullMessage ? R.string.error_response_bad_gateway : R.string.error_short_response_bad_gateway;
        break;
      case SERVICE_UNAVAILABLE:
        errorId = isFullMessage ? R.string.error_response_service_unavailable : R.string.error_short_response_service_unavailable;
        break;
      case GATEWAY_TIMEOUT:
        errorId = isFullMessage ? R.string.error_response_gateway_timeout : R.string.error_short_response_gateway_timeout;
        break;
      case JSON_ERROR:
        errorId = isFullMessage ? R.string.error_response_json_error : R.string.error_short_response_json_error;
        break;
      case HOSTNAME_ERROR:
        errorId = isFullMessage ? R.string.error_response_hostname_error : R.string.error_short_response_hostname_error;
        break;
      case TIMEOUT_ERROR:
        errorId = isFullMessage ? R.string.error_response_timeout_error : R.string.error_short_response_timeout_error;
        break;
      case CONNECT_ERROR:
        errorId = isFullMessage ? R.string.error_response_connect_error : R.string.error_short_response_connect_error;
        break;
      case NETWORK_ERROR:
        errorId = isFullMessage ? R.string.error_response_network_error : R.string.error_short_response_network_error;
        break;
      case UNKNOWN_ERROR:
        errorId = isFullMessage ? R.string.error_response_unknown_error : R.string.error_short_response_unknown_error;
        break;
      default:
        errorId = isFullMessage ? R.string.error_response_unknown_error : R.string.error_short_response_unknown_error;
        break;
    }
    return ResourceUtils.with(mContext).string(errorId);
  }



  private int parseCodeError(ErrorNetworkType type) {
    Integer code;
    try {
      code = Integer.parseInt(type.getValue());
    } catch (Exception ignored) {
      code = searchCode(type);
    }
    return code;
  }

  private int searchCode(ErrorNetworkType type) {
    switch (type) {
      case JSON_ERROR:
        return JSON_ERROR_CODE;
      case HOSTNAME_ERROR:
        return HOSTNAME_ERROR_CODE;
      case TIMEOUT_ERROR:
        return TIMEOUT_ERROR_CODE;
      case CONNECT_ERROR:
        return CONNECT_ERROR_CODE;
      case NETWORK_ERROR:
        return NETWORK_ERROR_CODE;
      case UNKNOWN_ERROR:
        return UNKNOWN_ERROR_CODE;
      default:
        return UNKNOWN_ERROR_CODE;
    }
  }

}
