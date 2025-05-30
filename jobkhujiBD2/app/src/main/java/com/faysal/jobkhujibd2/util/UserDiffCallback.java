package com.faysal.jobkhujibd2.util;




import androidx.recyclerview.widget.DiffUtil;

import com.faysal.jobkhujibd2.model.User;

import java.util.List;
import java.util.Objects;

public class UserDiffCallback extends DiffUtil.Callback {

    private final List<User> oldList;
    private final List<User> newList;

    public UserDiffCallback(List<User> oldList, List<User> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }

//    @Override
//    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//        // Assuming 'id' uniquely identifies a user
//        return Objects.equals(oldList.get(oldItemPosition).getId(), newList.get(newItemPosition).getId());
//    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}


