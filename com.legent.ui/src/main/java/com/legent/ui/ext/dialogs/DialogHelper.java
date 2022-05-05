package com.legent.ui.ext.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

import com.legent.ui.UI;

public class DialogHelper {

	final static public int ChoiceType_YesAndNo = 1;
	final static public int ChoiceType_OkAndCancel = 2;

	public static AlertDialog newOKDialog(Context cx, String title,
			String message, DialogInterface.OnClickListener listener) {
		return newOKDialogBuilder(cx, title, message, listener).create();
	}

	public static AlertDialog newDialog_YesNo(Context cx, String title,
			String message, DialogInterface.OnClickListener listener) {
		return newChoiceDialogBuilder(ChoiceType_YesAndNo, cx, title, message,
				listener).create();
	}

	public static AlertDialog newDialog_OkCancel(Context cx, String title,
			String message, DialogInterface.OnClickListener listener) {
		return newChoiceDialogBuilder(ChoiceType_OkAndCancel, cx, title,
				message, listener).create();
	}

	public static AlertDialog.Builder newOKDialogBuilder(Context cx,
			String title, String message,
			DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new Builder(cx);
		builder.setTitle(title);
		builder.setMessage(message);
//		builder.setPositiveButton(UI.getStr_Ok(cx), listener);
		builder.setPositiveButton("确定", listener);
		return builder;
	}

	public static AlertDialog.Builder newChoiceDialogBuilder(int choiceType,
			Context cx, String title, String message,
			DialogInterface.OnClickListener listener) {

//		String strYes = choiceType == ChoiceType_YesAndNo ? UI.getStr_Yes(cx) : UI.getStr_Ok(cx);
		String strYes = choiceType == ChoiceType_YesAndNo ? "是" : "确定";
//		String strNo = choiceType == ChoiceType_YesAndNo ? UI.getStr_No(cx): UI.getStr_Cancel(cx);
		String strNo = choiceType == ChoiceType_YesAndNo ? "否": "取消";
		AlertDialog.Builder builder = new Builder(cx);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(strYes, listener);
		builder.setNegativeButton(strNo, listener);
		return builder;
	}
}
