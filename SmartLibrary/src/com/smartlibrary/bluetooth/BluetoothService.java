package com.smartlibrary.bluetooth;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.smartlibrary.book.BookInfo;

public class BluetoothService {
	// Debugging
	private static final String TAG = "BluetoothService";

	// Intent request code
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// RFCOMM Protocol
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	// 블루투스 서비스 프로토콜 주소
	// http://dsnight.tistory.com/13

	private BluetoothAdapter btAdapter;

	private Activity mActivity;
	private Handler mHandler;

	private ConnectThread mConnectThread; // ������ �ٽ�
	private ConnectedThread mConnectedThread; // ������ �ٽ�

	private int mState;

	InputStream is = null;
	String result = null;
	String line = null;
	String bookCard = null;
	String bookIsbn;
	String bookTitle;
	String bookReservation;
	String userId;
	String extension;
	String GcmId;
	Boolean btpower = true;
	Boolean btgest = false;
	int loginonce = 0;
	String rfidcard;
	int once = 1;
	boolean receivecardid = true;
	String checkid;
	int forone = 1;
	boolean innerwhile = true;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;

	// ���¸� ��Ÿ���� ���� ����
	private static final int STATE_NONE = 0; // we're doing nothing
	private static final int STATE_LISTEN = 1; // now listening for incoming
												// connections
	private static final int STATE_CONNECTING = 2; // now initiating an outgoing
													// connection
	private static final int STATE_CONNECTED = 3; // now connected to a remote
													// device

	// Constructors
	public BluetoothService(Activity ac, Handler h) {
		mActivity = ac;
		mHandler = h;

		// BluetoothAdapter ���
		btAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * Check the Bluetooth support
	 * 
	 * @return boolean
	 */
	public boolean getDeviceState() {
		Log.i(TAG, "Check the Bluetooth support");

		if (btAdapter == null) {
			Log.d(TAG, "Bluetooth is not available");

			return false;

		} else {
			Log.d(TAG, "Bluetooth is available");

			return true;
		}
	}

	/**
	 * Check the enabled Bluetooth
	 */
	public void enableBluetooth() {
		Log.i(TAG, "Check the enabled Bluetooth");

		if (btAdapter.isEnabled()) {
			// ����� ������� ���°� On�� ���
			Log.d(TAG, "Bluetooth Enable Now");

			// Next Step
			scanDevice();
		} else {
			// ����� ������� ���°� Off�� ���
			Log.d(TAG, "Bluetooth Enable Request");

			Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
		}
	}

	/**
	 * Available device search
	 */
	public void scanDevice() {
		Log.d(TAG, "Scan Device");

		Intent serverIntent = new Intent(mActivity, DeviceListActivity.class);
		mActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
	}

	/**
	 * after scanning and get device info
	 * 
	 * @param data
	 */
	public void getDeviceInfo(Intent data) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BluetoothDevice object
		// BluetoothDevice device = btAdapter.getRemoteDevice(address);
		BluetoothDevice device = btAdapter.getRemoteDevice(address);

		Log.d(TAG, "Get Device Info \n" + "address : " + address);

		connect(device);
	}

	// Bluetooth ���� set
	private synchronized void setState(int state) {
		Log.d(TAG, "setState() " + mState + " -> " + state);
		mState = state;
		if (state == 3) {
			// Toast.makeText(mActivity, "블루투스가 연결되었습니다", 1).show();
			Log.d("kh", "블루투스 연결");
			Vibrator vibe = (Vibrator) mActivity
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibe.vibrate(500);
			// Toast toast = Toast.makeText(Context.getApplicationContext(),
			// "로그인이 필요합니다.", Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.TOP, 25, 400);
			// toast.show();
		}
	}

	// Bluetooth ���� get
	public synchronized int getState() {
		return mState;
	}

