package to.msn.wings.myscheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDatabaseHelper extends SQLiteOpenHelper {
    // データベースファイル名の定数フィールド
    private static final String DBNAME = "myschedule.sqlite";
    // バージョン情報の定数フィールド
    private static final int VERSION = 1;
    // コンストラクター
    ScheduleDatabaseHelper(Context context) {
        // 親クラスのコンストラクタの呼び出し
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成用SQL文字の作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE myschedule (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("title TEXT,");
        sb.append("day TEXT,");
        sb.append("content TEXT");
        sb.append(");");
        String sql = sb.toString();
        // SQLの実行
        db.execSQL(sql);
    }

    // データベースをバージョンアップした時、テーブルを再作成
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS myschedule");
        onCreate(db);
    }
}
