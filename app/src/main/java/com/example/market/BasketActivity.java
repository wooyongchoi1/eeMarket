package com.example.market;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// 장바구니 화면을 나타내는 액티비티
public class BasketActivity extends AppCompatActivity{

    //상품 정보를 담고있는 리스트
    ArrayList<BasketDB> BlistAll = null;
    //    ArrayList<ProductInfo> BlistP = null;
    String num = null;

    ListView BlistView = null;
    BasketAdapter BAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //버튼 연결
        Button AllSelBtn = findViewById(R.id.allSelectBtn);
        Button delBtn = findViewById(R.id.deleteBtn);
        Button buyBtn = findViewById(R.id.buyButton);


        //데이터 초기화
        //DB와 연결해야함!
        BlistAll = new ArrayList<BasketDB>();
//        BlistP = new ArrayList<ProductInfo>();

        ProductInfo a = new ProductInfo("1234","키보드","1000",null);
        BasketDB aAdd = new BasketDB(a,"2");

        ProductInfo b = new ProductInfo("코드","상품 이름","가격",null);
        BasketDB bAdd = new BasketDB(b,"개수");

        ProductInfo c = new ProductInfo("123456","사과","1000",null);
        BasketDB cAdd = new BasketDB(c,"3");


        BlistAll.add(aAdd);
        BlistAll.add(bAdd);
        BlistAll.add(cAdd);

        //어댑터에 데이터 추가
        BAdapter = new BasketAdapter(this,BlistAll);


        //어댑터와 리스트뷰 연결
        BlistView = (ListView)findViewById(R.id.listviewBasket);
        BlistView.setAdapter(BAdapter);

        //전체선택
        AllSelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0 ;
                count = BAdapter.getCount() ;

                for (int i=0; i<count; i++) {
                    BlistView.setItemChecked(i, true);

                }

            }
        });

        //선택삭제
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checkedItems = BlistView.getCheckedItemPositions();
                int count = BAdapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        BlistAll.remove(i) ;
                    }
                }

                // 모든 선택 상태 초기화.
                BlistView.clearChoices();

                BAdapter.notifyDataSetChanged();

            }
        });


        //구매 클릭시 실행
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메시지 출력
                Toast.makeText(getApplicationContext(),"결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                //결제 추가해야함!


                //결제된 리스트 삭제
                SparseBooleanArray checkedItems = BlistView.getCheckedItemPositions();
                int count = BAdapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        BlistAll.remove(i) ;
                    }
                }

                // 모든 선택 상태 초기화.
                BlistView.clearChoices();

                BAdapter.notifyDataSetChanged();

            }
        });

    }

}