package to.msn.wings.myscheduler;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private ScheduleDatabaseHelper helper = null;   // ヘルパーフィールド宣言
    private ListView list;      // スケジュールリストのビュー部品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new ScheduleDatabaseHelper(this);  // ヘルパーを取得
        list = findViewById(R.id.list);                     // ビューの取得
        showList();                                         // データの取得と表示

        // スケジュールクリック時にデータ取得＆結果情報のセット
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemIdNum = ((TextView)view.findViewById(R.id.itemId)).getText().toString();
                listClick(itemIdNum);
/*                String msg = position + "番目のアイテムがクリックされました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();*/
            }
        });
    }

    /** スケジュールクリック時の処理
     * (クリックしたスケジュールデータを編集画面に送る準備) **/
    public void listClick(String id) {
        try(SQLiteDatabase db = helper.getReadableDatabase();
            // SQL文で取り出したデータを保持して、その読み取り手段を提供する
            Cursor cs = db.rawQuery("SELECT * FROM myschedule where _id = " + id, null)) {
            cs.moveToFirst();
            // クリックしたリストのデータを格納
            String clickedIdData = cs.getString(0);
            String clickedDayData = cs.getString(1);
            String clickedTitleData = cs.getString(2);
            String clickedContentData = cs.getString(3);
            // 取り出したデータをインテント先に送る準備
            Intent i = new Intent(this, to.msn.wings.myscheduler.EditActivity.class);
            i.putExtra("from", "editItem");
            i.putExtra("itemId", clickedIdData);
            i.putExtra("editDay", clickedDayData);
            i.putExtra("editTitle", clickedTitleData);
            i.putExtra("editDetails", clickedContentData);
            // 結果情報をセット
            startActivityForResult(i, 1);
        }
    }

    /** [+]ボタンクリック時の処理
     * (リスト作成画面へ移動) **/
    public void btn_onClick(View view) {
        Intent i = new Intent(this, to.msn.wings.myscheduler.EditActivity.class);
        i.putExtra("from", "createItem");
        startActivityForResult(i, 1);
    }

    /** 編集画面から戻ってきた時の処理
     * (データの取得とスケジュールリストの表示)**/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            // データの取得
            showList();
        }
    }

    /** 画面遷移先から戻ってきた時の処理
     * (データの取得とスケジュールリストの表示)**/
    public void showList() {
        // Databaseの内容をArrayListに詰め替えるための準備
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        //  データベースの中身を読み取るためのオブジェクトを作成
        try(SQLiteDatabase db = helper.getReadableDatabase();
            // SQL文で取り出したデータを保持して、その読み取り手段を提供する
            Cursor cs = db.rawQuery("SELECT _id, day, title, content FROM myschedule", null)) {
            // カーソルが先頭行にあるかチェック（あればtrueを返す）
            boolean eol = cs.moveToFirst();
            // 全ての行をチェックしてデータHashMapに格納
            while (eol) {
                HashMap<String, String> dataList = new HashMap<>();
                dataList.put("itemId", cs.getString(0));
                dataList.put("day", cs.getString(1));
                dataList.put("title", cs.getString(2));
                dataList.put("content", cs.getString(3));
                data.add(dataList);
                eol = cs.moveToNext();
            }
        }
        // HashMap配列とレイアウトを関連付け
        SimpleAdapter adapter = new SimpleAdapter(
                this, data, R.layout.list_item,
                new String[] {"itemId", "day", "title", "content"},
                new int[] { R.id.itemId, R.id.showDay, R.id.showTitle, R.id.showDetails }
        );
        // アダプターを元にリストを作成
        list.setAdapter(adapter);
    }
}