package com.kwanwoo.android.tabletphonefragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TitlesFragment extends Fragment {


    public TitlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = (View)inflater.inflate(R.layout.fragment_titles, container, false);

        // id가 listview인 리스트뷰 객체를 얻어옴
        ListView listView = rootView.findViewById(R.id.listview);
        // 리스트뷰 객체에 어댑터 설정
        listView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1, Shakespeare.TITLES));
        // 리스트뷰 항목이 선택되었을 때, 항목 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 현재 프래그먼트와 연결된 액티비티를 반환
                Activity activity = getActivity();

                // 선택된 항목 위치(position)을 OnTitleSelectedListener 인터페이스를 구현한 액티비티로 전달
                if (activity instanceof OnTitleSelectedListener)
                    ((OnTitleSelectedListener)activity).onTitleSelected(position);
            }
        });
        return rootView;
    }

    public interface OnTitleSelectedListener {
        public void onTitleSelected(int i);
    }

}
