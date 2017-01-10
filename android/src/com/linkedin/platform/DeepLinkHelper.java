package com.linkedin.platform;

import org.appcelerator.titanium.TiApplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.linkedin.platform.errors.LIAppErrorCode;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.internals.AppStore;
import com.linkedin.platform.internals.LIAppVersion;
import com.linkedin.platform.listeners.DeepLinkListener;

/**
 * DeepLinkHelper enables linking to pages within the LinkedIn application
 */
public class DeepLinkHelper {
	public static final int LI_SDK_CROSSLINK_REQUEST_CODE = 13287;
	private static final String CURRENTLY_LOGGED_IN_MEMBER = "you";
	private static final String DEEPLINK_ERROR_CODE_EXTRA_NAME = "com.linkedin.thirdparty.deeplink.EXTRA_ERROR_CODE";
	private static final String DEEPLINK_ERROR_MESSAGE_EXTRA_NAME = "com.linkedin.thirdparty.deeplink.EXTRA_ERROR_MESSAGE";
	private static DeepLinkHelper deepLinkHelper;
	private DeepLinkListener deepLinkListener;
	private static String[] alertTexts;
	final String LCAT = "LinkedIn 👥";

	public static DeepLinkHelper getInstance() {
		if (deepLinkHelper == null) {
			deepLinkHelper = new DeepLinkHelper();
		}
		return deepLinkHelper;
	}

	/**
	 * opens up a view which shows the profile of the user that is currently
	 * logged in to the LinkedIn app.
	 * 
	 * @param activity
	 * @param callback
	 */
	public void openCurrentProfile(Activity activity, DeepLinkListener callback) {
		openOtherProfile(activity, CURRENTLY_LOGGED_IN_MEMBER, callback);
	}

	/**
	 * opens a view which shows the profile of the given member
	 * 
	 * @param activity
	 * @param memberId
	 *            obtained through an api call
	 * @param callback
	 */

	public void setTexts(String[] _alertTexts) {
		alertTexts = _alertTexts;
	}

	public void openOtherProfile(Activity activity, String memberId,
			DeepLinkListener callback) {
		this.deepLinkListener = callback;
		Log.d(LCAT, " openOtherProfile meemberId=\"" + memberId + "\"");
		Context ctx = activity.getBaseContext();
		LISession session = LISessionManager.getInstance(ctx).getSession();
		Log.d(LCAT, "session=" + session.toString());
		Log.d(LCAT, "session.isValid=" + session.isValid());
		AccessToken at = session.getAccessToken();
		if (at != null) {
			Log.d(LCAT, "at=" + session.getAccessToken().getValue());
			Log.d(LCAT, "atexp=" + session.getAccessToken().getExpiresOn());
		} else
			Log.w(LCAT, "AccessToken is null");
		if (!session.isValid()) {
			Log.d(LCAT,
					"openOtherProfile ends with 'no access token' for member "
							+ memberId);
			callback.onDeepLinkError(new LIDeepLinkError(
					LIAppErrorCode.NOT_AUTHENTICATED,
					"there is no access token"));
			return;
		}
		try {
			if (!LIAppVersion.isLIAppCurrent(activity)) {
				AppStore.goAppStore(activity, true, alertTexts);
				return;
			}
			deepLinkToProfile(activity, memberId, session.getAccessToken());
		} catch (ActivityNotFoundException e) {
			callback.onDeepLinkError(new LIDeepLinkError(
					LIAppErrorCode.LINKEDIN_APP_NOT_FOUND,
					"LinkedIn app needs to be either installed or` updated"));
			deepLinkListener = null;
		}
	}

	private void deepLinkToProfile(Activity activity, String memberId,
			AccessToken accessToken) {
		Intent i = new Intent("android.intent.action.VIEW");
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.scheme("linkedin");
		if (CURRENTLY_LOGGED_IN_MEMBER.equals(memberId)) {
			uriBuilder.authority(CURRENTLY_LOGGED_IN_MEMBER);
		} else {
			uriBuilder.authority("profile").appendPath(memberId);
		}
		uriBuilder.appendQueryParameter("accessToken", accessToken.getValue());
		uriBuilder.appendQueryParameter("src", "sdk");
		i.setData(uriBuilder.build());
		activity.startActivityForResult(i, LI_SDK_CROSSLINK_REQUEST_CODE);
	}

	/**
	 * call this method in your activity's onActivityResult method. Handles any
	 * response code from LinkedIn and calls the DeepLinkListener callback
	 * 
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		if (requestCode == LI_SDK_CROSSLINK_REQUEST_CODE
				&& deepLinkListener != null) {
			if (resultCode == Activity.RESULT_OK) {
				deepLinkListener.onDeepLinkSuccess();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				if (data == null || data.getExtras() == null) {
					deepLinkListener.onDeepLinkError(new LIDeepLinkError(
							LIAppErrorCode.USER_CANCELLED, ""));
				} else {
					String errorMessage = data.getExtras().getString(
							DEEPLINK_ERROR_MESSAGE_EXTRA_NAME);
					String errorCode = data.getExtras().getString(
							DEEPLINK_ERROR_CODE_EXTRA_NAME);
					deepLinkListener.onDeepLinkError(new LIDeepLinkError(
							errorCode, errorMessage));
				}
			}
		}
	}

}
