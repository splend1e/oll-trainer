package com.devmite.rubik.dialog;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devmite.rubik.R;
import com.devmite.rubik.database.MySQLiteHelper;
import com.devmite.rubik.model.Lesson;

@SuppressLint("NewApi")
public class SaveLessonDialog extends DialogFragment {

	public static final String KEY_LESSON_NAME = "lesson_name";
	public static final String KEY_LESSON_CONTENT = "lesson_content";
	public static final String KEY_LESSON_ARRAY = "lesson_array";

	private String lessonName, lessonContent;
	private int[] lessonArray;
	private EditText editName;

	@SuppressLint("NewApi")
	public static SaveLessonDialog newInstance(String lessonName,
			String lessonContent, int[] lessonArray) {
		SaveLessonDialog dialog = new SaveLessonDialog();

		Bundle args = new Bundle();
		args.putString(KEY_LESSON_NAME, lessonName);
		args.putString(KEY_LESSON_CONTENT, lessonContent);
		args.putIntArray(KEY_LESSON_ARRAY, lessonArray);
		dialog.setArguments(args);

		// Lesson lesson = new Lesson("Lesson", intArray);
		return dialog;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lessonName = getArguments().getString(KEY_LESSON_NAME);
		lessonContent = getArguments().getString(KEY_LESSON_CONTENT);
		lessonArray = getArguments().getIntArray(KEY_LESSON_ARRAY);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(R.string.save_lesson);
		View v = inflater
				.inflate(R.layout.dialog_save_lesson, container, false);
		editName = (EditText) v.findViewById(R.id.lesson_name);
		editName.setText("lesson_" + lessonName);
		editName.requestFocus();

		showSoftKeyboard(editName);
		
		EditText contentText = (EditText) v.findViewById(R.id.lesson_content);
		contentText.setText("Case " + lessonContent);

		Button button = (Button) v.findViewById(R.id.ok);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Lesson lesson = new Lesson(editName.getText().toString(),
						lessonArray);
				MySQLiteHelper sql = new MySQLiteHelper(getActivity());
				sql.insertLesson(lesson);

				Toast.makeText(getActivity(),
						getResources().getString(R.string.lesson_saved),
						Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});

		button = (Button) v.findViewById(R.id.cancel);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});

		return v;
	}

	public void showSoftKeyboard(View view) {
//	    if (view.requestFocus()) {
	        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//	    }
	}
	
}
