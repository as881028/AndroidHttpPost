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
		// �e����PHP������KEY,�᭱���n�Ǫ�VALUE
		Post_Main.AddPostData("ID", "BN100111");
		Post_Main.AddPostData("PW", "9876543cba");
		// ���ΰ�
		data.putSerializable("PostData", Post_Main.GetPostData());
		// VALUE��n�Ǫ��a�}
		data.putString("URI",
				"http://as881028.no-ip.biz/AMS_Project/App_Login.php");
		it.putExtras(data);
		it.setClass(getApplicationContext(), Post_Main.class);
		// �p�G�b�P�@�����h��PHP�n���q,�i�H�z�L���P��requestCode�h��
		// �аѦ�startActivityForResult�Ϊk
		startActivityForResult(it, Login2Post);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			//��ĳ�ϥ�switch�A�H�K�����X�W
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
