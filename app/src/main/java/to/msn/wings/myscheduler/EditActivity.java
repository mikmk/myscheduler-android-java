package to.msn.wings.myscheduler;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    private ScheduleDatabaseHelper helper = null;   // ヘルパーフィールド宣言
    String from;    // 保存、更新のどちらのボタンをクリックしたかを判断するフィールド
    String id = null;   // 主キー
    EditText editDay = null;   // 日付
    EditText editTitle = null;   // タイトル
    EditText editDetails = null;   // 詳細

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // ヘルパーを取得
        helper = new ScheduleDatabaseHelper(this);

        // ビュー取得
        editDay = findViewById(R.id.editDay);
        editTitle = findViewById(R.id.editTitle);
        editDetails = findViewById(R.id.editDetails);

        // インテントを取得、データを挿入
        Intent i = getIntent();
        from = i.getStringExtra("from");
        id = i.getStringExtra("itemId");
        editDay.setText(i.getStringExtra("editDay"));
        editTitle.setText(i.getStringExtra("editTitle"));
        editDetails.setText(i.getStringExtra("editDetails"));

        // スケジュールリストをクリックして編集画面に来たときのボタンテキスト変更
        if(from.equals("editItem")) {
            Button submit = findViewById(R.id.btnSubmit);
            submit.setText("更新");
        }
    }

    /** [保存]、[更新]ボタンクリック時の処理 **/
    public void btnSubmit_onClick(View view) {
        try(SQLiteDatabase db = helper.getWritableDatabase()) {
            // [保存]ボタンクリック時
            if(from.equals("createItem")) {
                db.execSQL("INSERT INTO myschedule (day, title, content) " +
                        "VALUES ('" + editDay.getText().toString() + "', '" +
                        editTitle.getText().toString() + "', '" +
                        editDetails.getText().toString() + "')");
            // [更新]ボタンクリック時
            } else {
                db.execSQL("UPDATE myschedule SET day = '" + editDay.getText().toString() + "' ," +
                        " title = '" + editTitle.getText().toString() +"' ," +
                        " content ='" + editDetails.getText().toString() + "' where _id = " + id);
            }
        }
        setResult(RESULT_OK);
        finish();
    }
    /** [削除]ボタンクリック時の処理 **/
    public void btnDelete_onClick(View view) {
        try(SQLiteDatabase db = helper.getWritableDatabase()) {
            db.execSQL("DELETE FROM myschedule WHERE _id = " + id);
            setResult(RESULT_OK);
            finish();
        }
    }

}