package com.XMPP.mainview;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterGroup;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.XMPP.R;
import com.XMPP.service.GroupProfile;
import com.XMPP.service.PersonProfile;
import com.XMPP.util.CircleImage;
import com.XMPP.util.L;
import com.XMPP.util.Test;
import com.atermenji.android.iconicdroid.IconicFontDrawable;
import com.atermenji.android.iconicdroid.icon.EntypoIcon;

public class ContactsFragment extends Fragment {
	RosterGroupCallback mCallback;
	ArrayList<GroupProfile> groupList;
	String[] groups_Name;
	String[][] items_Name;
	// Container Activity must implement this interface
	public interface RosterGroupCallback {
		public ArrayList<GroupProfile> getGroupList();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_contacts, container,
				false);
		ExpandableListView expandableListView = (ExpandableListView) view
				.findViewById(R.id.contactExpandableList);
		groupList = mCallback.getGroupList();
		turnGroupList(groupList);		
		Test.output1levelString(groups_Name);
		Test.output2levelString(items_Name);		
		ExpandableListAdapter expandAdapter = new mBaseExpandableListAdapter(groups_Name,items_Name);;		
		//groups = getGroupsName(groupList);
		expandableListView.setAdapter(expandAdapter);
		return view;
	}
	
    //create a String[][] of every friend and a String[] of every group from the groupList
	public void turnGroupList(ArrayList<GroupProfile> list){
		L.i("test if the ContactsFragment receive GroupList from the intent");
		if(list == null){
			L.i("test result : GroupProfile is null");
		}
		L.i("group size = " + list.size());
		groups_Name = new String[list.size()];
		items_Name = new String[list.size()][];
		for(int i = 0; i < list.size(); i++){
			L.i("group name = " + list.get(i).getGroupName());
			groups_Name[i] = list.get(i).getGroupName();
			items_Name[i] = new String[list.get(i).getPersonList().size()];
			for(int j = 0; j < list.get(i).getPersonList().size();j++){
				L.i("person name = " +  list.get(i).getPersonList().get(j).getName());
				items_Name[i][j] = list.get(i).getPersonList().get(j).getName();
			}
		}
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (RosterGroupCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement RosterGroupCallback");
		}
	}


	//ExpandableListAdapter expandAdapter = new mBaseExpandableListAdapter();
	class mBaseExpandableListAdapter extends BaseExpandableListAdapter{
		public mBaseExpandableListAdapter(String[] groups,String[][]items){
			this.groups = groups;
			this.items = items;
		}
		// names of the groups
		private String[] groups;
		// names of the items
		private String[][] items;



		// ��дExpandableListAdapter�еĸ�������
		@Override
		public int getGroupCount() {
			return groups.length;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return items[groupPosition].length;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return items[groupPosition][childPosition];
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			LinearLayout ll = (LinearLayout) View.inflate(
					ContactsFragment.this.getActivity(),
					R.layout.expand_list_title, null);
			ImageView arrowImage = (ImageView) ll
					.findViewById(R.id.listTitle_arrow);
			TextView groupName = (TextView) ll
					.findViewById(R.id.listTitle_groupname);
			groupName.setText(getGroup(groupPosition).toString());
			IconicFontDrawable iconicFontDrawable = new IconicFontDrawable(
					ContactsFragment.this.getActivity());
			if (!isExpanded) {
				iconicFontDrawable.setIcon(EntypoIcon.CHEVRON_THIN_RIGHT);
				iconicFontDrawable.setIconColor(getResources().getColor(com.XMPP.R.color.group_arrow_closed));
			} else {
				iconicFontDrawable.setIcon(EntypoIcon.CHEVRON_THIN_DOWN);
				iconicFontDrawable.setIconColor(getResources().getColor(com.XMPP.R.color.group_arrow_open));
			}
			arrowImage.setBackground(iconicFontDrawable);
			return ll;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LinearLayout ll = (LinearLayout) View.inflate(
					ContactsFragment.this.getActivity(),
					R.layout.expand_list_item, null);
			TextView itemName = (TextView) ll.findViewById(R.id.itemName);
			itemName.setText(getChild(groupPosition,childPosition).toString());
			ImageView itemImage = (ImageView) ll
					.findViewById(R.id.groupItemPhoto);
			Bitmap circleBitmap = CircleImage.toRoundBitmap(BitmapFactory
					.decodeResource(getResources(), R.drawable.channel_qq));
			itemImage.setImageBitmap(circleBitmap);

			return ll;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	};
}
