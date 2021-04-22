package is.hi.hbv601g.dotoo.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import is.hi.hbv601g.dotoo.Model.Friend;
import is.hi.hbv601g.dotoo.R;

public class FriendListAdapter extends BaseAdapter {

    private List<Friend> mFriends;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public FriendListAdapter(List<Friend> list, Context c) {
        mFriends = list;
        mContext = c;
        mLayoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = mLayoutInflater.inflate(R.layout.friend_list_view,parent,false);

        if(convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.friend_list_view, null);
        }

        Holder h = new Holder();

        // set id's
        h.friend_name = (TextView)(convertView.findViewById(R.id.friend_name));
        h.friend_streak = (TextView)(convertView.findViewById(R.id.friend_streak));

        Friend friend = mFriends.get(position);

        h.friend_name.setText(friend.getName());
        h.friend_streak.setText(String.valueOf(friend.getHighestStreak()));

        return convertView;
    }

    private class Holder
    {
        TextView friend_name;
        TextView friend_streak;
    }
}