	public synchronized void start() {
		Log.d(TAG, "start");

		// Cancel any thread attempting to make a connection
		if (mConnectThread == null) {

		} else {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread == null) {

		} else {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
	}

	// ConnectThread 초기화 device의 모든 연결 제거
	public synchronized void connect(BluetoothDevice device) {
		Log.d(TAG, "connect to: " + device);

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (mConnectThread == null) {

			} else {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread == null) {

		} else {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);

		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	// ConnectedThread �ʱ�ȭ
	public synchronized void connected(BluetoothSocket socket,
			BluetoothDevice device) {
		Log.d(TAG, "connected");

		// Cancel the thread that completed the connection
		if (mConnectThread == null) {

		} else {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread == null) {

		} else {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();

		setState(STATE_CONNECTED);
	}

	// ��� thread stop
	public synchronized void stop() {
		Log.d(TAG, "stop");

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		setState(STATE_NONE);
	}

	// ���� ���� �κ�(������ �κ�)
	public void write(byte[] out) { // Create temporary object
		ConnectedThread r; // Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		} // Perform the write unsynchronized r.write(out); }
		r.write(out);
	}

	// ���� ����������
	private void connectionFailed() {
		setState(STATE_LISTEN);
	}

	// ������ �Ҿ��� ��
	private void connectionLost() {
		setState(STATE_LISTEN);

	}

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		// private final BluetoothServerSocket mmServerSocket;

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;
			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server
				// code
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
			}
			mmSocket = tmp;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectThread");
			setName("ConnectThread");

			// 연결을 시도하기 전에는 항상 기기 검색을 중지한다.
			// 기기 검색이 계속되면 연결속도가 느려지기 때문이다.
			btAdapter.cancelDiscovery();

			// BluetoothSocket 연결 시도
			try {
				Log.d(TAG, "Try Bluetooth Connect ...");
				// BluetoothSocket 연결 시도에 대한 return 값은 succes 또는 exception이다.
				mmSocket.connect();
				Log.d(TAG, "Connect Success");

			} catch (IOException e) {
				connectionFailed(); // 연결 실패시 불러오는 메소드
				Log.d(TAG, "Connect Fail");

				// socket을 닫는다.
				try {
					mmSocket.close();
				} catch (IOException e2) {
					Log.e(TAG,
							"unable to close() socket during connection failure",
							e2);
				}
				// 연결중? 혹은 연결 대기상태인 메소드를 호출한다.
				BluetoothService.this.start();
				return;
			}

			// // ConnectThread 클래스를 reset한다.
			synchronized (BluetoothService.this) {
				mConnectThread = null;
			}

			// ConnectThread시작한다
			connected(mmSocket, mmDevice);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			Log.d(TAG, "create ConnectedThread");
			mmSocket = socket;
			BufferedInputStream tmpIn = null;
			OutputStream tmpOut = null;

			// BluetoothSocket의 inputstream 과 outputstream을 얻는다.
			try {
				tmpIn = new BufferedInputStream(mmSocket.getInputStream());
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			byte[] buffer = new byte[1024];
			int bytes;
			String[] strcard = null;
			int i = 0;

			String cardid = "";
			// Keep listening to the InputStream while connected

			while (innerwhile) {
				try {

					// InputStream으로부터 값을 받는 읽는 부분(값을 받는다)
					bytes = mmInStream.read(buffer);

					mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
							.sendToTarget();
					Log.d("kh",
							"bluetooth : "
									+ new String(buffer, Charset
											.forName("ASCII")).trim());

					// strcard[i++] += new String(buffer,
					// Charset.forName("ASCII")).trim();
					String cd = new String(buffer, Charset.forName("ASCII"))
							.trim();
					Log.d("kh", "cd  : " + cd);

					cardid = cd.split("$")[0];
					cardid = cd.split("#")[0];

					if (cardid.length() == 8) {

						// innerwhile=false;
						Log.d("kh", "10 cardid : " + cardid);

						rfidcard = cardid;
						// if (receivecardid) {
						if (forone++ == 1) {
							Log.d("hak", "forone : " + forone);
							select(cardid);
						}

					} else if (cardid.length() == 1) {

						// cardid = cd.split("*")[0];

						Log.d("kh", "## #cardid : " + cardid);

						if (once == 0) {

							if (cardid.equals("B")) {
								Log.d("kh", "B is : " + cardid);
								// 도서 등록 후 진동 보내자
								// 이건 대출
								sendborrow();
								// break;

							} else if (cardid.equals("G")) {
								Log.d("kh", "G is : " + cardid);
								// 도서 등록 후 진동 보내자
								// 이건 대여
								sendlend();
								// break;
							}
						}
						forone = 1;

					}
					buffer = new byte[1024];
					// Log.d("kh", "cardid : " + cardid);

				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					connectionLost();
					break;
				}

				// Log.d("kh", "cardid : " + cardid);

			}

		}

		/**
		 * Write to the connected OutStream.
		 * 
		 * @param buffer
		 *            The bytes to write
		 */
		public void write(byte[] buffer) {
			try {

				// 값을 쓰는 부분(값을 보낸다)
				mmOutStream.write(buffer);

				mmOutStream.write('\r');
				mmOutStream.flush();

				// Share the sent message back to the UI Activity
				mHandler.obtainMessage(MESSAGE_WRITE, -1, -1, buffer)
						.sendToTarget();
				Log.d("kh", "write conplete!!");
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	public String select(final String qtx) {

		try {
			return (new AsyncTask<String, String, String>() {

				// ArrayList<BorrowInfo> dataList = new ArrayList<BorrowInfo>();
				ArrayList<BookInfo> dataList = new ArrayList<BookInfo>();

				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {

					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("card", qtx));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/selectbookcard.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "connection success");
						// innerwhile=true;

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "UTF_8"), 8);
						StringBuilder sb = new StringBuilder();

						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						Log.d("kh", "result");
						result = sb.toString();
						Log.d("kh", result);
					} catch (Exception e) {
						Log.e("Fail 2", e.toString());

					}

					try {
						Log.d("kh", "1");
						JSONObject json_data = null;
						json_data = new JSONObject(result);
						Log.d("kh", "1.5"); // 여기는 됨
						JSONArray bkName = json_data.getJSONArray("results");
						String a = "";

						for (int i = 0; i < bkName.length(); i++) {
							Log.d("kh", "i " + i);
							JSONObject jo = bkName.getJSONObject(i);

							bookIsbn = jo.getString("isbn");
							bookTitle = jo.getString("title");
							// final String bookLocation =
							// jo.getString("location");
							bookReservation = jo.getString("reservation");
							bookCard = jo.getString("card");

						}

						return a;

					} catch (Exception e) {
						Log.e("Fail 3", e.toString());
					}

					return bookIsbn;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					Log.d("kh", "checkid");
					sharedPref = mActivity.getSharedPreferences("pref",
							Activity.MODE_PRIVATE);
					sharedEditor = sharedPref.edit();
					String findId = sharedPref.getString("id", "");
					// receivecardid = false;
					if (findId.equals("")) {
						if (loginonce == 0) {
							login();
							loginonce++;
						}
					} else {
						checkid(qtx);
					}
					// btpower=true;

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

	public String checkid(final String qtx) {

		try {
			return (new AsyncTask<String, String, String>() {

				// ArrayList<BorrowInfo> dataList = new ArrayList<BorrowInfo>();
				ArrayList<BookInfo> dataList = new ArrayList<BookInfo>();

				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					sharedPref = mActivity.getSharedPreferences("pref",
							Activity.MODE_PRIVATE);
					sharedEditor = sharedPref.edit();
					String findId = sharedPref.getString("id", "");
					nameValuePairs
							.add(new BasicNameValuePair("student", findId));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/checkreservationid.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "connection success");

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "UTF_8"), 8);
						StringBuilder sb = new StringBuilder();

						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						// Log.d("kh", "result");
						result = sb.toString();
						Log.d("kh", result);
					} catch (Exception e) {
						Log.e("Fail 2", e.toString());

					}

					try {
						// Log.d("kh", "1");
						JSONObject json_data = null;
						json_data = new JSONObject(result);
						// Log.d("kh", "1.5"); // 여기는 됨
						JSONArray bkName = json_data.getJSONArray("results");
						String a = "";

						for (int i = 0; i < bkName.length(); i++) {
							// Log.d("kh", "i " + i);
							JSONObject jo = bkName.getJSONObject(i);

							checkid = jo.getString("card");
							Log.d("kh", "서버에서 받은 checkid : " + checkid);

						}

						return a;

					} catch (Exception e) {
						Log.e("Fail 3", e.toString());
					}

					return checkid;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					// btpower=true;
					Log.d("kh", "예약 : " + bookReservation);
					if (bookReservation.equals("3")) {
						// 예약된 도서일경우인데

						sharedPref = mActivity.getSharedPreferences("pref",
								Activity.MODE_PRIVATE);
						sharedEditor = sharedPref.edit();
						String findId = sharedPref.getString("id", "");
						Log.d("kh", "checkid :   " + checkid + "  findId ::"
								+ rfidcard);
						if (rfidcard.equals(checkid)) {
							Log.d("kh", "내가 예약한 책이라면");
							String message = "$3";
							byte[] send = message.getBytes();
							write(send);
							Log.d("kh", "$3 sended");
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								Log.d("error", e.getMessage());
							}

							message = "$1";
							send = message.getBytes();
							write(send);
							// Log.d("kh", "$1 sended");

							try {
								once = 0;
								Thread.sleep(500);

							} catch (Exception e) {
								Log.d("error", e.getMessage());
							}

						} else {

							// 예약중이면 진동을 2번 보낸다
							Log.d("kh", "예약중인 도서");

							String message = "$3";
							byte[] send = message.getBytes();
							write(send);
							Log.d("kh", "$3 sended");

							try {
								Thread.sleep(2000);
							} catch (Exception e) {
								Log.d("error", e.getMessage());
							}
							write(send);
							alert();
							write(send);
						}

					} else if (qtx.equals(bookCard)) {
						// 받은 카드번호랑 등록된 디비의 카드 번호가 있으면
						// $1을 보낸다

						String message = "$3";
						byte[] send = message.getBytes();
						Log.d("kh", "진동을 보낸다");
						write(send);
						// Log.d("kh", "$3 sended");
						try {

							Thread.sleep(1000);
						} catch (Exception e) {
							Log.d("error", e.getMessage());
						}

						message = "$1";
						send = message.getBytes();
						write(send);
						Log.d("kh", "동작하라 보냈다");

						once = 0;
						try {
							Thread.sleep(1500);
							send = null;
						} catch (Exception e) {
							Log.d("error", e.getMessage());
						}

					}

				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

	// 대여 데이터 전송부분
	public String sendlend() {

		try {
			return (new AsyncTask<String, String, String>() {

				// ArrayList<BorrowInfo> dataList = new ArrayList<BorrowInfo>();
				ArrayList<BookInfo> dataList = new ArrayList<BookInfo>();

				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd", Locale.KOREA);
					Date currentTime = new Date();
					String mTime = mSimpleDateFormat.format(currentTime);

					// String mTime = "2014-08-22";

					sharedPref = mActivity.getSharedPreferences("pref",
							Activity.MODE_PRIVATE);
					sharedEditor = sharedPref.edit();
					String findId = sharedPref.getString("id", "");
					Log.d("kh", " user id: " + findId);

					Log.d("kh", "gcmid : " + sharedPref.getString("gcmid", ""));
					nameValuePairs
							.add(new BasicNameValuePair("card", bookCard));
					nameValuePairs
							.add(new BasicNameValuePair("student", findId));
					nameValuePairs.add(new BasicNameValuePair("startdate",
							mTime));
					nameValuePairs
							.add(new BasicNameValuePair("isbn", bookIsbn));
					nameValuePairs.add(new BasicNameValuePair("title",
							bookTitle));
					nameValuePairs.add(new BasicNameValuePair("gcmid",
							sharedPref.getString("gcmid", "")));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/lendinsert.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "connection success");
						once++;
						Log.d("kh", "once " + once);

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}

					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "UTF_8"), 8);
						StringBuilder sb = new StringBuilder();

						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						Log.d("kh", "result");
						result = sb.toString();
						Log.d("kh", result);
					} catch (Exception e) {
						Log.e("Fail 2", e.toString());

					}

					return result;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					// receivecardid = true;
					try {
						Thread.sleep(1000);
						once = 0;
					} catch (Exception e) {
						Log.d("error", e.getMessage());
					}

					// btpower = true;
					// btgest = false;
				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

	// 대출 데이터 전송부분
	public String sendborrow() {

		try {
			return (new AsyncTask<String, String, String>() {

				// ArrayList<BorrowInfo> dataList = new ArrayList<BorrowInfo>();
				// ArrayList<BookInfo> dataList = new ArrayList<BookInfo>();

				@Override
				protected void onProgressUpdate(String... values) {
					// TODO Auto-generated method stub
					super.onProgressUpdate(values);
				}

				@Override
				protected String doInBackground(String... params) {
					final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

					sharedPref = mActivity.getSharedPreferences("pref",
							Activity.MODE_PRIVATE);
					sharedEditor = sharedPref.edit();
					String findId = sharedPref.getString("id", "");
					Log.d("kh", " user id: " + findId);

					SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
							"yyyy-MM-dd", Locale.KOREA);
					Date currentTime = new Date();
					String mTime = mSimpleDateFormat.format(currentTime);

					Date date = null;
					try {
						date = mSimpleDateFormat.parse(mTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DATE, 14);
					String enddate = mSimpleDateFormat.format(cal.getTime());
					// Log.d("kh", "date extension : " + enddate);

					// String mTime = "2014-08-22";
					// String enddate = "2014-08-22";

					nameValuePairs
							.add(new BasicNameValuePair("card", bookCard));
					nameValuePairs
							.add(new BasicNameValuePair("student", findId));
					nameValuePairs.add(new BasicNameValuePair("startdate",
							mTime));
					nameValuePairs
							.add(new BasicNameValuePair("isbn", bookIsbn));
					nameValuePairs.add(new BasicNameValuePair("title",
							bookTitle));
					nameValuePairs.add(new BasicNameValuePair("enddate",
							enddate));
					nameValuePairs
							.add(new BasicNameValuePair("extension", "0"));

					try {
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(
								"http://112.108.40.87/borrowinsert.php");
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, HTTP.UTF_8));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						is = entity.getContent();
						Log.d("kh", "connection success");

					} catch (Exception e) {
						Log.e("Fail 1", e.toString());
					}
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(is, "UTF_8"), 8);
						StringBuilder sb = new StringBuilder();

						while ((line = reader.readLine()) != null) {
							sb.append(line + "\n");
						}
						is.close();
						Log.d("kh", "result");
						result = sb.toString();
						Log.d("kh", result);
					} catch (Exception e) {
						Log.e("Fail 2", e.toString());

					}

					return result;
				}

				@Override
				protected void onPostExecute(String result) {
					if (result == null)
						return;
					// receivecardid = true;

					once = 1;
					try {
						Thread.sleep(1500);
						once = 0;
					} catch (Exception e) {
						Log.d("error", e.getMessage());
					}
					// btpower = true;
					// btgest = false;
				}
			}.execute("")).get();

		} catch (Exception e) {
			return null;
		}

	}

	public void alert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // 여기서
																			// this는
																			// Activity의
																			// this
		Vibrator vibe = (Vibrator) mActivity
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(500);
		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("Error Message") // 제목 설정
				.setMessage("이미 예약된 도서입니다.") // 메세지 설정
				.setCancelable(false) // 뒤로 버튼 클릭시 취소 가능 설정
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					// 확인 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton) {
						// receivecardid = true;
					}
				});

		AlertDialog dialog = builder.create(); // 알림창 객체 생성
		dialog.show(); // 알림창 띄우기
	}

	public void login() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity); // 여기서
																			// this는
																			// Activity의
																			// this
		Vibrator vibe = (Vibrator) mActivity
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(500);
		// 여기서 부터는 알림창의 속성 설정
		builder.setTitle("Error Message") // 제목 설정
				.setMessage("로그인이 필요합니다.") // 메세지 설정
				.setCancelable(false) // 뒤로 버튼 클릭시 취소 가능 설정
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					// 확인 버튼 클릭시 설정
					public void onClick(DialogInterface dialog, int whichButton) {

						loginonce = 0;
						// receivecardid = true;
					}
				});

		AlertDialog dialog = builder.create(); // 알림창 객체 생성
		dialog.show(); // 알림창 띄우기
	}

}
