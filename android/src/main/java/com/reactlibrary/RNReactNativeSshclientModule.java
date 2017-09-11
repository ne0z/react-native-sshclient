package com.reactlibrary;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Vector;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class RNReactNativeSshclientModule extends ReactContextBaseJavaModule {
  private String username, password, ip_address, private_key, public_key, passphrase;
  private int port;
  private boolean mode;
  private Session session;

  private final ReactApplicationContext reactContext;

  public RNReactNativeSshclientModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "SSHClient";
  }

  @ReactMethod
  public void setup(String username,String ip_address,int port){
    this.username = username;
    this.password = password;
    this.password = "";
    this.private_key = "";
    this.public_key = "";
    this.passphrase = "";
    this.ip_address = ip_address;
    this.port = port;
  }


  @ReactMethod
  public void setIdentity(String private_key, String public_key, String passphrase){
    this.private_key = private_key;
    this.public_key = public_key;
    this.passphrase = passphrase;
  }

  @ReactMethod
  public void usePrivateKey(boolean mode){
    this.mode = mode;
  }

  @ReactMethod
  public void setPassword(String password){
    this.password = password;
  }

  @ReactMethod
  public void getPassword(Promise promise){
    try{
      promise.resolve(this.password);

    }catch(Exception e){
      promise.reject("ERROR", e.getMessage());
    }
  }

  @ReactMethod
  public void getPrivateKey(Promise promise){
    promise.resolve(this.private_key);
  }

  @ReactMethod
  public void setSession(Session session){
    this.session = session;
  }

  @ReactMethod
  public void connect(final Promise promise) {
    new Thread(new Runnable() {
      Session session = null;
      JSch jsch = new JSch();
      public void run() {
        try {
          if (!RNSSHClientModule.this.mode){
            session = jsch.getSession(RNSSHClientModule.this.username,RNSSHClientModule.this.ip_address, RNSSHClientModule.this.port);
            session.setPassword(RNSSHClientModule.this.password);
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
          }else{
            jsch.addIdentity("my key",RNSSHClientModule.this.private_key.getBytes(),RNSSHClientModule.this.public_key.getBytes(), RNSSHClientModule.this.passphrase.getBytes());
            session = jsch.getSession(RNSSHClientModule.this.username,RNSSHClientModule.this.ip_address, RNSSHClientModule.this.port);
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
          }
          session.connect();
          RNSSHClientModule.this.setSession(session);
          promise.resolve(true);
        }catch(Exception e){
          promise.reject("ERROR", e.getMessage());
        }
      }
    }).start();
  }

  @ReactMethod
  public void execute(final String command, final Promise promise){
    new Thread(new Runnable() {
      public void run() {
        try {
          ChannelExec channelExec = (ChannelExec) RNSSHClientModule.this.session.openChannel("exec");
          InputStream in = channelExec.getInputStream();
          channelExec.setCommand(command);
          channelExec.connect();
          String line;
          BufferedReader reader = new BufferedReader(new InputStreamReader(in));
          WritableNativeArray filenames = new WritableNativeArray();
          while ((line = reader.readLine()) != null) {
            filenames.pushString(line);
          }
          promise.resolve(filenames);
        } catch (Exception e) {
          promise.reject("ERROR", e.getMessage());
        }
      }

    }).start();
  }


  @ReactMethod
  public void close(Promise promise){
    Runnable r = new CloseOnThread(this.session,promise);
    new Thread(r).start();
  }
}

class CloseOnThread implements Runnable{
  private Session session;
  private Promise promise;
  public CloseOnThread(Session session,Promise promise){
    this.session = session;
    this.promise = promise;
  }
  public void run(){
    try{
      this.session.disconnect();
      this.promise.resolve(true);
    }catch(Exception e){
      this.promise.reject("ERROR", e.getMessage());
    }
  }
}
