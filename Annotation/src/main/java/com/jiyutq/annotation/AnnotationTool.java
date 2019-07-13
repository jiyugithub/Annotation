package com.jiyutq.annotation;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class AnnotationTool 
{
	public static void init(final Activity mActivity) {
		Class cls = mActivity.getClass();
		Field[] fArray = cls.getDeclaredFields();
		for (Field field : fArray) {
			if (field.isAnnotationPresent(FindViewById.class)) {
				FindViewById fvbi = field.getAnnotation(FindViewById.class);
				try {
					field.setAccessible(true);
					field.set(mActivity, mActivity.findViewById(fvbi.value()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		Method[] mArray = cls.getMethods();
		for (final Method method : mArray) {
			if (method.isAnnotationPresent(ViewClick.class)) {
				ViewClick vc = method.getAnnotation(ViewClick.class);
				mActivity.findViewById(vc.value()).setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View p1) {
							try {
								method.invoke(mActivity);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
			}
			if (method.isAnnotationPresent(ViewLongClick.class)) {
				ViewLongClick vc = method.getAnnotation(ViewLongClick.class);
				mActivity.findViewById(vc.value()).setOnLongClickListener(new OnLongClickListener(){
						@Override
						public boolean onLongClick(View p1) {
							try {
								method.invoke(mActivity);
							} catch (Exception e) {
								e.printStackTrace();
							}
							return true;
						}

					});
			}
			if (method.isAnnotationPresent(RunThread.class)) {
				new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								method.invoke(mActivity);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
			}
			if (method.isAnnotationPresent(ShowDialog.class)) {
				ShowDialog sd = method.getAnnotation(ShowDialog.class);
				new AlertDialog.Builder(mActivity)
					.setTitle(sd.title())
					.setMessage(sd.msg())
					.setPositiveButton(sd.btn(), new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface p1, int p2) {
							try {
								method.invoke(mActivity);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					})
					.create().show();
			}
		}
	}
}

