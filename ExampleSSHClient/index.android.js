/**
* Sample React Native App
* https://github.com/facebook/react-native
* @flow
*/

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Button
} from 'react-native';
import SSHClient from 'react-native-sshclient';

export default class ExampleSSHClient extends Component {
  constructor(props){
    super(props);
    this.state = {
      output: ""
    }
  }
  handleConnect(){
    SSHClient.setup("ne0z","192.168.1.1",22);
    SSHClient.usePrivateKey(false);
    SSHClient.setPassword("your_password");
    SSHClient.connect().then(
      (result)=>{
        alert("Connected !");
      },
      (error)=>{
        alert(error);
      }
    );
  }
  handleKernel(){
    SSHClient.execute("uname -a").then(
      (result)=>{
        this.setState({output:result});
      },
      (error)=>{
        alert(error);
      }
    );
  }
  handleLogout(){
    SSHClient.close();
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.instructions}>
          {this.state.output}
        </Text>
        <View style={{margin:20}}>
          <Button title="Connect SSH" onPress={this.handleConnect.bind(this)} />
        </View>
        <View style={{margin:20}}>
          <Button title="Get Kernel Version" onPress={this.handleKernel.bind(this)} />
        </View>
        <View style={{margin:20}}>
          <Button title="Logout" onPress={this.handleLogout.bind(this)} />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('ExampleSSHClient', () => ExampleSSHClient);
