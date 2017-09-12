
# react-native-sshclient

## Getting started

`$ npm install react-native-sshclient --save`

### Mostly automatic installation

`$ react-native link react-native-sshclient`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNReactNativeSshclientPackage;` to the imports at the top of the file
  - Add `new RNReactNativeSshclientPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-sshclient'
  	project(':react-native-sshclient').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-sshclient/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-sshclient')
  	```

## Usage : Using Password
```javascript
import SSHClient from 'react-native-sshclient';

// TODO: SSH Client using password
SSHClient.setup("root","192.168.1.1",22);
SSHClient.usePrivateKey(false);
SSHClient.setPassword("yourpassword");
SSHClient.connect();
SSHClient.execute("uname -a").then(
  (result)=>{
    alert(result);
  },
  (error)=>{
    alert(error);
  }
);
SSHClient.execute("ls").then(
  (result)=>{
    alert(result);
  },
  (error)=>{
    alert(error);
  }
);
SSHClient.close();
```
## Usage : Using Public & Private key
```javascript
// TODO: SSH Client using private & public key
SSHClient.setup("root","192.168.1.1",22);
SSHClient.usePrivateKey(true);
SSHClient.setIdentity("your_private_key_string","your_public_key_string","your_passphrase");
SSHClient.connect();
SSHClient.execute("uname -a").then(
  (result)=>{
    alert(result);
  },
  (error)=>{
    alert(error);
  }
);
SSHClient.execute("ls").then(
  (result)=>{
    alert(result);
  },
  (error)=>{
    alert(error);
  }
);
SSHClient.close();
```
