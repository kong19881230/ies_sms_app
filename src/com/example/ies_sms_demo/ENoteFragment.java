package com.example.ies_sms_demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ies_sms_demo.http.JSONParser;
import com.example.ies_sms_demo.model.Equipment;

public class ENoteFragment extends Fragment {
	private Context rootContext;
	private View rootView;
	public Equipment equipment;

	JSONParser jsonParser = new JSONParser();
	public final String TAG_EQUIPEMENT_NOTE = "equipment_note";
	public SharedPreferences sharedPref;

	private ProgressDialog pDialog;
	private static final String TAG_SUCCESS = "status";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_MESSAGE = "message";

	private static final String GET_EQUIPEMENT_URL = "http://uniquecode.net/job/ms/get_user_equipement.php";
	private static final String ADD_NOTE_URL = "http://uniquecode.net/job/ms/add_note.php";

	public EditText newNote;
	public TextView note;

	public ENoteFragment() {
	}

	public ENoteFragment(Context context, Equipment e) {
		this.rootContext = context;
		equipment = e;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.e_tab_note, container, false);

	    sharedPref = rootContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
		note = (TextView) rootView.findViewById(R.id.note);
		newNote = (EditText) rootView.findViewById(R.id.new_note);
		if (note != null) {
			note.setText(equipment.note);
		}
		Button addNote = (Button) rootView.findViewById(R.id.add_note);
		addNote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (newNote != null && !newNote.getText().toString().isEmpty()) {
					new AddNoteEquipement().execute();
				} else {
					Toast.makeText(rootContext, "New Note is empty", Toast.LENGTH_LONG)
							.show();
				}

			}/* Some Code */
		});
		return rootView;
	}

	
		class AddNoteEquipement extends AsyncTask {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			boolean failure = false;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(rootContext);
				pDialog.setMessage("Adding Note...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}

			@Override
			protected String doInBackground(Object... args) {
				// TODO Auto-generated method stub
				// Check for success tag
				int success;

				String username = sharedPref.getString(
						getString(R.string.user_name), "");
				String token = sharedPref.getString(getString(R.string.token),
						"");
				if (username == "" || token == "") {
					Intent i = new Intent(rootContext,
							LoginActivity.class);

					startActivity(i);
				} else {
					try {
						// Building Parameters
						List params = new ArrayList();
						params.add(new BasicNameValuePair("txtUName", username));
						params.add(new BasicNameValuePair("txtToken", token));

						params.add(new BasicNameValuePair("note", newNote
								.getText().toString()));
						params.add(new BasicNameValuePair("equipment_id", ""
								+ equipment.id));
						Log.d("request!", "starting");
						// getting product details by making HTTP request
						JSONObject json = jsonParser.makeHttpRequest(
								ADD_NOTE_URL, "POST", params);

						SharedPreferences.Editor editor = sharedPref.edit();
						// json success tag
						if (json != null) {
							// check your log for json response
							Log.d("Login attempt", json.toString());
							success = json.getInt(TAG_SUCCESS);
							if (success == 200) {
								Log.d("Login Successful!", json.toString());

								equipment.note = json
										.getString(TAG_EQUIPEMENT_NOTE);

								return json.getString(TAG_MESSAGE);
							} else {
								failure = true;
								editor.commit();
								Log.d("Login Failure!",
										json.getString(TAG_MESSAGE));
								return json.getString(TAG_MESSAGE);
							}
						} else {
							failure = true;
							return "No Network Connection";
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			@Override
			protected void onPostExecute(Object result) {
				// dismiss the dialog once product deleted
				if (!failure && note != null) {
					if (newNote != null) {
						note.setText(equipment.note);
						newNote.setText("");
					}

				}
				new GetUserEquipement().execute();
				pDialog.dismiss();
				if (result != null) {
					Toast.makeText(rootContext, result.toString(),
							Toast.LENGTH_LONG).show();
				}
			}

		}
		class GetUserEquipement extends AsyncTask {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			boolean failure = false;

			@Override
			protected String doInBackground(Object... args) {
				// TODO Auto-generated method stub
				// Check for success tag
				int success;

				String username = sharedPref.getString(
						getString(R.string.user_name), "");
				String token = sharedPref.getString(getString(R.string.token),
						"");
				if (username == "" || token == "") {
					Intent i = new Intent(rootContext, LoginActivity.class);
					startActivity(i);
				} else {
					try {
						// Building Parameters
						List params = new ArrayList();
						params.add(new BasicNameValuePair("txtUName", username));
						params.add(new BasicNameValuePair("txtToken", token));

						Log.d("request!", "starting");
						// getting product details by making HTTP request
						JSONObject json = jsonParser.makeHttpRequest(
								GET_EQUIPEMENT_URL, "POST", params);

						SharedPreferences.Editor editor = sharedPref.edit();
						// json success tag
						if (json != null) {
							// check your log for json response
							Log.d("Login attempt", json.toString());
							success = json.getInt(TAG_SUCCESS);
							if (success == 200) {
								Log.d("Login Successful!", json.toString());

								editor.putString(
										getString(R.string.project_list),
										json.toString());
								editor.commit();

								return json.getString(TAG_MESSAGE);
							} else if (success == 400) {
								Intent i = new Intent(rootContext,
										LoginActivity.class);
								startActivity(i);
							} else {
								failure = true;
								editor.commit();
								Log.d("Login Failure!",
										json.getString(TAG_MESSAGE));
								return json.getString(TAG_MESSAGE);
							}
						} else {
							failure = true;
							return "No Network Connection";
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			@Override
			protected void onPostExecute(Object result) {

			}

		}
	}
