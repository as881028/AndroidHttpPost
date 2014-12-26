package tw.div.stanleyli.postdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private final static int Login2Post = 1111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent it = new Intent();
		Bundle data = new Bundle();
		// AddPostData("key","value");
		// 前面為PHP接收的KEY,後面為要傳的VALUE
		Post_Main.AddPostData("ID", "BN100111");
		Post_Main.AddPostData("PW", "9876543cba");
		// 不用動
		data.putSerializable("PostData", Post_Main.GetPostData());
		// VALUE改要傳的地址
		data.putString("URI",
				"http://as881028.no-ip.biz/AMS_Project/App_Login.php");
		it.putExtras(data);
		it.setClass(getApplicationContext(), Post_Main.class);
		// 如果在同一頁有多個PHP要溝通,可以透過不同的requestCode去做
		// 請參考startActivityForResult用法
		startActivityForResult(it, Login2Post);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			//建議使用switch，以便未來擴增
			switch (requestCode) {
			case Login2Post:
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");
				Log.i("log", result);
				break;
				}
		}

	}

}
