package com.devmite.rubik.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.devmite.rubik.R;
import com.devmite.rubik.database.MySQLiteHelper;
import com.devmite.rubik.model.Lesson;

public class LoadLessonDialog extends DialogFragment implements
		OnMenuItemClickListener {
	private List<Lesson> lessonsList;
	private ListView listView;
	ArrayList<HashMap<String, String>> list;
	
	public static int DELETE_LESSON_MENU = 0;
	public static String DELETE = "Delete";

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_load_lesson, null);
		builder.setView(view);

		listView = (ListView) view
				.findViewById(R.id.listview_load_lesson);
		registerForContextMenu(listView);

		HashMap<String, String> item;
		list = new ArrayList<HashMap<String, String>>();

		MySQLiteHelper sql = new MySQLiteHelper(getActivity());
		lessonsList = sql.selectLessons();
		
		if (lessonsList!= null){
			for (int i = 0; i < lessonsList.size(); i++) {
				Lesson lesson = lessonsList.get(i);
				item = new HashMap<String, String>();
				item.put("line1", lesson.getName());
				item.put("line2", "Case " + lesson.getContentStringForShow());
				list.add(item);
			}

			SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), list, android.R.layout.two_line_list_item, 
					new String[] { "line1","line2" }, new int[] { android.R.id.text1,android.R.id.text2 });

			// Bind to our new adapter.
			listView.setAdapter(simpleAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					load(position);
				}
			});
		}

		builder.setTitle(R.string.load_lesson);
		return builder.create();
	}

	private void load(int position) {
		EditDialogListener activity = (EditDialogListener) getActivity();
		activity.updateResult(lessonsList.get(position));
		dismiss();
	}

	public interface EditDialogListener {
		void updateResult(Lesson chosenLesson);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.listview_load_lesson) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			int pos = info.position;
			Lesson lesson = lessonsList.get(pos);
			menu.setHeaderTitle(lesson.getName());
			MenuItem menuItem = menu.add(Menu.NONE, DELETE_LESSON_MENU, 0,
					DELETE);
			menuItem.setOnMenuItemClickListener(this);
		}
	}

	// @Override
	// public boolean onContextItemSelected(android.view.MenuItem item) {
	// AdapterView.AdapterContextMenuInfo info =
	// (AdapterView.AdapterContextMenuInfo) item
	// .getMenuInfo();
	// int menuItemIndex = item.getItemId();
	// int pos = info.position;
	//
	// return true;
	// }

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		if (menuItemIndex == DELETE_LESSON_MENU) {
			int pos = info.position;
			Lesson lesson = lessonsList.get(pos);
			MySQLiteHelper sql = new MySQLiteHelper(getActivity());
			sql.deleteLesson(lesson.getId());
			
			// ke depannya diubah? ga singkrong!
			lessonsList.remove(pos);
			list.remove(pos);
			((SimpleAdapter) listView.getAdapter()).notifyDataSetChanged();
			
			Toast.makeText(getActivity(), lesson.getName() + " deleted",
					Toast.LENGTH_SHORT);
		}
		return false;
	}

}
