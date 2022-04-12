package com.zeyalychat.com.utils;

public interface RecyclerAdapterListener<T>  {

    void onItemClicked(T data, int position);

    void onItemLongClicked(T data, int position);

}
