package tw.div.stanleyli.postdemo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;






import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Post_Main extends Activity implements OnClickListener

{
	
	private String uriAPI;
	protected static final int REFRESH_DATA = 0x00000001;
	
	Handler mHandler = new Handler()

	{

		@Override
		public void handleMessage(Message msg)
		{
			//GlobalVar
			//GlobalVar GlobalVar = (GlobalVar)getApplication();
			switch (msg.what)
			{
			// 顯示網路上回傳字串

			case REFRESH_DATA:
				String result = null;
				if (msg.obj instanceof String)
					result = (String) msg.obj;
			
				if (result != null)
				{ 
				
					result = trimResult(result);
					Log.i("log", "Result Data : "+result);
					
					//return result
					Intent it = new Intent();
					Bundle data= new Bundle();
					data.putString("result", result);
					it.putExtras(data);
					Post_Main.this.setResult(RESULT_OK,it);
					//
					finishPage();							
				}
				//Null
				else
				{
					Log.i("log", "Result Null Data");
					Toast.makeText(Post_Main.this,"沒有回傳資料", Toast.LENGTH_SHORT).show();
					finishPage();
				}
				break;

			}

		}

	};
	
	static ArrayList<HashMap<String, String>> PostData = new ArrayList<HashMap<String,String>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState)

	{
		super.onCreate(savedInstanceState);
		//set  Loading layout
		//setContentView(R.layout.activity_loading);
		setContentView(R.layout.activity_main);
		 Intent it = getIntent();
		 ArrayList<HashMap<String, String>> PostData = 
				 (ArrayList<HashMap<String, String>>) 
				 	it.getSerializableExtra("PostData");
		
		 chooseURI(it.getExtras().getString("URI")); 
	
		
		 
		Thread t = new Thread(new sendPostRunnable(PostData));
		t.start();

	}

	@Override
	public void onClick(View v)

	{
		
	}
	
	
	private String sendPostDataToInternet(ArrayList<HashMap<String, String>> PostData)

	{
	
		List<NameValuePair> params =new ArrayList<NameValuePair>();
		Log.i("log","URIAPI "+uriAPI);
		
		//迴圈取出List(PostData)裡面的值->存入List<NameValuePair>(params)
		for(int i = 0 ;i<PostData.size();i++){
			params.add(
					new BasicNameValuePair(
							PostData.get(i).get("Key"),
							PostData.get(i).get("Value")));
		}
	 
		Log.i("log", params.toString());
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(uriAPI);
		
		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			for (int index = 0; index < params.size(); index++) {
				if (params.get(index).getName()
						.equalsIgnoreCase("image")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(params.get(index).getName(),
							new FileBody(new File(params.get(index)
									.getValue())));
				} else {
					// Normal string data
					entity.addPart(
							params.get(index).getName(),
							new StringBody(params.get(index).getValue()));
					}
			}

			httpPost.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(httpPost,
					localContext);
			//
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(
						httpResponse.getEntity(), "UTF-8");
				return strResult;
			}
			//
			return null;

		} catch (IOException e) {
			Log.i("log", e.toString());
			return null;
		}
	
	}

	class sendPostRunnable implements Runnable

	{
		ArrayList<HashMap<String, String>> PostData = new ArrayList<HashMap<String,String>>();
	//	HashMap<String,String> PostData=new HashMap<String,String>();
		public sendPostRunnable(ArrayList<HashMap<String, String>> PostData)
		{
			this.PostData=PostData;
		}
		@Override
		public void run()

		{
			String result = sendPostDataToInternet(PostData);
			mHandler.obtainMessage(REFRESH_DATA, result).sendToTarget();
		}

	}

	//
	public void finishPage(){
		Post_Main.this.finish();
		}
	
	//
	private void chooseURI(String URI){
		uriAPI=URI ;
	}
	

	//去除result多餘空白格
	private String trimResult(String result){
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
        Matcher m = p.matcher(result);  
        result = m.replaceAll("");  
		return result;
	}
	
	/*
	 * params.add(new BasicNameValuePair(	key	, value	);
	 * key為在後台接收時使用
	 * value為要傳送的值
	 * key=value;
	 * */	
	 public static void AddPostData(String key,String value){
	    	HashMap<String, String> myHashMap = new HashMap<String, String>();
	    	myHashMap.put("Key", key);
	      	myHashMap.put("Value", value);
	      	PostData.add(myHashMap);
	      	
	       }
	 
	 public static ArrayList<HashMap<String, String>> GetPostData(){
	 	 return PostData;
	 }
	 
	 
	

}
