package com.test.boatstagram;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;

/**
 * A {@link Service} :<br>
 * <ul>
 * <li>sticky</li>
 * <li>which runs in background until {@link Service#stopSelf()} is called</li>
 * <li>without visible notification : starting Android API 16, a notification is always displayed in the Android notification bar. This class removes this one up to Android API 19.</li>
 * </ul>
 * The method {@link Service#onCreate()} is replaced by {@link #onStickyCreate()}.<br>
 * The method {@link Service#onStartCommand(Intent, int, int)} is replaced by {@link #onStickyStartCommand(Intent, int, int)}.
 */
public abstract class StickyService extends Service {

	/*
	 * CONSTRUCTORS
	 */

	public StickyService() {
	}

	/*
	 * ABSTRACT METHODS
	 */

	/**
	 * Replaces {@link #onCreate()}
	 */
	protected abstract void onStickyCreate();

	/**
	 * Replaces {@link #onStartCommand(Intent, int, int)}
	 * 
	 * @param intent
	 * @param flags
	 * @param startId
	 */
	protected abstract void onStickyStartCommand(Intent intent, int flags, int startId);

	/**
	 * The notification ID associated with this sticky service (exemple: 12345).
	 * 
	 * @return
	 */
	protected abstract int getNotificationId();

	/*
	 * PROTECTED METHODS
	 */

	/**
	 * Starting Android API 16, a notification for sticky Services is always displayed in the notifications bar.<br>
	 * However, this {@link StickyService} try to remove this one (up to Android 19 for now), but you have to give a valid drawable (which will not be visible so...).<br>
	 * Default returns android.R.drawable.sym_def_app_icon.
	 * 
	 * @return the id of a drawable
	 */
	protected int getNotificationIcon() {
		return android.R.drawable.sym_def_app_icon;
	}

	/*
	 * OVERRIDED METHODS: Service
	 */

	@Override
	public final void onCreate() {
		if (Build.VERSION.SDK_INT >= 16) {// >17
			// hack: call these methods in the onCreate(...) method (not in onStartCommand(...)) to remove the notification
			// on Android 4.3, will display a default notification at startup, so here, launch with a notification with an icon
			startForeground(getNotificationId(), new Notification.Builder(this).setSmallIcon(getNotificationIcon()).setAutoCancel(true).build());
			// and here, we remove it with an empty notification
			NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
			notificationManager.notify(getNotificationId(), new Notification.Builder(this).build());
		} else {
			startForeground(getNotificationId(), new Notification());
			// and here, we remove it with an empty notification
			NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
			notificationManager.notify(getNotificationId(), new Notification());
		}

		onStickyCreate();
	}

	@Override
	public final int onStartCommand(Intent intent, int flags, int startId) {
		onStickyStartCommand(intent, flags, startId);

		return START_STICKY;
	}

}
